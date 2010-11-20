/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import Client_Server.CallbackInterfaceTomcat;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JLA
 */
public class CallbackMethodsRMI_Servlets extends java.rmi.server.UnicastRemoteObject implements CallbackInterfaceTomcat {
    ChatServlet m=new ChatServlet();
    //falta isto do server!!!
    
    
    

    
    CallbackMethodsRMI_Servlets(ChatServlet aThis)throws java.rmi.RemoteException {
        m=aThis;
    }

    

    /**
     * printOnClient - prints a message on the client
     * @param s
     * @throws java.rmi.RemoteException
     */
    public void printOnClient(String s) throws java.rmi.RemoteException {
        System.out.println(s);
    }

    public void printMessage(String from, String message,String destination) throws java.rmi.RemoteException {
        System.out.println("MERDA print");
        
        synchronized (ChatServlet.clients) {
			try {
				HttpServletResponse resp = ChatServlet.clients.get(destination);
                try {
                    resp.getWriter().println("De:"+from+" "+message + "<br/>");
                } catch (IOException ex) {
                    Logger.getLogger(CallbackMethodsRMI_Servlets.class.getName()).log(Level.SEVERE, null, ex);
                }
		resp.getWriter().flush();
                                
			} catch (IOException ex) {
				// Trouble using the response object's writer so we remove
				// the user and response object from the hashtable
				m.removeClient( destination,null);
			}
		}

    }
}
