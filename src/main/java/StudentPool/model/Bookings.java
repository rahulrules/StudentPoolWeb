package StudentPool.model;

import java.sql.Timestamp;
import java.util.Date;

public class Bookings {

    private Integer id;
    private String user_id;
    private String booking_date;// This  time stamp is given as string
    private Integer rideoffered_id;
    private Integer slots_booked;
    private String instructions;

// Should this constructor be used for default initialization or should be removed.
    public Bookings() {
    }

    public Bookings(Integer slots_booked, String instructions) {
        this.slots_booked = slots_booked;
        this.booking_date = new Timestamp(new Date().getTime()).toString();//sql Time stamp and Java.util date
        this.instructions = instructions;
    }

    public Bookings(String user_id, Integer rideoffered_id, Integer slots_booked) {
        this.user_id = user_id;
        this.booking_date = new Timestamp(new Date().getTime()).toString();//sql Time stamp and Java.util date
        this.rideoffered_id = rideoffered_id;
        this.slots_booked = slots_booked;
    }

    public Bookings(String user_id, Integer rideoffered_id, Integer slots_booked, String instructions) {
        this.user_id = user_id;
        this.booking_date = new Timestamp(new Date().getTime()).toString();//sql Time stamp and Java.util date
        this.rideoffered_id = rideoffered_id;
        this.slots_booked = slots_booked;
        this.instructions = instructions;
    }

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

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public Integer getRideoffered_id() {
        return rideoffered_id;
    }

    public void setRideoffered_id(Integer rideoffered_id) {
        this.rideoffered_id = rideoffered_id;
    }

    public Integer getSlots_booked() {
        return slots_booked;
    }

    public void setSlots_booked(Integer slots_booked) {
        this.slots_booked = slots_booked;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
