package exemplo.Client;

import common.*;

import java.io.*;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ServerRMIInterface;

public class TwitterClientRMI extends UnicastRemoteObject implements common.Constantes, Serializable, ClientInterface {

    private String host;
    private String firstname;
    private String lastname;
    private String email;
    private String login;
    private String password;
    private boolean commandFlag = true;
    private boolean tweetsFlag = true;
    private boolean flagLogin = false;
    private boolean flagRegistar = false;
    private boolean flagBroadcast = false;
    private boolean flagLogout = true;
    private boolean pressFlagLogut = false;
    private MainTwitter maintwitter;
    private TwitterFunctions functions;
    private PingServer pingserver;
    private Vector<Object> buffer = new Vector<Object>();
    private Vector<Object> listaTweets = new Vector<Object>();
    private int timeLimit = 1000;
    private int TENTATIVAS = 6000 / timeLimit;
    private ServerRMIInterface serverInterface;

    public TwitterClientRMI(ServerRMIInterface serverInterface, String login, String password) throws RemoteException {
        this.serverInterface = serverInterface;
        this.login = login;
        this.password = password;

    }

    public TwitterClientRMI(ServerRMIInterface serverInterface, String firstname, String lastname,
            String login, String email, String password) throws RemoteException {
        this.serverInterface = serverInterface;
        this.firstname = firstname;
        this.lastname = lastname;
        this.login = login;
        this.email = email;
        this.password = password;

    }

    public boolean sendLogin() throws RemoteException {
        Mensagem msg = this.serverInterface.loginRequest(new Mensagem(LOGIN_REQ, this.login, this.password));
        respostaPedidoLogin(msg);

        return true;
    }

    public boolean sendRegisto() throws RemoteException {
        Mensagem msg = this.serverInterface.registerRequest(new Mensagem(REGISTER_REQ, this.firstname, this.lastname, this.login, this.email, this.password));
        resposta_registo(msg);
        return true;
    }

    public boolean sendLogout(String login) throws RemoteException {
        this.serverInterface.logoutRequest(new Mensagem(LOGOUT, login));
        return true;
    }

    public boolean reconnect() {
        setCommandFlag(false);
        setTweetsFlag(false);
        setFlagLogout(false);

        boolean ret = false, retry = false;
        int tentativas = 0, numRepeticao = 0;
        int porto = maintwitter.getPorto_primario();
        while (numRepeticao < 2) {
            do {
                try {
                    System.getProperties().put("java.security.policy", "policy.all");
                    System.setSecurityManager(new RMISecurityManager());
                    Registry registry = LocateRegistry.getRegistry(porto);
                    this.serverInterface = (ServerRMIInterface) registry.lookup("TWITTER");
                    maintwitter.setServerInterface(this.serverInterface);

                    login_resp_2(this.serverInterface.login_request_2(new Mensagem(LOGIN_REQ, this.login, this.password)));

                    this.setCommandFlag(true);
                    this.setTweetsFlag(true);
                    this.setFlagLogout(true);

                    retry = false;
                    ret = true;

                    numRepeticao=2;
                } catch (Exception e) {
                    if (tentativas < this.TENTATIVAS) {
                        //System.err.println("LIGACAO EM BAIXO...A TENTAR LIGAR AO NAMING SERVER NO PORTO " + maintwitter.getPorto() + " ( "+getTENTATIVAS()+" SEG )");
                        retry = true;
                        tentativas++;
                    } else {
                        retry = false;
                        if (tentativas == this.TENTATIVAS && numRepeticao < 2) {
                            tentativas = 0;
                            numRepeticao++;
                            porto = maintwitter.getPorto_secundario();
                            //System.err.println("Ligacao perdida....");
                            //this.flag_NamingService_down = true;
                            //System.exit(0);
                        }

                    }
                    try {
                        Thread.sleep(getTimeLimit());
                    } catch (InterruptedException i) {
                    }
                }
            } while (retry);
        }
        return ret;
    }

