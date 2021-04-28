package edu.ada.micronaut.service;

public interface FinancialService
{
	Object getDataFromYahoo(String stockIndex);
	Object getDataFromAlphaVantage(String stockIndex);
}
