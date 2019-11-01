package StudentPool.Database;

import java.sql.*;

public class Datasource {

    //making Datasource class a singleton class- Eager Initialization
    private static final Datasource datasource = new Datasource();

    //private constructor - so no dafualt no arg constructor is not applicable
    private Datasource(){

    }

    public static Datasource getInstance(){
        datasource.open();
        return Datasource.datasource;
    }

    private static final  String DB_NAME="StudentServices";
    private static final String CONNECTION="jdbc:postgresql://localhost/"+
            DB_NAME+"?user=postgres&password=Luhar123";

    private static final String USER_TAB ="users";
    private static final String USER_ID="id";
    private static final String USER_USER_ID="user_id";
    private static final String USER_USER_PAS="user_password";

    private static final String SP_TAB="studentprofiles";
    private static final String SP_ID="id";
    private static final String SP_USERID="user_id";
    private static final String SP_FIRSTNAME="first_name";
    private static final String SP_LASTNAME="last_name";
    private static final String SP_DEPT="department";
    private static final String SP_ADMIT="admityear";
    private static final String SP_RESIDE= "residentcity";

    private static final String R_TAB="rides";
    private static final String R_ID="id";
    private static final String R_USERID="user_id";
    private static final String R_DATE="ride_date";
    private static final String R_TIME="ride_time";
    private static final String R_START="start_from";
    private static final String R_END="end_to";
    private static final String R_SLOTS="slots_offered";
    private static final String R_INST="instructions";



    private static final String USER_INS= "INSERT INTO "+
            USER_TAB+"("+USER_USER_ID+","+USER_USER_PAS+") VALUES(?,?)";

    private static final String SP_INS= "INSERT INTO "+
            SP_TAB+"("+SP_ID+","+SP_USERID+","+SP_FIRSTNAME+") VALUES(?,?,?)";// start from here for OCT 22.




    private Connection conn;
    private PreparedStatement insertuser;

    private boolean open(){
        try{
            conn = DriverManager.getConnection(CONNECTION);
//            insertuser=conn.prepareStatement(USER_INS);

            return true;
        }
        catch (SQLException e){
            System.out.println("Can't Connect to database: "+e.getMessage());
            return false;
        }
    }

    public Connection getConn(){

            return this.conn;
    }

    public void close(){

        try{

            if(conn!=null){
                conn.close();
            }

        }catch (SQLException e){
            System.out.println("Couldn't close the connection: "+e.getMessage());

        }

    }
    //function to print all tables and their variables.

    public void printtableschema(){
        try{
            DatabaseMetaData dbmetadata= conn.getMetaData();//Storing schema tables in to a string of table names
            ResultSet resulttables = dbmetadata.getTables(null,null,null, new String[] {"TABLE"});
            System.out.println("Tables Schema");

            while(resulttables.next()){
                String query="SELECT * FROM "+resulttables.getString("TABLE_NAME");//schema query for each table

                try(Statement stmt= conn.createStatement();
                ResultSet result = stmt.executeQuery(query)){
                    System.out.print(resulttables.getString("TABLE_NAME")+"(");
                    ResultSetMetaData rsmetdata= result.getMetaData();
                    for(int i =1; i<=rsmetdata.getColumnCount();i++){
                        System.out.print(rsmetdata.getColumnName(i)+",");//printing column name of table
                    }
                    System.out.print(")");

                } catch (SQLException e){// catch block for result query
                    System.out.println("Can't execute the table schema query:"+ e.getMessage());
                }

                System.out.println();
//                System.out.print(" "+resulttables.getString("TABLE_NAME")+" ");
            }

        }
        catch (SQLException e){
            System.out.println("Can't get Database Metadata :"+ e.getMessage());

        }
    }

    //get count of items in the table , can be use to set the ID of the next insert entry.
    public int getcount(String table_name){
        String sql ="SELECT COUNT(*) FROM "+table_name;

        try {
            Statement stmt =conn.createStatement();
            ResultSet resultSet= stmt.executeQuery(sql);
            int count =0;
            while(resultSet.next()){
                count = resultSet.getInt("Count");
            }
            return count;
        }
        catch (SQLException e){
            System.out.println(" count query statement can't execute:"+e.getMessage());
            return -1;
        }
    }

}


