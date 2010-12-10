<%@page import="Client_Server.Login"%>
<%@page import="java.util.*" %>
<html>
<head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" CONTENT="-1">
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="css/page.css" rel="stylesheet" type="text/css" />

    <title>Comet Chat</title>
</head>

    
<body >
    <div id="bg_container">
        <div id="container">
	        <div id="userStatus">
	            Logged as <b id='username'>
                        <%  Login m=(Login)session.getAttribute("Login");
                            out.print(m.getName());%>
                    </b> - <a href="#" onclick="quitChat();window.location='about:blank';">Logout</a><br>
	            <b id='credito'>xx</b> creditos - <a href="#" onclick="resetCredito();">reset</a>           
	        </div>

	        <div id="displayCurrentMatches">
	            <h2>Current Matches</h2>
	            <div id="currentMatches">
	                
	            </div>
	            
	            <div id="inputAposta">
	                <form name="tipoApostaJogos">
	                Casa<input type="radio" name="tipoAposta" value="1" checked>
	                Empate<input type="radio" name="tipoAposta" value="0" >
	                Fora<input type="radio" name="tipoAposta" value="2" >
	                </form>
	                <input id="apostaMensagem" type="text" size="3" onKeyPress="return numbersonly(this, event)"/>
	
	                <input type="button" onclick="apostar()" value="Apostar" />
	            </div>
	        </div>
	        
	        <div id="displayCometChat">
	        	<h2>Comet Chat</h2>
	            <div id="cometChat">
	            	
	            </div>
	            
	            <div id="onlineUsers">
					
	            </div>
                
                <input id="message" type="text" size="82"/>
                To: <input id="destination" type="text" size="16"/>
                <input type="button" onclick="sendMsg()" value="Send" />
	        </div>
	        
	        <div id="displayTopNews">
	        	
	        	<div id="topNews">
	        		<h2>Top News</h2>
	        		
	        		<div id="left">
                                    <ul>
	        			<li><a href="#">England 11-21 South Africa | Autumn international match report</a></li>
	        			<li><a href="#">England 11-21 South Africa | Autumn international match report</a></li>
	        			<li><a href="#">England 11-21 South Africa | Autumn international match report</a></li>
                                    </ul>
	        		</div>
                                    <ul>
	        		
					<li><a href="#">England 11-21 South Africa | Autumn international match report</a></li>
					<li><a href="#">England 11-21 South Africa | Autumn international match report</a></li>
					<li><a href="#">England 11-21 South Africa | Autumn international match report</a></li>
                                    </ul>
	        	</div>
	        	
                        <a name="openNew"></a>
	        	<div id="selectedNew">
	        		<h3>Titulo da noticia</h3>
	        		<h4>Subtitulo da noticia que pode ser comprido e ter varias palavras</h4>
	        		<img src="images/bolacha.png" width="140" height="84" />
	        		Esta noticia é mesmo boa, como o milho lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum 
	        	</div>
	        </div>
	    </div>
	    
		<div id="logo"></div>
	</div>
	
	<div id="square"></div>
	
	<div id="footer">
	    <div class="line1">Application created to 2010 Distributed Systems project.</div>
	    <div class="line2">Developed by Jorge Figueira.</div>
	</div>
</body>



<script type="text/javascript" src="comet.js"> </script>
<script type="text/javascript">
	
    // Initiate Comet object
    var comet = Comet("http://localhost:8080/CometSample/");
    var board = document.getElementById('cometChat');
    var board2 = document.getElementById('currentMatches');
    var board3 = document.getElementById('onlineUsers');
    var board4 = document.getElementById('credito');
    var board5 = document.getElementById('topNews');
    var board6 = document.getElementById('selectedNew');
    
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
    comet.get("ChatServlet?type=register4", function(response5) {
        // updates the message board with the new response.
        //alert("merda4");
       var test=response5.split("\n\n\n").pop();
       var test4;
        if(test.search("%%%%")>-1){
          test4=test.split("%%%%").pop();  
        board5.innerHTML =test4;
        
        }
        if(test.search("!!!!")>-1){
            test4=test.split("!!!!").pop(); 
            board6.innerHTML =test4;
        }
            
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
    function linkRest(m) {
        
			
                        
           var msgg="linkrest\n "+m;             
        comet.post("ChatServlet", msgg, function(response1) {
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
        document.getElementById('apostaMensagem').value = '';
    }
    
    
	
    function quitChat() {
        
        comet.post("ChatServlet?type=exit", '', function(response1) {
            // Exits browser
            window.location='about:blank';
        })
        
    }
	
	
    //This makes the browser call the quitChat function before unloading(or closing) the page
    window.onunload = quitChat;
</script>
</html>