package echo_server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String userName;

    public Client(Socket socket){
        try {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg){
        try{
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readMessage(){
        Thread listeingToMessages = Thread.ofVirtual().start(() -> {
            String msgFromServer = null;
            while (!socket.isClosed()) {
                try {
                    msgFromServer = in.readLine();
                    if(msgFromServer == null) {
                        System.out.println("Chat closed...");
                        break;}
                    System.out.println("\n"+msgFromServer);
                    System.out.print("Lets go! "+userName+". Type your message: ");
                } catch (IOException e) {
                    e.printStackTrace();
                    closeEverything();
                    break;
                }
            }
        });
    }

    public void chat(){
        System.out.print("Enter a nick name: ");
        Scanner scanner = new Scanner(System.in);
        userName = scanner.nextLine();
        System.out.print("Lets go! "+userName+". Type your message: ");

        while(!socket.isClosed()){
            String msg = scanner.nextLine();
            sendMessage(msg);
            if(msg.contains("/q")){
                closeEverything();
                break;
            }
        }

        scanner.close();
    }

    public void closeEverything(){
        try{
            synchronized (socket){
                if(in != null) in.close();
                if(out != null) out.close();
                if(socket != null) socket.close();
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 5000;
        String proxy = "localhost";
        Socket socket = new Socket(proxy, port);
        Client client = new Client(socket);
        client.readMessage();
        client.chat();
    }
}
