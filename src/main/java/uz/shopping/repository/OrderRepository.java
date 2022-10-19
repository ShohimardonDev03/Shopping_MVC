package uz.shopping.repository;

import uz.shopping.entity.Order;
import uz.shopping.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order,Integer> {
    Order findById(int id);
    List<Order> findAllByUser(User user);
}
