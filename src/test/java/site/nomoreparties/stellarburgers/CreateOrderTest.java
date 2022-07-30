package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.OrderClient;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.model.RequestOrder;
import site.nomoreparties.stellarburgers.model.RequestRegisterUser;
import site.nomoreparties.stellarburgers.model.ResponseError;
import site.nomoreparties.stellarburgers.model.ResponseOrderOk;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateOrderTest {

    UserClient userClient;
    RequestRegisterUser requestRegisterUser;
    Response responseRegisterUser;
    String accessToken;

    OrderClient orderClient;
    RequestOrder requestOrder;
    Response responseOrder;
    ResponseOrderOk responseOrderOk;
    ResponseError responseError;

    @Before
    @Step("Подготовка тестовых данных и регистрация пользователя")
    public void init() {
        userClient = new UserClient();
        requestRegisterUser = RequestRegisterUser.getRandomUser();
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        accessToken = responseRegisterUser.body().jsonPath().getString("accessToken");
        orderClient = new OrderClient();
        requestOrder = orderClient.createRequestOrder(accessToken);
    }

    @After
    @Step("Удаление созданного пользователя")
    public void clear() {
        if (responseRegisterUser.statusCode() == 200) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа (с авторизацией и ингредиентами)")
    @Description("Проверка, что при создании заказа приходит 200 кода ответа")
    public void createOrderWithAutorizationStatusCodePositiveTest () {
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        assertEquals(SC_OK, responseOrder.statusCode());
    }

    @Test
    @DisplayName("Создание заказа (с авторизацией и ингредиентами)")
    @Description("Проверка, что в ответе в параметре success приходит true")
    public void createOrderWithAutorizationCheckSuccessPositiveTest () {
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        responseOrderOk = responseOrder.as(ResponseOrderOk.class);
        assertTrue(responseOrderOk.isSuccess());
    }

    @Test
    @DisplayName("Создание заказа (с авторизацией и ингредиентами)")
    @Description("Проверка, что в ответе в параметр name приходит не пустой")
    public void createOrderWithAutorizationCheckNamePositiveTest () {
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        responseOrderOk = responseOrder.as(ResponseOrderOk.class);
        assertNotNull(responseOrderOk.getName());
    }

    @Test
    @DisplayName("Создание заказа (с авторизацией и ингредиентами)")
    @Description("Проверка, что в ответе в параметр name приходит не пустой")
    public void createOrderWithAutorizationCheckOrderNumberPositiveTest () {
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        responseOrderOk = responseOrder.as(ResponseOrderOk.class);
        assertNotNull(responseOrderOk.getOrder().getNumber());
    }

    /*
    В документации к API ничего не сказано, что при создании заказа без авторизации метод должен возвращать ошибку.
    Так же при проверке через Postman при отправке запроса без токена в authorization заказ успешно создается.
    Поэтому за правильное поведение принимаю, что заказ можно успешно создать без авторизации.
     */
    @Test
    @DisplayName("Создание заказа (без авторизацией и ингредиентами)")
    @Description("Проверка, что при создании заказа приходит 200 кода ответа")
    public void createOrderWithoutAutorizationStatusCodePositiveTest () {
        responseOrder = orderClient.createOrder(requestOrder, "");
        assertEquals(SC_OK, responseOrder.statusCode());
    }

    @Test
    @DisplayName("Создание заказа (без авторизацией и ингредиентами)")
    @Description("Проверка, что в ответе в параметре success приходит true")
    public void createOrderWithoutAutorizationCheckSuccessPositiveTest () {
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        responseOrderOk = responseOrder.as(ResponseOrderOk.class);
        assertTrue(responseOrderOk.isSuccess());
    }

    @Test
    @DisplayName("Создание заказа (без авторизацией и ингредиентами)")
    @Description("Проверка, что в ответе в параметр name приходит не пустой")
    public void createOrderWithoutAutorizationCheckNamePositiveTest () {
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        responseOrderOk = responseOrder.as(ResponseOrderOk.class);
        assertNotNull(responseOrderOk.getName());
    }

    @Test
    @DisplayName("Создание заказа (без авторизацией и ингредиентами)")
    @Description("Проверка, что в ответе в параметр name приходит не пустой")
    public void createOrderWithoutAutorizationCheckOrderNumberPositiveTest () {
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        responseOrderOk = responseOrder.as(ResponseOrderOk.class);
        assertNotNull(responseOrderOk.getOrder().getNumber());
    }

    /*
    Так как работа метода с авторизацией и без авторизации совпадают.
    И в документации к API нет описания, что поведение должно различаться,
    то проверки создания заказа без ингредиентов и с неверным хешом ингредиентов будут проводиться с авторизацией
     */

    @Test
    @DisplayName("Создание заказа (с авторизацией и без ингредиентамов)")
    @Description("Проверка, что при создании заказа приходит 400 кода ответа")
    public void createOrderWithoutIngredientsStatusCodeError () {
        requestOrder.setIngredients(new ArrayList<>());
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        assertEquals(SC_BAD_REQUEST, responseOrder.statusCode());
    }

    @Test
    @DisplayName("Создание заказа (с авторизацией и без ингредиентамов)")
    @Description("Проверка, что в ответе в параметре success приходит false")
    public void createOrderWithoutIngredientsCheckSuccessError () {
        requestOrder.setIngredients(new ArrayList<>());
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        responseError = responseOrder.as(ResponseError.class);
        assertFalse(responseError.isSuccess());
    }

    @Test
    @DisplayName("Создание заказа (с авторизацией и без ингредиентамов)")
    @Description("Проверка, что в ответе в параметре message приходит \"Ingredient ids must be provided\"")
    public void createOrderWithoutIngredientsCheckMessageError () {
        requestOrder.setIngredients(new ArrayList<>());
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        responseError = responseOrder.as(ResponseError.class);
        assertEquals("Ingredient ids must be provided", responseError.getMessage());
    }

    @Test
    @DisplayName("Создание заказа (с авторизацией и с невалидными хешами ингредиентамов)")
    @Description("Проверка, что при создании заказа приходит 500 кода ответа")
    public void createOrderInvalidIngredientsStatusCodeError () {
        List<String> newHashIngredients = new ArrayList<>();
        newHashIngredients.add(requestOrder.getIngredients().get(0) + "1");
        newHashIngredients.add(requestOrder.getIngredients().get(1) + "1");
        newHashIngredients.add(requestOrder.getIngredients().get(2) + "1");
        requestOrder.setIngredients(newHashIngredients);
        responseOrder = orderClient.createOrder(requestOrder, accessToken);
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseOrder.statusCode());
    }

}
