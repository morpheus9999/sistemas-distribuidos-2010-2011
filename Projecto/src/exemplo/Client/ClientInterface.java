/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package exemplo.Client;

import common.Mensagem;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Helio Evora
 */
public interface ClientInterface extends Remote{

    public void broadcast(Mensagem msg)throws RemoteException;
}
