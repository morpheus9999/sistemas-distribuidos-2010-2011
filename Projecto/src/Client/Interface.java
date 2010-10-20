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
        Main.log.setName(temp);
        Main.buffer.setAuthor(temp);
        
        /*  get password    */
        System.out.print("Password: ");
        temp = Input.readString().trim();
        Main.log.setPassword(temp);

        /*  flag 100 to Login   */
        Main.opt.setOption(Constants.loginCode);
    }

    /**
     * Register menu (????precisara de alteracoes????)
     */
    public void register() {
        String temp;
        StringTokenizer token;

        System.out.println("#### Register ####");

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
                Main.reg.setMail(temp);
                break;
            }
            catch (NoSuchElementException error) {
                System.out.println("String inserted is not a email");
            }
        }
        
        System.out.print("Introduce you username: ");
        temp = Input.readString().trim();
        Main.reg.setName(temp);

        System.out.print("Introduce your password: ");
        temp = Input.readString().trim();
        Main.reg.setPassword(temp);

        /*  flag 101 to register   */
        Main.opt.setOption(Constants.regCode);
    }

    /**
     * Main menu
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
        Main.opt.setOption(Constants.creditCode);
    }

    /**
     * Reset Credit
     * resets the credit of the user
     */
    public void resetCredit() {
        System.out.println("#### Reset ####");
        Main.opt.setOption(Constants.resetCode);
    }

    /**
     * Current Matches
     * lets the user check the matches in play
     */
    public void checkMatches() {
        System.out.println("#### Matches ####");
        Main.opt.setOption(Constants.matchesCode);
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
        Main.bet.setAposta(aposta);
        System.out.print("Quantos creditos pretende apostar: ");
        int bet =Input.readInt();
        Main.bet.setIdGame(idJogo);
        Main.bet.setBet(bet);
        /*  pedir dados antes   */

        Main.opt.setOption(Constants.betCode);
    }

    /**
     * Online Users
     * presents the user with a list of online users
     */
    public void onlineUsers() {
        System.out.println("#### Online Users ####");
        Main.opt.setOption(Constants.onlineUsersCode);
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
        Main.buffer.setAuthor(Main.log.getName());
        /*  adiciona mensagem ao buffer */
        Main.buffer.addEntry(toUser, message);

        Main.opt.setOption(Constants.messageCode);
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
        Main.bufferAll.setAuthor(Main.log.getName());
        /*  adiciona mensagem ao buffer */
        Main.bufferAll.addEntry(Integer.toString(Main.bufferAll.getHashtable().size()+1), message);
        
        Main.opt.setOption(Constants.messageAllCode);
    }

    /**
     * Logout
     * drops the connection with the server
     */
    public void logout() {
        System.out.println("#### Logout ####");
        Main.opt.setOption(Constants.logoutCode);
    }

    /**
     * Requests offline messages from the server
     */
    public void requestMessage() {
        System.out.println("#### Messages received offline  ####");
        Main.opt.setOption(Constants.requestMessage);
    }
}
