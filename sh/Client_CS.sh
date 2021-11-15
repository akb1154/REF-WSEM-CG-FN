cd ./Beispiele;
chmod +x ./Client.cs;
mcs -out Client.exe ./Client.cs;
chmod +x ./Client.exe;
mono Client.exe;