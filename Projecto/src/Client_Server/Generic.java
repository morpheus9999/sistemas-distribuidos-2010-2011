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
public class Generic implements Serializable {
    private int code;
    private boolean confirmation;
    private Object obj;

    public Generic() {
        this.code = 0;
        this.confirmation = false;
        this.obj = null;
    }

    public Generic(int code) {
        this.code = code;
        this.confirmation = false;
        this.obj = null;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public boolean getConfirmation() {
        return this.confirmation;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return this.obj;
    }
}