    public void sendMensagem(Mensagem msg) throws RemoteException {
        if (msg.getType() == ADD_FOLLOW_REQ) {
            resposta_add_user(this.serverInterface.add_follow_request(msg));
        } else if (msg.getType() == ADD_TWEETS_REQ) {
            resposta_add_tweets(this.serverInterface.add_tweets_request(msg));
        } else if (msg.getType() == LIST_FOLLOWERS_REQ) {
            resposta_followers(this.serverInterface.list_followers_request(msg));
        } else if (msg.getType() == LIST_FOLLOWING_REQ) {
            resposta_following(this.serverInterface.list_following_request(msg));
        } else if (msg.getType() == LIST_TWEETS_REQ) {
            resposta_tweets(this.serverInterface.list_tweets_request(msg));
        } else if (msg.getType() == SEARCH_USER_REQ) {
            resposta_search(this.serverInterface.search_user_request(msg));
        }

    }

    public void resposta_following(Mensagem msg) throws RemoteException {
        print("========================================");
        print("============= My Followings ============");
        Vector<Object> aux = msg.getListaDados();
        printVector(aux);
    }

    public void resposta_followers(Mensagem msg) throws RemoteException {
        print("========================================");
        print("============= My Followers =============");
        Vector<Object> aux = msg.getListaDados();
        printVector(aux);
    }

    public void resposta_add_user(Mensagem msg) throws RemoteException {
        if (msg.getData().equals("true")) {
            System.out.println("User adicionado com sucesso");
        } else {
            System.out.println("Nao foi possivel adicionar o User");
        }

    }

    public void resposta_search(Mensagem msg) throws RemoteException {
        print("========================================");
        print("============ Search Results ============");
        Vector<Object> aux = msg.getListaDados();
        printVector(aux);

    }

    public void resposta_add_tweets(Mensagem msg) throws RemoteException {
        if (msg.getData().equals("true")) {
            System.out.println("Tweet adicionado com sucesso");
        } else {
            System.out.println("Nao foi possivel adicionar o tweet");
        }
    }

    public void resposta_tweets(Mensagem msg) {
        print("========================================");
        print("=============== My Tweets ==============");
        this.listaTweets = msg.getListaDados();
        printVector(this.listaTweets);

    }

