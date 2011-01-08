/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;

import Client_Server.Message;

/**
 *
 * @author jojo
 */
public interface MessageDAO {
    
    public Message getMensagensAccount(String nome);
    public boolean setMensagensAccount(String de, String para ,String mensagem);

}
