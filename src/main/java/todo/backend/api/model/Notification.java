package todo.backend.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    private String description;

    private LocalDate created;

    private int noOfNotifications;

    public Notification(){};
    public Notification(String title, String description){
        this.title = title;
        this.description = description;
        this.created = LocalDate.now();
        this.noOfNotifications = 0;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

}
