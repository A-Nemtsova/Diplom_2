package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.model.RequestLoginUser;
import site.nomoreparties.stellarburgers.model.RequestRegisterUser;
import site.nomoreparties.stellarburgers.model.ResponseError;
import site.nomoreparties.stellarburgers.model.ResponseRegisterAndLoginUserOk;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

@Story("Логин пользователя")
public class LoginUserTest {
    RequestRegisterUser requestRegisterUser;
    RequestLoginUser requestLoginUser;
    UserClient userClient;
    Response responseRegisterUser;
    String accessToken;
    Response responseLoginUser;

    @Before
    @Step("Подготовка тестовых данных и регистрация пользователя")
    public void init() {
        userClient = new UserClient();
        requestRegisterUser = RequestRegisterUser.getRandomUser();
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        requestLoginUser = new RequestLoginUser(requestRegisterUser.getEmail(), requestRegisterUser.getPassword());
    }

    @After
    @Step("Удаление созданного пользователя")
    public void clear() {
        if (responseRegisterUser.statusCode() == 200) {
            //userClient.deleteUser(userClient.getAccessToken(requestLoginUser));
            accessToken = responseRegisterUser.body().jsonPath().getString("accessToken");
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Авторизация сущетвующего пользователя")
    @Description("Проверка, что при успешной авторизации приходит 200 кода ответа")
    public void loginUserStatusCodePositiveTest() {
        responseLoginUser = userClient.loginUser(requestLoginUser);
        assertEquals(SC_OK, responseLoginUser.statusCode());
    }

    @Test
    @DisplayName("Авторизация сущетвующего пользователя")
    @Description("Проверка, что в ответе в параметре success приходит true")
    public void loginUserCheckSuccessPositiveTest() {
        responseLoginUser = userClient.loginUser(requestLoginUser);
        ResponseRegisterAndLoginUserOk responseLoginUserOk = responseLoginUser.as(ResponseRegisterAndLoginUserOk.class);
        assertTrue(responseLoginUserOk.isSuccess());
    }

    @Test
    @DisplayName("Авторизация сущетвующего пользователя")
    @Description("Проверка, что в ответе приходит не пустой accessToken")
    public void loginUserCheckAccessTokenPositiveTest() {
        responseLoginUser = userClient.loginUser(requestLoginUser);
        ResponseRegisterAndLoginUserOk responseLoginUserOk = responseLoginUser.as(ResponseRegisterAndLoginUserOk.class);
        assertNotNull(responseLoginUserOk.getAccessToken());
    }

    @Test
    @DisplayName("Авторизация сущетвующего пользователя")
    @Description("Проверка, что в ответе приходит не пустой refreshToken")
    public void loginUserCheckRefreshTokenPositiveTest() {
        responseLoginUser = userClient.loginUser(requestLoginUser);
        ResponseRegisterAndLoginUserOk responseLoginUserOk = responseLoginUser.as(ResponseRegisterAndLoginUserOk.class);
        assertNotNull(responseLoginUserOk.getRefreshToken());
    }

    @Test
    @DisplayName("Авторизация сущетвующего пользователя")
    @Description("Проверка, что в ответе приходит email, равный указанному при регистрации")
    public void loginUserCheckEmailPositiveTest() {
        responseLoginUser = userClient.loginUser(requestLoginUser);
        ResponseRegisterAndLoginUserOk responseLoginUserOk = responseLoginUser.as(ResponseRegisterAndLoginUserOk.class);
        assertEquals(requestRegisterUser.getEmail().toLowerCase(), responseLoginUserOk.getUser().getEmail());
    }

    @Test
    @DisplayName("Авторизация сущетвующего пользователя")
    @Description("Проверка, что в ответе приходит Name, равный указанному при регистрации")
    public void loginUserCheckNamePositiveTest() {
        responseLoginUser = userClient.loginUser(requestLoginUser);
        ResponseRegisterAndLoginUserOk responseLoginUserOk = responseLoginUser.as(ResponseRegisterAndLoginUserOk.class);
        assertEquals(requestRegisterUser.getName(), responseLoginUserOk.getUser().getName());
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Проверка, что приходит 401 кода ответа")
    public void loginUserWithInvalidEmailStatusCodePositiveTest() {
        RequestLoginUser requestLoginInvalidUser = new RequestLoginUser(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", requestRegisterUser.getPassword());
        responseLoginUser = userClient.loginUser(requestLoginInvalidUser);
        assertEquals(SC_UNAUTHORIZED, responseLoginUser.statusCode());
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Проверка, что в ответе в параметре success приходит false")
    public void loginUserWithInvalidEmailCheckSuccessError() {
        RequestLoginUser requestLoginInvalidUser = new RequestLoginUser(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", requestRegisterUser.getPassword());
        responseLoginUser = userClient.loginUser(requestLoginInvalidUser);
        ResponseError responseError = responseLoginUser.as(ResponseError.class);
        assertFalse(responseError.isSuccess());
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Проверка, что в ответе в параметре message приходит \"email or password are incorrect\"")
    public void loginUserWithInvalidEmailCheckMessageError() {
        RequestLoginUser requestLoginInvalidUser = new RequestLoginUser(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", requestRegisterUser.getPassword());
        responseLoginUser = userClient.loginUser(requestLoginInvalidUser);
        ResponseError responseError = responseLoginUser.as(ResponseError.class);
        assertEquals("email or password are incorrect", responseError.getMessage());
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Проверка, что приходит 401 кода ответа")
    public void loginUserWithInvalidPasswordStatusCodePositiveTest() {
        RequestLoginUser requestLoginInvalidUser = new RequestLoginUser(requestRegisterUser.getEmail(), RandomStringUtils.randomAlphabetic(10));
        responseLoginUser = userClient.loginUser(requestLoginInvalidUser);
        assertEquals(SC_UNAUTHORIZED, responseLoginUser.statusCode());
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Проверка, что в ответе в параметре success приходит false")
    public void loginUserWithInvalidPasswordCheckSuccessError() {
        RequestLoginUser requestLoginInvalidUser = new RequestLoginUser(requestRegisterUser.getEmail(), RandomStringUtils.randomAlphabetic(10));
        responseLoginUser = userClient.loginUser(requestLoginInvalidUser);
        ResponseError responseError = responseLoginUser.as(ResponseError.class);
        assertFalse(responseError.isSuccess());
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Проверка, что в ответе в параметре message приходит \"email or password are incorrect\"")
    public void loginUserWithInvalidPasswordCheckMessageError() {
        RequestLoginUser requestLoginInvalidUser = new RequestLoginUser(requestRegisterUser.getEmail(), RandomStringUtils.randomAlphabetic(10));
        responseLoginUser = userClient.loginUser(requestLoginInvalidUser);
        ResponseError responseError = responseLoginUser.as(ResponseError.class);
        assertEquals("email or password are incorrect", responseError.getMessage());
    }

}
