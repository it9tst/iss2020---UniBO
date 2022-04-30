var stompClient = null;
var hostAddr = "http://localhost:50850/move";
var oldResponse = null;
var idTable1 = null;
var idTable2 = null;
var kickResponse = null;
var oldKickResponse = null;

//SIMULA UNA FORM che invia comandi POST
function sendRequestData( params, method) {
    method = method || "post"; // il metodo POST � usato di default
    //alert	(" sendRequestData  params=" + params + " method=" + method + " hostAddr=" + hostAddr);
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", hostAddr);
    var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "move");
        hiddenField.setAttribute("value", params);
     	//console.log(" sendRequestData " + hiddenField.getAttribute("name") + " " + hiddenField.getAttribute("value"));
        form.appendChild(hiddenField);
    document.body.appendChild(form);
    console.log("body children num= "+document.body.children.length );
    form.submit();
    document.body.removeChild(form);
    console.log("body children num= "+document.body.children.length );
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/it-unibo-iss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        stompClient.subscribe('/topic/display-manager', function (msg) {
            showMsg(JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/topic/display-smartbell', function (msg) {
            updateClient(JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/topic/display-maxstaytime', function (msg) {
            kickClient(JSON.parse(msg.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendUpdateRequest(){
	console.log(" sendUpdateRequest "  );
    stompClient.send("/app/update", {}, JSON.stringify({'name': 'update' }));
}

function replaceAll(str, cerca, sostituisci) {
    return str.split(cerca).join(sostituisci);
}

function showMsg(message) {
	console.log(message);
    
    var str = replaceAll(message, "&quot;", '"');
    var obj = JSON.parse(str);
    
    document.getElementById("statewaiter").innerHTML = obj.Waiter;	
    document.getElementById("statebarman").innerHTML = obj.Barman;
    document.getElementById("statetable1").innerHTML = obj.TABLE1;
    document.getElementById("statetable2").innerHTML = obj.TABLE2;

    if (obj.Waiter.length > 17) {
        document.getElementById("divWaiter").classList.add("transition");
    } else {
        document.getElementById("divWaiter").classList.remove("transition");
    }

    if (obj.Barman.length > 17) {
        document.getElementById("divBarman").classList.add("transition");
    } else {
        document.getElementById("divBarman").classList.remove("transition");
    }

    if (obj.TABLE1.length > 17) {
        document.getElementById("divTable1").classList.add("transition");
    } else {
        document.getElementById("divTable1").classList.remove("transition");
    }

    if (obj.TABLE2.length > 17) {
        document.getElementById("divTable2").classList.add("transition");
    } else {
        document.getElementById("divTable2").classList.remove("transition");
    }

    idTable1 = obj.TABLE1.match(/\d/g);
    idTable2 = obj.TABLE2.match(/\d/g);
}

function kickClient(message) {
    console.log(message);
    
    var str = replaceAll(message, "&quot;", '"');
    var obj = JSON.parse(str);

    kickResponse = obj.TABLE;
    console.log("PRIMA oldKickResponse: " + oldKickResponse);
    console.log("PRIMA kickResponse: " + kickResponse);
    if (kickResponse.localeCompare(oldKickResponse) != 0 && oldKickResponse != null) {
        
        switch(kickResponse) {
            case "1":
                console.log("1 VADO IN CLIENT3");
                client3(idTable1, true);
                break;

            case "2":
                console.log("2 VADO IN CLIENT3");
                client3(idTable2, true);
                break;

            default:
                console.log("DEFAULT");
        };
    }

    oldKickResponse = kickResponse;
    console.log("DOPO oldKickResponse: " + oldKickResponse);
    console.log("DOPO kickResponse: " + kickResponse);
}

function sendTheMove(move){
    console.log("sendTheMove " + move);
    stompClient.send("/app/move", {}, JSON.stringify({'name': move }));
}

//USED BY SOCKET.IO-BASED GUI
$( "#h" ).click(function() { sendTheMove("h") });
$( "#w" ).click(function() { sendTheMove("w") });
$( "#s" ).click(function() { sendTheMove("s") });
$( "#r" ).click(function() { sendTheMove("r") });
$( "#l" ).click(function() { sendTheMove("l") });
$( "#x" ).click(function() { sendTheMove("x") });
$( "#z" ).click(function() { sendTheMove("z") });


function newClient(){
    console.log("new client");
    stompClient.send("/app/new", {}, JSON.stringify({'name': 'enterRequestClient'}));
}

function updateClient(message) {
    console.log(message);

    if (oldResponse != message) {
        var clientButtonText = "Ask To Order";
        var str = replaceAll(message, "&quot;", '"');
        var obj = JSON.parse(str);

        if (obj.State == "checkTempClient") {
            Toastify({
                text: "The client can't enter - Temperature KO : " + obj.Temp.substring(0,4) + " °C",
                duration: 5000,
                close:true,
                gravity:"top",
                position: "right",
                backgroundColor: "#f44336",
            }).showToast();
        } else if (obj.State == "clientEnterWithTime") {
            Toastify({
                text: "Inform the client about the total maximum waiting time: " + ((parseFloat(obj.MWT) % 60000) / 1000).toFixed(0) + " seconds",
                duration: 5000,
                close:true,
                gravity:"top",
                position: "right",
                backgroundColor: "#f44336",
            }).showToast();
        } else {
            $("#clientStateBody").append("<tr><td><p class='mb-0'>" + obj.ID + "</p></td><td><p class='mb-0'>" + obj.State + "</p></td><td><button class='btn btn-block btn-light-primary font-bold' onclick='client1(" + obj.ID + ")'>" + clientButtonText + "</button></td></tr>");
        }

        oldResponse = message;
    }
}

function client1(id){
    console.log("client1");
    stompClient.send("/app/test", {}, JSON.stringify({'id': id, 'name': 'clientReadyToOrder'}));

    var clientState = "Would like to order";
    var clientButtonText = "Order";

    var table = document.getElementById("clientStateTable");
    tr = table.getElementsByTagName('tr');
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (id == txtValue) {
                tr[i].innerHTML = "<tr><td><p class='mb-0'>" + id + "</p></td><td><p class='mb-0'>" + clientState + "</p></td><td><button class='btn btn-block btn-light-primary font-bold' onclick='client2(" + id + ")'>" + clientButtonText + "</button></td></tr>";
            }
        }
    }
}

function client2(id){
    console.log("client2");
    stompClient.send("/app/test", {}, JSON.stringify({'id': id, 'name': 'clientOrder'}));

    var clientState = "Would like to pay";
    var clientButtonText = "Pay";

    var table = document.getElementById("clientStateTable");
    tr = table.getElementsByTagName('tr');
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (id == txtValue) {
                tr[i].innerHTML = "<tr><td><p class='mb-0'>" + id + "</p></td><td><p class='mb-0'>" + clientState + "</p></td><td><button class='btn btn-block btn-light-primary font-bold' onclick='client3(" + id + ", false)'>" + clientButtonText + "</button></td></tr>";
            }
        }
    }
}

function client3(id, isKick){
    console.log("client3");

    if (!isKick){
        stompClient.send("/app/test", {}, JSON.stringify({'id': id, 'name': 'clientPayment'}));
    }

    var clientState = "Exit";
    var clientButtonText = "End Work";

    var table = document.getElementById("clientStateTable");
    tr = table.getElementsByTagName('tr');
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (id == txtValue) {
                tr[i].innerHTML = "<tr><td><p class='mb-0'>" + id + "</p></td><td><p class='mb-0'>" + clientState + "</p></td><td>" + clientButtonText + "</td></tr>";
                setTimeout(removeClient, 30000, tr[i]);
            }
        }
    }
}

