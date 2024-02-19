package exam.jin.companyexamone.exception;

import exam.jin.companyexamone.dto.MemberExistsRequest;
import exam.jin.companyexamone.dto.MemberJoinRequest;
import lombok.Getter;

@Getter
public class MemberExistsException extends RuntimeException {

    private final MemberExistsRequest request;

    public MemberExistsException(MemberJoinRequest request, String message) {
        super(message);
        this.request = new MemberExistsRequest(request);
    }
}
