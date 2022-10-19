package uz.shopping.repository;

import uz.shopping.entity.Category;
import uz.shopping.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findById(int id);

    Product findByName(String name);

    List<Product> findAllByOrderByIdAsc();

    List<Product> findAllByCategory(Category category);
}
