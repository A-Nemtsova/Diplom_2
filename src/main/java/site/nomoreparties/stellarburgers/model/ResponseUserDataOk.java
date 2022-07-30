package site.nomoreparties.stellarburgers.model;

public class ResponseUserDataOk {
    private boolean success;
    private User user;

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }
}
