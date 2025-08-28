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
    private boolean isStudent;
    
    public boolean getIsTeacher() {
        return isTeacher;
    }
    
    public void setIsTeacher(boolean isTeacher) {
        this.isTeacher = isTeacher;
    }
    
    public boolean getIsStudent() {
        return isStudent;
    }
    
    public void setIsStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }
}
