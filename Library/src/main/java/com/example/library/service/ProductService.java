package com.example.library.service;

import com.example.library.dto.ProductDto;
import com.example.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface ProductService {

    List<Product> findAll();

    List<ProductDto> allProducts();

    Product save(List<MultipartFile> imageProduct, ProductDto product);

    ProductDto findById(long id);

    Product update(List<MultipartFile> imageProduct,ProductDto productDto);

    void disable(long id);

    void enable(long id);

    Page<ProductDto> findAllByActivated(long id, int pageNo);
    Page<ProductDto> findAllByActivated(int pageNo,String sort);

    List<ProductDto> findAllProducts();

    List<ProductDto> findAllByOrderDesc();

    void deleteProduct(long id);

    Long countAllProducts();

    List<Object[]> getProductStats();

    List<Object[]> getProductsStatsBetweenDates(Date startDate,Date endDate);

    Product findBYId(long id);

    List<Product> findProductsByCategory(long id);

    Page<ProductDto> searchProducts(int pageNo,String keyword);

}
