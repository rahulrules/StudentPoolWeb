package StudentPool.Services;

import StudentPool.Database.Datasource;
import StudentPool.model.Bookings;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingServices {

    public static void main(String[] args) {
//        System.out.println(BOOK_INSERT);
        Bookings newbooking= new Bookings("RXJ08740@ucmo.edu",
                2,1,"I will be on time");
        BookingServices newBookService= new BookingServices();
        System.out.println(newBookService.insertBooking(newbooking));

    }

    private static final String BOOK_TAB ="bookings";
    private static final String BOOK_ID="id";
    private static final String BOOK_USER_ID="user_id";
    private static final String BOOK_DATE="booking_date";
    private static final String BOOK_RIDEID="rideoffered_id";
    private static final String BOOK_SLOTS="slots_booked";
    private static final String BOOK_INST="instructions";

    private static final String BOOK_VIEWBYRIDEID="SELECT * FROM "+BOOK_TAB+" WHERE "+BOOK_RIDEID+" =?";
    private static final String BOOK_INSERT="INSERT INTO "+BOOK_TAB+
            "("+BOOK_USER_ID+","+BOOK_DATE+","+BOOK_RIDEID+","+BOOK_SLOTS+","+BOOK_INST+") VALUES(?,?,?,?,?)";

    private static final String BOOK_DELID= "DELETE"+" FROM "+BOOK_TAB+
            " WHERE "+ BOOK_ID+"=?";

    private static final String BOOK_UPDATE="UPDATE "+BOOK_TAB+
            " SET "+BOOK_SLOTS+"=?"+","+BOOK_INST+"=?"+","+BOOK_DATE+"=?"+" WHERE "+BOOK_ID+"=?";
    private static final String BOOK_GETID= "SELECT * FROM "+BOOK_TAB+" WHERE "+BOOK_ID+"=?";

    private PreparedStatement ridebookings;
    private PreparedStatement insertbook;
    private PreparedStatement delbookbyID;
    private PreparedStatement updatebookbyID;
    private PreparedStatement getbookbyID;


    private boolean open(){

        try{
            ridebookings= Datasource.getInstance().getConn().prepareStatement(BOOK_VIEWBYRIDEID);
            insertbook= Datasource.getInstance().getConn().prepareStatement(BOOK_INSERT,Statement.RETURN_GENERATED_KEYS);
            delbookbyID= Datasource.getInstance().getConn().prepareStatement(BOOK_DELID);
            updatebookbyID= Datasource.getInstance().getConn().prepareStatement(BOOK_UPDATE);
            getbookbyID= Datasource.getInstance().getConn().prepareStatement(BOOK_GETID);
            return true;

        } catch (SQLException e){
            System.out.println("Can't Connect to Bookings Table: "+e.getMessage());
            return false;
        }
    }

    private void close(){
        try {
            if(ridebookings!=null){
                ridebookings.close();
            }
            if(insertbook!=null){
                insertbook.close();
            }
            if(delbookbyID!=null){
                delbookbyID.close();
            }
            if(updatebookbyID!=null){
                updatebookbyID.close();
            }
            if(getbookbyID!=null){
                getbookbyID.close();
            }
        }catch (SQLException e){
            System.out.println("Error Closing Prepared Statements for Bookings: "+e.getMessage());
        }

        finally {
            Datasource.getInstance().close();
        }
    }

    public Bookings ExtractBookingRS(ResultSet resultSet){
        Bookings tempbooking= new Bookings();

        try{
            tempbooking.setId(resultSet.getInt(1));
            tempbooking.setUser_id(resultSet.getString(2).toLowerCase());
            tempbooking.setBooking_date(resultSet.getDate(3).toString());
            tempbooking.setRideoffered_id(resultSet.getInt(4));
            tempbooking.setSlots_booked(resultSet.getInt(5));
            tempbooking.setInstructions(resultSet.getString(6));

        }catch (SQLException e){
            System.out.println("Error Processing Resultset: "+e.getMessage());

        }
        return tempbooking;
    }

    public List<Bookings> getBookingForRIdeID(Integer rideid){
        List<Bookings> result= new ArrayList<>();
        this.open();

        try{
            ridebookings.setInt(1,rideid);
            ResultSet rs= ridebookings.executeQuery();

            while (rs.next()){
                result.add(ExtractBookingRS(rs));
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

    public Integer insertBooking(Bookings newbooking){
        Integer createdid=0;
        this.open();

        try {
            insertbook.setString(1,newbooking.getUser_id());
            insertbook.setTimestamp(2, Timestamp.valueOf(newbooking.getBooking_date()));
            insertbook.setInt(3,newbooking.getRideoffered_id());
            insertbook.setInt(4,newbooking.getSlots_booked());
            insertbook.setString(5,newbooking.getInstructions());

            int affectedrows= insertbook.executeUpdate();
            if(affectedrows!=1){
                System.out.println("Affectedrows Error Inserting into Booking table");
                return -1;
            }
            ResultSet rs= insertbook.getGeneratedKeys();
            while (rs.next()){
                createdid=rs.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("Error Inserting In to Bookings Table: "+e.getMessage());
            return -1;
        }
        finally {
            this.close();
        }

        return createdid;
    }

    public Integer deletebookID(Integer id){
        this.open();
        try{
            delbookbyID.setInt(1,id);
            int affectedrows= delbookbyID.executeUpdate();
            if(affectedrows!=1){
                System.out.println("Affectedrows Error deleting from bookings table");
                return -1;
            }
            return affectedrows;
        }
        catch (SQLException e){
            System.out.println("SQL ERROR on Deleting booking:ID "+id+"'bookings' table: "+e.getMessage());
            return -1;
        }
        finally {
            this.close();
        }
    }

    public Integer updateBookingbyID(Bookings modifybook,String id){
        this.open();
        try {
            updatebookbyID.setInt(1,modifybook.getSlots_booked());
            updatebookbyID.setString(2,modifybook.getInstructions());
            updatebookbyID.setTimestamp(3, Timestamp.valueOf(modifybook.getBooking_date()));
            updatebookbyID.setInt(4,Integer.valueOf(id));
            int affectedrows= updatebookbyID.executeUpdate();
            if(affectedrows!=1){
                System.out.println("Affectedrows Error Updating into Booking table");
                return -1;
            }
        }catch (SQLException e){
            System.out.println("Error Updating In to Bookings Table: "+e.getMessage());
            return -1;
        }
        finally {
            this.close();
        }
        return 1;
    }

    public Bookings getBookingbyID(Integer bookingid){
       Bookings result= new Bookings();
        this.open();

        try{
            getbookbyID.setInt(1,bookingid);
            ResultSet rs= getbookbyID.executeQuery();

            while (rs.next()){
                result=ExtractBookingRS(rs);
            }
        }
        catch (SQLException e){
            System.out.println("Error Querying Booking ID: "+bookingid+"-:"+e.getMessage());
        }
        finally {
            this.close();
        }
        return result;
    }

}
