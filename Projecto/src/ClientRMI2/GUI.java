package ClientRMI2;

import Client_Server.Message;





public abstract class GUI extends javax.swing.JFrame{
    public abstract void displayMessage(Message m);
    public abstract void parseRequest(int tip,boolean stat,Object mensage);    
    public abstract void treatEvent(short eventType);
    public abstract void launch();

    public abstract void displayMessage(String string);
        
    public abstract void displayMessageString(String string);

    public abstract void displayMessageFrom(String from, String message);
       
    
}
