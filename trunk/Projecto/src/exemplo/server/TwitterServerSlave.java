package exemplo.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class TwitterServerSlave extends TwitterServer {

    private int DEFAULT_TCP_SLAVE_PORT_ = 6666;

    public TwitterServerSlave() {
        super();
    }
    public static void main(String args[]){
        TwitterServerSlave sServer = new TwitterServerSlave();

       
        int porto = sServer.DEFAULT_TCP_SLAVE_PORT_;
		// Caso no seja introduzido nenhum porto envia mensagem de erro
		if (args.length != 1) {
			sServer.print("Introduza o porto como argumento\nExemplo: java Servidor01.java <Porto desejado>");
			sServer.print("\nSocket ser atribuida no porto por defeito");
		} else if (args.length == 1) {
			porto = Integer.parseInt(args[0]);
		}


		// Parent thread - create a server socket and wait a connection
		ServerSocket ss = null;
		Socket s = null;
		sServer.listaClientesLigados = new Hashtable<String, ObjectOutputStream>();

		try {
			ss = new ServerSocket(porto);
			sServer.print("Socket TCP atribuida no porto slave: " + porto);

			while ((s = ss.accept()) != null) {
				// Connection received - create a thread
				TwitterTCP tcp = new TwitterTCP();
				tcp.setTwitterServer(sServer);
                //tcp.setDaemon(true);
                tcp.setSocket(s);
				tcp.start();
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		// should add finally block to close down
    }
    public int getDEFAULT_TCP_SLAVE_PORT_() {
        return DEFAULT_TCP_SLAVE_PORT_;
    }

    public void setDEFAULT_TCP_SLAVE_PORT_(int DEFAULT_TCP_SLAVE_PORT_) {
        this.DEFAULT_TCP_SLAVE_PORT_ = DEFAULT_TCP_SLAVE_PORT_;
    }
}
