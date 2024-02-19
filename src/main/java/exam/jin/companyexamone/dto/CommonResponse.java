package exam.jin.companyexamone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Data
public class CommonResponse<T> {

    @JsonInclude(NON_NULL)
    private String message;

    @JsonInclude(NON_NULL)
    private T data;

    public CommonResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
