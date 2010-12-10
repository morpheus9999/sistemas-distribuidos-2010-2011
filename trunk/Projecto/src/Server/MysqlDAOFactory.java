/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author jojo
 */
// Cloudscape concrete DAO Factory implementation
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MysqlDAOFactory extends DAOFactory {

    public static final String DBURL = "jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root";

    // method to create Cloudscape connections
    public static Connection createConnection() {
        // Use DRIVER and DBURL to create a connection
        // Recommend connection pool implementation/usage

        try {

            return DriverManager.getConnection(DBURL);
        
        } catch (SQLException ex) {
            return null;
        }

    }

    public CustomerDAO getCustomerDAO() {
        // MysqlCustomerDAO implements CustomerDAO
        return new MysqlCustomerDAO();
    }
    /*
    public AccountDAO getAccountDAO() {
    // CloudscapeAccountDAO implements AccountDAO
    return new CloudscapeAccountDAO();
    }
    public OrderDAO getOrderDAO() {
    // CloudscapeOrderDAO implements OrderDAO
    return new CloudscapeOrderDAO();
    }
     */
}
