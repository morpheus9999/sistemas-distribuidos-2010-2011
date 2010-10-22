/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;


/**
 *
 * @author JLA
 */
public class Constants {
    final public static int numJogos=8;
    final public static int tries = 3;
    final public static int sleepTime = 1000;
    final public static int reconnectTime = 1000;

    final public static String  primaryServerTCP = "localhost";
    final public static int     primaryServerTCPPort = 3500;
    final public static String  backupServerTCP = "localhost";
    final public static int     backupServerTCPPort = 3600;

    final private static String primaryServerRMIURL = "localhost";
    final public static int     primaryServerRMIPort = 1099;
    final public static String  primaryServerRMIObj = "RMIMethods";
    final private static String backupServerRMIURL = "localhost";
    final public static int     backupServerRMIPort = 2000;
    final public static String  backupServerRMIObj = "RMIMethods";

    final public static String  clientPrimaryServerRMI = "rmi://"+primaryServerRMIURL+":"+primaryServerRMIPort+"/"+primaryServerRMIObj;
    final public static String  clientBackupServerRMI = "rmi://"+backupServerRMIURL+":"+backupServerRMIPort+"/"+backupServerRMIObj;
    
    final public static int creditCode = 1;
    final public static int resetCode = 2;
    final public static int matchesCode = 3;
    final public static int betCode = 4;
    final public static int onlineUsersCode = 5;
    final public static int messageCode = 6;
    final public static int messageAllCode = 7;
    final public static int logoutCode = 8;
    final public static int loginCode = 100;
    final public static int regCode = 101;
    final public static int receiveMessage = 200;
    final public static int requestMessage = 201;
    final public static int reward=3;
    final public static int resetCredito=70;

}
