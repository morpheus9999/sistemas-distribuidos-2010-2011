/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client_Server.Generic;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author jojo
 */
public interface AccountDAO {
    
    
  boolean insertAccount(Generic generic);
  
  boolean loginAccount(Generic generic);

  Vector<String> getUsernameListAccount();

}
