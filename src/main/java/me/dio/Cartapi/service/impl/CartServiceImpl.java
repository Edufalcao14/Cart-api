package me.dio.Cartapi.service.impl;

import lombok.RequiredArgsConstructor;
import me.dio.Cartapi.enumeration.PaymentMethod;
import me.dio.Cartapi.models.Cart;
import me.dio.Cartapi.models.Item;
import me.dio.Cartapi.models.Product;
import me.dio.Cartapi.models.Restaurant;
import me.dio.Cartapi.repository.CartRepository;
import me.dio.Cartapi.repository.ItemRepository;
import me.dio.Cartapi.repository.ProductRepository;
import me.dio.Cartapi.resource.dto.ItemDto;
import me.dio.Cartapi.service.CartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    private final ItemRepository itemRepository;

    @Override
    public Cart seeCart(Long id) {
        return cartRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("This cart does not exist");
                }
        );
    }

    @Override
    public Cart closeCart(Long id, int numberPaymentMethod) {
        Cart cart = seeCart(id);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Include items in cart");
        }
        PaymentMethod paymentMethod =
                numberPaymentMethod == 0 ? PaymentMethod.CREDITCARD : PaymentMethod.DEBITCARD;
        cart.setPaymentMethod(paymentMethod);
        cart.setClosed(true);
        return cartRepository.save(cart);
    }

    @Override
    public Item addToCart(ItemDto itemDto) {
        Cart cart = seeCart(itemDto.getCartId());
        if (cart.isClosed()) {
            throw new RuntimeException("This cart is closed");
        }
        Item itemToAdd = Item.builder()
                .quantity(itemDto.getQuantity())
                .cart(cart)
                .product((Product) productRepository.findById(itemDto.getProductId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("This product does not exist");
                        }
                ))
                .build();

        List<Item> itemsFromCart = cart.getItems();
        if (itemsFromCart.isEmpty()) {
            itemsFromCart.add(itemToAdd);
        } else {
            Restaurant currentRestaurant = itemsFromCart.get(0).getProduct().getRestaurant();
            Restaurant restaurantItemToAdd = itemToAdd.getProduct().getRestaurant();

            if (currentRestaurant.equals(restaurantItemToAdd)) {
                itemsFromCart.add(itemToAdd);
            } else {
                throw new RuntimeException("You cannot add products from different restaurants. close or empty your cart! ");
            }
        }

        List<Double> priceItems = new ArrayList<>();

        for (Item itemFromCart : itemsFromCart){
            double totalPriceItems = itemFromCart.getProduct().getUnitPrice() * itemFromCart.getQuantity();

            priceItems.add(totalPriceItems);
        }

        double totalPriceCart = priceItems.stream()
                .mapToDouble(totalPriceOfEachItem -> totalPriceOfEachItem)
                .sum();

        cart.setTotalAmount(totalPriceCart);
        cartRepository.save(cart);
        return itemToAdd;
    }

    @Override
    public Cart deleteCart(Long cartId) {
        Cart cart = seeCart(cartId);
        if (cart.isClosed()) {
            throw new RuntimeException("You cannot delete a closed cart");
        }


        Cart cartDeleted = cart;
        cartRepository.delete(cart);
        return cartDeleted;
    }

    @Override
    public Item deleteItem(long itemId) {
      Item itemToDelete = itemRepository.findById(itemId).orElseThrow(
              () -> {
                  throw new RuntimeException("This product does not exist");
              }
      );
      Cart cart = itemToDelete.getCart();

     if (cart.isClosed()){
         throw new RuntimeException("You cannot delete a Item in a closed cart");
     }

     List<Item> itemsFromCart = cart.getItems();


     itemsFromCart.remove(itemToDelete);

        double totalPriceItemToDelete = itemToDelete.getProduct().getUnitPrice() * itemToDelete.getQuantity();
        double totalPriceCarBeforeDelete = cart.getTotalAmount();

        double currentTotalPrice = totalPriceCarBeforeDelete - totalPriceItemToDelete;
        cart.setTotalAmount(currentTotalPrice);
        cartRepository.save(cart);
    return itemToDelete;
    }

}
