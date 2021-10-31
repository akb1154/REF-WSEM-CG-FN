package com.akb.sig.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class HTTPRequest {
	
	private String RQLN;
	private HTTPmethods method;
	private HashMap<String, String> headers;
	
	private enum HTTPmethods {
		POST, PUT, DELETE, CONNECT, OPTIONS, TRACE,
		GET, HEAD;
		
		public boolean isSupported () {
			return (this == GET || this == HEAD);	
		}
	}
	
	
	/**
	 * @deprecated
	 * @param br
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	public HTTPRequest (BufferedReader br) throws IllegalArgumentException, Exception {
		HTTPRequest.read (br);
	}
	
	private HTTPRequest() {
		return;
	}
	
	public static HTTPRequest read (BufferedReader br) throws IllegalArgumentException, Exception {
		HTTPRequest http = new HTTPRequest();
		http.RQLN = readRQLN (br);
		http.method = HTTPmethods.valueOf(http.RQLN.split(" ")[0]);
		http.headers = new HashMap<String, String>();
		
		if (!http.method.isSupported())
			throw new IllegalArgumentException("Method not Supported");
		
		// read headers
		String read = br.readLine();
		while (read != "" && read != null ) {
			http.headers.put (read.split(":")[0].toLowerCase(), read.split(":")[1].trim().toLowerCase());
			read = br.readLine();
		}
		return http;
	}
	
	private static String readRQLN (BufferedReader br) throws Exception {
		String read = br.readLine();

		boolean isRQLN = false;
		while (!isRQLN) // read until RQLN
			try {
				HTTPmethods.valueOf(read.split(" ")[0]);
				isRQLN = true;
			} catch (IllegalArgumentException e) {}			
		
		return read;
	}
	
	public String getPath() {
		return RQLN.split(" ")[1];
	}

	public String getHeader(String HeaderKey) {
		return headers.get(HeaderKey);
	}

}
