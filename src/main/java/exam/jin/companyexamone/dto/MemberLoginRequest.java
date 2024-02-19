package exam.jin.companyexamone.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberLoginRequest {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

    public MemberLoginRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
