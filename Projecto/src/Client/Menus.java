/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import Client_Server.Login;
import Client_Server.User;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author JLA
 */
public class Menus {
    private Scanner scan;

    /*
     * NULL constructor
     */
    public Menus() {
        super();
    }

    /*
     * open Text Interface channel
     */
    public void openTIChannel() {
        scan = new Scanner(System.in);
    }

    /*
     * close Text Interface channel
     */
    public void closeTIChannel() {
        scan.close();
    }

    /*
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

    /*
     * Login menu
     */
    public Login loginMenu() {
        Boolean flag = true;
        Login log = new Login();
        StringTokenizer token;
        String temp;

        /*  get username    */
        System.out.print("Username: ");
        temp = scan.nextLine();
        log.setName(temp);
        
        /*  get password    */
        System.out.print("Password: ");
        temp = scan.nextLine();
        log.setPassword(temp);

        return log;
    }

    /*
     * Register menu (????precisara de alteracoes????)
     */
    public Login registerMenu() {
        User reg = new User();
        String temp;
        StringTokenizer token;

        while (true) {
            System.out.print("Introduce your email: ");

            try {
                temp = scan.nextLine();
                /*  check if string is a email  */
                token = new StringTokenizer(temp);
                token.nextToken("@");
                token.nextToken(".");
                token.nextToken("\n");

                /*  adds mail to the object */
                reg.setMail(temp);
                break;
            }
            catch (NoSuchElementException error) {
                System.out.println("String inserted is not a email");
            }
        }
        
        System.out.print("Introduce you username: ");
        temp = scan.nextLine();
        reg.setName(temp);

        System.out.print("Introduce your password: ");
        temp = scan.nextLine();
        reg.setPassword(temp);

        return reg;
    }

    /*
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
}
