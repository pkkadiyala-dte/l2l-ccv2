/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.singlestack.setup;

import static com.singlestack.constants.SinglestacksapcpiorderexchangeConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.singlestack.constants.SinglestacksapcpiorderexchangeConstants;
import com.singlestack.service.SinglestacksapcpiorderexchangeService;


@SystemSetup(extension = SinglestacksapcpiorderexchangeConstants.EXTENSIONNAME)
public class SinglestacksapcpiorderexchangeSystemSetup
{
	private final SinglestacksapcpiorderexchangeService singlestacksapcpiorderexchangeService;

	public SinglestacksapcpiorderexchangeSystemSetup(final SinglestacksapcpiorderexchangeService singlestacksapcpiorderexchangeService)
	{
		this.singlestacksapcpiorderexchangeService = singlestacksapcpiorderexchangeService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		singlestacksapcpiorderexchangeService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return SinglestacksapcpiorderexchangeSystemSetup.class.getResourceAsStream("/singlestacksapcpiorderexchange/sap-hybris-platform.png");
	}
}
