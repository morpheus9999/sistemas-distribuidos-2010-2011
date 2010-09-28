import java.io.IOException;
/*
 * Constants.java
 *
 * Created on 10 de Outubro de 2007, 15:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Administrador
 */
//Identificadores relevantes
public class Constants {
   
    //ficheiros
    
    final static String INFOCLIENTS = "Clients.txt";
    final static String INFOMERCHANTS = "Merchants.txt";
    final static String INFOPRODUCTS = "Product.dat";
    
    // IDs dos Tipos de mensagens
    final static int LISTPRODUCTS = 1;
    final static int LISTPRODUCTSMERCHANT = 2;
    final static int NEWPRODUCT = 3;
    final static int PUB = 4;
    final static int LISTPRODUCTSCLIENT = 30;
    final static int VALUELICITATION = 31;
    final static int REGISTER = 32;
    final static int LICITATION = 33;
    final static int CLOSE = 0;    
    final static int LOGINCLIENTS = 100;
    final static int LOGINMERCHANTS = 101;
    final static int ANSWER = 200;
    final static int BOOLEAN = 201;
    final static int SERVERPORTTCP = 6000;
    final static int SERVERPORTUDP = 6789;
    final static int CLOSEMERCHANT = 1000;
    final static int CLOSECLIENT = 1001;
    final static int CONFIRMALOGINCLIENT = 2000;
    final static int CONFIRMALOGINMERCHANT = 2001;
    final static int CONFIRMLOGIN = 2002;
    final static int CLIENT = 700;
    final static int MERCHANT = 701;
    
}
