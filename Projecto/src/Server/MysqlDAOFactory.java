/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.sql.*;

/**
 *
 * @author jojo
 */
// Cloudscape concrete DAO Factory implementation

public class MysqlDAOFactory extends DAOFactory {

    public static final String DBURL = "jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    static Connection con;

    // method to create Cloudscape connections
    public static Connection createConnection() {
        // Use DRIVER and DBURL to create a connection
        // Recommend connection pool implementation/usage
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(DBURL);
        } catch (Exception e) {
            return null;
            //e.printStackTrace();
        }
        return con;

    }

    public CustomerDAO getCustomerDAO() {
        // MysqlCustomerDAO implements CustomerDAO
        return new MysqlCustomerDAO();
    }
    
    public AccountDAO getAccountDAO() {
        // MysqlAccountDAO implements AccountDAO

        return new MysqlAccountDAO();
    }
    /*
    public OrderDAO getOrderDAO() {
    // CloudscapeOrderDAO implements OrderDAO
    return new CloudscapeOrderDAO();
    }
     */
}
