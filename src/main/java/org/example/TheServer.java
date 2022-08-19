package org.example;


import org.example.model.MyClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TheServer {
    private ServerSocket serverSocket;

    public TheServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection has been made");
                MyClientHandler clientHandler = new MyClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (IOException e){
           e.printStackTrace();
        }

        }

  public static void main(String[] args) throws IOException {
     ServerSocket serverSocket = new ServerSocket(8080);
       TheServer server = new TheServer(serverSocket);
       server.startServer();
    }
}