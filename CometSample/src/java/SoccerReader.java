
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class SoccerReader {
    ChatServlet m;
    // IMPORTANT
    // TODO 0
    // Get your own key at http://guardian.mashery.com/apps/register
    
    
    private String API_KEY = "zyw4m5v4z85hk9kwu5updfcd";

    public static void main(String[] args) {
        SoccerReader reader = new SoccerReader();

        // First we print the main headlines
        System.out.println("Headlines:");
        System.out.println("==========");
        Hashtable<String,String> lastID = reader.latestHeadlines("Portugal", "sport");

        // Then we print the main body of the first.
        System.out.println("\nMore Info:");
        System.out.println("==========");
        System.out.println("totall:" + lastID.size());
        int m = lastID.size() - 1;
        reader.recentBody(lastID.get("José Mourinho throws weight behind Portugal 2018 Ryder Cup bid"));
    }

    SoccerReader(ChatServlet m) {
        this.m=m;
    }

    private SoccerReader() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Hashtable<String,String> latestHeadlines(String query, String section) {
        // Used to store the last ID.
        String lastID = null;
        Hashtable<String,String> ID = new Hashtable<String,String>();
        try {
            // Initiate the REST client.
            URL url = new URL("http://content.guardianapis.com/search?q=" + query + "&section=" + section + "&order-by=newest&format=xml&api-key=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // HTTP Verb
            connection.setRequestMethod("GET");
            // Get requests data from the server.

            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("User-agent", "ToDo Manager");


            // If we get a Redirect or an Error (3xx, 4xx and 5xx)
            if (connection.getResponseCode() >= 300) {
                // We want more information about what went wrong.
                debug(connection);
                return null;
            }


            // Response body from InputStream.
            InputSource inputSource = new InputSource(connection.getInputStream());
            
            // XPath is a way of reading XML files.
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();


            // here we are querying the document (much like SQL) for all the todo tags inside todo elements.
            NodeList nodes = (NodeList) xPath.evaluate("/response/results/content", inputSource, XPathConstants.NODESET);
            // The last argument defines the type of result we are looking for. Might be NODESEQ for a list of Nodes
            // or NODE for a single node.


            // We don't need the connection anymore once we get the nodes.
            connection.disconnect();

            // Pretty printing of output
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);

                // Fetching the atributes of the node element
                String title = node.getAttributes().getNamedItem("web-title").getTextContent();
                lastID = node.getAttributes().getNamedItem("id").getTextContent();
                
                ID.put(title, lastID);
                //System.out.println(title);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return ID;
    }

    public Vector<String> recentBody(String lastID) {
        // This function should print the body of the last news item.
        try {

            /*
             * You should retrieve the contents of a certain news item, and print the trail-text.
             * 
             * Walkthrough:
             * 
             * => Signup for a API KEY (see above)
             * 
             * => Build the URL, based on the API explorer.
             * 		http://explorer.content.guardianapis.com/
             *  
             * => Use the Item Tab, and check Show fields: all. This is what allows you to get the trail-text.
             * 
             * => Don't forget to ask for XML, if you want to parse like in the previous example.
             * 
             */

            // TODO 1
            URL url = new URL("http://content.guardianapis.com/" + lastID + "?format=xml&show-fields=all&api-key=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // HTTP Verb
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("User-agent", "ToDo Manager");
Vector<String> envia=new Vector<String>();
            // If we get a Redirect or an Error (3xx, 4xx and 5xx)
            if (connection.getResponseCode() >= 300) {
                // We want more information about what went wrong.
                debug(connection);
                envia.addElement("ERROR::: "+connection.getResponseCode());
                return envia;
            }

            // Response body from InputStream.
            InputSource inputSource = new InputSource(connection.getInputStream());

            // XPath is a way of reading XML files.
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();

            // TODO 2: Extract all <field> elements using XPath.
            NodeList nodes = (NodeList) xPath.evaluate("response/content/fields/field", inputSource, XPathConstants.NODESET);
            
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                System.out.println(node.getAttributes().getNamedItem("name").getNodeValue());
                if(node.getAttributes().getNamedItem("name").getNodeValue().equals("headline")){
                    envia.addElement(node.getTextContent());
                    }
                
                if(node.getAttributes().getNamedItem("name").getNodeValue().equals("trail-text")){
                    envia.addElement(node.getTextContent()); 
                }
                if(node.getAttributes().getNamedItem("name").getNodeValue().equals("thumbnail")){
                    envia.addElement(node.getTextContent());                     
                }
                String title = node.getTextContent();
                System.out.println(title);
                
                
                //System.out.println(node.toString());
                // TODO 3
                // Check if name="trail-text"
                // And if is, print the content of the node element.
            }
            return envia;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void debug(HttpURLConnection connection) throws IOException {
        // This function is used to debug the resulting code from HTTP connections.

        // Response code such as 404 or 500 will give you an idea of what is wrong.
        System.out.println("Response Code:" + connection.getResponseCode());

        // The HTTP headers returned from the server
        System.out.println("_____ HEADERS _____");
        for (String header : connection.getHeaderFields().keySet()) {
            System.out.println(header + ": " + connection.getHeaderField(header));
        }

        // If there is an error, the response body is available through the method
        // getErrorStream, instead of regular getInputStream.
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getErrorStream()));
        StringBuilder builder = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            builder.append(inputLine);
        }
        in.close();
        System.out.println("Body: " + builder);
    }
}
