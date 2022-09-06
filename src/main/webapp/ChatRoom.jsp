<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewpoint" content="width=device-width, initial-scale=1.0">
<title>ChatRoom</title>
<style>
	body {
		background-color: powderblue;
		font-family: serif;
	}
	
	.div-1{
		text-align:center;
	}
	
	.div-2{
		display:flex;
		height:25px;
		justify-content:center;
		margin-top:10px;
	}
	
	.info{
		font-size:125%;
	}
	
	.hide{
		display: none;
	}
	
	#ret {
		display: none;
		position: absolute;
	}
	
</style>
<script>
var portBool = false;
var userBool = false;
var user = '';
var port;
var cid;

var reload;
var serverOn = true;

function reloadChat(){
	var xhttp = new XMLHttpRequest();
		
	xhttp.onreadystatechange = function(){
		if(xhttp.readyState == XMLHttpRequest.DONE){
			if(xhttp.responseText != 'Server Closing.'){
				addMSGFromServer(xhttp.responseText);
				if(serverOn){
					reload = setTimeout(reloadChat, 250);
				}
			} else {
				addMSGFromServer(xhttp.responseText);
				document.getElementById('msg').setAttribute('readonly', true);
				clearTimeout(reload);
				serverOn = false;
				document.getElementById('div-2').innerHTML = "<button style = 'border-radius:20px; width:200px; height:50px;' onclick = 'downloadFile()' id = 'download'>Download ChatLog</button>";
				document.getElementById('ret').style.display = 'initial';
			}
		}
	}
	
	xhttp.open('post', 'retrieve?cid=' + cid, true);
	xhttp.send();
}

function addMSGFromServer(msg) {
	const chat = document.getElementById('chat');
	if(msg != ""){
		chat.value = chat.value + "\n" + msg;		  
	}
}

function addMSG() {
  const chat = document.getElementById('chat');
  const msg = document.getElementById('msg').value;
  document.getElementById('msg').value = '';
  if(msg != ""){
	var fullMSG = (user + msg).padEnd(32) + getTime();
	chat.value = chat.value + "\n" + fullMSG;
	sendMSG(fullMSG);
  }
}

function sendMSG(msg){
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function(){
		if(xhttp.readyState == XMLHttpRequest.DONE){
			if(xhttp.responeText == "error"){
				alert("Server was closed");
				document.getElementById('msg').setAttribute('readonly', true);
				clearTimeout(reload);
				serverOn = false;
			}
		}
	}
	
	var msgURI = encodeURIComponent(msg);
	
	xhttp.open('post', 'send?msg=' + msgURI + '&cid=' + cid, true);
	xhttp.send();
}
 
function checkEnter1(){
	 var key = window.event.keyCode;
	 
	 if(key == 13){
		 
		 document.getElementById('msg').value = document.getElementById('msg').value.replace("\n","");
		 addMSG(); 
	 }
	 
 }
 
function checkEnter2(){
	 var key = window.event.keyCode;
	 
	 if(key == 13){
		 getUser(); 
	 }
	 
}

function checkEnter3(){
	 var key = window.event.keyCode;
	 
	 if(key == 13){
		 getPort(); 
	 }
	 
}

 function getUser(){
	 user = document.getElementById('user').value + ": ";
	 if(user != ""){
		userBool = true;
		if(userBool && portBool){
			sendPort(port);
			setTimeout(function(){
				var hiddenDocuments = document.getElementsByClassName('hide');
				for(let i = 0; i < hiddenDocuments.length; i++){
					hiddenDocuments[i].style.display = 'initial';
				}
			}, 1000);
		}
	 }
	 
 }
 
 function getPort(){
	 port = document.getElementById('port').value;
	 if(port != '' && Number(port) > 8999 && Number(port) < 10000){
	 	portBool = true;
		if(userBool && portBool){
			sendPort(port);
			setTimeout(function(){
				var hiddenDocuments = document.getElementsByClassName('hide');
				for(let i = 0; i < hiddenDocuments.length; i++){
					hiddenDocuments[i].style.display = 'initial';
				}
			}, 1000);
		}
	 } else {
		 document.getElementById('port').value = "Invalid input. Try again.";
	 }
	 
 }
 
 function sendPort(port){
	 var xhttp = new XMLHttpRequest();
	 var portURL = encodeURIComponent(port);
	 var nameURL = encodeURIComponent(user);
	 xhttp.onreadystatechange = function(){
		 if(xhttp.readyState == XMLHttpRequest.DONE){
			 if(xhttp.responseText == "error"){
				 alert("INVALID PORT")
			 } else {
				 cid = xhttp.responseText.substring(xhttp.responseText.lastIndexOf('*')+1);
				 
				 document.getElementById('port').setAttribute('readonly', true);
				 document.getElementById('chat').value = xhttp.responseText.substring(0, xhttp.responseText.lastIndexOf('*'));
				 document.getElementById('chat').style.textAlign = 'center';
				 reload = setTimeout(reloadChat, 250);
			 }
		 }
	 }
	 
	 xhttp.open('get', 'socket?port=' + portURL + "&name=" + nameURL, true);
	 xhttp.send();
	 
 }
 
 function getTime(){
	 var date = new Date();
	 var info = date.getMonth()+1 + "/" + date.getDate() + "/" + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
	 return info;
 }

 function exitChat(){
	 var xhttp = new XMLHttpRequest();
	 alert("Leaving...");
	 var nameURL = encodeURIComponent(user);
	 
	 document.getElementById('msg').setAttribute('readonly', true);
	 clearTimeout(reload);
	 serverOn = false;
	 
	 xhttp.onreadystatechange = function(){
		 if(xhttp.readyState == XMLHttpRequest.DONE){
			 var closing = new XMLHttpRequest();
			 
			 closing.open('post', 'delete?cid=' + cid, true);
			 closing.send();
		 }
	 }
	 
	 xhttp.open('post', 'leaving?name=' + nameURL + '&cid=' + cid, true);
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
	<a href = 'chat_page.html' id = "ret"><button>Back</button></a>
	<div class = 'div-1'>
		<h1>Welcome To A ChatRoom!</h1>
		<label class='info'>Port (9001-9999): </label><input autocomplete = "off" style = "width:160px;"onkeypress = 'checkEnter3()' type = "text" id = "port" class = 'info'>
		<button id = 'port_submit' onclick = 'getPort()' class = 'info'>submit</button><br>
		<label class = 'info'>User Name: </label><input autocomplete = "off" onkeypress = 'checkEnter2()' type = "text" id = 'user' class = 'info'>
		<button onclick = 'getUser()' id = 'user_btn' class = 'info'>submit</button><br>
		<textarea style = "resize:none;" class = 'hide' readonly id = "chat" name = "chat" rows = "30" cols = "50">
		</textarea><br>
		<div id = 'div-2' class = 'div-2'>
			<input style = 'width:250px;' autocomplete = "off" type = "text" class = 'hide' onkeypress = "checkEnter1()" id = 'msg' placeholder = 'Enter a message:'></input>
			<button class = 'hide' style = 'width:50px;' onclick = 'addMSG()' id = "msg_button">enter</button>
			<button style = 'width:50px;' onclick = 'exitChat()' class = 'hide'>exit</button><br>
		</div>
	</div>
	
</body>
</html>