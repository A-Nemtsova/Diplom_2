package site.nomoreparties.stellarburgers.model.responses;

public class ResponseError {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
