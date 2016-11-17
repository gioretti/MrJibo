$('document').ready(function(){
     var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
     var dateSocket = new WS($("#products").data("ws-url"));

     var receiveEvent = function(event) {
        let suggestions = JSON.parse(event.data);
        console.log(suggestions);
        $("#products").html("");
        suggestions.forEach(function(product){
            $("#products").append(
                "<p>" +
                    "<span class='bold'>" +
                        product.name +
                        " (" + product.price + " CHF)" +
                    "</span> <br />" +
                    product.description +
                "</p>"
            );
        });
    }

    dateSocket.onmessage = receiveEvent;
});
