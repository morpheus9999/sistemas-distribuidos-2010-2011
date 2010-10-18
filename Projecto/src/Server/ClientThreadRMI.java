/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client_Server.RMIInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author JLA
 */
public class ClientThreadRMI extends Thread {

    public ClientThreadRMI() {

    }

    public void run() {
        //  criar um objecto da implementacao das funcoes
        try {
            RMIInterface obj = new RMIMethods();
            LocateRegistry.createRegistry(1099).rebind("RMIMethods", obj);
            
        } catch (RemoteException ex) {
            System.out.println("RMI connection error");
        }
    }


}
