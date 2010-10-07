/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

/**
 *
 * @author JLA
 */
public class Selection {

    private int option;

    /*  constructor: selects the identifier of the function to be executed  */
    public Selection() {
        this.option = -1;
    }

    /*  sets option to the desirable value  */
    public synchronized void setOption(int opt) {
        this.option = opt;
        this.notify();
    }

    /*  gets the current option */
    public synchronized int getOption() {
        try {
            this.wait();
            return this.option;
        } catch(Exception error) {
//            System.out.println("Error waiting for object: "+ error.getMessage());
            return -1;
        }
    }
}
