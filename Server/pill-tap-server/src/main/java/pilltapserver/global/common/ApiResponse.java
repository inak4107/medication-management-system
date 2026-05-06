package pilltapserver.global.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final int status;
    private final String message;
    private final T data;

    private ApiResponse(int status, String message, T data){
        this.status=status;
        this.message=message;
        this.data=data;
    }
    //성공, 정보반환
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(200, "success", data);
    }
    //성공, 반환X
    public static <T> ApiResponse<T> success(String message){
        return new ApiResponse<>(200, message, null);
    }
    //실패
    public static <T> ApiResponse<T> error(int status, String message){
        return new ApiResponse<>(status, message, null);
    }

}

