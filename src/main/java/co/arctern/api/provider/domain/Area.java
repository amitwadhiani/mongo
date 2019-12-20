package co.arctern.api.provider.domain;

import co.arctern.api.provider.constant.OfficeType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference("cluster-area")
    private Cluster cluster;

    @Column(nullable = true)
    private Double latitude;

    @Column(nullable = true)
    private Double longitude;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1")
    private Boolean deliveryState;

    @Column(nullable = true)
    private String state;

    @Column
    private String region;

    @Column
    private String division;

    @Column
    private String circle;

    @Column
    private String phone;

    @Column
    private String district;

    @Column
    private String subOffice;

    @Column
    private String headOffice;

    @Enumerated(EnumType.STRING)
    private OfficeType officeType;

    @Column(nullable = true)
    private String name;

    @Pattern(regexp = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$")
    @Column(unique = true)
    private String pinCode;

    /**
     * duplicate pinCodes' grouping.
     */
    @Transient
    private List<Long> ids;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1", nullable = false)
    private Boolean isActive;

    @Column(columnDefinition = "tinyint(1) DEFAULT 0", nullable = false)
    private Boolean meddoDeliveryState;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @OneToMany(mappedBy = "area")
    private List<UserArea> areaUsers;

    @OneToMany(mappedBy = "area")
    private List<Address> addresses;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    public Area(Long version) {
        this.version = version;
    }

}
