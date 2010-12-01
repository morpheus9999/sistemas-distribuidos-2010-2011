<%@page import="Client_Server.Login"%>
<%@page import="java.util.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/page.css" rel="stylesheet" type="text/css" />

        <title>Comet Chat</title>
    </head>
    <body >
        

        <div id="estado" >
            Logged as <b id='Nome' style="color: blue">         
        <%  Login m=(Login)session.getAttribute("Login");
            out.print(m.getName());
        %>
            </b> e tem <b id='credito'>xx</b> creditos <a href="#top" onclick="resetCredito();"> reset</a>   <a href="#top" onclick="quitChat();window.location='about:blank';">Logout</a>        
        </div>

        <div id="displayCurrentMatches"style="  position: fixed;  top: 10px; left: 10px; width: 600px; height: 500px">
            <h2> Current Matches</h2>
            <div id="currentMatches" style="background-color: ghostwhite; overflow: auto; border:thin solid #000000; position: fixed; top: 80px; left: 20px; width: 400px; height: 400px">
                
                <!--<input type="radio" name="jogos" value="0" checked /> -->
            </div>
            <div id="inputAposta" style="overflow: auto; position: fixed; top: 490px; left: 60px; width: 400px; height: 60px">
                <form name="tipoApostaJogos">
                Casa<input type="radio" name="tipoAposta" value="1" checked>
                Empate<input type="radio" name="tipoAposta" value="0" >
                Fora<input type="radio" name="tipoAposta" value="2" >
                <br/>
                </form>
                <input id="apostaMensagem" type="text" size="10" onKeyPress="return numbersonly(this, event)"/>

                <input type="button" onclick="apostar()" value="Apostar" />
                
            </div>
        </div>
        <div id="display2" style="  position: fixed;  top: 80px; left: 1000px; width: 600px; height: 500px">
            <h2>Online Users:</h2>
            <div id="onlineUsers" style="overflow: auto; border: thin solid #000000; top: 80px; left: 60px; width: 150px; height: 300px">

            </div>
        </div> 
        <div id="display" style="  position: fixed;  top: 10px; left: 580px; width: 600px; height: 500px">            
            <h2>Comet Chat</h2>
            <div id="messagesboard" style="overflow: auto; border: thin solid #000000; top: 80px; left: 60px; width: 400px; height: 400px">

            </div>

            <div id="input" style=" top: 490px; left: 580px; width: 400px; height: 60px">
                <input id="message" type="text" size="90"/><br/>
                Send To: <input id="destination" type="text" size="20"/><br/>
                <input type="button" onclick="sendMsg()" value="Send" />
                <input type="button" onclick="quitChat();window.location='about:blank';" value="Quit" />
            </div>

        </div>
        <div id="displayTopNews" style=" background-color: blue; position: relative;  top: 1000px; left: 20px; width: 1000px; height: 100px">
        <h2> MERDA2 </h2>
        
            <div id="messagesboard" style="background: yellow; overflow: relative; border: thin solid #000000; top: 80px; left: 60px; width: 400px; height: 400px">
                <h2> MERDA1 </h2>
            </div>
            
            
        </div>
            
            
        <div id="displayNew" style="background: yellow; background-color: aqua; position: relative;  top: 1500px; left: 1000px; width: 600px; height: 500px">
        
            <h2> MERDA </h2>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla egestas condimentum tellus nec sagittis. Donec pharetra semper sem quis hendrerit. Sed id dui justo. Cras sit amet lacus sem. Quisque suscipit semper nunc, in interdum purus elementum non. Mauris a turpis a lacus imperdiet dapibus. Etiam purus ante, malesuada eu accumsan nec, imperdiet id leo. Nullam et sagittis diam. Nam porttitor congue nisl in rhoncus. Curabitur nec purus ac elit tempus tempor sit amet quis tortor. Morbi nisi lacus, facilisis vel molestie eget, ornare eget urna. Nunc vel mi risus, accumsan fermentum leo. Phasellus placerat mauris sed enim consectetur sit amet faucibus diam dictum. Ut hendrerit arcu sed nisl porttitor aliquam. Curabitur imperdiet libero felis. Curabitur ante nibh, porta sit amet ornare quis, convallis at quam. Integer lobortis, tortor eu mollis tempor, justo sapien fermentum nulla, quis aliquam magna massa quis orci. Pellentesque quam nisl, rutrum eu feugiat porta, semper et turpis.


            
            
        </div>    
    </body>
    <script type="text/javascript" src="comet.js"> </script>
    <script type="text/javascript">
    	
        // Initiate Comet object
        var comet = Comet("http://localhost:8080/CometSample/");
        var board = document.getElementById('messagesboard');
        var board2 = document.getElementById('currentMatches');
        var board3 = document.getElementById('onlineUsers');
        var board4 = document.getElementById('credito');
        // Register with Server for COMET callbacks.
        comet.get("ChatServlet?type=register", function(response1) {
            // updates the message board with the new response.
            //alert("merda1");
            board.innerHTML = response1;
        });
        comet.get("ChatServlet?type=register1", function(response2) {
            // updates the message board with the new response.
            //alert("merda2");
            board2.innerHTML =response2.split("\n\n\n").pop();
                
                
        });
        comet.get("ChatServlet?type=register2", function(response3) {
            // updates the message board with the new response.
            //alert("merda4");
            board3.innerHTML =response3.split("\n\n\n").pop();
               
                
        });
        comet.get("ChatServlet?type=register3", function(response4) {
            // updates the message board with the new response.
            //alert("merda4");
            board4.innerHTML =response4.split("\n\n\n").pop();
               
                
        });
        function numbersonly(myfield, e, dec){
            var key;
            var keychar;

            if (window.event)
                key = window.event.keyCode;
            else if (e)
                key = e.which;
            else
                return true;
            keychar = String.fromCharCode(key);

            // control keys
            if ((key==null) || (key==0) || (key==8) || 
                (key==9) || (key==13) || (key==27) )
                return true;

            // numbers
            else if ((("0123456789").indexOf(keychar) > -1))
                return true;

            // decimal point jump
            else if (dec && (keychar == "."))
            {
                myfield.form.elements[dec].focus();
                return false;
            }
            else
                return false;
        }
        
        function getRadioCheckedValue(oRadio) {
            //var oRadio = document.forms[0].elements[radio_name];
                    
            for(var i = 0; i < oRadio.length; i++) { 

                if(oRadio[i].checked) {
                    return oRadio[i].value;
                }

            }

            return '';
        }
        
        
        function sendMsg() {
            var msg = document.getElementById('message').value;
            var dest = document.getElementById('destination').value;
            if (dest == "") {
                msg = "allusers\n" + msg;
            }
            else {
                msg = dest + "\n" + msg;
            }
    			
            comet.post("ChatServlet", msg, function(response1) {
                // Do Nothing
            })
            // Clears the value of the message element
            document.getElementById('message').value = '';
        }
        
        function resetCredito() {
            
    			
            comet.post("ChatServlet", "reset\n a", function(response1) {
                // Do Nothing
            })
        }
        
        
        function apostar() {
            //alert("aposta");
            var msg = document.getElementById('apostaMensagem').value;
            
            //alert("msg"+msg);
             //var tipoaposta= document.getElementById('apostaMensagem');
             var tipoaposta=getRadioCheckedValue(document.tipoApostaJogos.tipoAposta);
             //alert("idaposta"+tipoaposta2);
            
            var idaposta= getRadioCheckedValue(document.jogoss.jogos);
            //alert("idaposta"+idaposta);
           
            msg="apostar\n"+idaposta+"\n"+tipoaposta+"\n"+msg;
            
        
    			
            comet.post("ChatServlet", msg, function(response1) {
                // Do Nothing
            })
            // Clears the value of the message element
            document.getElementById('apostaMensgem').value = '';
        }
        
        
    	
        function quitChat() {
            alert("entra");
            comet.post("ChatServlet?type=exit", '', function(response1) {
                // Exits browser
                window.location='about:blank';
            })
        }
    	
    	
        //This makes the browser call the quitChat function before unloading(or closing) the page
        window.onunload = quitChat;
    </script>
</html>
