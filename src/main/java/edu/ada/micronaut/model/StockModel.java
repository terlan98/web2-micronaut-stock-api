package edu.ada.micronaut.model;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Introspected
@Data
@AllArgsConstructor
public class StockModel implements Serializable
{
	private String name;
	private double price;
}
