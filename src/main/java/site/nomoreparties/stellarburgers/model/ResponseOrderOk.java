package site.nomoreparties.stellarburgers.model;

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
