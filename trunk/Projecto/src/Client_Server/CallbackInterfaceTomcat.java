/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author jojo
 */
public interface CallbackInterfaceTomcat extends java.rmi.Remote{
    public void printOnClient(String s) throws java.rmi.RemoteException;
    public void printMessage(String from, String message,String destination) throws java.rmi.RemoteException;
    public void printMessageall(String from, String message) throws java.rmi.RemoteException;
    public void printMatches(Generic l)throws java.rmi.RemoteException;

    public void UpdateUsersOnline()throws java.rmi.RemoteException;
    public void UpdateMatchs()throws java.rmi.RemoteException;
    public void UpdateCredit(String nome)throws java.rmi.RemoteException;
}
