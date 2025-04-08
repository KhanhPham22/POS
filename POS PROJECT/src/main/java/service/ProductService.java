package service;

import java.util.List;

import model.Product;



public interface ProductService {

    boolean createProduct(Product product);

    boolean updateProduct(Product product);

    boolean deleteProductById(long productId);

    boolean deleteProduct(Product product);

    Product getProductById(long productId);

    List<Product> getAllProducts();
}


