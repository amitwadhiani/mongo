package co.arctern.api.provider.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Data
public class Rating {

    @Id
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
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

    @Column(columnDefinition = "tinyint(1) DEFAULT 1")
    @NotNull
    private Boolean isSatisfied;

    @Size(min = 6, max = 6, message = "Invalid otp")
    private String otpYes;

    @Size(min = 6, max = 6, message = "Invalid otp")
    private String otpNo;

    @PrePersist
    public void setValue() {
        if (this.isSatisfied)
            this.value = 5;
        else
            this.value = 1;
    }

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    public Rating(Long version) {
        this.version = version;
    }


}
