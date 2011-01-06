/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientRMI2;

import ClientRMI2.StatePattern.ForbiddenActionException;
import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Message;
import Client_Server.OnlineUsers;
import Client_Server.ViewMatch;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jojo
 */
class TextGUI extends GUI {

    Interface screen;

    public TextGUI() {
        screen = new Interface();
    }

    @Override
    public void displayMessage(Message m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void treatEvent(short eventType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void launch() {


        while (!Client.login) {
            try {
                switch (screen.welcomeMenu()) {
                    case Constants.loginCode:
                        Client.constructLoginMessage(screen.login());
                        break;
                    case Constants.regCode:
                        Client.constructRegisterMessage(screen.register());
                        break;
                    default:
                        System.out.println("Wrong code");
                        break;
                }

                Client.opt.getOption();
            } catch (ForbiddenActionException ex) {
                Logger.getLogger(TextGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (Client.connected) {
            /*  gets messages stored in the server  */
            Client.opt.setOption(Constants.requestMessage);
            /*  waits for the answer    */
            Client.opt.getOption();
        }

        /*  Client menu   */
        while (!Client.exit) {

            //Client.log.setName(Client.log.getName());
            //Client.log.setPassword(Client.log.getPassword());

            if (Client.connected) {
                switch (screen.mainMenu()) {
                    case Constants.creditCode:
                        try {
                            /*  credit  */
                            Client.constructCredit(Client.log);
                        } catch (ForbiddenActionException ex) {
                            Logger.getLogger(TextGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case Constants.resetCode:
                        try {
                            /*  reset credit    */

                            Client.constructResetCredit(Client.log);
                        } catch (ForbiddenActionException ex) {
                            Logger.getLogger(TextGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        break;
                    case Constants.matchesCode:
                try {
                    /*  view matches    */
                    Client.constructViewMatches();
                } catch (ForbiddenActionException ex) {
                    Logger.getLogger(TextGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                        
                        break;
                    case Constants.betCode:
                try {
                    /*  bet */
                    
                    Client.constructBet(screen.bet());
                } catch (ForbiddenActionException ex) {
                    Logger.getLogger(TextGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                         
                        
                        break;
                    case Constants.onlineUsersCode:
                        /*  online users    */
                        Client.opt.setOption(Constants.onlineUsersCode);
                        break;
                    case Constants.messageCode:
                        try {
                            /*  send a message to a single person   */
                            Client.constructUserMessageUser(screen.messageSingleUser(Client.log));
                        } catch (ForbiddenActionException ex) {
                            Logger.getLogger(TextGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        break;
                    case Constants.messageAllCode:
                        try {
                            /*  send a message to everyone  */
                            Client.constructUserMessageAll(screen.messageAllUsers(Client.log));
                        } catch (ForbiddenActionException ex) {
                            Logger.getLogger(TextGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case Constants.logoutCode:
                        /*  logout  */
                        Client.opt.setOption(Constants.logoutCode);
                        Client.exit = true;
                        break;
                    default:
                        System.out.println("Wrong code");
                        break;
                }
            } else {
                switch (screen.offlineMenu()) {
                    case Constants.messageCode:

                        try {
                            /*  send a message to a single person   */
                            Client.constructUserMessageUser(screen.messageSingleUser(Client.log));
                        } catch (ForbiddenActionException ex) {
                            Logger.getLogger(TextGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case Constants.messageAllCode:
                        /*  send a message to everyone  */
                        try {
                            /*  send a message to everyone  */
                            Client.constructUserMessageAll(screen.messageAllUsers(Client.log));
                        } catch (ForbiddenActionException ex) {
                            Logger.getLogger(TextGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case Constants.logoutCode:
                        /*  logout  */
                        Client.opt.setOption(Constants.logoutCode);
                        Client.exit = true;
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

    @Override
    public void displayMessage(String string) {
        System.out.println(string);
    }

    @Override
    public void displayMessageString(String string) {
        System.out.println("ENTRA");
        System.out.println(string);
    }

    public void displaygames(Vector<ViewMatch> matches) {

        System.out.println("MATCHES:");
        for (int x = 0; x < matches.size(); x++) {
            System.out.println("[" + matches.elementAt(x).getIdJogo() + "]" + matches.elementAt(x).getHome() + " VS " + matches.elementAt(x).getFora());
        }
    }

    @Override
    public void parseRequest(int tip, boolean stat, Object mensage) {
        switch (tip) {

            case Constants.loginCode:

                System.out.println((String) mensage);
                if (stat) {
                    Client.setState(0);
                }
                break;
            case Constants.regCode:
                System.out.println((String) mensage);
                break;


            case Constants.creditCode:
                System.out.print((String) mensage);
                break;


            case Constants.matchesCode:
                displaygames((Vector<ViewMatch>) mensage);
                break;

            case Constants.betCode:
                System.out.print((String) mensage);
                break;
            case Constants.onlineUsersCode:
                ((OnlineUsers) mensage).printOnlineUsers();
                break;

        }
    }

    @Override
    public void displayMessageFrom(String from, String message) {
        System.out.println("########    Message ##########");
        System.out.println("From: " + from);
        System.out.println("Message:");
        System.out.println("> " + message);

    }
}
