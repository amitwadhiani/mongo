package co.arctern.api.provider.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Address {

    private  String xyz;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JsonBackReference("area-address")
    private Area area;

    @Column
    private String line;

    @Size(min = 6, max = 6, message = "Invalid pinCode")
    @Pattern(regexp = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$")
    @Column
    private String pinCode;

    @Column(nullable = false)
    private String city;

    private String locality;

    @Column(nullable = false)
    private String houseNumber;

    @Column(nullable = false)
    private String state;

    @Column(columnDefinition = "tinyint(1) DEFAULT 0", nullable = true)
    private Boolean isSourceAddress;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private String addressTag;

    @Column
    private String landmark;

    @OneToMany(mappedBy = "sourceAddress")
    List<Task> tasksForSourceAddress;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @OneToMany(mappedBy = "destinationAddress")
    List<Task> tasksForDestinationAddress;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    public Address(Long version) {
        this.version = version;
    }

}
