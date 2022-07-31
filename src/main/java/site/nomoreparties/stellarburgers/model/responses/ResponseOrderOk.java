package site.nomoreparties.stellarburgers.model.responses;

import site.nomoreparties.stellarburgers.model.Order;

public class ResponseOrderOk {
    private String name;
    private Order order;
    private boolean success;

    public String getName() {
        return name;
    }

    public Order getOrder() {
        return order;
    }

    public boolean isSuccess() {
        return success;
    }
}
