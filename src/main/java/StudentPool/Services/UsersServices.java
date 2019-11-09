package StudentPool.Services;

import StudentPool.Database.Datasource;
import StudentPool.model.Users;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsersServices {

    public static void main(String[] args) {
        UsersServices newservice = new UsersServices();
//        System.out.println(newservice.getUserFromID(2));
//        Users newuser= new Users("rakesh0100@ucmo.edu","NOpassword123@#");
//        newservice.CreateAccount(newuser);
//        System.out.println(newservice.IsUsernameAvailable(newuser));

    }
    private static final String USER_TAB ="users";
    private static final String USER_ID="id";
    private static final String USER_USER_ID="user_id";
    private static final String USER_USER_PAS="user_password";

    private static final String USER_INS= "INSERT INTO "+
            USER_TAB+"("+USER_USER_ID+","+USER_USER_PAS+") VALUES(?,?)";

    private static final String USER_LOGIN= "SELECT "+USER_ID+" FROM "+
            USER_TAB+" WHERE "+USER_USER_ID+"=? " +"AND "+ USER_USER_PAS+"=? ";

    private static final String USER_USERIDEXIST= "SELECT * FROM "+
            USER_TAB+" WHERE "+USER_USER_ID+"=? " ;

    private static final String USER_ALLUSERS= "SELECT * FROM "+
            USER_TAB;
    private static final String USER_RESETPASS= "UPDATE "+
            USER_TAB+" SET "+USER_USER_PAS+" =? "+" WHERE "+USER_USER_ID+" =? ";
    private static final String USER_FROMID= "SELECT "+USER_ID+","+USER_USER_ID+" FROM "+
            USER_TAB+" WHERE "+USER_ID+"=?";



    private PreparedStatement insertuser;
    private PreparedStatement logincheck;
    private PreparedStatement useridcheck;
    private PreparedStatement allusers;
    private PreparedStatement passwordreset;
    private PreparedStatement userfromid;


    private boolean open(){
        try{
            //All sql query prepared statements for the user table
            insertuser= Datasource.getInstance().getConn().prepareStatement(USER_INS);
            logincheck = Datasource.getInstance().getConn().prepareStatement(USER_LOGIN);
            useridcheck = Datasource.getInstance().getConn().prepareStatement(USER_USERIDEXIST);
            allusers = Datasource.getInstance().getConn().prepareStatement(USER_ALLUSERS);
            passwordreset= Datasource.getInstance().getConn().prepareStatement(USER_RESETPASS);
            userfromid= Datasource.getInstance().getConn().prepareStatement(USER_FROMID);
            return true;
        }
        catch (SQLException e){
            System.out.println("Can't Connect to database: "+e.getMessage());
            return false;
        }
    }

    private void close(){
        try{
            if(insertuser!=null){
                insertuser.close();
            }
            if(logincheck!=null){
                logincheck.close();
            }
            if(useridcheck!=null){
                useridcheck.close();
            }
            if(allusers!=null){
                allusers.close();
            }
            if(passwordreset!=null){
                passwordreset.close();
            }
            if(userfromid!=null){
                userfromid.close();
            }
        }catch (SQLException e){
            System.out.println("Error in Closing Prepared Statement: "+e.getMessage());
        }
        finally{
            Datasource.getInstance().close();
        }

    }


    public boolean Insertintousers(Users newuser){
        this.open();
        try{
            insertuser.setString(1,newuser.getUser_id().toLowerCase());
            insertuser.setString(2,newuser.getUser_password());
            int affectedrows= insertuser.executeUpdate();
            if(affectedrows!=1){
                System.out.println("Affectedrows Error Inserting into user table");
                return false;
            }
        }
        catch (SQLException e){
            System.out.println("Can't insert entry in to 'users' table: "+e.getMessage());
            return false;
        }
        finally {
            this.close();
        }
        return true;

    }

    public boolean IsValidUsername(Users newuser){
        String patternmatch= "^[a-z0-9]{5,}@ucmo\\.edu$";
        Pattern pattern = Pattern.compile(patternmatch, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(newuser.getUser_id());
        if(m.find()){
            return true;
        }
        System.out.println("Enter a valid UCM mail id");
        return false;
    }

    public boolean IsValidPassword(Users newuser){
        String patternmatch= "^[a-zA-Z0-9@*#&!()]{8,15}$";
        Pattern pattern = Pattern.compile(patternmatch, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(newuser.getUser_password());
        if(m.find()){
            return true;
        }
        System.out.println("Enter a password matching requirements");
        return false;

    }

    public Integer IsResgisteredUser(Users user){
        this.open();
        try{
            logincheck.setString(1,user.getUser_id().toLowerCase());
            logincheck.setString(2,user.getUser_password());
            ResultSet resultSet= logincheck.executeQuery();
            while(resultSet.next()){
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("Can't find the user in the database: "+ e.getMessage());
        }
        finally {
            this.close();
        }
    return -1;
    }

    public Users CreateAccount(Users newuser){
        if(this.IsValidUsername(newuser) && this.IsValidPassword(newuser)){
            if(this.Insertintousers(newuser)){
                newuser.setId(IsUsernameAvailable(newuser));
                System.out.println("Account Created");
                return newuser;
            }
            else{
                System.out.println("Username already exists");
            }
        }else
            System.out.println("Invalid Username or Password");
        return null;
    }

    public Integer IsUsernameAvailable(Users newuser){
        this.open();
        try{
            useridcheck.setString(1,newuser.getUser_id().toLowerCase());
            ResultSet resultSet= useridcheck.executeQuery();
            while(resultSet.next()){
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("Can't search the user in the database: "+ e.getMessage());
        }
        finally {
            this.close();
        }
        return -1;
    }


    public boolean Resetpassword(Users registereduser){
        if(IsUsernameAvailable(registereduser)>0){
            if(IsValidPassword(registereduser)){
                this.open();
                try{
                    passwordreset.setString(2,registereduser.getUser_id().toLowerCase());
                    passwordreset.setString(1,registereduser.getUser_password());
                    int affectedrows=passwordreset.executeUpdate();
                    if(affectedrows!=1){
                        System.out.println("Affectedrows Error Inserting into user table");
                        return false;
                    }
                }
                catch (SQLException e){
                    System.out.println("Can't Update Password in the 'users' table: "+e.getMessage());
                    return false;
                }
                finally {
                    this.close();
                }
            }
        }
        return true;
    }

    public List<Users> getAllUsers(){
        List<Users> result = new ArrayList<Users>();
        this.open();
        try{
            ResultSet resultSet =allusers.executeQuery();
            while(resultSet.next()){
                Users tempuser= new Users();
                tempuser.setId(resultSet.getInt(1));
                tempuser.setUser_id(resultSet.getString(2));
                tempuser.setUser_password(resultSet.getString(3));
//                System.out.println(tempuser.toString());
                result.add(tempuser);
            }
            return result;
        }catch (SQLException e){
            System.out.println("Query to get all users failed: "+ e.getMessage());
        }
        finally {
            this.close();
        }
        return result;
    }


    public Users getUserFromID(Integer id){
        Users tempuser= new Users();
        this.open();
        try{
            userfromid.setInt(1,id);
            ResultSet resultSet =userfromid.executeQuery();
            while(resultSet.next()){
                tempuser.setId(resultSet.getInt(1));
                tempuser.setUser_id(resultSet.getString(2));
            }
            return tempuser;

        }catch (SQLException e){
            System.out.println("Query to get user with id: "+ id+" failed: "+ e.getMessage());
        }
        finally {
            this.close();
        }
        return tempuser;
    }




}
