package uz.shopping.service;

import uz.shopping.entity.CartItem;
import uz.shopping.entity.Product;
import uz.shopping.entity.User;
import uz.shopping.repository.CartItemRepository;
import uz.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class CartServiceImplementation implements CartService{

    @Autowired
    private CartItemRepository cartRepo;

    @Autowired
    private ProductRepository productRepo;

    public List<CartItem> listCartItems(User user) {
        return cartRepo.findByUser(user);
    }

    public Integer addProduct(Integer product_id, Integer quantity, User user) {
        Integer addedQuantity = quantity;

        Product product = productRepo.findById(product_id).get();

        CartItem cartItem = cartRepo.findByUserAndProduct(user, product);

        if(cartItem != null) {
            addedQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(addedQuantity);
        } else {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setUser(user);
            cartItem.setProduct(product);
        }
        cartRepo.save(cartItem);


        return addedQuantity;
    }

    public void updateQuantity(Integer productId, Integer quantity, User user) {
        cartRepo.updateQuantity(quantity,productId,user.getId());
    }

    public void removeProduct(User user) {
        cartRepo.deleteByUser(user);
    }

    public void removeOneProductById(Integer productId, User user) {
        cartRepo.deleteByUserAndProduct(user.getId(), productId);
    }


}

