package site.nomoreparties.stellarburgers.model.responses;

import site.nomoreparties.stellarburgers.model.OrdersData;

import java.util.List;

public class ResponseGetOrdersOk {
    private boolean success;
    private List<OrdersData> orders;
    private  int total;
    private int totalToday;

    public boolean isSuccess() {
        return success;
    }

    public List<OrdersData> getOrders() {
        return orders;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalToday() {
        return totalToday;
    }
}
