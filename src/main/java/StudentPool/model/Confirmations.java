package StudentPool.model;


import java.sql.Timestamp;
import java.util.Date;

public class Confirmations {

    private Integer id;
    private String user_id;
    private String accepted_date;// This is a java sql time stamp
    private Integer riderequested_id;
    private String instructions;

    public Confirmations() {
    }

    public Confirmations(String user_id, Integer riderequested_id) {
        this.user_id = user_id;
        this.accepted_date= new Timestamp(new Date().getTime()).toString();//passing milliseconds to SQL timestamp,
        // new Date().getTime() returns milliseconds sice jan 1 1970 to current date.
        this.riderequested_id = riderequested_id;
    }

    public Confirmations(Integer riderequested_id, String instructions) {
        this.accepted_date= new Timestamp(new Date().getTime()).toString();
        this.riderequested_id = riderequested_id;
        this.instructions = instructions;
    }

    public Confirmations(String user_id, Integer riderequested_id, String instructions) {
        this.user_id = user_id;
        this.accepted_date= new Timestamp(new Date().getTime()).toString();
        this.riderequested_id = riderequested_id;
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

    public String getAccepted_date() {
        return accepted_date;
    }

    public void setAccepted_date(String accepted_date) {
        this.accepted_date = accepted_date;
    }

    public Integer getRiderequested_id() {
        return riderequested_id;
    }

    public void setRiderequested_id(Integer riderequested_id) {
        this.riderequested_id = riderequested_id;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
