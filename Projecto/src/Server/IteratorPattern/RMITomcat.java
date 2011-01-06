/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.IteratorPattern;

import Server.Main;

/**
 *
 * @author jojo
 */
public class RMITomcat implements Connection{
    

    public RMITomcat() {
        
    }
    

    public Object getCallbackObject() {
        return Main.calbackInterfaceTomcat;
    }

    
}
