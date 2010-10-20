/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

/**
 *
 * @author JLA
 */
public interface CallbackInterface extends java.rmi.Remote{
    public void printOnClient(String s) throws java.rmi.RemoteException;
    public void printMessage(String from, String message) throws java.rmi.RemoteException;
}
