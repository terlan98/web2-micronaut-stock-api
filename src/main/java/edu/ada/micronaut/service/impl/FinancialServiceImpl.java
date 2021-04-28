package edu.ada.micronaut.service.impl;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.DataType;
import com.crazzyghost.alphavantage.parameters.Interval;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import edu.ada.micronaut.service.FinancialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.inject.Singleton;
import java.io.IOException;
import java.math.BigDecimal;

@Singleton
public class FinancialServiceImpl implements FinancialService
{
	private static final Logger logger = LoggerFactory.getLogger(FinancialServiceImpl.class);
	
	@Override
	public Object getDataFromYahoo(String stockIndex)
	{
		stockIndex = stockIndex.toUpperCase();
		
		try
		{
			Stock stock = YahooFinance.get(stockIndex);
			BigDecimal price = stock.getQuote(true).getPrice();
			return price;
			
		} catch (IOException e)
		{
			logger.error(e.getMessage());
		}
		
		return "Couldn't get data from Yahoo";
	}
	
	@Override
	public Object getDataFromAlphaVantage(String stockIndex)
	{
		// AlphaVantage tutorial: https://github.com/crazzyghost/alphavantage-java
		// AlphaVantage KEY: J9Z16M0XBTMKWJQ9
		Config cfg = Config.builder().key("J9Z16M0XBTMKWJQ9").timeOut(10).build();
		AlphaVantage.api().init(cfg);
		
		TimeSeriesResponse response = AlphaVantage.api()
				.timeSeries()
				.intraday()
				.forSymbol(stockIndex)
				.outputSize(OutputSize.COMPACT)
				.dataType(DataType.JSON)
				.fetchSync();
		
		return response.getStockUnits().get(0).getClose();
	}
}
