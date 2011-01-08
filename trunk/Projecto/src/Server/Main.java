/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import ClientRMI2.observer.ApostaFootballObserver;
import Server.DAOFactoryPattern.AccountDAO;
import Server.DAOFactoryPattern.ConsistencyDAO;
import Server.DAOFactoryPattern.CustomerDAO;
import Server.DAOFactoryPattern.DAOFactory;
import Client_Server.CallbackInterface;
import Client_Server.CallbackInterfaceTomcat;
import Client_Server.Constants;
import Client_Server.RMIInterface;
import Client_Server.Selection;
import Server.DAOFactoryPattern.MessageDAO;
import Server.IteratorPattern.Connection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author JLA
 */
public class Main {

    /*  stores info about online users  */
    public static Hashtable<String, ClientThreadTCP> onlineUsersTCP = new Hashtable<String, ClientThreadTCP>();
    public static Hashtable<String, CallbackInterface> onlineUsersRMI = new Hashtable<String, CallbackInterface>();
    public static Vector <String> onlineUsersRMITomcat =new Vector<String>();
    
    
    public static java.util.Map<String,Connection> onlineUsers = new HashMap();
    
    
    
    //falta isto 
    public static CallbackInterfaceTomcat calbackInterfaceTomcat=null;
    
    public static Selection opt = new Selection();
    //public static BetThread game;
    public static ThreadGames game;
    public static AccountDAO accountDAO;
    public static CustomerDAO customerDAO;
    public static ConsistencyDAO consistencyDAO;
    public static MessageDAO messageDAO;
    
    
    
    public static void main(String args[]) {
        int counter = 0;
        
        DAOFactory mysqlFactory =   DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        accountDAO = mysqlFactory.getAccountDAO();
        customerDAO=mysqlFactory.getCustomerDAO();
        ApostaFootballObserver m=new ApostaFootballObserver();
        consistencyDAO=mysqlFactory.getConsistencyDAO(m);
        messageDAO=mysqlFactory.getMessageDAO();
        
        

        try {
            
            /*  waits for server to be primary before continuing    */
            UDPThreadPrimary udp = new UDPThreadPrimary();
            udp.start();

            /*  it waits!   */
            Main.opt.getOption();

            System.out.println("\n\n(Primary): sou Master :D\n\n");

            /*  opens a port to check for requests  */
            game = new ThreadGames(Constants.numJogos,consistencyDAO);
            game.start();

            /*  opens RMI connections   */
            
            try {
                RMIInterface obj = new RMIMethods();
                
                LocateRegistry.createRegistry(Constants.primaryServerRMIPort).rebind(Constants.primaryServerRMIObj, obj);
                
            } catch (RemoteException ex) {
                System.out.println("RMI connection error");
            }

            /*  opens socket to listen for connections  */
            ServerSocket listener = new ServerSocket(Constants.primaryServerTCPPort);

            /*  creates a thread pool   */
            ExecutorService pool = Executors.newCachedThreadPool();
            while(true) {
                System.out.println("Waiting for connection...");

                /*  waits for a connection  */
                Socket sock = listener.accept();
                //sock.setSoTimeout(60000);
                System.out.println("Running "+(++counter)+"º conection!");

                /*  runs the thread */
                pool.submit(new ClientThreadTCP(sock));
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            System.out.println("io exception");
        }


    }
}
