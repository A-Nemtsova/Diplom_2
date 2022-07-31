package site.nomoreparties.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.requests.RequestLoginUser;
import site.nomoreparties.stellarburgers.model.requests.RequestRegisterUser;
import site.nomoreparties.stellarburgers.model.User;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseApiClient {

    @Step("Создание пользователя")
    public Response registerUser (RequestRegisterUser requestRegisterUser) {
        return given()
                .spec(getSpecForJson(""))
                .body(requestRegisterUser)
                .when()
                .post(BASE_URL + "/api/auth/register");
    }

    @Step("Авторизация пользователя")
    public Response loginUser (RequestLoginUser requestLoginUser) {
        return given()
                .spec(getSpecForJson(""))
                .body(requestLoginUser)
                .when()
                .post(BASE_URL + "/api/auth/login");
    }

    @Step("Авторизация и получение accessToken")
    public String getAccessToken(RequestLoginUser requestLoginUser) {
        return loginUser(requestLoginUser).body().jsonPath().getString("accessToken");
    }

    @Step("Получение данных пользователя")
    public Response getUserData(String accessToken) {
        return given()
                .spec(getSpecForJson(accessToken))
                .when()
                .get(BASE_URL + "/api/auth/user");
    }

    @Step("Изменение данных пользователя")
    public Response updateUser(User user, String accessToken) {
        return given()
                .spec(getSpecForJson(accessToken))
                .body(user)
                .when()
                .patch(BASE_URL + "/api/auth/user");
    }

    @Step("Удаление пользователя")
    public Response deleteUser (String accessToken) {
        return given()
                .spec(getSpecForJson(accessToken))
                .when()
                .delete(BASE_URL + "/api/auth/user");
    }
}
