package edu.ucla.cs.cs144;

import java.io.IOException;


public class Item {
	private String id;
	private String name;
	private String price;
	
	public Item() {}
	public Item(String id, String name, String price)
	{
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	public String getId() {return id;}
	public String getName() {return name;}
	public String getPrice() {return price;}
}