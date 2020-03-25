/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.singlestack.controller;

import static com.singlestack.constants.SinglestacksapcpiorderexchangeConstants.PLATFORM_LOGO_CODE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.singlestack.service.SinglestacksapcpiorderexchangeService;


@Controller
public class SinglestacksapcpiorderexchangeHelloController
{
	@Autowired
	private SinglestacksapcpiorderexchangeService singlestacksapcpiorderexchangeService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(final ModelMap model)
	{
		model.addAttribute("logoUrl", singlestacksapcpiorderexchangeService.getHybrisLogoUrl(PLATFORM_LOGO_CODE));
		return "welcome";
	}
}
