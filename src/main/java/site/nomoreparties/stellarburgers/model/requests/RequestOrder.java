package site.nomoreparties.stellarburgers.model.requests;

import java.util.List;

public class RequestOrder {
    private List<String> ingredients;

    public RequestOrder(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }



    /*
    public RequestOrder(List<HashIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<HashIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<HashIngredient> ingredients) {
        this.ingredients = ingredients;
    }

 */
}
