/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;

import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author jojo
 */
public interface AccountDAO {
    
    
  boolean insertAccount(Generic generic);
  
  boolean loginAccount(Generic generic);
  
  Generic getCreditAccount(Generic gen, Login lg);

  Vector<String> getUsernameListAccount();
  
  public Generic resetCreditAccount(Generic temp, Login lg) ;
  
  public Message getMensagensAccount(String nome) ;
  
  public boolean setMensagensAccount(String de, String para ,String mensagem);
  

}
