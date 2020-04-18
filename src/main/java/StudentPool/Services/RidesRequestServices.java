package StudentPool.Services;

import StudentPool.Database.Datasource;
import StudentPool.model.Rides;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RidesRequestServices {

    public static void main(String[] args) {



        String[] emailids= {"RXJ08740@ucmo.edu","SXP28730@ucmo.edu","SXT04990@ucmo.edu","RXJ0007@ucmo.edu","rahul123@ucmo.edu"};
        String[] datestring={"2020-01-5","2020-01-15","2020-02-25","2020-02-04"};
        String[] timestring={"10:00:00","14:00:00","16:00:00"};
        String[] tocity={"San Jose","San Francisco","San Diego ","Santa Clara","San Marcos","Santa Monica","Santa Barbara"};
        String[] startcity={"Warrensburg","Lees summit","KansasCity","Overland Park"};
        Integer[] slots={2,3,4};
        String[] instruction={"paid ride", "tour of USA","Only friendly rides"};

        RidesRequestServices newservice= new RidesRequestServices();
        for(int i=0;i<200;i++){
            Rides newofferide= new Rides(emailids[(int)(Math.random()*emailids.length)],
                   (datestring[(int)(Math.random()*datestring.length)]),
                   (timestring[(int)(Math.random()*timestring.length)]),
                    startcity[(int)(Math.random()*startcity.length)],
                    tocity[(int)(Math.random()*tocity.length)],
                    slots[(int)(Math.random()*slots.length)],
                    instruction[(int)(Math.random()*instruction.length)]);

            newservice.InsertIntoRR(newofferide);

        }

    }

    private static final String RR_OFFERTAB="rides_requested";
    private static final String RR_ID ="id";
    private static final String RR_USERID="user_id";
    private static final String RR_DATE="ride_date";
    private static final String RR_TIME="ride_time";
    private static final String RR_START="start_from";
    private static final String RR_END="end_to";
    private static final String RR_SLOTS="slots_offered";
    private static final String RR_INST="instructions";

    private static final String RR_VIEWALL= "SELECT *"+" FROM "+RR_OFFERTAB;
    private static final String RR_INSERT= "INSERT INTO "+RR_OFFERTAB+
            "("+RR_USERID+","+RR_DATE+","+RR_TIME+","+RR_START+","+RR_END+","+RR_SLOTS+","+RR_INST+") VALUES(?,?,?,?,?,?,?)";
    private static final String RR_TOCITY= "SELECT *"+" FROM "+RR_OFFERTAB+
            " WHERE "+RR_END+"=?"+ " ORDER BY "+RR_DATE+","+RR_TIME;
    private static final String RR_FROMID= "SELECT *"+" FROM "+RR_OFFERTAB+
            " WHERE "+ RR_ID +"=?";
    private static final String RR_DELID= "DELETE"+" FROM "+RR_OFFERTAB+
            " WHERE "+ RR_ID +"=?";
    private static final String RR_UPDATE="UPDATE "+RR_OFFERTAB+
            " SET "+RR_USERID+"=?"+","+RR_DATE+"=?"+","+
            RR_TIME+"=?"+","+RR_START+"=?"+","+
            RR_END+"=?"+","+RR_SLOTS+"=?"+","+RR_INST+"=?"+ " WHERE "+RR_ID+"=?";

    private PreparedStatement viewrequestrides;
    private PreparedStatement insertriderequest;
    private PreparedStatement queryridetocity;
    private PreparedStatement queryridebyID;
    private PreparedStatement deleteridebyID;
    private PreparedStatement updateridebyID;


    private boolean open(){
        
        try{
            //All sql query prepared statements for the user table
            viewrequestrides = Datasource.getInstance().getConn().prepareStatement(RR_VIEWALL);
            insertriderequest= Datasource.getInstance().getConn().prepareStatement(RR_INSERT, Statement.RETURN_GENERATED_KEYS);
            queryridetocity= Datasource.getInstance().getConn().prepareStatement(RR_TOCITY);
            queryridebyID= Datasource.getInstance().getConn().prepareStatement(RR_FROMID);
            deleteridebyID= Datasource.getInstance().getConn().prepareStatement(RR_DELID);
            updateridebyID= Datasource.getInstance().getConn().prepareStatement(RR_UPDATE);
            return true;
        }
        catch (SQLException e){
            System.out.println("Can't Connect to Rides offered database: "+e.getMessage());
            return false;
        }
    }

    private void close(){
        try{
            if(viewrequestrides !=null){
                viewrequestrides.close();
            }
            if(insertriderequest!=null){
                insertriderequest.close();
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
            if(updateridebyID!=null){
                updateridebyID.close();
            }

        }catch (SQLException e){
            System.out.println("Error in Closing Prepared Statement: "+e.getMessage());
        }
        finally {
            Datasource.getInstance().close();
        }

    }

    public Integer InsertIntoRR(Rides riderequest){
        this.open();
        Integer createdid=0;

        try{
            insertriderequest.setString(1,riderequest.getUser_id().toLowerCase());
            insertriderequest.setDate(2, Date.valueOf(riderequest.getRide_date()));
            insertriderequest.setTime(3, Time.valueOf(riderequest.getRide_time()));
            insertriderequest.setString(4,riderequest.getStart_from());
            insertriderequest.setString(5,riderequest.getEnd_to());
            insertriderequest.setInt(6,riderequest.getSlots_offered());
            insertriderequest.setString(7,riderequest.getInstructions());
            int affectedrows= insertriderequest.executeUpdate();


            if(affectedrows!=1){
                System.out.println("Affectedrows Error Inserting into rides_requested table");
                return -1;
            }
            ResultSet rs= insertriderequest.getGeneratedKeys();
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
            tempride.setRide_date(resultSet.getDate(3).toString());
            tempride.setRide_time(resultSet.getTime(4).toString());
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

    public List<Rides> getAllRequestedRides(){
        List<Rides> result = new ArrayList<>();
        this.open();
        try{
            ResultSet resultSet = viewrequestrides.executeQuery();
            while(resultSet.next()){
                result.add(ExtractFromResultSet(resultSet));
            }
            return result;
        }catch (SQLException e){
            System.out.println("Query to get all requested rides failed: "+ e.getMessage());
        }
        finally {
            this.close();
        }
        return new ArrayList<>(result);
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
            System.out.println("Query to get all requested rides to "+destinationcity +" failed: "+ e.getMessage());
        }
        finally {
            this.close();
        }
        return new ArrayList<>(result);
    }

    public List<Rides> getAllRequestedRidesPaginated(List<Rides> input,int start, int size){
        List<Rides> result = new ArrayList<>(input);
        if((start+size)>result.size()){
            return result;
        }
        else{
            return new ArrayList<>(result.subList(start,start+size)) ;
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
            System.out.println("Query to get all requested rides to "+id +" failed: "+ e.getMessage());
        }
        finally {
            this.close();
        }
        return result;// possibility of this being Null// change here to give a new Instance of Rides object.
    }

    public Integer deleteRidebyID(Integer id){
        this.open();
        try{
            deleteridebyID.setInt(1,id);
            int affectedrows= deleteridebyID.executeUpdate();
            if(affectedrows!=1){
                System.out.println("Affectedrows Error Deleting from requested table");
                return -1;
            }
            return affectedrows;
        }
        catch (SQLException e){
            System.out.println("SQL ERROR on Deleting ride:ID "+id+"'rides_requested' table: "+e.getMessage());
            return -1;
        }
        finally {
            this.close();
        }
    }


    public Integer updateRideInfo(Rides modifyoffer,String id){
        this.open();

        try{
            updateridebyID.setString(1,modifyoffer.getUser_id().toLowerCase());
            updateridebyID.setDate(2,Date.valueOf(modifyoffer.getRide_date()));
            updateridebyID.setTime(3,Time.valueOf(modifyoffer.getRide_time()));
            updateridebyID.setString(4,modifyoffer.getStart_from());
            updateridebyID.setString(5,modifyoffer.getEnd_to());
            updateridebyID.setInt(6,modifyoffer.getSlots_offered());
            updateridebyID.setString(7,modifyoffer.getInstructions());
            updateridebyID.setInt(8,Integer.valueOf(id));
            int affectedrows= updateridebyID.executeUpdate();

            if(affectedrows!=1){
                System.out.println("Affectedrows Error Updating into rides_requested table");
                return -1;
            }

        }
        catch (SQLException e){
            System.out.println("SQL ERROR on 'rides_requested' table: "+e.getMessage());
            return -1;
        }
        finally {
            this.close();
        }
        return 1;

    }

}
