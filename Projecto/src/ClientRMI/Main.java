/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI;

import Client_Server.Selection;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JLA
 */
public class Main {

    static Selection opt = new Selection();
    static Login lg = new Login();

    static boolean connected = false;
    static boolean exit = false;
    static boolean login = false;
    static Generic gen = new Generic();


    
    public static void main(String args[]) {
        OnlineUsers online;
        Credit cred = new Credit();
        Login log = new Login();
        Bet bet = new Bet();

        Interface screen = new Interface();

        ClientRMIThread rmi = new ClientRMIThread();
        rmi.start();

        /*  welcome screen  */
        while(!login) {
            switch(screen.welcomeMenu()) {
                case Constants.loginCode:
                    Main.gen = screen.login();
                    Main.opt.setOption(Constants.loginCode);
                break;
                case Constants.regCode:
                    Main.gen = screen.register();
                    Main.opt.setOption(Constants.regCode);
                break;
                default:
                    System.out.println("Wrong code");
                break;
            }

            Main.opt.getOption();
        }

        /*  main menu   */
        while(!exit) {
            log.setName(Main.lg.getName());
            log.setPassword(Main.lg.getPassword());

            if(connected) {
                /*  gets messages stored in the server  */
                Main.opt.setOption(Constants.requestMessage);
                
                switch(screen.mainMenu()) {
                    case Constants.creditCode:
                        /*  credit  */
                        Main.gen.setObj(new Credit());
                        Main.opt.setOption(Constants.creditCode);
                    break;
                    case Constants.resetCode:
                        /*  reset credit    */
                        Main.gen.setObj(cred);
                        Main.opt.setOption(Constants.resetCode);
                    break;
                    case Constants.matchesCode:
                        /*  view matches    */
                        Main.opt.setOption(Constants.matchesCode);
                    break;
                    case Constants.betCode:
                        /*  bet */
                        Main.gen = screen.bet();
                        Main.opt.setOption(Constants.betCode);
                    break;
                    case Constants.onlineUsersCode:
                        /*  online users    */
                        Main.opt.setOption(Constants.onlineUsersCode);
                    break;
                    case Constants.messageCode:
                        /*  send a message to a single person   */
                        Main.gen = screen.messageSingleUser(log);
                        Main.opt.setOption(Constants.messageCode);
                    break;
                    case Constants.messageAllCode:
                        /*  send a message to everyone  */
                        Main.gen = screen.messageAllUsers(log);
                        Main.opt.setOption(Constants.messageAllCode);
                    break;
                    case Constants.logoutCode:
                        /*  logout  */
                        Main.opt.setOption(Constants.logoutCode);
                        Main.exit = true;
                    break;
                    default:
                        System.out.println("Wrong code");
                    break;
                }
            } else {
                switch(screen.offlineMenu()) {
                    case Constants.messageCode:
                        /*  send a message to a single person   */
                        Main.gen = screen.messageSingleUser(log);
                        Main.opt.setOption(Constants.messageCode);
                    break;
                    case Constants.messageAllCode:
                        /*  send a message to everyone  */
                        Main.gen = screen.messageAllUsers(log);
                        Main.opt.setOption(Constants.messageAllCode);
                    break;
                    case Constants.logoutCode:
                        /*  logout  */
                        Main.opt.setOption(Constants.logoutCode);
                        Main.exit = true;
                    break;
                    default:
                        System.out.println("Wrong code");
                    break;
                }
            }
        }


        System.out.println("Bye Bye");
        System.exit(0);
    }
}
