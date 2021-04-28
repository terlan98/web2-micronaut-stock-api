package edu.ada.micronaut.controller.impl;

import edu.ada.micronaut.controller.FinancialController;
import edu.ada.micronaut.service.FinancialService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Controller(value = "/finance")
public class FinancialControllerImpl implements FinancialController
{
	private static final Logger logger = LoggerFactory.getLogger(FinancialControllerImpl.class);
	
	@Inject
	private FinancialService financialService;
	
	@Override
	@Get(value = "/data")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getFinancialData(
			@QueryValue("provider") String dataProviderName,
			@QueryValue("index") String stockIndex)
	{
		String result = "";
		
		logger.info("Received request for stocks: " + stockIndex);
		dataProviderName = dataProviderName.toUpperCase();
		
		String[] stockIndices = stockIndex.split(",");
		
		for (String index : stockIndices)
		{
			logger.info("Requesting " + index + " from " + dataProviderName);
			switch (dataProviderName)
			{
				case "YAHOO":
					result += financialService.getDataFromYahoo(index) + "\n";
					break;
				case "ALPHAVANTAGE":
					result += financialService.getDataFromAlphaVantage(index) + "\n";
					break;
			}
		}
		
		return result;
	}
}
