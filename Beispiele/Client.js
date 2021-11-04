const net = require('net'); // ~= import java.net.*;

const client = new net.Socket();
client.connect({ port: 442, host: "127.0.0.1"});
client.write("GET /index.html HTTP/1.1\r\nHost: 127.0.0.1\r\n\r\n");
client.on('data', (data) => {
    console.log(""+data.toString("utf-8"));
});
client.destroy();
