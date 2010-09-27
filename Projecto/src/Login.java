/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jorge
 */
public class Login {
    private String username;
    private String password;

    public Login(String name, String password){
        this.username=name;
        this.password=password; 
    }

    public String getUserName(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

}
