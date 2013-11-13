package org.arquillian.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

public class Basket implements Serializable {

	private List<String> items;
	@EJB
	private OrderRepository orderRepo;
	
	public void addItem(String item) {
		items.add(item);
	}
	
	public List<String> getItems() {
		return Collections.unmodifiableList(items);
	}
	
	public int getItemCount() {
		return items.size();
	}
	
	public void placeOrder() {
		orderRepo.addOrder(items);
		items.clear();
	}
	
	@PostConstruct
	void init() {
		items = new ArrayList<String>();
	}
}
