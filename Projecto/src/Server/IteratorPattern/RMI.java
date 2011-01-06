/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.IteratorPattern;

import Client_Server.CallbackInterface;

/**
 *
 * @author jojo
 */
public class RMI implements Connection{
    
    CallbackInterface callbackInterface;

    public RMI( CallbackInterface callbackInterface) {
        
        this.callbackInterface = callbackInterface;
    }
    

    public Object getCallbackObject() {
        return callbackInterface;
        
    }
    

}
