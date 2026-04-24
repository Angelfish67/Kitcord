package ch.samira.tesan.focusdesk.task;

import ch.samira.tesan.focusdesk.task.enums.TaskPriority;
import ch.samira.tesan.focusdesk.task.enums.TaskStatus;
import ch.samira.tesan.focusdesk.user.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(length = 100, nullable = false)
        @Size(max = 100)
        @NotEmpty
        private String title;

        private String description;

        @Enumerated(EnumType.STRING)
        private TaskStatus status;

        @Enumerated(EnumType.STRING)
        private TaskPriority priority;


        public Task(){
        }



}
