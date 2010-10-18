/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI;

import Client_Server.RMIInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author JLA
 */
public class Main {

    public static void main(String args[]) {

        try {
            RMIInterface obj = (RMIInterface) Naming.lookup("rmi://localhost/RMIMethods");

            boolean flag = obj.login(null);

            System.out.println("flag: "+flag);

        } catch (NotBoundException ex) {
            System.out.println("Object not found");
        } catch (MalformedURLException ex) {
            System.out.println("Wrong IP/object name");
        } catch (RemoteException error) {
            System.out.println("RMI error");
        }

    }
}
