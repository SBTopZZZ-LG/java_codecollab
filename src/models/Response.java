package models;

public class Response<T> {
    public final boolean wasSuccess;
    public final T data;

    public Response(boolean wasSuccess, T data) {
        this.wasSuccess = wasSuccess;
        this.data = data;
    }

    public static <T> Response<T> withSuccess(final T data) {
        return new Response<>(true, data);
    }

    public static <T> Response<T> withFailure() {
        return new Response<>(false, null);
    }
}
