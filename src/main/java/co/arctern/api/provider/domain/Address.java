package co.arctern.api.provider.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String line;

    @Size(min = 6, max = 6, message = "Invalid pinCode")
    @Pattern(regexp = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$")
    private String pinCode;

    @Column(nullable = false)
    private String city;

    private String locality;

    @Column(nullable = false)
    private String houseNumber;

    @Column(nullable = false)
    private String state;

    private String patientId;

    @OneToMany(mappedBy = "sourceAddress")
    List<Task> tasksForSourceAddress;

    @OneToMany(mappedBy = "destinationAddress")
    List<Task> tasksForDestinationAddress;

    @ManyToOne
    @JsonBackReference("area-address")
    private Area area;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    public Address(Long version) {
        this.version = version;
    }

}
