/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author JLA
 */
public class Message implements Serializable{
    private String author = null;
    private Hashtable<String, String> messageBuffer = null;
    private String text;

    public Message() {
        this.author = null;
        this.messageBuffer = new Hashtable<String, String>();
    }
    public Message(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getText() {
        return this.text;
    }

    public void setHashtable(Hashtable<String, String> temp) {
        this.messageBuffer.putAll(temp);
    }

    public Hashtable<String, String> getHashtable() {
        return this.messageBuffer;
    }

    public boolean addEntry(String key, String message) {
        if(key != null && message != null) {
            this.messageBuffer.put(key, message);
            return true;
        } else
            return false;
    }

    public String getEntry(String key) {
        if(key != null)
            return this.messageBuffer.get(key);
        else
            return null;
    }

    public void clearHashtable() {
        this.messageBuffer.clear();
    }

    /*  to ease searches in the hashtable   */
    public Enumeration<String> getMessagesEnumeration() {
        return this.messageBuffer.elements();
    }

    /*  to ease searches in the hashtable   */
    public Enumeration<String> getKeysEnumeration() {
        return this.messageBuffer.keys();
    }
}
