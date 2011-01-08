/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI2.observer;

import Client_Server.CallbackInterface;
import Client_Server.Message;
import java.rmi.RemoteException;

import Server.*;
import Server.IteratorPattern.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author jojo
 */
public class ApostaFootballObserver implements Observer {

    public ApostaFootballObserver() {
    }
    
    


    public void update(Observable o, Object arg) {
        
        System.out.println("IUUUUUPIIII!!!!! :P");
        if (arg instanceof Message) {
            Message enviar = (Message) arg;
            String para = enviar.getAuthor();

            Object envia = Main.onlineUsers.get(para);

            if (envia instanceof TCP) {
                ((ClientThreadTCP)((TCP)envia).getCallbackObject()).messageUser("", enviar.getAuthor(), enviar.getText());
            } else if (envia instanceof RMI) {
                System.out.println("ENTRA");
                
                
                CallbackInterface callback = (CallbackInterface) ((RMI) envia).getCallbackObject();

                try {
                    callback.printMessage("", enviar.getText());
                } catch (Exception error) {
                    /*  if it throws an error, delete it    */
                    Main.onlineUsers.remove(enviar.getAuthor());
                    Main.accountDAO.setMensagensAccount("", enviar.getAuthor(), enviar.getText());
                }
//                try {
//                    RMIMethods.messageUser("", enviar.getAuthor(), enviar.getText());
//                } catch (RemoteException ex) {
//                    System.out.println("ERRO");
//                }
            }else if(envia instanceof RMITomcat){
                try {
                    RMIMethods.messageUser("", enviar.getAuthor(), enviar.getText());
                } catch (RemoteException ex) {
                    Logger.getLogger(ApostaFootballObserver.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
            }



        }


    }
    
}

