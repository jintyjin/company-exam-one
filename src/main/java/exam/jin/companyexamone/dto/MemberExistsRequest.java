package exam.jin.companyexamone.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberExistsRequest {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String username;

    public MemberExistsRequest(String loginId, String username) {
        this.loginId = loginId;
        this.username = username;
    }

    public MemberExistsRequest(MemberJoinRequest request) {
        this.loginId = request.getLoginId();
        this.username = request.getUsername();
    }
}
