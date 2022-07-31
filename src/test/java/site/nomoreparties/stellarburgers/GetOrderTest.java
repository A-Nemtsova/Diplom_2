package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.OrderClient;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.model.requests.RequestOrder;
import site.nomoreparties.stellarburgers.model.requests.RequestRegisterUser;
import site.nomoreparties.stellarburgers.model.responses.ResponseError;
import site.nomoreparties.stellarburgers.model.responses.ResponseGetOrdersOk;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

@Story("Получение заказа")
public class GetOrderTest {
    /*
    При прогоне тестов рандомно падают некоторые тесты с 429 ошибкой "Too Many Requests".
    Если каждый тест запускать по отдельности, то все нормально, падений нет.
    Обратилась с данной проблемой к наставнику, на что получила ответ, что "эту ошибку можно игнорировать", это по сути "дефект приложения".
     */

    UserClient userClient;
    RequestRegisterUser requestRegisterUser;
    Response responseRegisterUser;
    String accessToken;
    OrderClient orderClient;
    RequestOrder requestOrder;
    Response responseOrder;
    ResponseGetOrdersOk responseGetOrdersOk;
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
    @DisplayName("Получене заказа конкретного пользователя (с авторизацией)")
    @Description("Проверка, что при создании заказа приходит 200 кода ответа")
    public void getOrderForUserWithAutorizationStatusCodePositiveTest () {
        orderClient.createOrder(requestOrder, accessToken);
        responseOrder = orderClient.getOrdersForUser(accessToken);
        assertEquals(SC_OK, responseOrder.statusCode());
    }

    @Test
    @DisplayName("Получене заказа конкретного пользователя (с авторизацией)")
    @Description("Проверка, что в ответе в параметре success приходит true")
    public void getOrderForUserWithAutorizationCheckSuccessPositiveTest () {
        orderClient.createOrder(requestOrder, accessToken);
        responseOrder = orderClient.getOrdersForUser(accessToken);
        responseGetOrdersOk = responseOrder.as(ResponseGetOrdersOk.class);
        assertTrue(responseGetOrdersOk.isSuccess());
    }

    @Test
    @DisplayName("Получене заказа конкретного пользователя (с авторизацией)")
    @Description("Проверка, что в ответе приходит не пустой total")
    public void getOrderForUserWithAutorizationCheckTotalPositiveTest () {
        orderClient.createOrder(requestOrder, accessToken);
        responseOrder = orderClient.getOrdersForUser(accessToken);
        responseGetOrdersOk = responseOrder.as(ResponseGetOrdersOk.class);
        assertNotNull(responseGetOrdersOk.getTotal());
    }

    @Test
    @DisplayName("Получене заказа конкретного пользователя (с авторизацией)")
    @Description("Проверка, что в ответе приходит не пустой totalToday")
    public void getOrderForUserWithAutorizationCheckTotalTodayPositiveTest () {
        //orderClient.createOrder(requestOrder, accessToken);
        responseOrder = orderClient.getOrdersForUser(accessToken);
        responseGetOrdersOk = responseOrder.as(ResponseGetOrdersOk.class);
        assertNotNull(responseGetOrdersOk.getTotalToday());
    }

    @Test
    @DisplayName("Получене заказа конкретного пользователя (без авторизацией)")
    @Description("Проверка, что при создании заказа приходит 401 кода ответа")
    public void getOrderForUserWithoutAutorizationStatusCodeError () {
        //orderClient.createOrder(requestOrder, accessToken);
        responseOrder = orderClient.getOrdersForUser("");
        assertEquals(SC_UNAUTHORIZED, responseOrder.statusCode());
    }

    @Test
    @DisplayName("Получене заказа конкретного пользователя (без авторизацией)")
    @Description("Проверка, что в ответе в параметре success приходит false")
    public void getOrderForUserWithoutAutorizationCheckSuccessError () {
        //orderClient.createOrder(requestOrder, accessToken);
        responseOrder = orderClient.getOrdersForUser("");
        responseError = responseOrder.as(ResponseError.class);
        assertFalse(responseError.isSuccess());
    }

    @Test
    @DisplayName("Получене заказа конкретного пользователя (без авторизацией)")
    @Description("Проверка, что в ответе в параметре message приходит \"You should be authorised\"")
    public void getOrderForUserWithoutAutorizationCheckMessageError () {
        //orderClient.createOrder(requestOrder, accessToken);
        responseOrder = orderClient.getOrdersForUser("");
        responseError = responseOrder.as(ResponseError.class);
        assertEquals("You should be authorised", responseError.getMessage());
    }
}
