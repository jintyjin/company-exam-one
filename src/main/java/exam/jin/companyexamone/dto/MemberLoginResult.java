package exam.jin.companyexamone.dto;

import lombok.Data;

@Data
public class MemberLoginResult {

    private String loginId;

    private String accessToken;

    public MemberLoginResult(String loginId, String accessToken) {
        this.loginId = loginId;
        this.accessToken = accessToken;
    }
}
