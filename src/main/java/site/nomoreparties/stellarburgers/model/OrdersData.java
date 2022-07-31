package site.nomoreparties.stellarburgers.model;

import java.util.List;

public class OrdersData {
    private List<String> ingredients;
    private String _id;
    private String status;
    private int number;
    private String createdAt;
    private String updatedAt;

    public List<String> getIngredients() {
        return ingredients;
    }

    public String get_id() {
        return _id;
    }

    public String getStatus() {
        return status;
    }

    public int getNumber() {
        return number;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
