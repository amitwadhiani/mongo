package co.arctern.rider.api.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonBackReference;

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
    @JsonBackReference("timeslotgroup=timeslot")
    TimeslotGroup timeslotGroup;


}
