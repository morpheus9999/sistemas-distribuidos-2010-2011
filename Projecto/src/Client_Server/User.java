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
public class User extends Login implements Serializable {
    private int credit;
    private String mail;

    public User() {
        this.mail = null;
        this.credit = 0;
    }

    public User(String name, String password, String mail) {
        super(name, password);
        this.mail = mail;
        this.credit = 0;
    }

    public int getCredit() {
        return this.credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
