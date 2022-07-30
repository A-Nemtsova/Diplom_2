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
import site.nomoreparties.stellarburgers.model.RequestLoginUser;
import site.nomoreparties.stellarburgers.model.RequestRegisterUser;
import site.nomoreparties.stellarburgers.model.ResponseRegisterAndLoginUserOk;
import site.nomoreparties.stellarburgers.model.ResponseError;

import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

@Story("Создание пользователя")
public class RegisterUserTest {
    RequestRegisterUser requestRegisterUser;
    UserClient userClient;
    Response responseRegisterUser;
    String accessToken;

    @Before
    @Step("Подготовка тестовых данных")
    public void init() {
        requestRegisterUser = RequestRegisterUser.getRandomUser();
        userClient = new UserClient();
    }

    @After
    @Step("Удаление созданного пользователя")
    public void clear() {

        if (responseRegisterUser.statusCode() == 200) {
            //requestLoginUser = new RequestLoginUser(requestRegisterUser.getEmail(), requestRegisterUser.getPassword());
            //userClient.deleteUser(userClient.getAccessToken(requestLoginUser));
            accessToken = responseRegisterUser.body().jsonPath().getString("accessToken");
            userClient.deleteUser(accessToken);
        }

         /*
        accessToken = responseRegisterUser.body().jsonPath().getString("accessToken");
        //System.out.println(accessToken);
        if (accessToken.length() != 0) {
            userClient.deleteUser(accessToken);
        }*/
    }

    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Проверка, что при успешной регистрации приходит 200 кода ответа")
    public void registerUserStatusCodePositiveTest() {
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        assertEquals(SC_OK, responseRegisterUser.statusCode());
    }

    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Проверка, что в ответе в параметре success приходит true")
    public void registerUserCheckSuccessPositiveTest() {
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseRegisterAndLoginUserOk responseRegisterUserOk = responseRegisterUser.as(ResponseRegisterAndLoginUserOk.class);
        assertTrue(responseRegisterUserOk.isSuccess());
    }

    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Проверка, что в ответе приходит не пустой accessToken")
    public void registerUserCheckAccessTokenPositiveTest() {
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseRegisterAndLoginUserOk responseRegisterUserOk = responseRegisterUser.as(ResponseRegisterAndLoginUserOk.class);
        assertNotNull(responseRegisterUserOk.getAccessToken());
    }

    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Проверка, что в ответе приходит не пустой refreshToken")
    public void registerUserCheckRefreshTokenPositiveTest() {
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseRegisterAndLoginUserOk responseRegisterUserOk = responseRegisterUser.as(ResponseRegisterAndLoginUserOk.class);
        assertNotNull(responseRegisterUserOk.getRefreshToken());
    }

    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Проверка, что в ответе приходит email, равный указанному при регистрации")
    public void registerUserCheckEmailPositiveTest() {
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseRegisterAndLoginUserOk responseRegisterUserOk = responseRegisterUser.as(ResponseRegisterAndLoginUserOk.class);
        assertEquals(requestRegisterUser.getEmail().toLowerCase(),responseRegisterUserOk.getUser().getEmail());
    }

    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Проверка, что в ответе приходит Name, равный указанному при регистрации")
    public void registerUserCheckNamePositiveTest() {
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseRegisterAndLoginUserOk responseRegisterUserOk = responseRegisterUser.as(ResponseRegisterAndLoginUserOk.class);
        assertEquals(requestRegisterUser.getName(),responseRegisterUserOk.getUser().getName());
    }

    @Test
    @DisplayName("Повторная регистрация существующего пользователя")
    @Description("Проверка, что при попытке повторной регистрации сущесвующего пользователя приходит 403 кода ответа")
    public void registerTwoIdenticalUsersStatusCodeError() {
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        Response responseRegisterUserDouble = userClient.registerUser(requestRegisterUser);
        assertEquals(SC_FORBIDDEN, responseRegisterUserDouble.statusCode());
    }

    @Test
    @DisplayName("Повторная регистрация существующего пользователя")
    @Description("Проверка, что в ответе в параметре success приходит false")
    public void registerTwoIdenticalUsersCheckSuccessError() {
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        Response responseRegisterUserDouble = userClient.registerUser(requestRegisterUser);
        ResponseError responseError = responseRegisterUserDouble.as(ResponseError.class);
        assertFalse(responseError.isSuccess());
    }

