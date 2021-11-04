#!/usr/bin/bash

main (){
    mkdir ~/.http;
    cp ./Beispiele/index.html ~/.http/index.html;
    isErr=$?;
    clear;
    echo "$isErr";
    sudo apt install npm npm-*; 
    sudo npm cache clean -f; 
    sudo npm install -g n;
    sudo n latest; 
}

main $@