/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.util.Collection;
import java.util.Vector;
import javax.sql.RowSet;

/**
 *
 * @author jojo
 */
// Interface that all CustomerDAOs must support
public interface CustomerDAO {
  public int insertCustomer();
  public boolean deleteCustomer();
  public Vector findCustomer();
  public boolean updateCustomer();
  public RowSet selectCustomersRS();
  public Collection selectCustomersTO();
  
}