    @Test
    @DisplayName("Повторная регистрация существующего пользователя")
    @Description("Проверка, что в ответе в параметре message приходит \"User already exists\"")
    public void registerTwoIdenticalUsersCheckMessageError() {
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        Response responseRegisterUserDouble = userClient.registerUser(requestRegisterUser);
        ResponseError responseError = responseRegisterUserDouble.as(ResponseError.class);
        assertEquals("User already exists", responseError.getMessage());
    }

    @Test
    @DisplayName("Регистация пользователя с незаполненными обязательными полями")
    @Description("Проверка, что регистрации пользователя с пустым email приходит 403 кода ответа")
    public void registerUserWithoutEmailStatusCodeError() {
        requestRegisterUser.setEmail("");
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        assertEquals(SC_FORBIDDEN, responseRegisterUser.statusCode());
    }

    @Test
    @DisplayName("Регистация пользователя с незаполненными обязательными полями")
    @Description("Проверка, что регистрации пользователя с пустым email в ответе в параметре success приходит false")
    public void registerUserWithoutEmailCheckSuccessError() {
        requestRegisterUser.setEmail("");
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseError responseError = responseRegisterUser.as(ResponseError.class);
        assertFalse(responseError.isSuccess());
    }

    @Test
    @DisplayName("Регистация пользователя с незаполненными обязательными полями")
    @Description("Проверка, что регистрации пользователя с пустым email в ответе в параметре message приходит \"Email, password and name are required fields\"")
    public void registerUserWithoutEmailCheckMessageError() {
        requestRegisterUser.setEmail("");
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseError responseError = responseRegisterUser.as(ResponseError.class);
        assertEquals("Email, password and name are required fields", responseError.getMessage());
    }

    @Test
    @DisplayName("Регистация пользователя с незаполненными обязательными полями")
    @Description("Проверка, что регистрации пользователя с пустым password приходит 403 кода ответа")
    public void registerUserWithoutPasswordStatusCodeError() {
        requestRegisterUser.setPassword("");
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        assertEquals(SC_FORBIDDEN, responseRegisterUser.statusCode());
    }

    @Test
    @DisplayName("Регистация пользователя с незаполненными обязательными полями")
    @Description("Проверка, что регистрации пользователя с пустым password в ответе в параметре success приходит false")
    public void registerUserWithoutPasswordCheckSuccessError() {
        requestRegisterUser.setPassword("");
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseError responseError = responseRegisterUser.as(ResponseError.class);
        assertFalse(responseError.isSuccess());
    }

    @Test
    @DisplayName("Регистация пользователя с незаполненными обязательными полями")
    @Description("Проверка, что регистрации пользователя с пустым password в ответе в параметре message приходит \"Email, password and name are required fields\"")
    public void registerUserWithoutPasswordCheckMessageError() {
        requestRegisterUser.setPassword("");
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseError responseError = responseRegisterUser.as(ResponseError.class);
        assertEquals("Email, password and name are required fields", responseError.getMessage());
    }

    @Test
    @DisplayName("Регистация пользователя с незаполненными обязательными полями")
    @Description("Проверка, что регистрации пользователя с пустым name приходит 403 кода ответа")
    public void registerUserWithoutNameStatusCodeError() {
        requestRegisterUser.setName("");
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        assertEquals(SC_FORBIDDEN, responseRegisterUser.statusCode());
    }

    @Test
    @DisplayName("Регистация пользователя с незаполненными обязательными полями")
    @Description("Проверка, что регистрации пользователя с пустым name в ответе в параметре success приходит false")
    public void registerUserWithoutNameCheckSuccessError() {
        requestRegisterUser.setName("");
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseError responseError = responseRegisterUser.as(ResponseError.class);
        assertFalse(responseError.isSuccess());
    }

    @Test
    @DisplayName("Регистация пользователя с незаполненными обязательными полями")
    @Description("Проверка, что регистрации пользователя с пустым name в ответе в параметре message приходит \"Email, password and name are required fields\"")
    public void registerUserWithoutNameCheckMessageError() {
        requestRegisterUser.setName("");
        responseRegisterUser = userClient.registerUser(requestRegisterUser);
        ResponseError responseError = responseRegisterUser.as(ResponseError.class);
        assertEquals("Email, password and name are required fields", responseError.getMessage());
    }
}
