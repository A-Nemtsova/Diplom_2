package site.nomoreparties.stellarburgers.model.responses;

import site.nomoreparties.stellarburgers.model.User;

public class ResponseRegisterAndLoginUserOk {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    /*
    {
    "success":true,
     "user":{
        "email":"nastasiya.wami@yandex.ru",
        "name":"Wami"
        },
     "accessToken":"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYyZDQ0MDA1OTNlYmMyMDAxYjRhMmIyNCIsImlhdCI6MTY1ODA3NzE4OSwiZXhwIjoxNjU4MDc4Mzg5fQ.347UhfCFda2hpaIgXrHWxHmWBE8Rnofrukq5waNgSso",
     "refreshToken":"db21d68cd3bdb42d8eee0efa8c3ba4b7d1218735b265c4e5b9d0d45b978d88120ed78e4ac377b731"
     }
     */
}
