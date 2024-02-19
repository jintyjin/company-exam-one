package exam.jin.companyexamone.dto;

import lombok.Data;

@Data
public class MemberInfoResult {

    private String loginId;

    private String username;

    private String accessToken;

    public MemberInfoResult(String loginId, String username, String accessToken) {
        this.loginId = loginId;
        this.username = username;
        this.accessToken = accessToken;
    }
}
