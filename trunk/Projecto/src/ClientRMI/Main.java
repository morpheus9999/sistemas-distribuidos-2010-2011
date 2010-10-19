/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI;

import Client_Server.Bet;
import Client_Server.CallbackInterface;
import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import Client_Server.Input;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.OnlineUsers;
import Client_Server.RMIInterface;
import Client_Server.ViewMatch;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 *
 * @author JLA
 */
public class Main {

    public static Login lg;

    public static void main(String args[]) {
        Generic gen;
        Credit cred;
        Bet bet;
        OnlineUsers online;
        boolean connected = false;
        boolean login = false;
        boolean exit = false;
        Interface screen = new Interface();
        Message buffer = new Message();
        Message bufferAll = new Message();
        
        try {
            /*  gets stub   */
            RMIInterface obj = (RMIInterface) Naming.lookup("rmi://localhost/RMIMethods");

            /*  sets callback object    */
            CallbackMethods callback = new CallbackMethods();
            obj.setCallback((CallbackInterface)callback);

            /*  sets connected to true  */
            connected = true;

            while(!login)
                /*  welcome screen  */
                switch(screen.welcomeMenu()) {
                    case Constants.loginCode:
                        if(obj.login(screen.login())) {
                            System.out.println("Login sucessfull");
                            login = true;
                        }
                        else
                            System.out.println("Login failed");
                    break;
                    case Constants.regCode:
                        if(obj.register(screen.register()))
                            System.out.println("Register sucessfull");
                        else
                            System.out.println("Register failed");
                    break;
                    default:
                        System.out.println("Wrong code");
                    break;
                }

            while(!exit)
                if(connected)
                    /*  main menu   */
                    switch(screen.mainMenu()) {
                        case Constants.creditCode:
                            /*  credit  */
                            gen = obj.getCredit(new Generic(), Main.lg);
                            cred = (Credit) gen.getObj();

                            System.out.println("Your credit is: "+cred.getCredit());
                        break;
                        case Constants.resetCode:
                            /*  reset credit    */
                            gen = obj.getCredit(new Generic(), Main.lg);
                            cred = (Credit) gen.getObj();

                            System.out.println("Your credit (reseted) is: "+cred.getCredit());
                        break;
                        case Constants.matchesCode:
                            /*  view matches    */
                            gen = obj.viewMathces(new Generic());
                            Vector <ViewMatch> matches= (Vector<ViewMatch>) gen.getObj();
                            System.out.println("MATCHES:");
                            for(int x=0;x<matches.size();x++){
                                System.out.println("["+matches.elementAt(x).getIdJogo()+"]"+ matches.elementAt(x).getHome()+" VS "+matches.elementAt(x).getFora());
                            }
                        break;
                        case Constants.betCode:
                            /*  bet */
                            gen = screen.bet();
                            gen = obj.bet(gen, lg);

                            if(gen.getConfirmation())
                                System.out.println("Bet placed");
                            else
                                System.out.println("Bet failed");

                        break;
                        case Constants.onlineUsersCode:
                            /*  online users    */
                            gen = obj.onlineUsers(new Generic());
                            online = (OnlineUsers)gen.getObj();
                            online.printOnlineUsers();
                        break;
                        case Constants.messageCode:
                            /*  send a message to a single person   */
                            gen = screen.messageSingleUser(lg);
                            if(obj.messageUser(gen))
                                System.out.println("Message sent");
                            else
                                System.out.println("Message failed");
                        break;
                        case Constants.messageAllCode:
                            /*  send a message to everyone  */
                            gen = screen.messageAllUsers(lg);
                            if(obj.messageAll(gen))
                                System.out.println("Message sent");
                            else
                                System.out.println("Message failed");
                        break;
                        case Constants.logoutCode:
                            /*  logout  */
                            System.out.println("Bye Bye");
                            exit = true;
                        break;
                        default:
                            System.out.println("Wrong code");
                        break;
                    }
                else
                    switch(screen.offlineMenu()) {
                        case Constants.messageCode:
                            /*  send a message to a single person   */
                            gen = screen.messageSingleUser(lg);
                            if(obj.messageUser(gen))
                                System.out.println("Message sent");
                            else
                                System.out.println("Message failed");
                        break;
                        case Constants.messageAllCode:
                            /*  send a message to everyone  */
                            gen = screen.messageAllUsers(lg);
                            if(obj.messageAll(gen))
                                System.out.println("Message sent");
                            else
                                System.out.println("Message failed");
                        break;
                        case Constants.logoutCode:
                            /*  logout  */
                            System.out.println("Bye Bye");
                            exit = true;
                        break;
                        default:
                            System.out.println("Wrong code");
                        break;
                    }


        } catch (NotBoundException ex) {
            System.out.println("Object not found");
        } catch (MalformedURLException ex) {
            System.out.println("Wrong IP/object name");
        } catch (RemoteException error) {
            System.out.println("RMI error: "+error.getMessage());
            connected = false;
        }

    }
}
