/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.singlestackb2b.job;

import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.core.GenericSearchConstants.LOG;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundOrderModel;
import de.hybris.platform.sap.sapcpiadapter.service.SapCpiOutboundService;
import de.hybris.platform.sap.sapcpiorderexchange.service.SapCpiOrderOutboundConversionService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import org.apache.log4j.Logger;


/**
 * Rahul Verma
 */
public class SendOrderToScpiJob extends AbstractJobPerformable<CronJobModel>
{


	private static final Logger LOG = Logger.getLogger(SendOrderToScpiJob.class);
	private SapCpiOrderOutboundConversionService sapCpiOrderOutboundConversionService;
	private ModelService modelService;
	private int maxTries;
	public static final String ORDER_DETAILS = "SELECT {o.pk} FROM {order as o} WHERE {o.code}=?orderNumber";
	public static final String ORDERS_NOTEXPORTED = "SELECT {o.pk} FROM {Order as o} WHERE {o.exportStatus}=?notExported";
	private SapCpiOutboundService sapCpiOutboundService;

	/**
	 * @return the sapCpiOutboundService
	 */
	public SapCpiOutboundService getSapCpiOutboundService()
	{
		return sapCpiOutboundService;
	}

	/**
	 * @param sapCpiOutboundService
	 *           the sapCpiOutboundService to set
	 */
	public void setSapCpiOutboundService(SapCpiOutboundService sapCpiOutboundService)
	{
		this.sapCpiOutboundService = sapCpiOutboundService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.
	 * CronJobModel )
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{

		LOG.debug("Starting SendOrderToDatahubCronJob .........");
		final List<OrderModel> orders = getNotExportedOrders();
		LOG.debug("order ........." + orders);
		if (null != orders && orders.size() > 0)
		{
			LOG.debug("order size........." + orders.size());
			for (final OrderModel order : orders)
			{
				if (null != order)
				{
					boolean success = false;
					int count = 0;
					while (!success && count++ < this.maxTries)
					{
						LOG.debug("Retry count........." + count);
						LOG.debug("Max retry count........." + this.maxTries);
						try
						{
							final SAPCpiOutboundOrderModel scpiOrder = sapCpiOrderOutboundConversionService
									.convertOrderToSapCpiOrder(order);
							sapCpiOutboundService.sendOrder(scpiOrder).subscribe(

									// onNext
									responseEntityMap -> {

										if (SapCpiOutboundService.isSentSuccessfully(responseEntityMap))
										{

											setOrderStatus(order, ExportStatus.EXPORTED);
											//resetEndMessage(process);
											LOG.info(String.format(
													"The OMM order [%s] has been successfully sent to the SAP backend through SCPI! %n%s",
													order.getCode(), SapCpiOutboundService.getPropertyValue(responseEntityMap,
															SapCpiOutboundService.RESPONSE_MESSAGE)));

										}
										else
										{

											setOrderStatus(order, ExportStatus.NOTEXPORTED);
											LOG.error(String.format("The OMM order [%s] has not been sent to the SAP backend! %n%s",
													order.getCode(), SapCpiOutboundService.getPropertyValue(responseEntityMap,
															SapCpiOutboundService.RESPONSE_MESSAGE)));

										}



									}

									// onError
									, error -> {

										setOrderStatus(order, ExportStatus.NOTEXPORTED);
										LOG.error(
												String.format("The OMM order [%s] has not been sent to the SAP backend through SCPI! %n%s",
														order.getCode(), error.getMessage()),
												error);


									}

							);

						}
						catch (final Exception e)
						{
							LOG.debug("Exception occurred while sending order, " + order.getCode() + " to scpi");
							e.printStackTrace();
						}
					}
				}
			}
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected void setOrderStatus(final OrderModel order, final ExportStatus exportStatus)
	{
		order.setExportStatus(exportStatus);
		modelService.save(order);
	}

	public int getMaxTries()
	{
		return maxTries;
	}

	public void setMaxTries(final int maxTries)
	{
		this.maxTries = maxTries;
	}

	/**
	 * @return the sapCpiOrderOutboundConversionService
	 */
	public SapCpiOrderOutboundConversionService getSapCpiOrderOutboundConversionService()
	{
		return sapCpiOrderOutboundConversionService;
	}

	/**
	 * @param sapCpiOrderOutboundConversionService
	 *           the sapCpiOrderOutboundConversionService to set
	 */
	public void setSapCpiOrderOutboundConversionService(SapCpiOrderOutboundConversionService sapCpiOrderOutboundConversionService)
	{
		this.sapCpiOrderOutboundConversionService = sapCpiOrderOutboundConversionService;
	}


	public ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	//service
	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	@Override
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public List<OrderModel> getNotExportedOrders()
	{
		LOG.debug("Entering the method: getAllOrders");
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(ORDERS_NOTEXPORTED);
		flexibleSearchQuery.addQueryParameter("notExported", ExportStatus.NOTEXPORTED);
		final SearchResult<OrderModel> searchResult = getFlexibleSearchService().search(flexibleSearchQuery);
		if (searchResult.getCount() > 0)
		{
			return searchResult.getResult();
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("No Orders found in NOT EXPORTED Status: Exiting the method: getOrderDetails");
			}
		}
		return null;
	}





}
