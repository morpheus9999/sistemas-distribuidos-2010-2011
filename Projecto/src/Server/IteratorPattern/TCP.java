/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.IteratorPattern;

import Server.ClientThreadTCP;

/**
 *
 * @author jojo
 */
public class TCP implements Connection{

    ClientThreadTCP clientThreadTCP;

    public TCP( ClientThreadTCP clientThreadTCP) {
        
        this.clientThreadTCP = clientThreadTCP;
    }
    
    

    public Object getCallbackObject() {
        return clientThreadTCP;
    }

}
