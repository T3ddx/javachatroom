<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Server</title>
<style>
	body {
		background-color: powderblue;
		font-size: 150%;
		font-family:serif;
	}
	
	.div-1 {
		text-align:center;
		
	}
	.hide{
		display: none;
	}
	
	#ret {
		position: absolute;
	}
</style>

<script>
var portBool = false;
var nameBool = false;
var name;
var port;
var sid;

var clientAccepter;
var serverOn = true;

var reload;


function reloadChat(){
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
		if(xhttp.readyState == XMLHttpRequest.DONE){
			const chat = document.getElementById('chat');
			
			if(xhttp.responseText != "***"){
				chat.value = chat.value + "\n" + xhttp.responseText;
			}
			
			if(serverOn){
				reload = setTimeout(reloadChat, 500);
			}
		}
	}
	
	xhttp.open('post', 'getmsg?sid=' + sid, true);
	xhttp.send();
}

function accept(){	
	clientAccepter = new XMLHttpRequest();
	var nameURI = encodeURIComponent(name);
	
	clientAccepter.onreadystatechange = function(){
		if(clientAccepter.readyState == XMLHttpRequest.DONE){
			if(serverOn){
				lastLeft();
			}
		}
	}
	
	clientAccepter.open('post', 'accept?name=' + nameURI + '&sid=' + sid, true);
	clientAccepter.send();
}

function checkEnter1(){
	 var key = window.event.keyCode;
	 
	 if(key == 13){
		 getPort(); 
	 }
	 
}

function checkEnter2(){
	 var key = window.event.keyCode;
	 
	 if(key == 13){
		 getName(); 
	 }
	 
}

function getName(){
	 name = document.getElementById('name').value;
	 document.getElementById('chat').value = name;
	 document.getElementById('chat').style.textAlign = 'center';
	 if(name != ""){
		nameBool = true;
		if(nameBool && portBool){
			makeServer(port);
			var hiddenDocuments = document.getElementsByClassName('hide');
			for(let i = 0; i < hiddenDocuments.length; i++){
				hiddenDocuments[i].style.display = 'initial';
			}
		}
	 }
	 
}

function getPort(){
	 port = document.getElementById('port').value;
	 if(port != '' && Number(port) > 8999 && Number(port) < 10000){
	 	portBool = true;
		if(nameBool && portBool){
			makeServer(port);
			var hiddenDocuments = document.getElementsByClassName('hide');
			for(let i = 0; i < hiddenDocuments.length; i++){
				hiddenDocuments[i].style.display = 'initial';
			}
		}
	 } else {
		 document.getElementById('port').value = "Invalid input. Try again.";
	 }
	 
}

function makeServer(port){
	var xhttp = new XMLHttpRequest();
	var portURL = encodeURIComponent(port);

	xhttp.onreadystatechange = function(){
		if(xhttp.readyState == XMLHttpRequest.DONE){
			if(xhttp.responseText == "error"){
				alert("INVALID PORT");
			} else {
				sid = xhttp.responseText;
				
				document.getElementById('port').setAttribute('readonly', true);
				accept();
				reload = setTimeout(reloadChat, 500);
			}
		}
	}
	 
	 xhttp.open('post', 'server?port=' + portURL, true);
	 xhttp.send();
	 
}

function closeServer(){
	alert("closing server...");
	clearTimeout(reload);
	serverOn = false;
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.open('post', 'close?sid=' + sid, true);
	xhttp.send();
		
	document.getElementById('div-2').innerHTML = "<button style = 'border-radius:20px; width:200px; height:50px;' onclick = 'downloadFile()' id = 'download'>Download ChatLog</button>";
	document.getElementById('ret').style.display = 'initial';
	
}

function lastLeft(){
	alert("closing server...");
	clearTimeout(reload);
	serverOn = false;
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.open('post', 'close?nousers=t&sid=' + sid, true);
	xhttp.send();
		
	document.getElementById('div-2').innerHTML = "<button style = 'border-radius:20px; width:200px; height:50px;' onclick = 'downloadFile()' id = 'download'>Download ChatLog</button>";
	document.getElementById('ret').style.display = 'initial';
}

function downloadFile(){
	alert('downloading...');
	
	var xhttp = new XMLHttpRequest();
	var chatLogURI = encodeURIComponent(document.getElementById('chat').value);
	
	xhttp.open('post', 'download?chatlog=' + chatLogURI, true);
	xhttp.send();
}



</script>
</head>
<body>
	<a style = 'float:left; display:none;' id = 'ret' href = 'chat_page.html'><button>Back</button></a>
	<div class = 'div-1'>
		<label>Port (9001-9999):</label><input autocomplete = "off" style = "height:25px; font-size:90%" id = 'port' onkeydown = "checkEnter1()" type = "text">
		<button style = "font-size:80%;" onclick = "getPort()">submit</button><br>
		<label>Name:</label><input autocomplete = "off" style = "height:25px; width:338px; font-size:90%" id = 'name' onkeydown = "checkEnter2()" type = 'text'>
		<button style = "font-size:80%;" onclick = "getName()">submit</button><br>
		<textarea style = "resize:none;" class = 'hide' readonly id = "chat" name = "chat" rows = "30" cols = "50">
			Welcome To My ChatRoom!
		</textarea><br>
		<div id = 'div-2' class = 'div-2'>
			<button onclick = 'closeServer()' style = 'border-radius:20px; width:200px; height:50px;'class = 'hide' id = 'close'>Close Server</button><br><br>
		</div>
	</div>
</body>
</html>