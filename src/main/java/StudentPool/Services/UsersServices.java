package StudentPool.Services;

import StudentPool.Database.Datasource;
import StudentPool.model.Users;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsersServices {

    public static void main(String[] args) {
//        Users user1= new Users("Billkill","Pandey-pandu");
//        UsersServices usersServices= new UsersServices(user1);
//        usersServices.Insertintousers();
//
//        System.out.println(Datasource.getInstance().getcount("users"));



    }

    private Users user ;

    // should a no arg constructor be defined?

    public UsersServices(Users inputuser){
        this.user= inputuser;
    }

    public Users getUser() {
        return user;
    }

    private static final String USER_TAB ="users";
    private static final String USER_ID="id";
    private static final String USER_USER_ID="user_id";
    private static final String USER_USER_PAS="user_password";

    private static final String USER_INS= "INSERT INTO "+
            USER_TAB+"("+USER_USER_ID+","+USER_USER_PAS+") VALUES(?,?)";



    private PreparedStatement insertuser;


    private boolean open(){
        try{
//            conn = DriverManager.getConnection(CONNECTION);
            //All sql query prepared statements for the user table
            insertuser= Datasource.getInstance().getConn().prepareStatement(USER_INS);
            return true;
        }
        catch (SQLException e){
            System.out.println("Can't Connect to database: "+e.getMessage());
            return false;
        }
    }

    public void close(){

        try{
            if(insertuser!=null){
                insertuser.close();
            }

        }catch (SQLException e){
            System.out.println("Couldn't close the connection: "+e.getMessage());

        }
        Datasource.getInstance().close();

    }


    public void Insertintousers(){

        this.open();

        try{
//            insertuser.setInt(1,id);
            insertuser.setString(1,this.user.getUser_id());
            insertuser.setString(2,this.user.getUser_password());
//            System.out.println(insertuser);
            int affectedrows= insertuser.executeUpdate();
//            System.out.println("Result Successful");

            if(affectedrows!=1){
                System.out.println("Affectedrows Error Inserting into user table");
            }

        }
        catch (SQLException e){

            System.out.println("Can't insert entry in to 'users' table: "+e.getMessage());
        }

        this.close();

    }

}
