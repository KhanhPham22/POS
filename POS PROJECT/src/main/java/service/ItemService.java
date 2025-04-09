package service;
import java.util.ArrayList;
import java.util.List;

import model.Item;

public interface ItemService {
boolean createItem(Item item) ;
	
	boolean deleteItem(long itemId) ;
	
	boolean updateItem(Item newItem) ;
	
	boolean updateListItem(Item[] items) ;
	
	Item getItem(long itemId) ;
	
	List<Item> getAllItems() ;
	
	List<Item> getItemByName(String input) ;
	
	public List<Item> findItem(String input) ;
}
