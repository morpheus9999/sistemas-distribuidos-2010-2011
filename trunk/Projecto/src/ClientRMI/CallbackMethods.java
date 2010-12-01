/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI;

import Client_Server.CallbackInterface;
import java.rmi.RemoteException;

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

    public void printMessage(String from, String message) throws java.rmi.RemoteException {
        System.out.println("########    Message ##########");
        System.out.println("From: "+from);
        System.out.println("Message:");
        System.out.println("> "+message);
    }
}
