package exemplo.Client;

import Client.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class MainTwitter implements Serializable {

    private TwitterClient client;
    private String host;
    private boolean stop;

    public MainTwitter() {
        this.host = "localhost";
        stop = true;
    }

    public void choose() {
        int op = 0;
        do {
            try {
                System.out.println(showMenuLogin());
                op = User.readInt();
                if (op == 1) {
                    askForLogin();
                    break;
                } else if (op == 2) {
                    askForRegister();
                    break;
                } else if (op == 3) {
                    System.exit(0);
                    break;
                }
            } catch (IOException ex) {
            }

        } while (op != 1 || op != 2);
    }

    public void askForLogin() throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-------LOGIN---------");
        System.out.print("Insert your login> ");
        String login = stdin.readLine().trim();
        System.out.print("Insert your password> ");
        String password = stdin.readLine().trim();
        login(getHost(), login, password);
    }

    public void askForRegister() throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-------REGISTO---------");
        System.out.print("Insert your first name> ");
        String firstname = stdin.readLine().trim();
        System.out.print("Insert your last name> ");
        String lastname = stdin.readLine().trim();
        System.out.print("Insert your login> ");
        String login = stdin.readLine().trim();
        System.out.print("Insert your e-mail> ");
        String email = stdin.readLine().trim();
        System.out.print("Insert your password> ");
        String pass1 = stdin.readLine().trim();
        System.out.print("Confirm your password> ");
        String pass2 = stdin.readLine().trim();
        if (!pass1.equals(pass2)) {
            System.out.println("ERROR! The password dosn't match");
            askForRegister();
        } else {
            registration(getHost(), firstname, lastname, login, email, pass1);
        }
    }

    public synchronized boolean login(String servidor, String login, String password) {
        boolean ret = false;
        this.client = new TwitterClient(servidor, login, password);
        if (client.initThread(client.getTcp_first_port())) {
            if (client.sendLogin()) {
                client.setMaintwitter(this);
                if (!client.isRunning()) {
                    client.start();
                    ret = true;
                }
            }
        } else {
            int k = 1, porto = client.getTcp_first_port();
            do {
                //i=1;
                for (int i = 0; i < client.getTENTATIVAS(); i++) {
                    System.err.println("Trying to connect to the host:" + this.client.getHost() + " " + "on port:" + porto);
                    if (client.initThread(porto)) {
                        if (client.sendLogin()) {
                            client.setMaintwitter(this);
                            if (!client.isRunning()) {
                                client.start();
                                ret = true;
                            }
                            this.stop = false;
                            break;

                        }
                    }
                    try {
                        Thread.sleep(client.getTimeLimit());
                    } catch (InterruptedException t) {
                    }
                }

                if (!this.stop) {
                    porto = client.getTcp_second_port();
                    k++;
                } else {
                    System.out.println("!!!Nao consegiu efectuar a ligacao!!!");
                    System.exit(0);
                }

            } while (stop);
        }
        return ret;
    }

    public synchronized boolean registration(String servidor, String firstname, String lastname, String login, String email, String password) {
        boolean ret = false;
        this.client = new TwitterClient(servidor, firstname, lastname, login, email, password);


        if (client.initThread(client.getTcp_first_port())) {
            if (client.sendRegisto()) {
                client.setMaintwitter(this);
                client.start();

            }
            //clientTCP.enviaStatus(clientTCP.getMyMail(), "online");
        } else {
            int k = 1, i, porto = client.getTcp_first_port();
            do {
                i = 1;
                while (i++ <= 5) {
                    System.err.println("Trying to connect to the host:" + this.client.getHost() + " " + "on port:" + this.client.getTcp_first_port());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException t) {
                    }
                    if (client.initThread(porto)) {
                        if (client.sendLogin()) {
                            client.setMaintwitter(this);
                            client.start();

                        }
                    }
                }
                if (k < 2) {
                    porto = client.getTcp_second_port();
                    k++;
                } else {
                    System.out.println("!!!Nao consegiu efectuar a ligacao!!!");
                    System.exit(0);

                }
                //i++;


            } while (true);

        }
        return ret;
    }

    public void reconect() {
    }

    public String showMenuLogin() {
        String shMenu = ("========================\n" + "=      MENU LOGIN      =\n" + "========================\n" + "= 1 - Login            =\n" + "= 2 - Registar         =\n" + "= 3 - Exit Aplication  =\n" + "========================\n");

        return shMenu;
    }

    public static void print(String s) {
        System.out.println(s);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public TwitterClient getClient() {
        return client;
    }

    public void setClient(TwitterClient client) {
        this.client = client;
    }

    public static void main(String args[]) {
        MainTwitter m = new MainTwitter();
        m.choose();
    }
}
