/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;


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
            opt = scan.nextInt();
        } while (opt < 1 || opt > 2);

        return opt;
    }

    /**
     * Login menu
     */
    public void login() {
        Boolean flag = true;
        StringTokenizer token;
        String temp;

        /*  get username    */
        System.out.print("Username: ");
        temp = scan.nextLine().trim();
        Main.log.setName(temp);
        
        /*  get password    */
        System.out.print("Password: ");
        temp = scan.nextLine().trim();
        Main.log.setPassword(temp);

        /*  flag 100 to Login   */
        Main.opt.setOption(100);
    }

    /**
     * Register menu (????precisara de alteracoes????)
     */
    public void register() {
        String temp;
        StringTokenizer token;

        while (true) {
            System.out.print("Introduce your email: ");

            try {
                temp = scan.nextLine().trim();
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
        temp = scan.nextLine().trim();
        Main.reg.setName(temp);

        System.out.print("Introduce your password: ");
        temp = scan.nextLine().trim();
        Main.reg.setPassword(temp);

        /*  flag 101 to register   */
        Main.opt.setOption(101);
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
            opt = scan.nextInt();
        } while (opt < 1 || opt > 8);

        return opt;
    }

    /**
     * Credit
     * asks for the actual user credit
     */
    public void credit() {
        Main.opt.setOption(1);
    }

    /**
     * Reset Credit
     * resets the credit of the user
     */
    public void resetCredit() {
        Main.opt.setOption(2);
    }

    /**
     * Current Matches
     * lets the user check the matches in play
     */
    public void checkMatches() {
        Main.opt.setOption(3);
    }

    /**
     * Bet
     * lets the user bet on any match
     */
    public void bet() {


        /*  pedir dados antes   */

        Main.opt.setOption(4);
    }

    /**
     * Online Users
     * presents the user with a list of online users
     */
    public void onlineUsers() {
        Main.opt.setOption(5);
    }

    /**
     * Message User
     * sends a message to a specific user
     */
    public void messageSingleUsers() {
        Main.opt.setOption(6);
    }

    /**
     * Message All Users
     * sends a message to all online users
     */
    public void messageAllUsers() {
        Main.opt.setOption(7);
    }

    /**
     * Logout
     * drops the connection with the server
     */
    public void logout() {
        Main.opt.setOption(8);
    }
}
