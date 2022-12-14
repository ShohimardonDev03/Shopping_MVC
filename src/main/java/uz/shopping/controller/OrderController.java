package uz.shopping.controller;


import uz.shopping.dto.OrderRequest;
import uz.shopping.entity.CartItem;
import uz.shopping.entity.Order;
import uz.shopping.entity.User;
import uz.shopping.repository.OrderRepository;
import uz.shopping.repository.UserRepository;
import uz.shopping.service.CartService;
import uz.shopping.service.OrderServiceImplementation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderServiceImplementation orderServiceImplementation;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    private final double TAX = 0.2533;

    @RequestMapping(value = "/createOrder", method = {RequestMethod.POST})
    public String create(ModelMap model, HttpServletRequest req, RedirectAttributes attr, OrderRequest orderRequest) {
        if (StringUtils.equals(req.getMethod(), RequestMethod.POST.toString())) {
            return orderServiceImplementation.saveOrder(orderRequest.getOrder(), attr, req);
        }
        return invalidRequestResponse(attr);
    }

    @GetMapping(value = {"/cartt"})
    public String cart() {
        return "cart";
    }

    private String invalidRequestResponse(RedirectAttributes attr) {
        attr.addFlashAttribute("error", "Invalid Request Method");
        return "redirect:/";
    }

    @GetMapping("/orders")
    public String showShoppingCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername());
        List<Order> orders = orderRepository.findAllByUser(user);
        List<CartItem> cartItems = cartService.listCartItems(user);
        double cartSum = cartItems.stream().mapToDouble(o -> o.getProduct().getPrice()).sum();
        double totalCartSum = Math.floor((cartSum + cartSum * TAX) * 100) / 100;
        int totalCartItems = cartItems.stream().mapToInt(el -> el.getQuantity()).sum();

        model.addAttribute("userDetails", userDetails);
        model.addAttribute("cartSum", cartSum);
        model.addAttribute("orders", orders);
        model.addAttribute("totalCartSum", totalCartSum);
        model.addAttribute("totalCartItems", totalCartItems);

        return "orders";
    }
}
