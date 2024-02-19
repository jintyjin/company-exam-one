package exam.jin.companyexamone.dto;

import lombok.Data;

@Data
public class MemberLoginResponse {

    private String loginId;

    private String accessToken;

    public MemberLoginResponse(MemberLoginResult result) {
        this.loginId = result.getLoginId();
        this.accessToken = result.getAccessToken();
    }

    public MemberLoginResponse() {
    }
}
