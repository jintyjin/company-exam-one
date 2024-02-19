package exam.jin.companyexamone.dto;

import lombok.Data;

@Data
public class MemberExistsResult {

    private String loginId;

    public MemberExistsResult(String loginId) {
        this.loginId = loginId;
    }
}