    public void respostaPedidoLogin(Mensagem msg) throws RemoteException {
        if (msg.getData().equalsIgnoreCase("true")) {

            loadBuffer();

            if (this.buffer != null && !this.buffer.isEmpty()) {
                //this.sendMensagem(new Mensagem(ADD_TWEETS_REQ_FROM_BUFFER, this.getLogin(), this.buffer), this.getOut());
                this.serverInterface.add_tweets_request_from_buffer(new Mensagem(ADD_TWEETS_REQ_FROM_BUFFER, this.getLogin(), this.buffer));
            }
            print("Autenticacao valida");
            functions = new TwitterFunctions(this);
            functions.setDaemon(true);
            functions.start();

            setPressFlagLogut(false);

            pingserver = new PingServer(this);
            pingserver.setDaemon(true);
            pingserver.start();

            try {
                this.serverInterface.join(login, this);
            } catch (NotBoundException ex) {
                Logger.getLogger(TwitterClientRMI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex) {
                Logger.getLogger(TwitterClientRMI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            print("Autenticacao invalida. Cliente ja esta ligado ou nao existe este cliente!!!");
            System.out.println(maintwitter);
            maintwitter.choose();
        }

    }

    private void login_resp_2(Mensagem msg) throws RemoteException {

        if (msg.getData().equalsIgnoreCase("true")) {
            loadBuffer();
            if (this.buffer != null && !this.buffer.isEmpty()) {
                add_tweets_from_buffer(this.serverInterface.add_tweets_request_from_buffer(new Mensagem(ADD_TWEETS_REQ_FROM_BUFFER, this.getLogin(), this.buffer)));
            }
            try {
                this.serverInterface.join(login, this);
            } catch (NotBoundException ex) {
                Logger.getLogger(TwitterClientRMI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex) {
                Logger.getLogger(TwitterClientRMI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (isPressFlagLogut()) {
                sendLogout(getLogin());
            }
//            this.buffer.removeAllElements();
//            saveBuffer();
        }

    }

    public void resposta_registo(Mensagem msg) throws RemoteException {
        if (msg.getData().equalsIgnoreCase("true")) {
            print("Registo bem sucedido.\n!!!Por favor, volta a fazer login!!!");
            maintwitter.choose();

        } else {
            print("Nao foi possivel efectuar o registo. O login ja existe!!!");
            maintwitter.choose();

        }

    }

    public void broadcast(Mensagem msg) throws RemoteException {
        int numTweets = -1;
        if (!msg.getData().equalsIgnoreCase("undefined")) {
            getListaTweets().add(0, msg.getData());
            numTweets = 1;
        } else if (msg.getListaDados() != null) {
            Vector<Object> aux = msg.getListaDados();
            for (int i = 0; i < aux.size(); i++) {
                Message message = (Message) aux.elementAt(i);
                getListaTweets().add(0, message.toString());
            }
            numTweets = aux.size();
        }

        if (isFlagBroadcast()) {
            print("========================================");
            print("=============== My Tweets ==============");
            printVector(this.listaTweets);
        } else {
            System.out.println("!!! You have " + numTweets + " new tweets !!!");
        }
    }

    public void add_tweets_from_buffer(Mensagem msg) throws RemoteException {
        if (msg.getData().equalsIgnoreCase("true")) {
            //System.out.println("Tweets do buffer adicionado com sucesso");
            this.buffer.removeAllElements();
            saveBuffer();
        }
//        else {
//            this.serverInterface.add_tweets_request_from_buffer(new Mensagem(ADD_TWEETS_REQ_FROM_BUFFER, this.getLogin(), this.buffer));
//        }

    }

    public boolean checkFile(String nomeFicheiro) {
        File f = new File(nomeFicheiro);
        if (!f.exists()) {
            try {
                if (f.createNewFile()) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean saveBuffer() {
        boolean ret = false;
        String nomeFicheiro = getLogin() + ".dat";
        if (checkFile(nomeFicheiro)) {
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(nomeFicheiro));
                oos.writeObject(this.buffer);
                oos.close();
                ret = true;
            } catch (FileNotFoundException e) {
                ret = false;

            } catch (WriteAbortedException e) {
                ret = false;

            } catch (IOException e) {
                ret = false;

            }
        }
        return ret;
    }

    private void loadBuffer() {
        String nomeFicheiro = getLogin() + ".dat";
        if (checkFile(nomeFicheiro)) {
            try {

                FileInputStream fis = new FileInputStream(nomeFicheiro);
                ObjectInputStream ois = new ObjectInputStream(fis);
                this.buffer = (Vector<Object>) ois.readObject();
                ois.close();
                fis.close();

            } catch (WriteAbortedException e) {
                return;

            } catch (EOFException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }

    }

    private void printVector(Vector<Object> v) {
        for (int i = 0; i < v.size(); i++) {
            System.out.println((String) v.elementAt(i));
        }
    }

    public void print(String s) {
        System.out.println(s);
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

    public boolean isFlagBroadcast() {
        return flagBroadcast;
    }

    public void setFlagBroadcast(boolean flagBroadcast) {
        this.flagBroadcast = flagBroadcast;
    }

    public Vector<Object> getListaTweets() {
        return listaTweets;
    }

    public void setListaTweets(Vector<Object> listaTweets) {
        this.listaTweets = listaTweets;
    }

    public boolean isFlagLogout() {
        return flagLogout;
    }

    public void setFlagLogout(boolean flagLogout) {
        this.flagLogout = flagLogout;
    }

    public boolean isPressFlagLogut() {
        return pressFlagLogut;
    }

    public void setPressFlagLogut(boolean pressFlagLogut) {
        this.pressFlagLogut = pressFlagLogut;
    }

    public ServerRMIInterface getServerInterface() {
        return serverInterface;
    }

    public void setServerInterface(ServerRMIInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    public Vector<Object> getBuffer() {
        return buffer;
    }

    public void setBuffer(Vector<Object> buffer) {
        this.buffer = buffer;
    }

    public TwitterFunctions getFunctions() {
        return functions;
    }

    public void setFunctions(TwitterFunctions functions) {
        this.functions = functions;
    }

    public PingServer getPingserver() {
        return pingserver;
    }

    public void setPingserver(PingServer pingserver) {
        this.pingserver = pingserver;
    }

}
