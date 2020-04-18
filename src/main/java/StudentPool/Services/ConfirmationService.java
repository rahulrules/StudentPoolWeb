package StudentPool.Services;

import StudentPool.Database.Datasource;
import StudentPool.model.Bookings;
import StudentPool.model.Confirmations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfirmationService {


    public static void main(String[] args) {
//        System.out.println(BOOK_INSERT);
//        Bookings newbooking= new Bookings("RXJ08740@ucmo.edu",
//                2,1,"I will be on time");
//        BookingServices newBookService= new BookingServices();
//        System.out.println(newBookService.insertBooking(newbooking));

    }

    private static final String CONF_TAB ="confirmations";
    private static final String CONF_ID="id";
    private static final String CONF_USER_ID="user_id";
    private static final String CONF_DATE="accepted_date";
    private static final String CONF_RIDEID="riderequested_id";
//    private static final String CONF_SLOTS="slots_booked";
    private static final String CONF_INST="instructions";

    private static final String CONF_VIEWBYRIDEID="SELECT * FROM "+CONF_TAB+" WHERE "+CONF_RIDEID+" =?";
    private static final String CONF_INSERT="INSERT INTO "+CONF_TAB+
            "("+CONF_USER_ID+","+CONF_DATE+","+CONF_RIDEID+","+CONF_INST+") VALUES(?,?,?,?)";

    private static final String CONF_DELID= "DELETE"+" FROM "+CONF_TAB+
            " WHERE "+ CONF_ID+"=?";

    private static final String CONF_UPDATE="UPDATE "+CONF_TAB+
            " SET "+CONF_INST+"=?"+","+CONF_DATE+"=?"+" WHERE "+CONF_ID+"=?";
    private static final String CONF_GETID= "SELECT * FROM "+CONF_TAB+" WHERE "+CONF_ID+"=?";

    private PreparedStatement rideconfirmations;
    private PreparedStatement insertconfirm;
    private PreparedStatement delconfirmbyID;
    private PreparedStatement updateconfirmbyID;
    private PreparedStatement getconfirmbyID;




    private boolean open(){

        try{
            rideconfirmations = Datasource.getInstance().getConn().prepareStatement(CONF_VIEWBYRIDEID);
            insertconfirm= Datasource.getInstance().getConn().prepareStatement(CONF_INSERT, Statement.RETURN_GENERATED_KEYS);
            delconfirmbyID= Datasource.getInstance().getConn().prepareStatement(CONF_DELID);
            updateconfirmbyID = Datasource.getInstance().getConn().prepareStatement(CONF_UPDATE);
            getconfirmbyID= Datasource.getInstance().getConn().prepareStatement(CONF_GETID);
            return true;

        } catch (SQLException e){
            System.out.println("Can't Connect to Bookings Table: "+e.getMessage());
            return false;
        }
    }

    private void close(){
        try {
            if(rideconfirmations !=null){
                rideconfirmations.close();
            }
            if(insertconfirm!=null){
                insertconfirm.close();
            }
            if(delconfirmbyID!=null){
                delconfirmbyID.close();
            }
            if(updateconfirmbyID !=null){
                updateconfirmbyID.close();
            }
            if(getconfirmbyID!=null){
                getconfirmbyID.close();
            }
        }catch (SQLException e){
            System.out.println("Error Closing Prepared Statements for Confirmations: "+e.getMessage());
        }

        finally {
            Datasource.getInstance().close();
        }
    }

    public Confirmations ExtractConfirmationsRS(ResultSet resultSet){
        Confirmations tempconfirm= new Confirmations();

        try{
            tempconfirm.setId(resultSet.getInt(1));
            tempconfirm.setUser_id(resultSet.getString(2).toLowerCase());
            tempconfirm.setAccepted_date(resultSet.getDate(3).toString());
            tempconfirm.setRiderequested_id(resultSet.getInt(4));
            tempconfirm.setInstructions(resultSet.getString(5));

        }catch (SQLException e){
            System.out.println("Error Processing Resultset: "+e.getMessage());

        }
        return tempconfirm;
    }

    public List<Confirmations> getConfirmForRIdeID(Integer rideid){
        List<Confirmations> result= new ArrayList<>();
        this.open();

        try{
            rideconfirmations.setInt(1,rideid);
            ResultSet rs= rideconfirmations.executeQuery();

            while (rs.next()){
                result.add(ExtractConfirmationsRS(rs));
            }
        }
        catch (SQLException e){
            System.out.println("Error Querying Ride ID: "+rideid+"-:"+e.getMessage());
        }
        finally {
            this.close();
        }
        return new ArrayList<>(result);
    }

    public Integer insertConfirmation(Confirmations newconfirmation){
        Integer createdid=0;
        this.open();


        try {
            insertconfirm.setString(1,newconfirmation.getUser_id());
            insertconfirm.setTimestamp(2, Timestamp.valueOf(newconfirmation.getAccepted_date()));
            insertconfirm.setInt(3,newconfirmation.getRiderequested_id());
            insertconfirm.setString(4,newconfirmation.getInstructions());

            int affectedrows= insertconfirm.executeUpdate();
            if(affectedrows!=1){
                System.out.println("Affectedrows Error Inserting into Confirmations table");
                return -1;
            }
            ResultSet rs= insertconfirm.getGeneratedKeys();
            while (rs.next()){
                createdid=rs.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("Error Inserting In to Confirmations Table: "+e.getMessage());
            return -1;
        }
        finally {
            this.close();
        }

        return createdid;
    }

    public Integer deleteconfirmID(Integer id){
        this.open();
        try{
            delconfirmbyID.setInt(1,id);
            int affectedrows= delconfirmbyID.executeUpdate();
            if(affectedrows!=1){
                System.out.println("Affectedrows Error deleting from Confirmations table");
                return -1;
            }
            return affectedrows;
        }
        catch (SQLException e){
            System.out.println("SQL ERROR on Deleting Confirmation:ID "+id+"'Confirmations' table: "+e.getMessage());
            return -1;
        }
        finally {
            this.close();
        }
    }

    public Integer updateConfirmationbyID(Confirmations modifyconfirm, String id){
        this.open();
        try {
            updateconfirmbyID.setString(1,modifyconfirm.getInstructions());
            updateconfirmbyID.setTimestamp(2, Timestamp.valueOf(modifyconfirm.getAccepted_date()));
            updateconfirmbyID.setInt(3,Integer.valueOf(id));
            int affectedrows= updateconfirmbyID.executeUpdate();
            if(affectedrows!=1){
                System.out.println("Affectedrows Error Updating into Confirmations table");
                return -1;
            }
        }catch (SQLException e){
            System.out.println("Error Updating In to Confirmations Table: "+e.getMessage());
            return -1;
        }
        finally {
            this.close();
        }
        return 1;
    }

    public Confirmations getConfirmbyID(Integer confirmid){
        Confirmations result= new Confirmations();
        this.open();

        try{
            getconfirmbyID.setInt(1,confirmid);
            ResultSet rs= getconfirmbyID.executeQuery();

            while (rs.next()){
                result= ExtractConfirmationsRS(rs);
            }
        }
        catch (SQLException e){
            System.out.println("Error Querying Confirmations ID: "+confirmid+"-:"+e.getMessage());
        }
        finally {
            this.close();
        }
        return result;
    }

}
