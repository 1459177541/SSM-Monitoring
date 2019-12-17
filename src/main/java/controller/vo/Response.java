package controller.vo;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

public class Response<T> implements Serializable {

    private boolean success;

    private T data;

    private int code;

    private String message;

    public boolean isSuccess() {
        return success;
    }

    public Response<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Response<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response<?> response = (Response<?>) o;
        return success == response.success &&
                code == response.code &&
                Objects.equals(data, response.data) &&
                Objects.equals(message, response.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, data, code, message);
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public static <T> Response<T> create(Supplier<T> supplier){
        Response<T> response = new Response<T>();
        try{
            response.setData(supplier.get())
                    .setMessage("success")
                    .setSuccess(true)
                    .setCode(200);
        } catch (Throwable e) {
            e.printStackTrace();
            response.setMessage(e.getMessage())
                    .setSuccess(false)
                    .setCode(400);
        }
        return response;
    }

    public static <T> Response<T> success(T data){
        return new Response<T>()
                .setCode(200)
                .setData(data)
                .setMessage("success")
                .setSuccess(true);
    }

    public static <T> Response<T> fail(T data){
        return new Response<T>()
                .setCode(400)
                .setData(data)
                .setMessage("fail")
                .setSuccess(false);
    }
}
