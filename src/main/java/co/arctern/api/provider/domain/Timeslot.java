package co.arctern.api.provider.domain;

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
    @JsonBackReference("timeSlotGroup-timeSlot")
    TimeslotGroup timeSlotGroup;


}
