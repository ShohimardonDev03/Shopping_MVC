package uz.shopping.dto;


import uz.shopping.entity.Order;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderRequest {

    private Order order;

    private OrderRequest() {}

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
