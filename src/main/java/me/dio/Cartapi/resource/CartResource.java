package me.dio.Cartapi.resource;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.dio.Cartapi.models.Cart;
import me.dio.Cartapi.models.Item;
import me.dio.Cartapi.resource.dto.ItemDto;
import me.dio.Cartapi.service.CartService;
import org.springframework.web.bind.annotation.*;

@Api(value="/api-cart/cart")

@RestController
@RequestMapping("/api-cart/cart")
@RequiredArgsConstructor
public class CartResource {
    private final CartService cartService;

    @PostMapping
    public Item  addToCart (@RequestBody ItemDto itemDto){
        return cartService.addToCart(itemDto);
    }

   @GetMapping("/{id}")
    public Cart seeCart(@PathVariable("id") long id){
        return cartService.seeCart(id);

    }
    @PatchMapping("/closeCart/{idCart}")
    public Cart closeCart(@PathVariable("idCart") Long idCart , @RequestParam("paymentMethod") int methodPayment){
        return cartService.closeCart(idCart, methodPayment);
    }
    @DeleteMapping ("/deleteCart/{cartId}")
    @ResponseBody
   public Cart deleteCart (@PathVariable("cartId") Long cartId){
        return cartService.deleteCart(cartId);
   }
}
