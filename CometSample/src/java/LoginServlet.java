
/**
 * 
 */
import ClientRMI.CallbackMethods;
import Client_Server.CallbackInterface;
import Client_Server.Constants;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.RMIInterface;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author nseco
 */
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = -8608034654794572382L;
    RMIInterface obj;

    public void init() {
        
        try {
            this.obj = (RMIInterface) Naming.lookup(Constants.clientPrimaryServerRMI);


        } catch (NotBoundException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*  sets callback object    */


    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String user = request.getParameter("userName");
        String pass = request.getParameter("passWord");
        //String nick = request.getParameter("nickName");

        //String verifiedPass;
        RequestDispatcher dispatcher;

        Login s = new Login();
        s.setName(user);
        s.setPassword(pass);

        Generic m = new Generic();
        m.setObj(s);
        m.setCode(Constants.loginCode);
        try {
            if (this.obj.login(m) == true) {
                System.out.println("FIxE" + s.getName());



                // The parameter true defines that if the session
                // doesn't exist, it will be created
                HttpSession session = request.getSession(true);
                session.setAttribute("rmi", this.obj);
                session.setAttribute("gen", m);
                session.setAttribute("Login", s);

                dispatcher = request.getRequestDispatcher("/chat.jsp");

            } else {
                dispatcher = request.getRequestDispatcher("/login.html");
            }
        } catch (Exception e) {

            dispatcher = request.getRequestDispatcher("/login.html");
        }

        dispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
