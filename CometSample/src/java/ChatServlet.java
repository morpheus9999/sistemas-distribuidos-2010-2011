
import Client_Server.Bet;
import Client_Server.CallbackInterfaceTomcat;
import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.OnlineUsers;
import Client_Server.RMIInterface;
import Client_Server.ViewMatch;
import java.io.IOException;
import java.lang.String;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;

/**
 * 
 * @author Andre Lourenco, aglour@student.dei.uc.pt
 */
public class ChatServlet extends HttpServlet implements CometProcessor {

    private static final long serialVersionUID = -7025063881838597634L;
    // The clients Map is used to associate a specific user id with a particular
    // HttpServletResponse object. This way if, later on, we want to send 
    // something to the client's socket, we can retrieve the HttpServletResponse.
    public static Map<String, Vector<HttpServletResponse>> clients = new Hashtable<String, Vector<HttpServletResponse>>();
    public static RMIInterface obj = null;
    public static OnlineUsers online = null;
    public static CallbackMethodsRMI_Servlets callback = null;
    public static Vector<ViewMatch> currentMatches = null;
    // Method called when a client is registers with the CometProcessor

    public void addClient(String nickName, HttpServletResponse clientResponseObject, int i) throws RemoteException {
        Vector<HttpServletResponse> m;
        synchronized (ChatServlet.clients) {

            if (!ChatServlet.clients.containsKey(nickName)) {

                System.out.println("add0 " + i + ":" + nickName);
                m = new Vector<HttpServletResponse>();
                m.setSize(4);
                m.setElementAt(clientResponseObject, i);
                System.out.println(m.toString());
                
                ChatServlet.clients.put(nickName, m);

            } else {
                System.out.println("add1 " + i + ":" + nickName);

                m = (Vector<HttpServletResponse>) ChatServlet.clients.get(nickName);
                
                System.out.println(m.toString());
                
                m.setElementAt(clientResponseObject, i);


                System.out.println(m.toString());
                ChatServlet.clients.put(nickName, m);


                System.out.println("::::" + ChatServlet.clients.get(nickName).size() + "\n\n");
            }
            System.out.println("LISTA:::::" + clients.toString());


            if (callback != null) {
                if (clients.get(nickName).elementAt(0) != null && i == 0) {
                    callback.printMessageall(nickName, " está online");
                }
            }

            if (clients.get(nickName).elementAt(2) != null && i == 2) {
                if (online == null) {
                    online = (OnlineUsers) obj.onlineUsers(new Generic()).getObj();



                    updateUsersOnline(nickName);
                } else {
                    updateUsersOnline(nickName);
                }

            } else if (clients.get(nickName).elementAt(1) != null && i == 1) {
                if (currentMatches == null) {
                    currentMatches = (Vector<ViewMatch>) obj.viewMathces(new Generic()).getObj();
                    updateCurrentMatches(nickName);
                } else {
                    updateCurrentMatches(nickName);
                }
            }else if(clients.get(nickName).elementAt(3)!=null && i==3){
                Login j=new Login();
                j.setName(nickName);
                Credit c= new Credit();
                Generic h= new Generic();
                h.setObj(c);
                
                 try {
                     h=obj.getCredit(h,j);
                        c=(Credit)h.getObj();
                        HttpServletResponse resp = (HttpServletResponse) ChatServlet.clients.get(nickName).elementAt(3);

                            resp.getWriter().println(c.getCredit());
                        

                        resp.getWriter().flush();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // Trouble using the response object's writer so we remove
                        // the user and response object from the hashtable
                    }

            }
        }

        // TODO 1: Write your code here.
    }