function removeClient(item){
    item.innerHTML = "";
}



/*
$(function () {
     $("form").on('submit', function (e) {
         e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });

//USED BY SOCKET.IO-BASED GUI  

    $( "#h" ).click(function() {  sendTheMove("h") });
    $( "#w" ).click(function() {  sendTheMove("w") });
    $( "#s" ).click(function() {  sendTheMove("s") });
    $( "#r" ).click(function() {  sendTheMove("r") });
    $( "#l" ).click(function() {  sendTheMove("l") });
    $( "#x" ).click(function() {  sendTheMove("x") });
    $( "#z" ).click(function() {  sendTheMove("z") });
    $( "#p" ).click(function() {  sendTheMove("p") });

    //$( "#rr" ).click(function() { console.log("submit rr"); redirectPost("r") });
    //$( "#rrjo" ).click(function() { console.log("submit rr"); jqueryPost("r") });

//USED BY POST-BASED GUI   
    
    $( "#ww" ).click(function() { sendRequestData( "w") });
    $( "#ss" ).click(function() { sendRequestData( "s") });
    $( "#rr" ).click(function() { sendRequestData( "r") });
    $( "#ll" ).click(function() { sendRequestData( "l") });
    $( "#zz" ).click(function() { sendRequestData( "z") });
    $( "#xx" ).click(function() { sendRequestData( "x") });
    $( "#pp" ).click(function() { sendRequestData( "p") });
    $( "#hh" ).click(function() { sendRequestData( "h") });

//USED BY POST-BASED BOUNDARY  
    $( "#start" ).click(function() { sendRequestData( "w") });
    $( "#stop" ).click(function()  { sendRequestData( "h") });

	$( "#update" ).click(function() { sendUpdateRequest(  ) });
});

//alert("app.js")

*/