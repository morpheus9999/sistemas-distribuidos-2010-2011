
import java.util.Hashtable;

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
                if(lastnews!=null){
                System.out.println("::>>>>>"+lastnews.toString());
                m.actualizaLinks(lastnews, "all");
                    }
                    Thread.sleep(60000);
            } catch (InterruptedException ex) {
                    ex.printStackTrace();
                
            }
                     
        }
        
    }
    

}
