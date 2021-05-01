package edu.ada.micronaut.controller.impl;

import edu.ada.micronaut.controller.FinancialController;
import edu.ada.micronaut.model.StockModel;
import edu.ada.micronaut.service.FinancialService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
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
		List<StockModel> result = null;
		
		logger.info("Received request for provider :: {} and stock :: {}", dataProviderName, stockIndex);
		dataProviderName = dataProviderName.toUpperCase();
		
		String[] stockIndices = stockIndex.split(",");
		
		switch (dataProviderName)
		{
			case "YAHOO":
				result = financialService.getDataFromYahoo(stockIndices);
				break;
			case "ALPHAVANTAGE":
				result = financialService.getDataFromAlphaVantage(stockIndices);
				break;
			default:
				return HttpResponse.badRequest("Please make sure that the provider name is valid");
		}
		
		return result;
	}
}
