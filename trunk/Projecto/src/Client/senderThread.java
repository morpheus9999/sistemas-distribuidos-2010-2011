/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

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

        while(true) {
            switch(Main.opt.getOption()) {
                case 1:
                    /*  get credit balance  */
                    //Main.out.writeObject();
                    break;
                case 2:
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
        }
    }
}
