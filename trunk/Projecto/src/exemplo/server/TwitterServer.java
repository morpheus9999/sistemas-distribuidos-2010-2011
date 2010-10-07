package exemplo.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class TwitterServer{
	private int DEFAULT_TCP_PORT = 5555;
	public Hashtable<String, ObjectOutputStream> listaClientesLigados;
	public boolean stop;
    private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
    
    public TwitterServer(){
        
    }
	public static void main(String[] args) {

        TwitterServer server = new TwitterServer();
        

		int porto = server.DEFAULT_TCP_PORT;
		// Caso no seja introduzido nenhum porto envia mensagem de erro
		if (args.length != 1) {
			server.print("Introduza o porto como argumento\nExemplo: java Servidor01.java <Porto desejado>");
			server.print("\nSocket ser atribuida no porto por defeito");
		} else if (args.length == 1) {
			porto = Integer.parseInt(args[0]);
		}


		// Parent thread - create a server socket and wait a connection
		ServerSocket ss = null;
		Socket s = null;
		server.listaClientesLigados = new Hashtable<String, ObjectOutputStream>();

		try {
			ss = new ServerSocket(porto);
			server.print("Socket TCP atribuida no porto: " + porto);
			while ((s = ss.accept()) != null) {
				// Connection received - create a thread
				TwitterTCP tcp = new TwitterTCP();
				tcp.setTwitterServer(server);
                //tcp.setDaemon(true);
                tcp.setSocket(s);
				tcp.start();
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		// should add finally block to close down
	}

	public synchronized void print(String s) {
		System.out.println(s);
	}



	public void addListaClientesLigados(String login,
			ObjectOutputStream out) {
		listaClientesLigados.put(login, out);
	}

	public ObjectOutputStream getSocketDeUmClienteLigado(String login) {
		return listaClientesLigados.get(login);
	}

	public void removeClienteDaListaLigados(String login) {
		listaClientesLigados.remove(login);
	}
    
    public int getDEFAULT_TCP_PORT() {
        return DEFAULT_TCP_PORT;
    }

    public void setDEFAULT_TCP_PORT(int DEFAULT_TCP_PORT) {
        this.DEFAULT_TCP_PORT = DEFAULT_TCP_PORT;
    }
	
}
