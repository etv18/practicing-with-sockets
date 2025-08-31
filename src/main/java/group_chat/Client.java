package group_chat;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Client {
    private final int port = 9001;
    private final String host = "localhost";
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public Client(Socket socket){
        try {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws IOException {
        int port = 9001;
        String host = "localhost";
        Socket socket = new Socket(host, port);
        Client client = new Client(socket);
        client.readMessages();
        client.chat();
    }

    public String getDateTimeFormatted(){

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        String dateTimeFormatted = dateTime.format(formatter);

        return dateTimeFormatted;
    }

    public void readMessages(){
        new Thread(() -> {
            String msg;
            while(socket.isConnected()){
                try{
                    msg = in.readLine();
                    System.out.println(getDateTimeFormatted()+" MESSAGE FROM OTHERS: "+msg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
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

    public void chat(){
        String msg;
        Scanner scanner = new Scanner(System.in);
        while(socket.isConnected()){
            System.out.print("Type your message: ");
            msg = scanner.nextLine();
            sendMessage(msg);
        }
    }
}
