package Client_Server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jorge
 */

import java.io.Serializable;


public class Login implements Serializable {
    private String mail;
    private String password;

    public Login() {
        this.mail = null;
        this.password = null;
    }

    public Login(String name, String password){
        this.mail = name;
        this.password = password;
    }

    public String getMail(){
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }
}