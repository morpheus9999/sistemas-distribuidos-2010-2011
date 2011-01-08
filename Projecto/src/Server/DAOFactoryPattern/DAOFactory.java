/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;

import ClientRMI2.observer.ApostaFootballObserver;

/**
 *
 * @author jojo
 */
// Abstract class DAO Factory
public abstract class DAOFactory {

  // Lista de base de dados 
  public static final int CLOUDSCAPE = 1;
  public static final int ORACLE = 2;
  public static final int SYBASE = 3;
  public static final int MYSQL = 4;

  
  public abstract CustomerDAO getCustomerDAO();
  public abstract AccountDAO getAccountDAO();
  public abstract ConsistencyDAO getConsistencyDAO(ApostaFootballObserver m);
  public abstract MessageDAO getMessageDAO();
  //public abstract OrderDAO getOrderDAO();
  

  public static DAOFactory getDAOFactory(int whichFactory) {
  
    switch (whichFactory) {
      case CLOUDSCAPE: 
          return new CloudscapeDAOFactory();
          
      case ORACLE    : 
          //return new OracleDAOFactory();      
          return null;
        case SYBASE    :
         // return new SybaseDAOFactory();
      case MYSQL: 
          return new MysqlDAOFactory();
      default           : 
          return null;
    }
  }
}