    // Method called after an Exception is thrown when the server tries to write to a client's socket.
    public void removeClient(String nickName, HttpServletRequest request) {
        System.out.println("entrou no remove client!!!!");
        if (ChatServlet.clients.remove(nickName) != null) {
            Login k = new Login();
            k.setName(nickName);
            try {
                obj.logout(k);
                request.removeAttribute("Login");
                request.getSession().invalidate();
                


                //request.removeAttribute(nickName);

                // TODO 2: Write your code here
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Main method that handles all the assynchronous calls to the servlet.
    // Receives a CometEvent object, that might have three types of EventType:
    // - BEGIN (when the connection starts. It is used to initialize variables and register the callback
    // - READ (means that there is data sent by the client available to be processed.
    // - END (happens when the connection is terminated, to clean variables and so on.
    // - ERROR (Happens when some IOException is thrown when writing/reading the connection.
    public void event(CometEvent event) throws IOException, ServletException {

//                                        
//             Login m = (Login) request.getSession().getAttribute("Login");
//             System.out.println("login:"+m.getName()+" "+m.getPassword());
//                                        
//             Generic s=new Generic();
//             s.setObj(m);
//                     
//                                       
//             obj.getMessage(m);
        // request and response exactly like in Servlets
        HttpServletRequest request = event.getHttpServletRequest();
        HttpServletResponse response = event.getHttpServletResponse();




        if (obj == null) {
            System.out.println("ENTRA connect");
            obj = (RMIInterface) request.getSession().getAttribute("rmi");
            callback = new CallbackMethodsRMI_Servlets(this);
            obj.setCallbackTomcat((CallbackInterfaceTomcat) callback);

        }


        // Parse the something from "?type=something" in the URL.
        String reqType = request.getParameter("type");

        // Initialize the SESSION and Cache headers.
        String sessionId = request.getSession().getId();
        //String nickName = (String) request.getSession().getAttribute("nickName");
        Login s = (Login) request.getSession().getAttribute("Login");
        System.out.println("entra e imprime:" + s.getName());
        //System.out.println("Nick: " + nickName);
        System.out.println("SESSION: " + sessionId);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-control", "no-cache");
        // Disabling the cache, means that the browser will _always_ call this code.


        // Let's see which even is being processed right now.
        System.out.println("Event:" + event.getEventType() + ".");

        // Since the "event" method is called for every kind of event, we have to decide what to do
        // based on the Event type. There for we check for all 4 kinds of events: BEGIN, READ, END and ERROR
        if (event.getEventType() == CometEvent.EventType.BEGIN) {
            // A connection is initiliazed

            if (reqType != null) {
                if (reqType.equalsIgnoreCase("register")) {
                    // Register will add the client HttpServletResponse to the callback array and start a streamed response.

                    // This header is sent to keep the connection open, in order to send future updates.
                    response.setHeader("Content-type", "application/octet-stream");
                    // Here is where the important Comet magic happens.

                    // Let's save the HttpServletResponse with the nickName key.
                    //  That response object will act as a callback to the client.
                    System.out.println("meter na 0:");
                    addClient(s.getName(), response, 0);



                } else if (reqType.equalsIgnoreCase("register1")) {
                    // Register will add the client HttpServletResponse to the callback array and start a streamed response.

                    // This header is sent to keep the connection open, in order to send future updates.
                    response.setHeader("Content-type", "application/octet-stream");
                    // Here is where the important Comet magic happens.

                    // Let's save the HttpServletResponse with the nickName key.
                    //  That response object will act as a callback to the client.
                    System.out.println("meter na 1:");
                    addClient(s.getName(), response, 1);

                    //callback.printMatches(obj.viewMathces(new Generic()));
                    //ChatServlet.clients.toString();


                } else if (reqType.equalsIgnoreCase("register2")) {
                    // Register will add the client HttpServletResponse to the callback array and start a streamed response.

                    // This header is sent to keep the connection open, in order to send future updates.
                    response.setHeader("Content-type", "application/octet-stream");
                    // Here is where the important Comet magic happens.

                    // Let's save the HttpServletResponse with the nickName key.
                    //  That response object will act as a callback to the client.
                    System.out.println("meter na 2:");
                    addClient(s.getName(), response, 2);

                    //callback.printMatches(obj.viewMathces(new Generic()));
                    //ChatServlet.clients.toString();


                } else if (reqType.equalsIgnoreCase("register3")) {
                    // Register will add the client HttpServletResponse to the callback array and start a streamed response.

                    // This header is sent to keep the connection open, in order to send future updates.
                    response.setHeader("Content-type", "application/octet-stream");
                    // Here is where the important Comet magic happens.

                    // Let's save the HttpServletResponse with the nickName key.
                    //  That response object will act as a callback to the client.
                    System.out.println("meter na 3:");
                    addClient(s.getName(), response, 3);

                    //callback.printMatches(obj.viewMathces(new Generic()));
                    //ChatServlet.clients.toString();


                } else if (reqType.equalsIgnoreCase("exit")) {
                    // if the client wants to quit, we do it.
                    System.out.println("EXIT::" + s.getName());

                    removeClient(s.getName(), request);

                }
            }
        } else if (event.getEventType() == CometEvent.EventType.READ) {
            // READ event indicates that input data is available


            // The first line read indicates the destination user.
            String dest = request.getReader().readLine().trim();
            // If it is 'allusers',the message should be delivered to all users

            // The second line is the message itself.
            String msg = request.getReader().readLine().trim();

            // For debug purposes
            System.out.println("msg = [" + msg + "] to " + dest);
            Generic gen = new Generic();
            Message mes = new Message();
            if (msg != null && !msg.isEmpty()) {
                if (dest.equals("allusers")) {

                    mes = new Message();
                    mes.setAuthor(s.getName());
                    mes.addEntry(dest, msg);
                    gen.setCode(Constants.messageAllCode);
                    gen.setObj(mes);
                    obj.messageAll(gen);

                    //sendMessageToAll(msg);

                } else if (dest.equals("apostar")) {
                    System.out.println("entra aposta");
                    try {
                        Generic m = new Generic();
                        Bet j = new Bet();
                        j.setAposta(Integer.parseInt(request.getReader().readLine().trim()));
                        j.setBet(Integer.parseInt(request.getReader().readLine().trim()));
                        j.setIdGame(Integer.parseInt(msg));

                        m.setObj(j);


                        m = this.obj.bet(m, s);
                        System.out.println("acaba aposta");

                        actualizaCredito(s.getName());
                        HttpServletResponse resp = (HttpServletResponse) ChatServlet.clients.get(s.getName()).elementAt(0);

                        if (m.getConfirmation()) {
                            resp.getWriter().println("<font color=\"red\">"+s.getName() + " apostou bem..." + "</font>"+"<br/>");
                        } else {
                            resp.getWriter().println("<font color=\"red\">"+s.getName() + " apostou mal!..." + "</font>"+"<br/>");
                        }

                        resp.getWriter().flush();
                        
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // Trouble using the response object's writer so we remove
                        // the user and response object from the hashtable
                        System.out.println("client remove:" + s.getName());
                        //removeClient(client, null);
                    }


                }else if (dest.equals("reset")) {
                    System.out.println("entra reset");
                    try {
                        Generic m = new Generic();
                        Credit j=new Credit();
                        m.setObj(j);


                        m = this.obj.resetCredit(m, s);
                        System.out.println("acaba rest");

                        actualizaCredito(s.getName());
                        HttpServletResponse resp = (HttpServletResponse) ChatServlet.clients.get(s.getName()).elementAt(0);

                        if (m.getConfirmation()) {
                            resp.getWriter().println("<font color=\"red\">"+s.getName() + " reset bem..." +"</font>"+ "<br/>");
                        } else {
                            resp.getWriter().println("<font color=\"red\">"+s.getName() + " reset mal!..." +"</font>"+ "<br/>");
                        }

                        resp.getWriter().flush();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // Trouble using the response object's writer so we remove
                        // the user and response object from the hashtable
                        System.out.println("client remove:" + s.getName());
                        //removeClient(client, null);
                    }


                } else {
                    System.out.println("de:" + s.getName());

                    mes = new Message();
                    mes.setAuthor(s.getName());
                    mes.addEntry(dest, msg);
                    gen.setCode(Constants.messageCode);
                    gen.setObj(mes);
                    obj.messageUser(gen);
                    //sendMessage(msg,dest);
                }
            }
            event.close();

        } else if (event.getEventType() == CometEvent.EventType.ERROR) {
            // In case of any error, we terminate the connection.
            // The connection remains in cache anyway, and it's later removed
            // when an Exception at write-time is raised.
            System.out.println("ERRO::" + s.getName());
            //obj.logout(s);
            event.close();

        } else if (event.getEventType() == CometEvent.EventType.END) {
            // When the clients wants to finish, we do it the same way as above.
            System.out.println("TERMINA::" + s.getName());
            //obj.logout(s);

            event.close();
        }
    }

    
    public void updateUsersOnline(String nome) throws RemoteException {
        System.out.println("ENTRA UPDATE USERS " + nome);

        Vector temp = online.getList();

        synchronized (ChatServlet.clients) {
            Set<String> clientKeySet = ChatServlet.clients.keySet();
            if (nome.compareTo("all") == 0) {
            } else {
                clientKeySet = new HashSet<String>();
                clientKeySet.add(nome);
            }
            System.out.println("updateUsers::" + clients.toString());
            // Let's iterate through the clients and send each one the message.
            for (String client : clientKeySet) {
                try {
                    HttpServletResponse resp = (HttpServletResponse) ChatServlet.clients.get(client).elementAt(2);

                    //resp.resetBuffer();
                    resp.getWriter().println("\n\n\n");
                    for (int x = 0; x < temp.size(); x++) {
                        resp.getWriter().println(temp.elementAt(x) + "<br/>");
                    }


                    resp.getWriter().flush();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Trouble using the response object's writer so we remove
                    // the user and response object from the hashtable
                    System.out.println("client remove:" + client);
                    //removeClient(client, null);
                }
            }
        }
        System.out.println("acaba UPDATE USERS");

    }

    public void setOnlineUsers(OnlineUsers onlineUsers) {
        online = onlineUsers;
        try {
            updateUsersOnline("all");
        } catch (Exception ex) {
            System.out.println("merda all!!!!!!!!!!!!!!");
            Logger.getLogger(ChatServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setCurrentMatches(Vector<ViewMatch> vector) {
        currentMatches = vector;
        updateCurrentMatches("all");
    }

    private void updateCurrentMatches(String nome) {

        Vector<ViewMatch> matches = currentMatches;

        synchronized (ChatServlet.clients) {
            Set<String> clientKeySet = ChatServlet.clients.keySet();
            if (nome.compareTo("all") == 0) {
            } else {


                clientKeySet = new HashSet<String>();
                clientKeySet.add(nome);
            }
            // Let's iterate through the clients and send each one the message.
            for (String client : clientKeySet) {
                try {
                    HttpServletResponse resp = (HttpServletResponse) ChatServlet.clients.get(client).elementAt(1);

                    //resp.resetBuffer();
                    resp.getWriter().println("\n\n\n");

                    resp.getWriter().println("<form name=\"jogoss\">");
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
                    resp.getWriter().println("</form>");
                    resp.getWriter().flush();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Trouble using the response object's writer so we remove
                    // the user and response object from the hashtable
                    System.out.println("client remove:" + client);
                    //removeClient(client, null);
                }
            }
        }


    }

    public void actualizaCredito(String name) {
        try {
                        Generic m = new Generic();
                        Credit j=new Credit();
                        m.setObj(j);

                        Login s=new Login();
                        s.setName(name);
                        m = this.obj.getCredit(m,s );

                        j=(Credit)m.getObj();
                        
                        HttpServletResponse resp = (HttpServletResponse) ChatServlet.clients.get(s.getName()).elementAt(3);

                        resp.getWriter().println("\n\n\n");
                        resp.getWriter().println(j.getCredit());
                        

                        resp.getWriter().flush();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // Trouble using the response object's writer so we remove
                        // the user and response object from the hashtable
                        //removeClient(client, null);
                    }
    }
}
