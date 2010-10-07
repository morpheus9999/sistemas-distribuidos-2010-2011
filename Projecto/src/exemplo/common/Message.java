package exemplo.common;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

public class Message implements Serializable {

	//private Date date;
	private Timestamp date;
	private String from = "undefined";
	private String to = "undefined";
	private String message = "undefined";
	
	public Message() { 
		
	}
	
	public Message(Timestamp date, String from, String message){
		this.date = date;
		this.from = from;
		this.message = message;
	}
	
	public Message(Timestamp date, String from, String to, String message){
		this.date = date;
		this.from = from;
		this.to = to;
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public String toString(){
		return this.message +"   "+this.date.toString();
	}

}
