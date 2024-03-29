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
import ClientRMI2.observer.ApostaFootballObserver;
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
    return new CloudscapeCustomerDAO();
  }

    @Override
    public AccountDAO getAccountDAO() {
        return new CloudscapeAccountDAO();
        
    }

    @Override
    public ConsistencyDAO getConsistencyDAO(ApostaFootballObserver m) {
        return new CloudscapeConsistencyDAO();
    }

    @Override
    public MessageDAO getMessageDAO() {
        return new CloudscapeMessageDAO();
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
