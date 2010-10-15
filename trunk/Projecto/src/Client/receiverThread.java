/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import Client_Server.Login;
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

        try {
            while(true) {
                try {
                    gen = (Generic) Main.in.readObject();
                    switch (gen.getCode()) {
                        case Constants.creditCode:
                            /*  get credit balance  */
                            Credit cred = (Credit) gen.getObj();
                            System.out.println("Credit: "+ cred.getCredit());
                            break;
                        case Constants.resetCode:
                            /*  resets credit balance   */
                            if (gen.getConfirmation())
                                System.out.println("Credit reset success!");
                            else
                                System.out.println("Credit reset failed!");
                            break;
                        case Constants.matchesCode:
                            /*  lists current matches   */

                            /*  fazer print dos jogos   */


                            break;
                        case Constants.betCode:
                            /*  bets on a match */
                            if(gen.getConfirmation())
                                System.out.println("Bet sucessfully made!");
                            else
                                System.out.println("Bet not successful!");
                            break;
                        case Constants.onlineUsersCode:
                            /*  lists online users  */

                            /*  em que formato vem? num vector? */

                            break;
                        case Constants.messageCode:
                            /*  messages a user */
                            if(gen.getConfirmation())
                                System.out.println("Message sent!");
                            else
                                System.out.println("Message failed!");
                            break;
                        case Constants.messageAllCode:
                            /*  messages all users  */
                            if(gen.getConfirmation())
                                System.out.println("Message sent!");
                            else
                                System.out.println("Message failed!");
                            break;
                        case Constants.logoutCode:
                            /*  logout and exit */
                            System.out.println("Connection terminated!");
                            return;
                        case Constants.loginCode:
                            /*  login   */
                            if(gen.getConfirmation()) {
                                /*  mudar a flag de logado para true    */
                                Main.logged = true;
                                System.out.println("Login successfull!");
                            }
                            else {
                                /*  manter a flag a falso   */
                                Main.logged = false;
                                System.out.println("Login failed!");
                            }

                            /*  notifies main thread so it can continue */
                            synchronized(Main.class) {
                                Main.class.notify();
                            }

                            break;
                        case Constants.regCode:
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
                } catch (ClassNotFoundException ex) {
                    System.out.println("Class not found when receiving: "+ ex.getMessage());
                }
            }
        } catch (IOException ex) {
            //  System.out.println("Error receiving object: "+ ex.getMessage());
            //  System.out.println("Ending receiving thread");
        }
    }
}
