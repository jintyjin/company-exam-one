package exam.jin.companyexamone.dto;

import exam.jin.companyexamone.entity.Member;
import lombok.Data;

@Data
public class MemberJoinResult {

    private Long id;

    private String loginId;

    private String username;

    public MemberJoinResult(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.username = member.getUsername();
    }
}
