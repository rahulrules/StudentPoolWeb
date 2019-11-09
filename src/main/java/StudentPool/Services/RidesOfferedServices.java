package StudentPool.Services;

import StudentPool.Database.Datasource;
import StudentPool.model.Rides;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RidesOfferedServices {

    public static void main(String[] args) {

        RidesOfferedServices newservice= new RidesOfferedServices();
//        System.out.println(newservice.RidesToCity("NewYork").size());
        Rides newride= new Rides("RXJ123@ucmo.edu",Date.valueOf("2019-05-20"),Time.valueOf("18:00:00"),"Warrensburg","Sanjose");
        System.out.println(newservice.InsertIntoRO(newride));
//        System.out.println(newservice.getRideFromId(5).getEnd_to());
//        System.out.println(newservice.deleteRidebyID(201));

    }

    private static final String RO_OFFERTAB="rides_offered";
    private static final String RO_ID ="id";
    private static final String RO_USERID="user_id";
    private static final String RO_DATE="ride_date";
    private static final String RO_TIME="ride_time";
    private static final String RO_START="start_from";
    private static final String RO_END="end_to";
    private static final String RO_SLOTS="slots_offered";
    private static final String RO_INST="instructions";

    private static final String RO_VIEWALL= "SELECT *"+" FROM "+RO_OFFERTAB;
    private static final String RO_INSERT= "INSERT INTO "+RO_OFFERTAB+
            "("+RO_USERID+","+RO_DATE+","+RO_TIME+","+RO_START+","+RO_END+","+RO_SLOTS+","+RO_INST+") VALUES(?,?,?,?,?,?,?)";
    private static final String RO_TOCITY= "SELECT *"+" FROM "+RO_OFFERTAB+
            " WHERE "+RO_END+"=?"+ " ORDER BY "+RO_DATE+","+RO_TIME;
    private static final String RO_FROMID= "SELECT *"+" FROM "+RO_OFFERTAB+
            " WHERE "+ RO_ID +"=?";
    private static final String RO_DELID= "DELETE"+" FROM "+RO_OFFERTAB+
            " WHERE "+ RO_ID +"=?";


    private PreparedStatement viewofferedrides;
    private PreparedStatement insertrideoffer;
    private PreparedStatement queryridetocity;
    private PreparedStatement queryridebyID;
    private PreparedStatement deleteridebyID;


    private boolean open(){
        try{
            //All sql query prepared statements for the user table
            viewofferedrides= Datasource.getInstance().getConn().prepareStatement(RO_VIEWALL);
            insertrideoffer= Datasource.getInstance().getConn().prepareStatement(RO_INSERT,Statement.RETURN_GENERATED_KEYS);
            queryridetocity= Datasource.getInstance().getConn().prepareStatement(RO_TOCITY);
            queryridebyID= Datasource.getInstance().getConn().prepareStatement(RO_FROMID);
           deleteridebyID= Datasource.getInstance().getConn().prepareStatement(RO_DELID);

            return true;
        }
        catch (SQLException e){
            System.out.println("Can't Connect to database: "+e.getMessage());
            return false;
        }
    }

    private void close(){
        try{
            if(viewofferedrides!=null){
                viewofferedrides.close();
            }
            if(insertrideoffer!=null){
                insertrideoffer.close();
            }
            if(queryridetocity!=null){
                queryridetocity.close();
            }
            if(queryridebyID!=null){
                queryridebyID.close();
            }
            if(deleteridebyID!=null){
                deleteridebyID.close();
            }

        }catch (SQLException e){
            System.out.println("Error in Closing Prepared Statement: "+e.getMessage());
        }
        finally {
            Datasource.getInstance().close();
        }

    }

    public Integer InsertIntoRO(Rides rideoffer){
        this.open();
        Integer createdid=0;
        try{
            insertrideoffer.setString(1,rideoffer.getUser_id().toLowerCase());
            insertrideoffer.setDate(2,rideoffer.getRide_date());
            insertrideoffer.setTime(3,rideoffer.getRide_time());
            insertrideoffer.setString(4,rideoffer.getStart_from());
            insertrideoffer.setString(5,rideoffer.getEnd_to());
            insertrideoffer.setInt(6,rideoffer.getSlots_offered());
            insertrideoffer.setString(7,rideoffer.getInstructions());
            int affectedrows= insertrideoffer.executeUpdate();


            if(affectedrows!=1){
                System.out.println("Affectedrows Error Inserting into rides_offered table");
                return -1;
            }
            ResultSet rs= insertrideoffer.getGeneratedKeys();
            while (rs.next()){
                createdid=rs.getInt(1);
            }
        }
        catch (SQLException e){
            System.out.println("SQL ERROR on 'rides_offered' table: "+e.getMessage());
            return -1;
        }
        finally {
            this.close();
        }
        return createdid;

    }

    private Rides ExtractFromResultSet(ResultSet resultSet){
        Rides tempride= new Rides();
        try
        {
            tempride.setId(resultSet.getInt(1));
            tempride.setUser_id(resultSet.getString(2));
            tempride.setRide_date(resultSet.getDate(3));
            tempride.setRide_time(resultSet.getTime(4));
            tempride.setStart_from(resultSet.getString(5));
            tempride.setEnd_to(resultSet.getString(6));
            tempride.setSlots_offered(resultSet.getInt(7));
            tempride.setInstructions(resultSet.getString(8));
        }
        catch (SQLException e){
            System.out.println("Error Processing ResultSet: "+e.getMessage());
        }

        return tempride;
    }

    public List<Rides> getAllOfferedRides(){
        List<Rides> result = new ArrayList<>();
        this.open();
        try{
            ResultSet resultSet =viewofferedrides.executeQuery();
            while(resultSet.next()){
                result.add(ExtractFromResultSet(resultSet));
            }
            return result;
        }catch (SQLException e){
            System.out.println("Query to get all offered rides failed: "+ e.getMessage());
        }
        finally {
            this.close();
        }
        return result;
    }

    public List<Rides> RidesToCity(String destinationcity){
        List<Rides> result = new ArrayList<>();
        this.open();
        try{
            queryridetocity.setString(1,destinationcity);
            ResultSet resultSet =queryridetocity.executeQuery();
            while(resultSet.next()){
                result.add(ExtractFromResultSet(resultSet));
            }
            return result;
        }catch (SQLException e){
            System.out.println("Query to get all offered rides to "+destinationcity +" failed: "+ e.getMessage());
        }
        finally {
            this.close();
        }
        return new ArrayList<>(result);
    }

    public List<Rides> getAllOfferedRidesPaginated(int start, int size){
        List<Rides> result = new ArrayList<>(this.getAllOfferedRides());
        if((start+size)>result.size()){
            return result;
        }
        else{
           return result.subList(start,size);
        }
    }

    public Rides getRideFromId(Integer id){
        Rides result=new Rides();
        this.open();
        try{
            queryridebyID.setInt(1,id);
            ResultSet resultSet =queryridebyID.executeQuery();
            while(resultSet.next()){
                result=ExtractFromResultSet(resultSet);
            }
            return result;
        }catch (SQLException e){
            System.out.println("Query to get all offered rides to "+id +" failed: "+ e.getMessage());
        }
        finally {
            this.close();
        }
        return result;// possibility of this being Null
    }

    public Integer deleteRidebyID(Integer id){
        this.open();
        try{
            deleteridebyID.setInt(1,id);
            int affectedrows= deleteridebyID.executeUpdate();
            if(affectedrows!=1){
                System.out.println("Affectedrows Error Deleting from rides_offered table");
                return -1;
            }
            return affectedrows;
        }
        catch (SQLException e){
            System.out.println("SQL ERROR on Deleting ride:ID "+id+"'rides_offered' table: "+e.getMessage());
            return -1;
        }
        finally {
            this.close();
        }
    }

}
