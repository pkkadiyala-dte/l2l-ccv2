/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.singlestack.service;

public interface SinglestacksapcpiorderexchangeService
{
	String getHybrisLogoUrl(String logoCode);

	void createLogo(String logoCode);
}
