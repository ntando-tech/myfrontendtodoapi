package todo.backend.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;

    private String password;

    private boolean completed;

    private boolean enable;

    private String verificationToken;
    private Date verificationTokenExpiration;

    private String resetPasswordToken;
    private Date resetPasswordExpiration;

    public Users(){}

    public Users(String username, String email, String password,String verificationToken,Date verificationTokenExpiration,String resetPasswordToken,Date resetPasswordExpiration ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.completed = false;
        this.enable = false;
        this.verificationToken = verificationToken;
        this.verificationTokenExpiration = verificationTokenExpiration;
        this.resetPasswordToken = resetPasswordToken;
        this.resetPasswordExpiration = resetPasswordExpiration;
    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Task> tasks;
}
