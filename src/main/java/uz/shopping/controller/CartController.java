package uz.shopping.controller;

import uz.shopping.entity.CartItem;
import uz.shopping.entity.Order;
import uz.shopping.entity.Product;
import uz.shopping.entity.User;
import uz.shopping.repository.OrderRepository;
import uz.shopping.repository.UserRepository;
import uz.shopping.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderServiceImplementation orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final double TAX = 0.2533;

    @GetMapping("/cart")
    public String showShoppingCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername());
        List<CartItem> cartItems = cartService.listCartItems(user);
        double cartSum = cartItems.stream().mapToDouble(o -> o.getProduct().getPrice()).sum();
        double totalCartSum = Math.floor((cartSum + cartSum * TAX) * 100) / 100;
        int totalCartItems = cartItems.stream().mapToInt(el -> el.getQuantity()).sum();
        boolean disableCheckoutButton = cartItems.isEmpty();

        model.addAttribute("userDetails", userDetails);
        model.addAttribute("cartSum", cartSum);
        model.addAttribute("totalCartSum", totalCartSum);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalCartItems", totalCartItems);
        model.addAttribute("disableCheckoutButton", disableCheckoutButton);

        return "cart";
    }

    @GetMapping("/cart/add/{id}/{qty}")
    public String add(@PathVariable("id") int id, @PathVariable("qty") int quantity,
                      @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null || userDetails instanceof AnonymousAuthenticationToken) {
            return "redirect:/";
        }
        User user = userRepository.findByUsername(userDetails.getUsername());

        Integer addedQuantity = cartService.addProduct(id, quantity, user);
        System.out.println("product added");

        return "redirect:/cart";
    }

    @RequestMapping(value = "/cartt/update/{id}/{qty}", method = {RequestMethod.POST})
    public String update(@PathVariable("id") int id, @PathVariable("qty") int quantity,
                         @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername());

        if(user == null) {
            return "redirect:/";
        }

        cartService.updateQuantity(id, quantity, user);
        return "redirect:/cartt";
    }

    @GetMapping("/cart/remove/{id}")
    public String remove(@PathVariable("id") int id,
                         @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername());

        if(user == null) {
            return "redirect:/";
        }
        cartService.removeOneProductById(id, user);

        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest req, RedirectAttributes attr) {
            System.out.println("1");
        List<Product> listProducts = new ArrayList<>();
        int quantity = 0;
        float total = 0;
        Date orderDate = new Date();

        User user = userRepository.findByUsername(userDetails.getUsername());
        List<CartItem> cartItems = cartService.listCartItems(user);

        for(CartItem item: cartItems) {
            listProducts.add(item.getProduct());
            quantity += item.getQuantity();
            total += item.getProduct().getPrice()  + item.getProduct().getPrice() * TAX;
            cartService.removeProduct(user);
        }

        Order order = new Order(user, quantity, total, orderDate, listProducts);
        orderService.saveOrder(order, attr, req);

        model.addAttribute("userDetails", userDetails);
        model.addAttribute("total", total);

        return "checkout";
    }
}

