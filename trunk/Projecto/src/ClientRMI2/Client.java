
package ClientRMI2;

//import tools.Monitor;

import ClientRMI2.StatePattern.*;


import Client_Server.Bet;
import Client_Server.Constants;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.RMIInterface;
import Client_Server.Selection;
import Client_Server.User;




public class Client{
    private static int my_state;
    private static ClientState      c_state;
    private static ClientOnline     c_online;
    private static ClientOffline    c_offline;
    
    private static int iD;
    
    private static String username;
    private static String IP;
    
    private static GUI uI;
    private static GUIFactory uIFactory;
    static boolean connected = false;
    static boolean exit = false;
    public static Generic gen = new Generic();
    static RMIInterface obj;
    static boolean login = false;
    public static Selection opt= new Selection();;
    static Login lg = new Login();
    //private static Monitor[] monitors;
    private static final int MONITOR_ME       = 0;
    private static final int MONITOR_STATE    = 1;
    private static final int MONITOR_ID       = 2;
    private static final int MONITOR_THREAD   = 3;
    public static Login log =null;
    
    public static void main(String args[]) {
        //new Client();
        log = new Login();
        ClientRMIThread rmi = new ClientRMIThread();
        rmi.start();
        iD = 0;
        username = "User";

        
        c_online = new ClientOnline();
        c_offline = new ClientOffline();
        
        c_state = c_offline;
        
        my_state = Constants.STATE_OFFLINE;
//        monitors = new Monitor[4];
//        for(int i=0; i<monitors.length; i++)
//            monitors[i] = new Monitor();
//        
        //uIFactory = new SimpleGUIFactory();
        uIFactory = new TextGUIFactory();
        
        uI = uIFactory.createGUI();
        uI.launch();
        
        
//        try {
//            synchronized(monitors[MONITOR_ME]){
//                monitors[MONITOR_ME].wait();
//            }
//        } catch (InterruptedException ex) {
//            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    public static String getIP(){
        return Client.IP;
    }
    
    public static GUI getGUI(){
        return Client.uI;
    }
    
//    public static short getClientState(){
//        synchronized(monitors[MONITOR_STATE]){
//            return my_state;
//        }
//    }
//    
//    public static int getNewID(){
//        synchronized(monitors[MONITOR_ID]){
//            return ++iD;
//        }
//    }
    
    public static void setUsername(String username){
        synchronized(username){
            Client.username = username;
        }
    }
    
    public static String getUsername(){
        synchronized(username){
            return new String(username);
        }
    }
    
    public static void setState(int nState){
        //synchronized(monitors[MONITOR_STATE]){
            switch( nState ){
                //case Define.STATE_OFFLINE: c_state = c_offline; break;
                default:
                     c_state = c_online;
            }
            
            my_state = nState;
        //}
    }
    
//    public static Buffer getQueue(){
//        return queue;
//    }
//    
//    public static void setUpThreads(){
//        synchronized(monitors[MONITOR_THREAD]){
//            try {
//                sender = new Sender(queue);
//            } catch (SingletonException se) {
//                //System.out.println("sender is up already");
//            }
//
//            try {
//                receiver = new Receiver(queue);
//            } catch (SingletonException se) {
//                //System.out.println("receiver is up already");
//            }
//        }
//    }
    
    public static void constructUserMessageUser(Message m) 
            throws ForbiddenActionException{
        c_state.constructUserMessage(m);
    }
    
    public static void constructLoginMessage(Login m) 
            throws ForbiddenActionException{
        c_state.constructLoginMessage(m);
    }
    
    public static void constructRegisterMessage(User m) 
            throws ForbiddenActionException{
        c_state.constructRegisterMessage(m);
    }
    
    public static void constructCredit(Login m) 
            throws ForbiddenActionException{
        c_state.constructCreditMessage(m);
    }

    static void constructUserMessageAll(Message messageAllUsers) 
            throws ForbiddenActionException {
        c_state.constructUserMessageAll(messageAllUsers);
    }

    static void constructResetCredit(Login log) 
            throws ForbiddenActionException{
        c_state.constructResetCredit(log);
        }

    static void constructViewMatches() 
            throws ForbiddenActionException{
        c_state.constructViewMatches();
        
            }

    static void constructBet(Bet bet) 
            throws ForbiddenActionException{
        c_state.constructBet(bet);
        
    }
    
    
    
}
