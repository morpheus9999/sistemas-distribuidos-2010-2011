/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import java.io.IOException;

/**
 *
 * @author JLA
 */
public class senderThread extends Thread{

    /*
     * Constructor
     */
    public senderThread() {
        super();
    }

    /*
     * Run
     * the main method of the sender thread
     */
    public void run() {
        Generic gen = new Generic();

        while(true) {
            try {
                switch(Main.opt.getOption()) {
                    case 1:
                        /*  get credit balance  */
                        gen.setCode(Constants.creditCode);
                        gen.setObj(new Credit());
                        break;
                    case 2:
                        /*  resets credit balance   */
                        gen.setCode(Constants.regCode);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 100:
                        break;
                    case 101:
                        break;
                    default:
                        break;
                }
                
                /*  sends the request to the server */
                Main.out.writeObject(gen);
            } catch (IOException ex) {
                System.out.println("IOException: "+ex.getMessage());
            }
        }
    }
}
