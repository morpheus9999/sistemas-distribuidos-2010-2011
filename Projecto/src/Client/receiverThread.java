/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import Client_Server.Credit;
import Client_Server.Generic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JLA
 */
public class receiverThread extends Thread{

    /*
     * Constructor
     */
    public receiverThread() {
    }

    /*
     * Run
     * the main method of the receiver thread
     */
    public void run() {
        Generic gen;

        while(true) {
            try {
                gen = (Generic) Main.in.readObject();
                switch (gen.getCode()) {
                    case 1:
                        /*  get credit balance  */
                        Credit cred = (Credit) gen.getObj();
                        System.out.println("Credit: "+ cred.getCredit());
                        break;
                    case 2:
                        /*  resets credit balance   */
                        if (gen.getConfirmation())
                            System.out.println("Credit reset success!");
                        else
                            System.out.println("Credit reset failed!");
                        break;
                    case 3:
                        /*  lists current matches   */

                        /*  fazer print dos jogos   */


                        break;
                    case 4:
                        /*  bets on a match */
                        if(gen.getConfirmation())
                            System.out.println("Bet sucessfully made!");
                        else
                            System.out.println("Bet not successful!");
                        break;
                    case 5:
                        /*  lists online users  */

                        /*  em que formato vem? num vector? */

                        break;
                    case 6:
                        /*  messages a user */
                        if(gen.getConfirmation())
                            System.out.println("Message sent!");
                        else
                            System.out.println("Message failed!");
                        break;
                    case 7:
                        /*  messages all users  */
                        if(gen.getConfirmation())
                            System.out.println("Message sent!");
                        else
                            System.out.println("Message failed!");
                        break;
                    case 8:
                        /*  logout and exit */
                        System.out.println("Connection terminated!\nBye Bye :)");
                        return;
                    case 100:
                        /*  login   */
                        if(gen.getConfirmation()) {
                            /*  mudar a flag de logado para true    */
                            Main.logged=true;
                            System.out.println("Login successfull!");
                        }
                        else {
                            /*  manter a flag a falso   */
                            Main.logged=false;
                            System.out.println("Login failed!");
                        }
                        
                        /*  notifies main thread so it can continue */
                        synchronized(Main.class) {
                            Main.class.notify();
                        }

                        break;
                    case 101:
                        /*  register    */
                        if(gen.getConfirmation())
                            System.out.println("Register successful!");
                        else
                            System.out.println("Register failed!");
                        break;
                    default:
                        System.out.println("Code not recognized :X: "+gen.getCode());
                        break;
                }
            } catch (IOException ex) {
                System.out.println("Error receiving object: "+ ex.getMessage());
                try {
                    Thread.currentThread().sleep(1000);
                    //reconect
                    
                    this.run();
                } catch (InterruptedException ex1) {
                    Logger.getLogger(receiverThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
                

            } catch (ClassNotFoundException ex) {
                System.out.println("Class not found when receiving: "+ ex.getMessage());
            }
        }
    }
}
