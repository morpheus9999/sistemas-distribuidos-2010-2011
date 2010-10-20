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
import java.io.IOException;
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

    public static Login lg = new Login();

    public static void main(String args[]) {
        Credit cred;
        Bet bet;
        OnlineUsers online;
        Login log;
        boolean connected = false;
        boolean login = false;
        boolean exit = false;
        Interface screen = new Interface();
        
        try {
            /*  gets stub   */
            RMIInterface obj = (RMIInterface) Naming.lookup("rmi://localhost/RMIMethods");

            /*  sets callback object    */
            CallbackMethods callback = new CallbackMethods();
            obj.setCallback((CallbackInterface)callback);

            /*  sets connected to true  */
            connected = true;

            /*  welcome screen  */
            while(!login) {
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
            }

            /*  main menu   */
            while(!exit) {
                Generic gen = new Generic();
                bet = new Bet();
                cred = new Credit();
                log = new Login();
                
                log.setName(Main.lg.getName());
                log.setPassword(Main.lg.getPassword());

                if(connected) {
                    switch(screen.mainMenu()) {
                        case Constants.creditCode:
                            /*  credit  */
                            gen.setObj(cred);
                            gen = obj.getCredit(gen, log);
                            cred = (Credit) gen.getObj();

                            System.out.println("Your credit is: "+cred.getCredit());
                        break;
                        case Constants.resetCode:
                            /*  reset credit    */
                            gen.setObj(cred);
                            gen = obj.resetCredit(gen, log);
                            cred = (Credit) gen.getObj();

                            System.out.println("Your credit (reseted) is: "+cred.getCredit());
                        break;
                        case Constants.matchesCode:
                            /*  view matches    */
                            gen = obj.viewMathces(gen);
                            Vector <ViewMatch> matches= (Vector<ViewMatch>) gen.getObj();
                            System.out.println("MATCHES:");
                            for(int x=0;x<matches.size();x++){
                                System.out.println("["+matches.elementAt(x).getIdJogo()+"]"+ matches.elementAt(x).getHome()+" VS "+matches.elementAt(x).getFora());
                            }
                        break;
                        case Constants.betCode:
                            /*  bet */
                            gen = screen.bet();
                            gen = obj.bet(gen, log);

                            if(gen.getConfirmation())
                                System.out.println("Bet placed");
                            else
                                System.out.println("Bet failed");

                        break;
                        case Constants.onlineUsersCode:
                            /*  online users    */
                            gen = obj.onlineUsers(gen);
                            online = (OnlineUsers)gen.getObj();
                            online.printOnlineUsers();
                        break;
                        case Constants.messageCode:
                            /*  send a message to a single person   */
                            gen = screen.messageSingleUser(log);
                            if(obj.messageUser(gen)) {
                                System.out.println("Message sent");
                                Interface.buffer.clearHashtable();
                            }
                            else
                                System.out.println("Message failed");
                        break;
                        case Constants.messageAllCode:
                            /*  send a message to everyone  */
                            gen = screen.messageAllUsers(log);
                            if(obj.messageAll(gen)) {
                                System.out.println("Message sent");
                                Interface.bufferAll.clearHashtable();
                            }
                            else
                                System.out.println("Message failed");
                        break;
                        case Constants.logoutCode:
                            /*  logout  */
                            obj.logout(log);
                            exit = true;
                        break;
                        default:
                            System.out.println("Wrong code");
                        break;
                    }
                } else {
                    switch(screen.offlineMenu()) {
                        case Constants.messageCode:
                            /*  send a message to a single person   */
                            gen = screen.messageSingleUser(log);
                            if(obj.messageUser(gen)) {
                                System.out.println("Message sent");
                                Interface.buffer.clearHashtable();
                            }
                            else
                                System.out.println("Message failed");
                        break;
                        case Constants.messageAllCode:
                            /*  send a message to everyone  */
                            gen = screen.messageAllUsers(log);
                            if(obj.messageAll(gen)) {
                                System.out.println("Message sent");
                                Interface.buffer.clearHashtable();
                            }
                            else {
                                System.out.println("Message failed");
                                Interface.bufferAll.clearHashtable();
                            }
                        break;
                        case Constants.logoutCode:
                            /*  logout  */
                            obj.logout(log);
                            exit = true;
                        break;
                        default:
                            System.out.println("Wrong code");
                        break;
                    }
                }
            }

        } catch (NotBoundException ex) {
            System.out.println("Object not found");
        } catch (MalformedURLException ex) {
            System.out.println("Wrong IP/object name");
        } catch (RemoteException error) {
            System.out.println("RMI error: "+error.getMessage());
            connected = false;
        } catch (IOException error) {
            System.out.println("Error sending message through TCP");
        }


        System.out.println("Bye Bye");
        System.exit(0);
    }
}
