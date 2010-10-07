/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

import java.io.Serializable;

/**
 *
 * @author JLA
 */
public class Credit implements Serializable {
    private int credit;

    public Credit() {
        this.credit = -1;
    }

    public int getCredit() {
        return this.credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
