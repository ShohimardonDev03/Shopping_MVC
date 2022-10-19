package uz.shopping.service;

import uz.shopping.entity.Order;
import uz.shopping.entity.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    String saveOrder(Order order, RedirectAttributes attr, HttpServletRequest req);
    Order findById(int id);
    List<Order> findAllByUser(User user);
}
