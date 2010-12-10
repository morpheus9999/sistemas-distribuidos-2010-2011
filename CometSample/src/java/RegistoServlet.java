/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import ClientRMI.CallbackMethods;
import Client_Server.CallbackInterface;
import Client_Server.Constants;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.RMIInterface;
import Client_Server.User;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jojo
 */
public class RegistoServlet extends HttpServlet {
    private RMIInterface obj;
   
    
    
    
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
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegistoServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegistoServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String user = request.getParameter("userName");
        String pass = request.getParameter("passWord");
        String email= request.getParameter("mail");
        int credit = Integer.valueOf( request.getParameter("credit"));
        RequestDispatcher dispatcher;
        User reg= new User();
        reg.setName(user);
        reg.setPassword(pass);
        reg.setMail(email);
        reg.setCredit(credit);
        
        Generic gen=new Generic();
        gen.setObj(reg);
        
        try{
            
            if(obj.register(gen))
                dispatcher = request.getRequestDispatcher("/registerUser.html");
            else{
                dispatcher = request.getRequestDispatcher("/registerUser.jsp");
            }
            
        }catch(IOException error){
            dispatcher = request.getRequestDispatcher("/registerUser.jsp");
        }
        
        
        dispatcher.forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
        
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    
}
