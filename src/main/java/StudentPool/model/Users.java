package StudentPool.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


public class Users {
    private int id;
    private String user_id;
    private String user_password;
    private List<Link> links=new ArrayList<>();

    public Users() {
    }

    public Users(int id, String user_id) {
        this.id = id;
        this.user_id = user_id;
    }

    public Users(String user_id, String user_password) {
        this.user_id = user_id;
        this.user_password = user_password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addlink(String link,String rel){
        Link templink= new Link(link,rel);
        links.add(templink);
    }

    @Override
    public String toString() {
        return "Id: "+this.getId()+" user_id: "+this.getUser_id()+" password: "+this.getUser_password();
    }
}
