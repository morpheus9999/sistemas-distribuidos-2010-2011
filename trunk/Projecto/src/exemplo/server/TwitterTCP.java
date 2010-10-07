package exemplo.server;

import java.io.*;
import java.net.*;
import java.sql.*;
import exemplo.common.*;
import java.util.*;

public class TwitterTCP extends Thread implements exemplo.common.Constantes, Serializable {

    private Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;
    private boolean stop;
    private TwitterServer twitterServer;

    public TwitterTCP() {
        this.socket = null;
        stop = false;

    }

    public synchronized void setSocket(Socket s) {
        this.socket = s;
        notify();

    }

    @Override
    public void run() {
        while (!stop) {
            if (socket == null) {
                ;
            } else {
                try {
                    executa();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void executa() {
        Mensagem m = null;
        // Define tcp da socket sem atraso
        try {
            this.socket.setTcpNoDelay(true);
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        try {
            out = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
            DataInputStream ids = new DataInputStream(this.socket.getInputStream());
            DataOutputStream ods = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (!stop) {

            try {
                Object obj = in.readObject();
                if (obj instanceof Mensagem) {
                    m = (Mensagem) obj;
                    handleMensagem(m);
                }
            } catch (IOException e) {
                try {
                    socket.close();
                    in.close();
                    out.close();
                    stop = true;
                } catch (IOException e1) {
                    e1.printStackTrace();                   
                }
                //e.printStackTrace();
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
            }

        }

    }

    private void closed() {
        try {
            socket.close();
            in.close();
            out.close();
            stop = true;
        } catch (IOException e) {
            System.err.println("[TwitterTCP-exectuta]:Erro ao fechar streams");
        }
    }

    public void handleMensagem(Mensagem m) {
        switch (m.getType()) {
            case LOGIN_REQ:
                loginRequest(m);
                break;
            case REGISTER_REQ:
                registerRequest(m);
                break;
            case LIST_TWEETS_REQ:
                list_tweets_request(m);
                break;
            case ADD_TWEETS_REQ:
                add_tweets_request(m);
                break;
            case SEARCH_USER_REQ:
                search_user_request(m);
                break;
            case ADD_FOLLOW_REQ:
                add_follow_request(m);
                break;
            case LIST_FOLLOWERS_REQ:
                list_followers_request(m);
                break;
            case LIST_FOLLOWING_REQ:
                list_following_request(m);
                break;
            case LOGOUT:
                logoutRequest(m);
                break;
            default:
                System.err.println("[Worker-handleMensagem()]:Recebida mensagem desconhecida");
                break;
        }
    }

    private void loginRequest(Mensagem m) {
        if (twitterServer.listaClientesLigados.containsKey(m.getLogin())) {
            m = new Mensagem(LOGIN_RESP, m.getLogin(), "false");
            sendMensagem(m, out);
        } else {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost/sd?"
                        + "user=root&password=admin";
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement stmt = con.createStatement();
                ResultSet rs;

                int rowCount = -1;
                stmt = con.createStatement();
                rs = stmt.executeQuery("SELECT COUNT(login) FROM users WHERE login='" + m.getLogin() + "'and pass=" + m.getData());
                rs.next();
                rowCount = rs.getInt(1);

                if (rowCount == 1) {
                    m = new Mensagem(LOGIN_RESP, m.getLogin(), "true");
                    sendMensagem(m, out);
                    twitterServer.addListaClientesLigados(m.getLogin(), out);
                    System.out.println("size ::: " + twitterServer.listaClientesLigados.size());
                    while (rs.next()) {
                        String name = rs.getString("first_name");
                        String login = rs.getString("login");
                        String email = rs.getString("email1").concat("@").concat(rs.getString("email2"));
                        //System.out.println(name+" "+email+" "+login);
                    }

                } else {
                    m = new Mensagem(LOGIN_RESP, m.getLogin(), "false");
                    sendMensagem(m, out);
                }
            } catch (SQLException e) {
                m = new Mensagem(LOGIN_RESP, m.getLogin(), "false");
                sendMensagem(m, out);
                System.out.println("SQL Exception: " + e.toString());
            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
            }
        }
    }

    private void logoutRequest(Mensagem m) {
        twitterServer.listaClientesLigados.remove(m.getLogin());
        System.out.println("size ::: " + twitterServer.listaClientesLigados.size());
        closed();
    }

    private void registerRequest(Mensagem m) {
        boolean flag = false;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/sd?"
                    + "user=root&password=admin";
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement stmt = con.createStatement();
            String[] email = m.getEmail().split("@");
            flag = stmt.execute("INSERT INTO sd.users VALUES ('" + m.getFirstname() + "','" + m.getLastname() + "','" + email[0] + "','" + email[1] + "','" + m.getLogin() + "'," + m.getData() + ")");
            if (!flag) {
                m = new Mensagem(REGISTER_RESP, m.getLogin(), "true");
                sendMensagem(m, out);
            }

        } catch (SQLException e) {
            m = new Mensagem(REGISTER_RESP, m.getLogin(), "false");
            sendMensagem(m, out);
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }

    }

    private void list_tweets_request(Mensagem m) {

        Vector<String> listaTweets = new Vector<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/sd?"
                    + "user=root&password=admin";
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement stmt = con.createStatement();
            ResultSet rs1, rs2;
            Vector<String> vec = new Vector<String>();

            rs1 = stmt.executeQuery("SELECT * FROM message WHERE (login in (select hisLogin from follow where login = '" + m.getLogin() + "'))OR login='" + m.getLogin() + "' ORDER BY date DESC");
            while (rs1.next()) {
                String login = rs1.getString("login");
                String time = rs1.getString("date");
                String message = rs1.getString("message");
                String s = login + " : " + time + " : " + message;
                listaTweets.add(s);
                //System.out.println(login+" : "+time+" : "+message);
            }
            m = new Mensagem(LIST_TWEETS_RESP, m.getLogin(), listaTweets);
            sendMensagem(m, out);
        } catch (SQLException e) {
            listaTweets.add("Nao foi possivel executar o comando!!!");
            m = new Mensagem(LIST_TWEETS_RESP, m.getLogin(), listaTweets);
            sendMensagem(m, out);
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }
    }

    private void add_tweets_request(Mensagem m) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/sd?"
                    + "user=root&password=admin";
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement stmt = con.createStatement();
            stmt.execute("INSERT INTO message VALUES ('" + m.getLogin() + "','" + m.getMessage().getDate() + "','" + m.getMessage().getMessage() + "')");
            m = new Mensagem(ADD_TWEETS_RESP, m.getLogin(), "true");
            sendMensagem(m, out);
        } catch (SQLException e) {
            m = new Mensagem(ADD_TWEETS_RESP, m.getLogin(), "false");
            sendMensagem(m, out);
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }

    }
//

    private void search_user_request(Mensagem m) {
        Vector<String> listaUsers = new Vector<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/sd?"
                    + "user=root&password=admin";
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement stmt = con.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT * FROM users WHERE (first_name LIKE '%" + m.getData() + "%') OR (last_name LIKE '%" + m.getData() + "%') OR (CONCAT_WS(' ', first_name, last_name) LIKE '%" + m.getData() + "%' )");
            while (rs.next()) {
                String name = rs.getString("first_name").concat(" ").concat(rs.getString("last_name"));
                String login = rs.getString("login");
                String email = rs.getString("email1").concat("@").concat(rs.getString("email2"));
                String s = name + " " + email + " " + login;
                listaUsers.add(s);
                //System.out.println(name+" "+email+" "+login);
            }
            rs.close();
            m = new Mensagem(SEARCH_USER_RESP, m.getLogin(), listaUsers);
            sendMensagem(m, out);
        } catch (SQLException e) {
            listaUsers.add("Nao foi possivel executar o comando!!!");
            m = new Mensagem(SEARCH_USER_RESP, m.getLogin(), listaUsers);
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }

    }
//

