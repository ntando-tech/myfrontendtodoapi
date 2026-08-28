package todo.backend.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Setter
@Getter
//@NoArgsConstructor

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;


    private String description;

    //@Enumerated(EnumType.STRING)
    private String priority;

    private LocalDate created;

    private String dueDate;

    private boolean completed;


    public Task(String title, String description, String priority,
                String dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.created = LocalDate.now();
        this.completed = false;
        this.dueDate = dueDate;

    }

    public Task(){}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

}
