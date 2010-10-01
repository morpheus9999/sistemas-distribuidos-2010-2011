/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

/**
 *
 * @author JLA
 */
public class User extends Login{
    private int credit;

    public User() {
        super();
        this.credit = 0;
    }

    private User(String mail, String password) {
        super(mail, password);
        this.credit = 0;
    }

    private User(String mail, String password, int credit) {
        super(mail, password);
        this.credit = credit;
    }

    private User(int credit) {
        super();
        this.credit = credit;
    }

    private int getCredit() {
        return this.credit;
    }

    private void setCredit(int credit) {
        this.credit = credit;
    }
}
