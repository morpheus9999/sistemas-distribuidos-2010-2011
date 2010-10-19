/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI;

import Client_Server.CallbackInterface;

/**
 *
 * @author JLA
 */
public class CallbackMethods extends java.rmi.server.UnicastRemoteObject implements CallbackInterface {

    public CallbackMethods() throws java.rmi.RemoteException {
        
    }

    /**
     * printOnClient - prints a message on the client
     * @param s
     * @throws java.rmi.RemoteException
     */
    public void printOnClient(String s) throws java.rmi.RemoteException {
        System.out.println(s);
    }
}
