package uz.shopping.service;

import uz.shopping.entity.CartItem;
import uz.shopping.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    List<CartItem> listCartItems(User user);
    Integer addProduct(Integer product_id, Integer quantity, User user);
    void updateQuantity(Integer productId, Integer quantity, User user);
    void removeProduct(User user);
    void removeOneProductById(Integer productId, User user);

}
