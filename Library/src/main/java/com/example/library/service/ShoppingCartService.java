package com.example.library.service;


import com.example.library.dto.ProductDto;
import com.example.library.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username, Long size);

    ShoppingCart updateCart(ProductDto productDto, int quantity, String username,Long cart_Item_Id,long size_id);

    ShoppingCart removeItemFromCart(ProductDto productDto, String username);

    ShoppingCart updateTotalPrice(Double newTotalPrice,String username);

    void deleteCartById(long id);
}
