package edu.ada.micronaut.service;

import edu.ada.micronaut.model.StockModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FinancialService
{
	List<StockModel> getDataFromYahoo(String[] stockIndices);
	List<StockModel> getDataFromAlphaVantage(String[] stockIndices);
}
