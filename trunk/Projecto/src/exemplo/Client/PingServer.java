/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exemplo.Client;



/**
 *
 * @author Francisco Fortunato
 */
public class PingServer extends Thread {

    private TwitterClientRMI twitterclientrmi;
    private boolean stop;
    public PingServer(TwitterClientRMI twitterclientrmi) {
        this.twitterclientrmi = twitterclientrmi;
        stop=true;
        
    }

    public void run() {
        execute();
    }

    public void execute() {
        while(stop){
            try {
                this.twitterclientrmi.getServerInterface().getStateServer();
                Thread.sleep(50);
            } catch (Exception e) {
                this.twitterclientrmi.reconnect();

            }
        }
    }

    public TwitterClientRMI getTwitterclientrmi() {
        return twitterclientrmi;
    }

    public void setTwitterclientrmi(TwitterClientRMI twitterclientrmi) {
        this.twitterclientrmi = twitterclientrmi;
    }

    
}
