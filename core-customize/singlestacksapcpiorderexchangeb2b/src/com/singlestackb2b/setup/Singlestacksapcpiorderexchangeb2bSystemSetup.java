/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.singlestackb2b.setup;

import static com.singlestackb2b.constants.Singlestacksapcpiorderexchangeb2bConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.singlestackb2b.constants.Singlestacksapcpiorderexchangeb2bConstants;
import com.singlestackb2b.service.Singlestacksapcpiorderexchangeb2bService;


@SystemSetup(extension = Singlestacksapcpiorderexchangeb2bConstants.EXTENSIONNAME)
public class Singlestacksapcpiorderexchangeb2bSystemSetup
{
	private final Singlestacksapcpiorderexchangeb2bService singlestacksapcpiorderexchangeb2bService;

	public Singlestacksapcpiorderexchangeb2bSystemSetup(final Singlestacksapcpiorderexchangeb2bService singlestacksapcpiorderexchangeb2bService)
	{
		this.singlestacksapcpiorderexchangeb2bService = singlestacksapcpiorderexchangeb2bService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		singlestacksapcpiorderexchangeb2bService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return Singlestacksapcpiorderexchangeb2bSystemSetup.class.getResourceAsStream("/singlestacksapcpiorderexchangeb2b/sap-hybris-platform.png");
	}
}
