package edu.ada.micronaut.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class StockModel implements Serializable
{
	private String name;
	private double price;
}
