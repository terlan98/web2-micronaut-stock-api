package edu.ada.micronaut.controller;

public interface FinancialController
{
	Object getFinancialData(String dataProviderName, String stockIndex);
}
