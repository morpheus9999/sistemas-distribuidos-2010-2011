/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;

import Client_Server.Generic;
import Client_Server.Login;
import java.util.Collection;
import java.util.Vector;
import javax.sql.RowSet;

/**
 *
 * @author jojo
 */
// Interface that all CustomerDAOs must support
public interface CustomerDAO {
  public Generic viewMatchesCustomer(Generic temp, int ronda);
  public boolean newBetCustomer(Generic generic, Login lg,int ronda);
  
  
  
}
