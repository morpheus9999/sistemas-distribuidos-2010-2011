/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;

/**
 *
 * @author jojo
 */
// Cloudscape concrete DAO Factory implementation
import java.sql.*;

public class CloudscapeDAOFactory extends DAOFactory {
  public static final String DRIVER=
    "COM.cloudscape.core.RmiJdbcDriver";
  public static final String DBURL=
    "jdbc:cloudscape:rmi://localhost:1099/CoreJ2EEDB";

  // method to create Cloudscape connections
  public static Connection createConnection() {
    // Use DRIVER and DBURL to create a connection
    // Recommend connection pool implementation/usage
      return null;
  }
  public CustomerDAO getCustomerDAO() {
    // CloudscapeCustomerDAO implements CustomerDAO
    return new MysqlCustomerDAO();
  }

    @Override
    public AccountDAO getAccountDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ConsistencyDAO getConsistencyDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
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
