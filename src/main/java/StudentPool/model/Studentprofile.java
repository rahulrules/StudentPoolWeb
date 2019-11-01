package StudentPool.model;

public class Studentprofile {
    private Integer id;
    private String user_id;
    private String first_name;
    private String last_name;
    private String department=null;
    private String admityear=null;
    private String residentcity=null;

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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAdmityear() {
        return admityear;
    }

    public void setAdmityear(String admityear) {
        this.admityear = admityear;
    }

    public String getResidentcity() {
        return residentcity;
    }

    public void setResidentcity(String residentcity) {
        this.residentcity = residentcity;
    }
}