    private void add_follow_request(Mensagem m) {
        Vector<String> listFollowing = list_following_request(m);
        if (!listFollowing.contains(m.getData())) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost/sd?"
                        + "user=root&password=admin";
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement stmt = con.createStatement();
                ResultSet rs;
                System.out.println("login: " + m.getLogin());
                System.out.println("his login: " + m.getData());
                int rowCount = -1;
                stmt = con.createStatement();
                rs = stmt.executeQuery("SELECT COUNT(login) FROM users WHERE login='" + m.getLogin() + "'");
                rs.next();
                rowCount = rs.getInt(1);

                if (rowCount == 1) {
                    stmt.execute("INSERT INTO follow VALUES ('" + m.getLogin() + "','" + m.getData() + "')");
                    m = new Mensagem(ADD_FOLLOW_RESP, m.getLogin(), "true");
                    sendMensagem(m, out);
                } else {
                    m = new Mensagem(ADD_FOLLOW_RESP, m.getLogin(), "false");
                    sendMensagem(m, out);
                }
            } catch (SQLException e) {
                m = new Mensagem(ADD_FOLLOW_RESP, m.getLogin(), "false");
                sendMensagem(m, out);
                System.out.println("SQL Exception: " + e.toString());
            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
            }
        } else {
            m = new Mensagem(ADD_FOLLOW_RESP, m.getLogin(), "false");
            sendMensagem(m, out);
        }
    }
