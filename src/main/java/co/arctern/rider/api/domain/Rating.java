package co.arctern.rider.api.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Rating {

    @Id
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @OneToOne
    @JoinColumn(name = "task_id")
    @JsonBackReference("task-rating")
    private Task task;

    @Size(min = 1, max = 5, message = "Invalid rating value")
    private Integer value;

    private Boolean isSatisfied;

    @PrePersist
    public void setValue() {
        if (this.isSatisfied)
            this.value = 5;
        else
            this.value = 1;
    }

}
