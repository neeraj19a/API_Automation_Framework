package Utilities;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import utilities.PropertyReader;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Database {
    private final static Log LOGGER = LogFactory.getLog(Database.class.getName());
    String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    String DbUrl = null;
    String User;
    String Password;
    String Env = null;

    public void initializeDbDomain() {
        int flag;
        PropertyReader propertyReader = new PropertyReader();
        Env = System.getProperty("DbEnv") == null ? propertyReader.getProperties().get("DbEnv") : System.getProperty("DbEnv");

        switch (Env) {
            case "stage":    //.33 staging server
                System.out.println("In stage DB");
                DbUrl = propertyReader.getProperties().get("DbUrlForStage");
                User = propertyReader.getProperties().get("DbUserForStage");
                Password = propertyReader.getProperties().get("DbPassForStage");
                flag = 1;
                break;

            case "integration":    //.43 integration server
                System.out.println("In Integration DB");
                DbUrl = propertyReader.getProperties().get("DbUrlForIntegration");
                User = propertyReader.getProperties().get("DbUserForIntegration");
                Password = propertyReader.getProperties().get("DbPassForIntegration");
                flag = 1;
                break;

            case "production":    //. production Server
                DbUrl = propertyReader.getProperties().get("DbUrlForProduction");
                User = propertyReader.getProperties().get("DbUserForProduction");
                Password = propertyReader.getProperties().get("DbPassForProduction");
                flag = 1;
                break;

            default:
                flag = 0;
                Assert.assertTrue(flag == 1, "DbDomain value in config.properties file is not a valid match with switch case");
        }
    }

    public String GetResultQueryExecutor(String dbName, String queryStatement) throws ClassNotFoundException, IOException {
        ResultSet retVal = null;
        long id = 0;
        Connection conn = null;
        Statement stmt = null;
        String value = null;
        int flag = 1; //0=indicates no failure, 1=indicates success of all query execution
        initializeDbDomain();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DbUrl + dbName, User, Password);

            stmt = conn.createStatement();

            String sql = queryStatement;
            ResultSet rs = stmt.executeQuery(sql);

            if (rs != null) {
                rs.beforeFirst();
                rs.last();
                rs.getRow();
                value = rs.getString(1);
            }
//            System.out.println("value = " + value);
        } catch (SQLException se) {
            flag = 0;
            se.printStackTrace();
        } catch (Exception e) {
            flag = 0;
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            Assert.assertTrue(flag == 1, "Error!! Query execution failed...");
        }
        return value;
    }


    public void InsertIntoTable(String dbName, String queryStatement) throws ClassNotFoundException, IOException {
        ResultSet retVal = null;
        long id = 0;
        Connection conn = null;
        Statement stmt = null;
        String value = null;
        int flag = 1; //0=indicates no failure, 1=indicates success of all query execution
        initializeDbDomain();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DbUrl + dbName, User, Password);

            stmt = conn.createStatement();

            String sql = queryStatement;
            stmt.executeUpdate(sql);

        } catch (SQLException se) {
            flag = 0;
            se.printStackTrace();
        } catch (Exception e) {
            flag = 0;
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            Assert.assertTrue(flag == 1, "Error!! Query execution failed...");
        }
    }

    public String GetResultQueryExecutor(String dbName, String queryStatement, String columnName) throws ClassNotFoundException, IOException {
        Connection conn = null;
        Statement stmt = null;
        String value = null;
        int flag = 1; //0=indicates no failure, 1=indicates success of all query execution
        initializeDbDomain();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DbUrl + dbName, User, Password);

            stmt = conn.createStatement();

            String sql = queryStatement;
            ResultSet rs = stmt.executeQuery(sql);

            if (rs != null) {
                rs.beforeFirst();
                rs.last();
                rs.getRow();
                value = rs.getString(columnName);
            }
            System.out.println("value = " + value);
        } catch (SQLException se) {
            flag = 0;
            se.printStackTrace();
        } catch (Exception e) {
            flag = 0;
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            Assert.assertTrue(flag == 1, "Error!! Query execution failed...");
        }
        return value;
    }


    public List<List<String>> GetResultQueryExecutorAsList(String dbName, String queryStatement) throws ClassNotFoundException, IOException {
        Connection conn = null;
        Statement stmt = null;
        List<List<String>> result = new ArrayList<>();
        int flag = 1; //0=indicates no failure, 1=indicates success of all query execution
        initializeDbDomain();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DbUrl + dbName, User, Password);

            stmt = conn.createStatement();

            String sql = queryStatement;
            ResultSet resultset = stmt.executeQuery(sql);//from DB
            int numcols = resultset.getMetaData().getColumnCount();

            while (resultset.next()) {
                List<String> row = new ArrayList<>(numcols); // new list per row

                for (int i = 1; i <= numcols; i++) {  // don't skip the last column, use <=
                    row.add(resultset.getString(i));
                    //System.out.print(resultset.getString(i) + "\t");
                }
                result.add(row); // add it to the result
                //System.out.println();
            }

        } catch (SQLException se) {
            flag = 0;
            se.printStackTrace();
        } catch (Exception e) {
            flag = 0;
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            Assert.assertTrue(flag == 1, "Error!! Query execution failed...");
        }
        return result;
    }


    public List<List<Integer>> GetResultQueryExecutorAsIntegerList(String dbName, String queryStatement) throws ClassNotFoundException, IOException {
        Connection conn = null;
        Statement stmt = null;
        List<List<Integer>> result = new ArrayList<>();
        int flag = 1; //0=indicates no failure, 1=indicates success of all query execution
        initializeDbDomain();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DbUrl + dbName, User, Password);

            stmt = conn.createStatement();

            String sql = queryStatement;
            ResultSet resultset = stmt.executeQuery(sql);//from DB
            int numcols = resultset.getMetaData().getColumnCount();

            while (resultset.next()) {
                List<Integer> row = new ArrayList<>(numcols); // new list per row

                for (int i = 1; i <= numcols; i++) {  // don't skip the last column, use <=
                    row.add(resultset.getInt(i));
                    //System.out.print(resultset.getString(i) + "\t");
                }
                result.add(row); // add it to the result
                //System.out.println();
            }

        } catch (SQLException se) {
            flag = 0;
            se.printStackTrace();
        } catch (Exception e) {
            flag = 0;
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            Assert.assertTrue(flag == 1, "Error!! Query execution failed...");
        }
        return result;
    }

    //Generic Method to Get List of Values from DB
    public List<List<String>> getListValuesFromDatabase(String dbName, String sqlQuery) {

        List<List<String>> objects = new ArrayList<>();
        try {
            objects = GetResultQueryExecutorAsList(dbName, sqlQuery);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public List<List<Integer>> getListIntegerValuesFromDatabase(String dbName, String sqlQuery) {

        List<List<Integer>> objects = new ArrayList<>();
        try {
            objects = GetResultQueryExecutorAsIntegerList(dbName, sqlQuery);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public String getOfferId(String AdId) {
        String offerId = null;
        try {
            String query = "SELECT `id` FROM `escrow_offer` WHERE `adid`='" + AdId + "';";
            offerId = GetResultQueryExecutor("escrow_c2c", query);
            System.out.println("Offer ID for is : : " + offerId);
            LOGGER.info("Offer ID for is : : " + offerId);
        } catch (IOException io) {
            System.out.println("IOException in getOfferId");
            io.printStackTrace();
        } catch (ClassNotFoundException cnf) {
            System.out.println("ClassNotFoundException in getOfferId");
            cnf.printStackTrace();
        }
        return offerId;
    }

    public ArrayList getResultQuery(String dbName, String query, String AdId) {

        initializeDbDomain();
        ResultSet retVal = null;
        ResultSetMetaData columnName = null;
        int numberOfColumns = 0;

        Connection conn = null;
        Statement stmt = null;

        ArrayList list = null;
        HashMap row = null;
        int flag = 1; //0=indicates no failure, 1=indicates success of all query execution

        initializeDbDomain();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DbUrl + dbName, User, Password);

            stmt = conn.createStatement();

            String sql = query;
            retVal = stmt.executeQuery(sql);

            if (retVal != null) {
                columnName = retVal.getMetaData();
                numberOfColumns = columnName.getColumnCount();
                list = new ArrayList();
                //row = new HashMap();
                while (retVal.next()) {
                    for (int i = 1; i <= numberOfColumns; i++) {

                        String column = columnName.getColumnName(i);
                        list.add(retVal.getString(column));

                    }

                }
                Thread.sleep(5000);
            }

        } catch (SQLException se) {
            flag = 0;
            se.printStackTrace();
        } catch (Exception e) {
            flag = 0;
            e.printStackTrace();
        } finally {
            try {
                retVal.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }


            Assert.assertTrue(flag == 1, "Error!! Query execution failed...");
        }
        return list;


    }

    //Generic method to return String value from Database
    public String returnStringValuefromDatabase(String dbName, String sqlQuery) {

        initializeDbDomain();
        String value = null;
        try {
            value = GetResultQueryExecutor(dbName, sqlQuery);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    //Generic method to return int value from Database
    public int returnIntValuefromDatabase(String dbName, String sqlQuery) {

        initializeDbDomain();
        int value = 0;
        try {
            value = (Integer.parseInt(GetResultQueryExecutor(dbName, sqlQuery)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

}
