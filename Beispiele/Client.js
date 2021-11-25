const net = require('net'); // ~= import java.net.*;

let client = new net.Socket();
client.connect({ "port": 442, "host": "127.0.0.1"});
client.write("GET /index.html HTTP/1.1\r\nHost: 127.0.0.1\r\n\r\n");
client = client.setEncoding("utf-8")
client.pause();
var response = "";
var read = "";
while (read != null) {
    response += read;
    read = client.read(1);
}
console.log(response);
client.destroy();
