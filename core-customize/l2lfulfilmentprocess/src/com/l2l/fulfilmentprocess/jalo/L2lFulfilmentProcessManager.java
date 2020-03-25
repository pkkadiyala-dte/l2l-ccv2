/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.l2l.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.l2l.fulfilmentprocess.constants.L2lFulfilmentProcessConstants;

public class L2lFulfilmentProcessManager extends GeneratedL2lFulfilmentProcessManager
{
	public static final L2lFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (L2lFulfilmentProcessManager) em.getExtension(L2lFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
