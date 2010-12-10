/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import Client_Server.CallbackInterfaceTomcat;
import Client_Server.Generic;
import Client_Server.OnlineUsers;
import Client_Server.ViewMatch;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JLA
 */
public class CallbackMethodsRMI_Servlets extends java.rmi.server.UnicastRemoteObject implements CallbackInterfaceTomcat {

    ChatServlet m = null;
    //falta isto do server!!!

    CallbackMethodsRMI_Servlets(ChatServlet aThis) throws java.rmi.RemoteException {
        m = aThis;
    }

    /**
     * printOnClient - prints a message on the client
     * @param s
     * @throws java.rmi.RemoteException
     */
    @Override
    public void printOnClient(String s) throws java.rmi.RemoteException {
        System.out.println(s);
    }

    @Override
    public void printMatches(Generic l)throws java.rmi.RemoteException {
        System.out.println("entra matches!!!");
        Vector<ViewMatch> matches = (Vector<ViewMatch>) l.getObj();

        synchronized (m.clients) {
            Set<String> clientKeySet = m.clients.keySet();
            // Let's iterate through the clients and send each one the message.
            for (String client : clientKeySet) {
                try {
                    HttpServletResponse resp = (HttpServletResponse)m.clients.get(client).elementAt(1);
                    
                        resp.getWriter().println("\n\n\n");
                        //resp2.resetBuffer();
                        //resp.resetBuffer();
                        for (int x = 0; x < matches.size(); x++) {
                            if (x == 0) {
                                resp.getWriter().println("<INPUT TYPE=\"radio\" NAME=\"jogos\" VALUE=\"" + matches.elementAt(x).getIdJogo() + "\" CHECKED>" + matches.elementAt(x).getHome() + " VS " + matches.elementAt(x).getFora() + "<br/>");
                            } else {
                                resp.getWriter().println("<INPUT TYPE=\"radio\" NAME=\"jogos\" VALUE=\"" + matches.elementAt(x).getIdJogo() + "\" >" + matches.elementAt(x).getHome() + " VS " + matches.elementAt(x).getFora() + "<br/>");
                            }

                            //System.out.println("["+matches.elementAt(x).getIdJogo()+"]"+ matches.elementAt(x).getHome()+" VS "+matches.elementAt(x).getFora());
                        }
                        //resp2.getWriter().println("AHAH" + "<br/>");
                        //resp.getOutputStream().println("De:"+from+" "+message + "<br/>");
                    

                    //resp.getOutputStream().flush();
                    resp.getWriter().flush();

                    //resp.toString();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    
                    // Trouble using the response object's writer so we remove
                    // the user and response object from the hashtable
                   // m.removeClient(client, null);
                }
            }

        }
        System.out.println("acaba matches!!!");

    }
    
    @Override
    public void printMessage(String from, String message, String destination) throws java.rmi.RemoteException {
        System.out.println("entra msg");

        synchronized (m.clients) {
            try {

                HttpServletResponse resp = m.clients.get(destination).elementAt(0);

                //HttpServletResponse resp2 = ChatServlet.clients.get(destination+"1");
                
                    //resp2.resetBuffer();
                    //resp.resetBuffer();
                    //resp2.getWriter().println("AHAH" + "<br/>");
                if(from.length()==0){
                    
                    resp.getWriter().println("<font color=\"red\">"+ message+"</font>" + "<br/>");
                }else{
                    
                    resp.getWriter().println(from + ": " + message + "<br/>");
                
                }
                resp.getWriter().flush();
                //resp2.getWriter().flush();  

                //resp.toString();

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ENtra printmsg"+destination);
                
                // Trouble using the response object's writer so we remove
                // the user and response object from the hashtable
                m.removeClient(destination, null);
            }
        
        }
        System.out.println("sai msg");
    }

    @Override
    public void printMessageall(String from, String message) throws java.rmi.RemoteException {
        System.out.println("entra msg all print");

        synchronized (m.clients) {
            Set<String> clientKeySet = m.clients.keySet();
            // Let's iterate through the clients and send each one the message.
            for (String client : clientKeySet) {
                try {
                    HttpServletResponse resp = m.clients.get(client).elementAt(0);
                    //resp.resetBuffer();
                    resp.getWriter().println(from+ message + "<br/>");
                    resp.getWriter().flush();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("ENtra printmsgall"+client);
                    // Trouble using the response object's writer so we remove
                    // the user and response object from the hashtable
                    m.removeClient(client, null);
                }
            }
        }
        System.out.println("sai msg all print");

    }
    @Override
    public void UpdateUsersOnline() throws java.rmi.RemoteException{
                System.out.println("Start!!!userss");
                m.setOnlineUsers((OnlineUsers)m.obj.onlineUsers(new Generic()).getObj());
        
        System.out.println("end!!!userss");
    }
    @Override
    public void UpdateMatchs() throws java.rmi.RemoteException{
                System.out.println("Start!!!matches");
                m.setCurrentMatches((Vector<ViewMatch>) m.obj.viewMathces(new Generic()).getObj());
        
        System.out.println("end!!!matches");
    }
    @Override
    public void UpdateCredit(String nome)throws java.rmi.RemoteException{
                m.actualizaCredito(nome);
        
        System.out.println("end!!!userss");
    }

    

    
}
