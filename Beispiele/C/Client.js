var net = require('net');

let $host = '127.0.0.1';
let $port = 80;

var client = new net.Socket();
client.connect($port, $host);

console.log('<!--Connected to '+$host+(($port != 80)? ":"+$port : "")+" -->");
client.write("GET /index.html HTTP/1.1\nHost: 127.0.0.1");
client.on('close', function() {
	console.log('Connection closed');
});
// event handler: print all data
var $in = "";
while (($in = client.read()) != null)
	console.log ($in);
client.destroy();