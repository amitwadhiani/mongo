package co.arctern.rider.api.domain;

import co.arctern.api.internal.api.emr.model.NextAvailable;
import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.sql.Time;
import java.time.DayOfWeek;

@Entity
@Data
public class Timeslot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    Time start;
    Time end;
    DayOfWeek day;

    @ManyToOne
    TimeslotGroup intervalGroup;


}
