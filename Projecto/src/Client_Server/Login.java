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
    private String name;
    private String password;
    
    public Login() {
        this.name = null;
        this.password = null;      
    }

    /**
     * Constructor only used by User class to register clients
     * @param name
     * @param password
     */
    protected Login(int code) {
        this.name = null;
        this.password = null;
    }

    /**
     * Constructor only used by User class to register clients
     * @param name
     * @param password
     */
    protected Login(String name, String password){
        this.name = name;
        this.password = password;
    }
    
    protected Login(String name){
        this.name = name;
        
    }
    
    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }
}
