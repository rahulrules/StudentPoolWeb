package StudentPool.Resources.Beans;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class RidesFilterBean {

    private @QueryParam("to") String Toplace;
    private @QueryParam("from") String Fromplace;
    private @QueryParam("date") String dateofride;
    private @DefaultValue("0") @QueryParam("start") Integer start;
    private @DefaultValue("10") @QueryParam("size") Integer size;

    public String getToplace() {
        return Toplace;
    }

    public void setToplace(String toplace) {
        Toplace = toplace;
    }

    public String getFromplace() {
        return Fromplace;
    }

    public void setFromplace(String fromplace) {
        Fromplace = fromplace;
    }

    public String getDateofride() {
        return dateofride;
    }

    public void setDateofride(String dateofride) {
        this.dateofride = dateofride;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
