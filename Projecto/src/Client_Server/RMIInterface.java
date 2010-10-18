/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

import Client_Server.Generic;
import Client_Server.Login;

/**
 *
 * @author JLA
 */
public interface RMIInterface extends java.rmi.Remote{

    public boolean login(Generic gen) throws java.rmi.RemoteException;
    public boolean register(Generic gen) throws java.rmi.RemoteException;
    public Generic getCredit(Generic gen, Login log) throws java.rmi.RemoteException;

}
