package exam.jin.companyexamone.entity;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_loginId", unique = true)
    private String loginId;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_username")
    private String username;

    protected Member() {
    }

    public Member(String loginId, String password, String username) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
