package service;

import java.util.List;


import model.Product;



public interface ProductService {

    boolean createProduct(Product product);

    boolean updateProduct(Product product);

    boolean deleteProductById(long productId);

    boolean deleteProduct(Product product);

    Product getProductById(long productId);

    Product getProductByName(String name);
    
    public Product findbyEAN13(String ean13) ;
    
    List<Product> getAllProducts(int pageNumber, int pageSize);
}


