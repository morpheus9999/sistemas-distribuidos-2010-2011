package exemplo.Client;

import Client.User;
import java.io.*;
import java.sql.Timestamp;

import exemplo.common.*;

public class TwitterFunctions extends Thread implements exemplo.common.Constantes, Serializable {

    public TwitterClient client;
    private boolean stop;

    public TwitterFunctions(TwitterClient client) {
        this.client = client;
        this.stop=false;
        
    }

    public void run() {
        try {
            execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute() throws IOException {
        
        int op = 0;
        do {
            menu();
            op = User.readInt();
            if (op == 1) {
                if(this.client.isCommandFlag())
                    listTweets(this.client.getLogin());
                else
                    System.out.println("Nao e possivel utilizar esse comando!!!");
            }else
            if (op == 2) {
                print("Write message>");
                String data = User.readString();
                Timestamp date = new Timestamp(System.currentTimeMillis());
                if(this.client.isTweetsFlag()){
                    sendTweets(this.client.getLogin(), new Message(date, this.client.getLogin(), data));
                }
                else{
                    Message message = new Message(date, this.client.getLogin(), data);
                    if(this.client.buffer.size()<10)
                        this.client.buffer.add(message);
                    else
                        System.out.println("O buffer de tweets ja esta cheio!!!");
                }
            }else
            if (op == 3) {
                if(this.client.isCommandFlag()){
                    print("Enter User>");
                    String hisLogin = User.readString();
                    searchUsers(this.client.getLogin(), hisLogin);
                }
                else
                    System.out.println("Nao e possivel utilizar esse comando!!!");
            }else
            if (op == 4) {
                if(this.client.isCommandFlag()){
                    print("Insert login of the user to follow>");
                    String hisLogin = User.readString();
                    sendAddUser(this.client.getLogin(), hisLogin);
                }
                else
                    System.out.println("Nao e possivel utilizar esse comando!!!");
            }else
            if (op == 5) {
                if(this.client.isCommandFlag()){
                    listFollowers(this.client.getLogin());
                }
                else
                    System.out.println("Nao e possivel utilizar esse comando!!!");
            }else
            if (op == 6) {
                if(this.client.isCommandFlag()){
                    listFollowings(this.client.getLogin());
                }
                else
                    System.out.println("Nao e possivel utilizar esse comando!!!");
            }else
            if (op == 7) {
                if(this.client.isCommandFlag()){
                    signout(this.client.getLogin());
                }
                else
                    System.out.println("Nao e possivel utilizar esse comando!!!");
            }

        } while (!stop);
    }

    public boolean sendAddUser(String myLogin, String hisLogin) {
        //System.out.println("Envei um pedido de adicionar um user  --->"+ myLogin + ":" + hisLogin);
        return this.client.sendMensagem(new Mensagem(ADD_FOLLOW_REQ, myLogin, hisLogin), this.client.getOut());
    }

    public boolean sendRemoveUser(String myLogin, String hisLogin) {
        //System.out.println("Envei um pedido de remover user --->" + myLogin+ ":" + hisLogin);
        return this.client.sendMensagem(new Mensagem(REM_USER_REQ, myLogin, hisLogin), this.client.getOut());
    }

    public boolean sendTweets(String myLogin, Message msg) {
        //System.out.println("Envei um pedido de remover user --->" + myLogin+ ":" + msg.toString());
        return this.client.sendMensagem(new Mensagem(ADD_TWEETS_REQ, myLogin, msg), this.client.getOut());
    }

    public boolean listFollowers(String myLogin) {
        //System.out.println("Envei um pedido de remover user --->" + myLogin);
        return this.client.sendMensagem(new Mensagem(LIST_FOLLOWERS_REQ, myLogin), this.client.getOut());
    }

    public boolean listFollowings(String myLogin) {
        //System.out.println("Envei um pedido de remover user --->" + myLogin);
        return this.client.sendMensagem(new Mensagem(LIST_FOLLOWING_REQ, myLogin), this.client.getOut());
    }

    public boolean listTweets(String myLogin) {
        //System.out.println("Envei um pedido de remover user --->" + myLogin);
        return this.client.sendMensagem(new Mensagem(LIST_TWEETS_REQ, myLogin), this.client.getOut());
    }

    public boolean searchUsers(String myLogin, String hisLogin) {
        //System.out.println("Envei um pedido de remover user --->" + myLogin);
        return this.client.sendMensagem(new Mensagem(SEARCH_USER_REQ, myLogin, hisLogin), this.client.getOut());
    }

    public void signout(String myLogin) {
        this.stop=true;
        this.client.sendMensagem(new Mensagem(LOGOUT, myLogin), this.client.getOut());
        this.client.getMaintwitter().choose();
    }

    public void menu() {
        print("========================================");
        print("=             OPERATION MENU           =");
        print("========================================");
        print("= 1 - Display Tweets                   =");
        print("= 2 - Send a Tweet                     =");
        print("= 3 - Search Users                     =");
        print("= 4 - Follow a Users                   =");
        print("= 5 - Display list of followers        =");
        print("= 6 - Display list of followings       =");
        print("= 7 - Sign-out                         =");
        print("========================================");
    }

    public void print(String s) {
        System.out.println(s);
    }

    

}
