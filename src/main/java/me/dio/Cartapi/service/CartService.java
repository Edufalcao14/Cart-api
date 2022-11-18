package me.dio.Cartapi.service;

import me.dio.Cartapi.models.Cart;
import me.dio.Cartapi.models.Item;
import me.dio.Cartapi.resource.dto.ItemDto;

public interface CartService {
    Cart seeCart (Long id);
    Cart closeCart (Long id, int paymentMethod);
    Item addToCart (ItemDto itemDto);
    Cart deleteCart (Long cartId);


}
