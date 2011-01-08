/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;


import Client_Server.Input;
import Client_Server.Constants;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author JLA
 */
public class Interface {
    private Scanner scan;

    /**
     * NULL constructor
     */
    public Interface() {
        super();
    }

    /**
     * open Text Interface channel
     */
    public void openTIChannel() {
        scan = new Scanner(System.in);
    }

    /**
     * close Text Interface channel
     */
    public void closeTIChannel() {
        scan.close();
    }

    /**
     * Welcome menu
     */
    public int welcomeMenu() {
        int opt = -1;
        int passagens = 0;

        do {
            /*  checks if it's not the first time asking for input  */
            if(++passagens > 1)
                System.out.println("Wrong input");

            System.out.println("#### Welcome ####");
            System.out.println("1 - Login");
            System.out.println("2 - Register");

            System.out.print("Option: ");
            opt = Input.readInt();
            

        } while (opt < 1 || opt > 2);

        if (opt == 1)
            opt = Constants.loginCode;
        else
            opt = Constants.regCode;

        return opt;
    }

    /**
     * Login menu
     */
    public void login() {
        
        String temp;
        
        System.out.println("#### Login ####");
        
        /*  get username    */
        System.out.print("Username: ");
        temp = Input.readString().trim();
        Client.log.setName(temp);
        Client.buffer.setAuthor(temp);
        
        /*  get password    */
        System.out.print("Password: ");
        temp = Input.readString().trim();
        Client.log.setPassword(temp);

        /*  flag 100 to Login   */
        Client.opt.setOption(Constants.loginCode);
    }

    /**
     * Register menu (????precisara de alteracoes????)
     */
    public void register() {
        String temp;
        StringTokenizer token;

        System.out.println("#### Register ####");
        int k=0;
        while (true) {
            System.out.print("Introduce your email: ");

            try {
                temp = Input.readString().trim();
                /*  check if string is a email  */
                token = new StringTokenizer(temp);
                token.nextToken("@");
                token.nextToken(".");
                token.nextToken("\n");

                /*  adds mail to the object */
                Client.reg.setMail(temp);
                break;
            }
            catch (NoSuchElementException error) {
                System.out.println("String inserted is not a email");
            }
        }
        k=0;
        while (true) {

            try {
                if(k==0)
                    System.out.print("Introduce your username: ");
                else
                    System.out.print("Introduce a valid username plz :P : ");

                temp = Input.readString().trim();
                Client.reg.setName(temp);
                k++;
                if (temp.length() > 0) {
                    break;
                }
            } catch (NoSuchElementException error) {
                System.out.println("String inserted is not correct");
            }
        }
        k=0;
        while (true) {

            try {
                if(k==0)
                    System.out.print("Introduce your password: ");
                //else
                    ///System.out.print("Introduce a valid password plz :P : ");

                k++;
                temp = Input.readString().trim();
                Client.reg.setPassword(temp);

                if (temp.length() > 0) {
                    break;
                }
            } catch (NoSuchElementException error) {
                System.out.println("String inserted is not correct");
            }
        }
        /*  flag 101 to register   */
        Client.opt.setOption(Constants.regCode);
    }

    /**
     * Client menu
     */
    public int mainMenu() {
        int opt = -1;

        do {
            System.out.println("#### Main Menu ####");
            System.out.println("1 - Credit");
            System.out.println("2 - Reset Credit");
            System.out.println("3 - Current Matches");
            System.out.println("4 - Bet");
            System.out.println("5 - Online Users");
            System.out.println("6 - Message User");
            System.out.println("7 - Message All");
            System.out.println("8 - Logout");

            System.out.print("Option: ");
            opt = Input.readInt();
        } while (opt < 1 || opt > 8);

        return opt;
    }

    /**
     * Offline Menu
     */
    public int offlineMenu() {
        int opt = -1;

        do {
            System.out.println("#### Offline Menu ####");
            System.out.println("6 - Message User");
            System.out.println("7 - Message All");
            System.out.println("8 - Logout");

            System.out.print("Option: ");
            opt = Input.readInt();
        } while (opt < 6 || opt > 8);

        return opt;
    }

    /**
     * Credit
     * asks for the actual user credit
     */
    public void credit() {
        System.out.println("#### Credit ####");
        Client.opt.setOption(Constants.creditCode);
    }

    /**
     * Reset Credit
     * resets the credit of the user
     */
    public void resetCredit() {
        System.out.println("#### Reset ####");
        Client.opt.setOption(Constants.resetCode);
    }

    /**
     * Current Matches
     * lets the user check the matches in play
     */
    public void checkMatches() {
        System.out.println("#### Matches ####");
        Client.opt.setOption(Constants.matchesCode);
    }

    /**
     * Bet
     * lets the user bet on any match
     */
    public void bet() {
        System.out.println("#### Bet ####");
        System.out.print("ID Jogo: ");
        int idJogo = Input.readInt();
        System.out.print("Victoria Casa (1) Victoria fora (2) Empate (0): ");
        int aposta =Input.readInt();
        Client.bet.setAposta(aposta);
        int bet=-1;
        while(bet<0){
        System.out.print("Quantos creditos pretende apostar: ");
            bet =Input.readInt();
        }
        Client.bet.setIdGame(idJogo);
        Client.bet.setBet(bet);
        /*  pedir dados antes   */

        Client.opt.setOption(Constants.betCode);
    }

    /**
     * Online Users
     * presents the user with a list of online users
     */
    public void onlineUsers() {
        System.out.println("#### Online Users ####");
        Client.opt.setOption(Constants.onlineUsersCode);
    }

    /**
     * Message User
     * sends a message to a specific user
     */
    public void messageSingleUser() {
        String message = null;
        String toUser = null;

        System.out.println("#### Message to user ####");
        System.out.print("To: ");
        toUser = Input.readString();
        System.out.println("Message: ");
        System.out.print("> ");
        message = Input.readString();

        /*  define autor da mensagem    */
        Client.buffer.setAuthor(Client.log.getName());
        /*  adiciona mensagem ao buffer */
        Client.buffer.addEntry(toUser, message);

        Client.opt.setOption(Constants.messageCode);
    }

    /**
     * Message All Users
     * sends a message to all online users
     */
    public void messageAllUsers() {
        String message;
        System.out.println("#### Message to all users ####");
        System.out.println("Message: ");
        System.out.print("> ");
        message = Input.readString();

        /*  define autor da mensagem    */
        Client.bufferAll.setAuthor(Client.log.getName());
        /*  adiciona mensagem ao buffer */
        Client.bufferAll.addEntry(Integer.toString(Client.bufferAll.getHashtable().size()+1), message);
        
        Client.opt.setOption(Constants.messageAllCode);
    }

    /**
     * Logout
     * drops the connection with the server
     */
    public void logout() {
        System.out.println("#### Logout ####");
        Client.opt.setOption(Constants.logoutCode);
    }

    /**
     * Requests offline messages from the server
     */
    public void requestMessage() {
        
        Client.opt.setOption(Constants.requestMessage);
    }
}
