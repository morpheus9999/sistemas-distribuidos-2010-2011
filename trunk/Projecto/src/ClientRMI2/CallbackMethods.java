/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI2;

import Client_Server.CallbackInterface;
import java.rmi.RemoteException;
import Client_Server.Constants;
import Client_Server.ViewMatch;
import java.util.Vector;


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
        Client.getGUI().displayMessage(s);
    }

    public void printMessage(String from, String message) throws java.rmi.RemoteException {
        Client.getGUI().displayMessageFrom(from,message);
        
        
        
        
    }
    
//    public void printGames(Vector<ViewMatch> games) throws java.rmi.RemoteException {
//        Client.getGUI().parseRequest(Constants.matchesCode, true, games);
//    }
    
    
}
