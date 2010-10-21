package exemplo.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.ServerRMIInterface;

public class MainTwitterRMI implements Serializable {

    private TwitterClientRMI client;
    private String host;
    private boolean stop;
    private int porto_primario;
    private int porto_secundario;
    private ServerRMIInterface serverInterface;
    private int timeLimit = 1000;
    private int TENTATIVAS = 6000 / timeLimit;

    public MainTwitterRMI() {
        this.host = "localhost";


    }

    public MainTwitterRMI(int porto_primario, int porto_secundario) {
        this.porto_primario = porto_primario;
        this.porto_secundario = porto_secundario;
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

    public boolean login(String servidor, String login, String password) throws RemoteException {
        if(serverInterface!=null){
            this.client = new TwitterClientRMI(serverInterface, login, password);
            client.setMaintwitter(this);
            client.sendLogin();
        }else
            System.err.println("Servidores em baixo...Desligue e volta a ligar a aplicação!!!");
        return true;
    }

    public boolean registration(String servidor, String firstname, String lastname, String login, String email, String password) throws RemoteException {
        if(serverInterface!=null){
            this.client = new TwitterClientRMI(serverInterface, firstname, lastname, login, email, password);
            client.setMaintwitter(this);
            client.sendRegisto();
        }else
            System.err.println("Servidores em baixo...Desligue e volta a ligar a aplicação!!!");
        return true;
    }

    public boolean connect() {
        boolean ret = false, retry = false;
        int tentativas = 0, numRepeticao = 0;
        int porto = this.porto_primario;
        while (numRepeticao < 2) {
            
            do {
                
                try {
                    System.getProperties().put("java.security.policy", "policy.all");
                    System.setSecurityManager(new RMISecurityManager());
                    Registry registry = LocateRegistry.getRegistry(porto);
                    serverInterface = (ServerRMIInterface) registry.lookup("TWITTER");
                    System.out.println("estou aqui");
                    retry = false;
                    ret = true;
                    numRepeticao=2;
                } catch (Exception e) {
                    if (tentativas < this.TENTATIVAS) {
                        System.err.println("LIGACAO EM BAIXO...A TENTAR LIGAR AO NAMING SERVER NO PORTO " + porto + " ( " + getTENTATIVAS() + " SEG )");
                        retry = true;
                        tentativas++;
                    } else {
                        retry = false;
                        if (tentativas == this.TENTATIVAS && numRepeticao < 2) {
                            tentativas = 0;
                            numRepeticao++;
                            porto = this.porto_secundario;
                            //System.err.println("Ligacao perdida....");
                            //this.flag_NamingService_down = true;
                            //System.exit(0);
                        }
                    }
                    try {
                        Thread.sleep(getTimeLimit());
                    } catch (InterruptedException i) {
                    }
                }
            } while (retry);
//            if(numRepeticao==2)
//                System.err.println("Ligacao perdida....");
        }
        return ret;
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

    public TwitterClientRMI getClient() {
        return client;
    }

    public void setClient(TwitterClientRMI client) {
        this.client = client;
    }

    public int getPorto_primario() {
        return porto_primario;
    }

    public void setPorto_primario(int porto_primario) {
        this.porto_primario = porto_primario;
    }

    public int getPorto_secundario() {
        return porto_secundario;
    }

    public void setPorto_secundario(int porto_secundario) {
        this.porto_secundario = porto_secundario;
    }

    public int getTENTATIVAS() {
        return TENTATIVAS;
    }

    public void setTENTATIVAS(int TENTATIVAS) {
        this.TENTATIVAS = TENTATIVAS;
    }

    public ServerRMIInterface getServerInterface() {
        return serverInterface;
    }

    public void setServerInterface(ServerRMIInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public static void main(String args[]) {
        int porto_primario = 0, porto_secundario = 0;

        if (args.length != 2) {
            porto_primario = 6000;
            porto_secundario = 6005;

        } else if (args.length == 2) {
            porto_primario = Integer.parseInt(args[0]);
            porto_secundario = Integer.parseInt(args[1]);
        }
        MainTwitterRMI m = new MainTwitterRMI(porto_primario, porto_secundario);
        m.connect();
        do{
            m.choose();
        }while(m.getServerInterface()==null);
    }
}
