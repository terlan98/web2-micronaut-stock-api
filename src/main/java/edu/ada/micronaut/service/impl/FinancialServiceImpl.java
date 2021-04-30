package edu.ada.micronaut.service.impl;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.DataType;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import edu.ada.micronaut.model.StockModel;
import edu.ada.micronaut.service.FinancialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.inject.Singleton;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class FinancialServiceImpl implements FinancialService
{
	private static final Logger logger = LoggerFactory.getLogger(FinancialServiceImpl.class);
	
	@Override
	public List<StockModel> getDataFromYahoo(String[] stockIndices)
	{
		List<StockModel> stockModels = new ArrayList<>();
		
		for (int i = 0; i < stockIndices.length; i++) // converting all indices to uppercase
		{
			stockIndices[i] = stockIndices[i].toUpperCase();
		}
		
		try
		{
			BigDecimal price;
			
			if (stockIndices.length == 1) // single mode
			{
				Stock stock = YahooFinance.get(stockIndices[0]);
				price = stock.getQuote(true).getPrice();
				stockModels.add(new StockModel(stockIndices[0], price.doubleValue()));
			}
			else // batch mode
			{
				Map<String, Stock> stockMap = YahooFinance.get(stockIndices);
				
				for (String stockIndex : stockMap.keySet())
				{
					double stockPrice = stockMap.get(stockIndex).getQuote(true).getPrice().doubleValue();
					StockModel stockModel = new StockModel(stockIndex, stockPrice);
					stockModels.add(stockModel);
				}
			}
			
		} catch (IOException e)
		{
			logger.error(e.getMessage());
		}
		
		return stockModels;
	}
	
	@Override
	public List<StockModel> getDataFromAlphaVantage(String[] stockIndices)
	{
		// AlphaVantage tutorial: https://github.com/crazzyghost/alphavantage-java
		List<StockModel> stockModels = new ArrayList<>();
		Config cfg = Config.builder().key("J9Z16M0XBTMKWJQ9").timeOut(10).build();
		AlphaVantage.api().init(cfg);
		
		for (String stockIndex : stockIndices)
		{
			TimeSeriesResponse response = AlphaVantage.api()
					.timeSeries()
					.intraday()
					.forSymbol(stockIndex)
					.outputSize(OutputSize.COMPACT)
					.dataType(DataType.JSON)
					.fetchSync();
			
			List<StockUnit> stockUnits = response.getStockUnits();
			
			if (stockUnits != null && !stockUnits.isEmpty())
			{
				double stockPrice = response.getStockUnits().get(0).getClose();
				stockModels.add(new StockModel(stockIndex, stockPrice));
			}
		}
		
		return stockModels;
	}
}
