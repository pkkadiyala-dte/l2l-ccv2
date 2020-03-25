/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.l2l.core.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.l2l.core.constants.L2lCoreConstants;
import com.l2l.core.setup.CoreSystemSetup;


/**
 * Do not use, please use {@link CoreSystemSetup} instead.
 * 
 */
public class L2lCoreManager extends GeneratedL2lCoreManager
{
	public static final L2lCoreManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (L2lCoreManager) em.getExtension(L2lCoreConstants.EXTENSIONNAME);
	}
}
