package co.arctern.rider.api.domain;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Data
@Entity
public class TimeslotGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany
    private List<Timeslot> slots;

    Time start;
    Time end;

    // can add days/months as well


}
