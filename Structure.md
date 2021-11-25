<!-- markdownlint-disable-file-->
# VB-WSEM-REF-C-0F

## Table of Content
1. Überblick
2. Implementation
3. Interaktionen mit und ohne Protokoll
4. Sockets in C-Artigen Sprachen
----------

## Structure
### 1. Überblick 
#### Was sind Sockets?
(src = RFC-147)
> A socket has been defined to be
> the identification of a port for
> machine to machine communication
> through the ARPA network.   

#### Seit wann sind sie in Java?
(src. Java-SE8 DOCS (java.net.Socket), https://en.wikipedia.org/wiki/Java_version_history)
> JDK 1.0 (Jan 1996)

#### Wo kann man Sockets Finden?
```json
{
  "types":[
    "mp-games" {
      "examples": ["Minecraft", "DOOM(1993, 2016)", "Microsoft Flight Simulator(2020)", "Fortnite", "CS:GO", "Portal 2"]
    },
    "web" {
      "examples": ["nginx (Server)", "Mozilla", "Chrome", "InternetExplorer", "Edge"]
    }
    "VoIP/Streaming" {
      "examples": ["Twitch", "Youtube", "Discord", "Skype", "MS-Teams"]
    }
  ]
}
```

#### Wichtige Klassen und Packages

*java.io*
java.io.IOException
java.io.InputStreamReader
java.io.PrintStream
java.io.InputStream
java.io.OutputStream
java.io.BufferedReader (can be replaced by java.util.Scanner)

*java.lang*
java.lang.Thread

*java.net*
java.net.Socket
java.net.ServerSocket
java.net.InetAddress

*java.util*
java.util.Scanner

### 2. Implementierung
![IMG](presentation/img/Socket_verbindungsaufbau_javatpoint.com.png)
[Example](Beispiele/java/com/akb/sig/mpchat/)
<!--Minimal Chat System over IP-->

- Programs split up into Client and Server
- The Ports need to match up
- Test in "localhost" (or "127.0.0.1")
  
### 3. Interaktionen mit und ohne Protokolle
[Example](Beispiele/java/com/akb/sig/mpnim/)
[Example](Beispiele/java/com/akb/sig/server/)
- Technically both Examples have Protocols, but only one of 'em is Standardized

### 4. Sockets in anderen, C-Artigen Sprachen
![IMG](presentation/img/influenceChain.png)
[Example](Beispiele/LnxServer.cpp) [Example](Beispiele/WinServer.cpp)
[Example](Beispiele/Client.cs)
[Example](Beispiele/Client.js)
[Example](Beispiele/Client.py)

?q="Was haben C#, C++, Java, JS und Python gemeinsam?"
> Sie werden alle als C-Artig klassifiziert, d.h. Sie haben eine ähnliche Syntax und stammen von C (1972) ab.

```json
// Instructions for influenceChain
{
  "langs":[
    "ALGOL-68" {
      "influenced": ["C", "C++", "Python", "Java", "JS", "C#"],
      "appeared": 1986;
    },
    "C" {
      "influenced": ["C++", "Python", "Java", "JS", "C#"],
      "appeared": 1972,
      "extra-connect": [{"C++", "cyclic"}]
    }
    "C++" { // C with Classes
      "influenced": ["Python", "Java", "JavaScript", "C#"],
      "appeared": 1985,
      "extra-connect":[{"C", "cyclic"}, {"C#", "VHV-both"}]
    }
    "Python" {
      "influenced": ["JavaScript"]
      "appeared": 1991,
    }
    "Java" {
      "influenced": ["Python", "JavaScript", "C#"],
      "appeared": 1995,
      "extra-connect": [{"C#", "-VHV-tipLeft"}]
    }
    "C#"{ // C with Classes x Java x VisualBasic
      "influenced": ["Java", "C++"]
      "appeared": 2000,
      "extra-connect": [{"C++", "VHV-both"}, {"Java", "-VHV-tipLeft"}]
    }

  ]
}
```