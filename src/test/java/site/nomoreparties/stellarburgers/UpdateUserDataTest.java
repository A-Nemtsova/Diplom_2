package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.model.*;
import site.nomoreparties.stellarburgers.model.requests.RequestRegisterUser;
import site.nomoreparties.stellarburgers.model.responses.ResponseError;
import site.nomoreparties.stellarburgers.model.responses.ResponseRegisterAndLoginUserOk;
import site.nomoreparties.stellarburgers.model.responses.ResponseUserDataOk;

import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

@Story("Изменение данных пользователя")
public class UpdateUserDataTest {
    /*
    При прогоне тестов рандомно падают некоторые тесты с 429 ошибкой "Too Many Requests".
    Если каждый тест запускать по отдельности, то все нормально, падений нет.
    Обратилась с данной проблемой к наставнику, на что получила ответ, что "эту ошибку можно игнорировать", это по сути "дефект приложения".
     */

    UserClient userClient;
    RequestRegisterUser requestRegisterUser;
    Response responseRegisterUser;
    String accessToken;
    User user;
    String email;
    String name;
    Response responseUserData;

    @Before
    @Step("Подготовка тестовых данных и регистрация пользователя")
    public void init() {
        userClient = new UserClient();
        requestRegisterUser = RequestRegisterUser.getRandomUser();
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        accessToken = responseRegisterUser.body().jsonPath().getString("accessToken");
        user = responseRegisterUser.as(ResponseRegisterAndLoginUserOk.class).getUser();
        name = user.getName();

    }

    @After
    @Step("Удаление созданного пользователя")
    public void clear() {
        if (responseRegisterUser.statusCode() == 200) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Обновление информации о пользователе (с авторизацией)")
    @Description("Проверка, что при успешном обновлении приходит 200 кода ответа")
    public void updateUserDataWithAutorizationStatusCodePositiveTest () {
        user.setEmail("new" + email);
        user.setName("New" + name);
        responseUserData = userClient.updateUser(user, accessToken);
        assertEquals(SC_OK, responseUserData.statusCode());
    }

    @Test
    @DisplayName("Обновление информации о пользователе (с авторизацией)")
    @Description("Проверка, что в ответе в параметре success приходит true")
    public void updateUserDataWithAutorizationCheckSuccessPositiveTest () {
        user.setEmail("new" + email);
        responseUserData = userClient.updateUser(user, accessToken);
        ResponseUserDataOk responseUserDataOk =  responseUserData.as(ResponseUserDataOk.class);
        assertTrue(responseUserDataOk.isSuccess());
    }

    @Test
    @DisplayName("Обновление информации о пользователе (с авторизацией)")
    @Description("Проверка, что в ответе приходит обновленный email")
    public void updateUserDataWithAutorizationCheckEmailPositiveTest () {
        user.setEmail("new" + email);
        responseUserData = userClient.updateUser(user, accessToken);
        ResponseUserDataOk responseUserDataOk =  responseUserData.as(ResponseUserDataOk.class);
        assertEquals(("new" + email).toLowerCase(),responseUserDataOk.getUser().getEmail());
    }

    @Test
    @DisplayName("Обновление информации о пользователе (с авторизацией)")
    @Description("Проверка, что в ответе приходит обновленный name")
    public void updateUserDataWithAutorizationCheckNamePositiveTest () {
        user.setName("New"+ name);
        responseUserData = userClient.updateUser(user, accessToken);
        ResponseUserDataOk responseUserDataOk =  responseUserData.as(ResponseUserDataOk.class);
        assertEquals(("New" + name),responseUserDataOk.getUser().getName());
    }

    @Test
    @DisplayName("Обновление информации о пользователе (без авторизацией)")
    @Description("Проверка, что без accessToken приходит 401 кода ответа")
    public void updateUserDataWithoutAutorizationStatusCodeError () {
        user.setEmail("new" + email);
        user.setName("New" + name);
        accessToken = "";
        responseUserData = userClient.updateUser(user, accessToken);
        assertEquals(SC_UNAUTHORIZED, responseUserData.statusCode());
    }

    @Test
    @DisplayName("Обновление информации о пользователе (без авторизацией)")
    @Description("Проверка, что в ответе в параметре success приходит false")
    public void updateUserDataWithoutAutorizationCheckSuccessError () {
        user.setEmail("new" + email);
        user.setName("New" + name);
        accessToken = "";
        responseUserData = userClient.updateUser(user, accessToken);
        ResponseError responseError =  responseUserData.as(ResponseError.class);
        assertFalse(responseError.isSuccess());
    }

    @Test
    @DisplayName("Обновление информации о пользователе (без авторизацией)")
    @Description("Проверка, что в ответе в параметре message приходит \"You should be authorised\"")
    public void updateUserDataWithoutAutorizationCheckMessageError () {
        user.setEmail("new" + email);
        user.setName("New" + name);
        accessToken = "";
        responseUserData = userClient.updateUser(user, accessToken);
        ResponseError responseError =  responseUserData.as(ResponseError.class);
        assertEquals("You should be authorised",responseError.getMessage());
    }
}
