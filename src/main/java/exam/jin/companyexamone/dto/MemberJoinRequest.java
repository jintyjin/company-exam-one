package exam.jin.companyexamone.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberJoinRequest {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

    @NotEmpty
    private String username;

    public MemberJoinRequest(String loginId, String password, String username) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
    }
}
