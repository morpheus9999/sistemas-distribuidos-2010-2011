
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jojo
 */
public class Rest extends Thread{
    
    ChatServlet m;
    SoccerReader games;
    Hashtable  <String,String>lastnews=null;
    public Rest(ChatServlet m) {
        this.m=m;
        
    }
    
    
    
    public void run() {
       games =new SoccerReader(m);
        
        while(true){
            try {
                
            lastnews=games.latestHeadlines("portugal", "sport");
                System.out.println("::>>>>>"+lastnews.toString());
            m.actualizaLinks(lastnews, "all");
            
                Thread.sleep(600000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        }
        
    }
    

}
