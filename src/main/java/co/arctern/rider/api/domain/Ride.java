package co.arctern.rider.api.domain;

import javax.persistence.Entity;

@Entity
@Data
public class Ride {

    @Id
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @OneToMany
    private List<RideStateFlow> riderStateFlows;

}
