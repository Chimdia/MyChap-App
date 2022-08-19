package org.example.model;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Clients {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String userName;

public Clients(Socket socket, String userName){
    try {
        this.socket = socket;
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.userName = userName;
    }catch (IOException e){
        closeEverything(socket,bufferedReader,bufferedWriter);
    }
}

public  void sendMessage(){
    try {
        bufferedWriter.write(userName);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        Scanner scanner = new Scanner(System.in);
        while(socket.isConnected()){
            String messageToSend = scanner.nextLine();
            bufferedWriter.write(userName + ": " + messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
    }catch (IOException e){
        closeEverything(socket, bufferedReader, bufferedWriter);
    }
}

public void listenForMessage(){
    new Thread(new Runnable() {

        @Override
        public void run() {
            String messageFromGroupChat;

            while(socket.isConnected()){
                try {
                    messageFromGroupChat = bufferedReader.readLine();
                    System.out.println(messageFromGroupChat);
                }catch (IOException e){
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }
    }).start();
    }
    public  void  closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (bufferedReader !=null){
                bufferedReader.close();
            }
            if (bufferedWriter !=null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {

      Scanner scanner =new Scanner(System.in);
        System.out.println("Marvel Heros Command center.");
        System.out.println("Enter your name to begin: ");
        String clientUserName = scanner.nextLine();
        Socket socket = new Socket("localhost", 8080);
        Clients client = new Clients(socket, clientUserName);
        client.listenForMessage();
        client.sendMessage();

    }
}
