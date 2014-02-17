package edu.ucla.cs.cs144;

import java.io.IOException;
import java.util.ArrayList;
public class ShoppingCart{
	
	private ArrayList<Item> items;
	
	public ShoppingCart() {}
	public ShoppingCart(String id, String name, String price)
	{
		Item first = new Item(id, name, price);
		items.add(first);
	}
	
	public void addEntry(String id, String name, String price) {
		Item newEntry = new Item(id, name, price);
		items.add(newEntry);
	}
	public Item getEntry(String id) {
		for (int i = 0; i < items.size(); i ++) {
			if(items.get(i).getId().equals(id)) {
				return items.get(i);
			}
		}
		return null;
	}
	
	public boolean isInCart(String id) {
		for (int i = 0; i < items.size(); i ++) {
			if(items.get(i).getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public int getSize() {return items.size();}
}