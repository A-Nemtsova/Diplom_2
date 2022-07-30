package site.nomoreparties.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseApiClient{

    @Step("Получение ингредиентов")
    public Response getResponseIngredients(String accessToken) {
        return given()
                .spec(getSpecForJson(accessToken))
                .when()
                .get(BASE_URL + "/api/ingredients");
    }

    @Step("Создание тела запроса из списка ингредиентов (булка, мясо, соус) для заказа")
    public RequestOrder createRequestOrder(String accessToken) {
        List<String> hashIngredientList = new ArrayList<>();
        List<IngredientData> ingredientDataList = getResponseIngredients((accessToken)).as(ResponseIngredient.class).getData();
        //Добавление хеша булки
        hashIngredientList.add(ingredientDataList.get(0).get_id());
        //Добавление хеша мяса
        hashIngredientList.add(ingredientDataList.get(1).get_id());
        //Добавление хеша соуса
        hashIngredientList.add(ingredientDataList.get(4).get_id());

            return new RequestOrder(hashIngredientList);
        }


    public Response createOrder (RequestOrder requestOrder,String accessToken) {
        return given()
                .spec(getSpecForJson(accessToken))
                .body(requestOrder)
                .when()
                .post(BASE_URL + "/api/orders");
    }

}
