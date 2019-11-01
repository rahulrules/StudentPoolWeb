package StudentPool.model;

import java.sql.Date;
import java.sql.Time;


public class Rides {
    private Integer id;
    private String user_id;
    private Date ride_date; // ** Check how to make this SQL compatible
    private Time ride_time;
    private String start_from=null;
    private String end_to;
    private Integer slots_offered=null;
    private String instructions=null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getRide_date() {
        return ride_date;
    }

    public void setRide_date(Date ride_date) {
        this.ride_date = ride_date;
    }

    public Time getRide_time() {
        return ride_time;
    }

    public void setRide_time(Time ride_time) {
        this.ride_time = ride_time;
    }

    public String getStart_from() {
        return start_from;
    }

    public void setStart_from(String start_from) {
        this.start_from = start_from;
    }

    public String getEnd_to() {
        return end_to;
    }

    public void setEnd_to(String end_to) {
        this.end_to = end_to;
    }

    public Integer getSlots_offered() {
        return slots_offered;
    }

    public void setSlots_offered(Integer slots_offered) {
        this.slots_offered = slots_offered;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