//

    private void list_followers_request(Mensagem m) {
        Vector<String> listaFollowers = new Vector<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/sd?"
                    + "user=root&password=admin";
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM follow WHERE hisLogin = '" + m.getLogin() + "'");
            while (rs.next()) {
                String hisLogin = rs.getString("login");
                listaFollowers.add(hisLogin);
                //System.out.println(hisLogin);
            }
            m = new Mensagem(LIST_FOLLOWERS_RESP, m.getLogin(), listaFollowers);
            sendMensagem(m, out);
        } catch (SQLException e) {
            listaFollowers.add("Nao foi possivel executar o comando!!!");
            m = new Mensagem(LIST_FOLLOWERS_RESP, m.getLogin(), listaFollowers);
            sendMensagem(m, out);
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }

    }
//
//

    private Vector<String> list_following_request(Mensagem m) {
        Vector<String> listaFollowings = new Vector<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/sd?"
                    + "user=root&password=admin";
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM follow WHERE login = '" + m.getLogin() + "'");
            while (rs.next()) {
                String hisLogin = rs.getString("hisLogin");
                listaFollowings.add(hisLogin);
                //System.out.println(hisLogin);
            }
            m = new Mensagem(LIST_FOLLOWING_RESP, m.getLogin(), listaFollowings);
            sendMensagem(m, out);
            return listaFollowings;
        } catch (SQLException e) {
            listaFollowings.add("Nao foi possivel executar o comando!!!");
            m = new Mensagem(LIST_FOLLOWING_RESP, m.getLogin(), listaFollowings);
            sendMensagem(m, out);
            System.out.println("SQL Exception: " + e.toString());
            return null;
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
            return null;
        }

    }

    private synchronized boolean sendMensagem(Mensagem m, ObjectOutputStream out) {
        try {
            out.writeObject(m);
            out.flush();
            out.reset();
        } catch (Exception e) {
            e.printStackTrace();
//			System.err.println("Erro ao enviar mensagem:\n-Tipo: "
//					+ m.getType() + "\n-From: " + m. + "\n-To: "
//					+ m.getTo() + "\n-Data: " + m.getData());
            return false;
        }
        return true;
    }

    public TwitterServer getTwitterServer() {
        return twitterServer;
    }

    public void setTwitterServer(TwitterServer twitterServer) {
        this.twitterServer = twitterServer;
    }
}
