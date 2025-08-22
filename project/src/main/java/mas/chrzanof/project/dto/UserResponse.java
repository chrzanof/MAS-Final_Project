package mas.chrzanof.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private Integer phoneNumber;
    
    public UserResponse(Long id, String name, String surname, String email, Integer phoneNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
