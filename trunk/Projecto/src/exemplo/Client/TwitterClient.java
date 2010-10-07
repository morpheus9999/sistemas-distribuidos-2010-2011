package exemplo.Client;

import exemplo.common.*;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class TwitterClient extends Thread implements exemplo.common.Constantes, Serializable {

    private String host;
    private String firstname;
    private String lastname;
    private String email;
    private String login;
    private String password;

    private boolean flag = true;
    private boolean commandFlag = true;
    private boolean tweetsFlag = true;
    private boolean stop;
    private boolean running = false;
    private boolean flagLogin = false;
    private boolean flagRegistar = false;


    private int tcp_first_port = 5555;
    private int tcp_second_port = 6666;

    private Socket theSocket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    

    private MainTwitter maintwitter;
    public TwitterFunctions functions;
    public Vector<Message> buffer = new Vector<Message>();
    
    private int timeLimit = 1000;
    private int TENTATIVAS = 60000/timeLimit;

    public TwitterClient(String host, String login, String password) {
        this.host = host;
        this.login = login;
        this.password = password;
        this.stop = false;
    }

    public TwitterClient(String host, String firstname, String lastname,
            String login, String email, String password) {
        this.host = host;
        this.firstname = firstname;
        this.lastname = lastname;
        this.login = login;
        this.email = email;
        this.password = password;
        this.stop = false;
    }

    public boolean connectSocket(int porto) {
        boolean ret = true;
        try {
            this.setTheSocket(new Socket(InetAddress.getByName(this.getHost()), porto));
            this.setOut(new ObjectOutputStream(new DataOutputStream(this.theSocket.getOutputStream())));
            this.setIn(new ObjectInputStream(new DataInputStream(this.theSocket.getInputStream())));
        //System.out.println(this.getTheSocket().getInetAddress().toString() +":"+this.getTheSocket().getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            ret = false;
        } catch (IOException e) {
            // e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public boolean sendLogin() {
        return sendMensagem(new Mensagem(LOGIN_REQ, this.login, this.password),
                this.getOut());
    }

    public boolean sendRegisto() {
        return sendMensagem(new Mensagem(REGISTER_REQ, this.firstname,
                this.lastname, this.login, this.email, this.password), this.getOut());
    }

    public boolean sendLogout(String login, String password) {
        return sendMensagem(new Mensagem(LOGOUT, login, password), this.getOut());
    }

    public boolean sendMensagem(Mensagem msg, ObjectOutputStream out) {
        try {
            out.writeObject(msg);
            out.flush();
            out.reset();
            return true;

        } catch (IOException e) {
            reconnect();
            return false;
        }
    }

    public void close() {
        try {

            this.getTheSocket().close();
            this.getOut().close();
            this.getIn().close();
            stop = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void close2() {
        try {

            this.getTheSocket().close();
            this.getOut().close();
            this.getIn().close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean initThread(int porto) {
        boolean ret = true;
        if (!connectSocket(porto)) {
            ret = false;
        }
        return ret;
    }
    public boolean isRunning(){
        return this.running;
    }

    public void run() {
        running = true;
        Mensagem msg = new Mensagem();
        while (!stop) {
            try {
                Object obj = this.getIn().readObject();
                if (obj == null) {
                    continue;
                } else if (obj instanceof Mensagem) {
                    msg = (Mensagem) obj;
                    handleMensagem(msg);
                }

            }catch (IOException e) {
                reconnect();
          
            //e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    private Mensagem handleMensagem(Mensagem msg) throws IOException {
        switch (msg.getType()) {
            case LOGIN_RESP:
                respostaPedidoLogin(msg);
                break;
            case REGISTER_RESP:
                resposta_registo(msg);
                break;
            case UNDEF:
                System.out.println("[ClienteTCP:handleMensagem]:Message received UNDEFINED");
                break;
            case LIST_TWEETS_RESP:
                resposta_tweets(msg);
                break;
            case ADD_TWEETS_RESP:
                resposta_add_tweets(msg);
                break;
            case SEARCH_USER_RESP:
                resposta_search(msg);
                break;
            case ADD_FOLLOW_RESP:
                resposta_add_user(msg);
                break;
            case LIST_FOLLOWERS_RESP:
                resposta_followers(msg);
                break;
            case LIST_FOLLOWING_RESP:
                resposta_following(msg);
                break;
            default:
                System.out.println("[ClienteTCP:handleMensagem]:Message malformed");
                break;
        }
        flag = false;
        return msg;
    }

    private void reconnect() {
        setCommandFlag(false);
        setTweetsFlag(false);

        //print("!!!The server is down!!!");
        close2();
        int porto = this.tcp_first_port;
        int n = 0, k = 1;
        do {

            try {
                this.theSocket = new Socket("localhost", porto);
                this.in = new ObjectInputStream(this.theSocket.getInputStream());
                this.out = new ObjectOutputStream(this.theSocket.getOutputStream());

                this.setCommandFlag(true);
                this.setTweetsFlag(true);
                while (!this.buffer.isEmpty()) {
                    this.sendMensagem(new Mensagem(ADD_TWEETS_REQ, this.getLogin(), this.buffer.firstElement()), this.getOut());
                    this.buffer.remove(0);
                }
            } catch (Exception ex) {
                in = null;
                out = null;
                // System.out.println("creating socket "+ex.getMessage());
            }

            if (k <= 2) {
                if (this.theSocket.isClosed() || in == null || out == null) {
                    //System.out.println("LIGACAO EM BAIXO...A TENTAR LIGAR AO SERVIDOR_" + k + " ( 10 SEG )");
                    n++;
                    if (n == TENTATIVAS) {
                        if (k == 2) {
                            System.exit(0);
                        }
                        k++;
                        n = 0;
                        porto = this.tcp_second_port;
                    }
                    try {
                        Thread.sleep(timeLimit);
                    } catch (InterruptedException i) {
                    }
                }
            }
        } while (this.theSocket.isClosed() || this.in == null || this.out == null);
    }

    private void resposta_following(Mensagem msg) {
        print("========================================");
        print("============= My Followings ============");
        Vector<String> aux = msg.getListaDados();
        for (int i = 0; i < aux.size(); i++) {
            System.out.println(aux.elementAt(i));
        }
        
    }

    private void resposta_followers(Mensagem msg) {
        print("========================================");
        print("============= My Followers =============");
        Vector<String> aux = msg.getListaDados();
        for (int i = 0; i < aux.size(); i++) {
            System.out.println(aux.elementAt(i));
        }

    }

    private void resposta_add_user(Mensagem msg) {
        if (msg.getData().equals("true")) {
            System.out.println("User adicionado com sucesso");
        } else {
            System.out.println("Nao foi possivel adicionar o User");
        }

    }

    private void resposta_search(Mensagem msg) {
        print("========================================");
        print("============ Search Results ============");
        Vector<String> aux = msg.getListaDados();
        for (int i = 0; i < aux.size(); i++) {
            System.out.println(aux.elementAt(i));
        }

    }

    private void resposta_add_tweets(Mensagem msg) {
        if (msg.getData().equals("true")) {
            System.out.println("Tweet adicionado com sucesso");
        } else {
            System.out.println("Nao foi possivel adicionar o tweet");
        }
    }

    private void resposta_tweets(Mensagem msg) {
        print("========================================");
        print("=============== My Tweets ==============");
        Vector<String> aux = msg.getListaDados();
        for (int i = 0; i < aux.size(); i++) {
            System.out.println(aux.elementAt(i));
        }

    }

    private void respostaPedidoLogin(Mensagem msg) {

        if (msg.getData().equalsIgnoreCase("true")) {
            print("Autenticacao valida");
            functions = new TwitterFunctions(this);
            functions.setDaemon(true);
            functions.start();
        
        } else {

            print("Autenticacao invalida. Cliente ja esta ligado ou nao existe este cliente!!!");
            maintwitter.choose();

        }

    }

    private void resposta_registo(Mensagem msg) {
        if (msg.getData().equalsIgnoreCase("true")) {
            print("Registo bem sucedido.\n!!!Por favor, volta a fazer login!!!");
            maintwitter.choose();

        } else {
            print("Nao foi possivel efectuar o registo. O login ja existe!!!");
            maintwitter.choose();
        
        }

    }

    public void print(String s) {
        System.out.println(s);
    }

    public Socket getTheSocket() {
        return theSocket;
    }

    public void setTheSocket(Socket theSocket) {
        this.theSocket = theSocket;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public void setTcp_first_port(int tcp_first_port) {
        this.tcp_first_port = tcp_first_port;
    }

    public void setTcp_second_port(int tcp_second_port) {
        this.tcp_second_port = tcp_second_port;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTcp_first_port() {
        return tcp_first_port;
    }

    public int getTcp_second_port() {
        return tcp_second_port;
    }

    public MainTwitter getMaintwitter() {
        return maintwitter;
    }

    public void setMaintwitter(MainTwitter maintwitter) {
        this.maintwitter = maintwitter;
    }

    public int getTENTATIVAS() {
        return TENTATIVAS;
    }

    public void setTENTATIVAS(int TENTATIVAS) {
        this.TENTATIVAS = TENTATIVAS;
    }

    public boolean isCommandFlag() {
        return commandFlag;
    }

    public void setCommandFlag(boolean commandFlag) {
        this.commandFlag = commandFlag;
    }

    public boolean isTweetsFlag() {
        return tweetsFlag;
    }

    public void setTweetsFlag(boolean tweetsFlag) {
        this.tweetsFlag = tweetsFlag;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
    public boolean isFlagLogin() {
        return flagLogin;
    }

    public void setFlagLogin(boolean flagLogin) {
        this.flagLogin = flagLogin;
    }

    public boolean isFlagRegistar() {
        return flagRegistar;
    }

    public void setFlagRegistar(boolean flagRegistar) {
        this.flagRegistar = flagRegistar;
    }

    
}
