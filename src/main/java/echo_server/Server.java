package echo_server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public String getDateTimeFormatted(){

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        String dateTimeFormatted = dateTime.format(formatter);

        return dateTimeFormatted;
    }

    public void sendMessage(String msg){
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String readMessage(){

        String msg = null;

        try {
            msg = in.readLine();
            System.out.println(getDateTimeFormatted()+"- CLIENT MESSAGE: "+msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }

    public void startServer(){
        System.out.println("***** SERVER IS UP *****");
        try {
            socket = serverSocket.accept();
            System.out.println("Client connected...");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while(!serverSocket.isClosed()){
                String msg = readMessage();
                sendMessage(getDateTimeFormatted()+"- SERVER REPLIED"+msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 5000;
        ServerSocket serverSocket = new ServerSocket(port);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}
