package mas.chrzanof.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
    private Integer phoneNumber;
    private boolean isTeacher;
}
