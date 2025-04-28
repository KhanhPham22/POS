package service;
import java.util.ArrayList;
import java.util.List;

import model.Item;
import model.Supplier;

public interface ItemService {
    boolean createItem(Item item);
    
    boolean deleteItem(long itemId);
    
    boolean updateItem(Item item);
    
    boolean updateListItem(Item[] items);
    
    Item getItem(long itemId);
    
    List<Item> getAllItems(int pageNumber, int pageSize);
    
    List<Item> getItemByName(String input);
    
    List<Item> getItemsBySupplierId(long supplierId, int pageNumber, int pageSize);
   
}
