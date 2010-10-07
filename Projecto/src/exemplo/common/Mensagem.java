package exemplo.common;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

public class Mensagem implements Constantes,Serializable{
	
	private int type = UNDEF;
	private String login = "undefined";
	private String data = "undefined";
	private String firstname = "undefined";
	private String lastname = "undefined";
	private String email = "undefined";
	private Message message = null;
	private ObjectOutputStream out;
	Vector <String>listaDados;


	public Mensagem(){
		
	}
	public Mensagem(int type, String login, String data ) {
        this.type = type;
        this.login = login;
        this.data = data;
    }
	public Mensagem(int type, String login, Message msg) {
        this.type = type;
        this.login = login;
        this.message = msg;
    }
    public Mensagem(int type, String login, Vector <String>listaDados ) {
        this.type = type;
        this.login = login;
        this.listaDados = listaDados;
    }
    
    public Mensagem(int type, String login, String data, ObjectOutputStream out) {
        this.type = type;
        this.login = login;
        this.data = data;
        this.out = out;
    }
        
    public Mensagem(int type, String firstname, String lastname, String login, String email, String data) {
        this.type = type;
        this.firstname = firstname;
        this.lastname = lastname;
        this.login = login;
        this.email = email;
        this.data = data;
    }
    
    public Mensagem(int type, String login) {
        this.type = type;
        this.login = login;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
     public Vector<String> getListaDados() {
        return listaDados;
    }

    public void setListaDados(Vector<String> listaDados) {
        this.listaDados = listaDados;
    }

}
