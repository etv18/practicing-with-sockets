package echo_server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

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
        String msgFromServer = null;
        try {
            msgFromServer = in.readLine();
            System.out.println(msgFromServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chat(){
        System.out.print("Enter a nick name: ");
        Scanner scanner = new Scanner(System.in);
        String userName = scanner.nextLine();
        while(!socket.isClosed()){
            System.out.print("\nLets go! "+userName+". Type your message: ");
            String msg = scanner.nextLine();
            sendMessage(msg);
            if(msg.contains("/q")) break;
            readMessage();
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 5000;
        String proxy = "localhost";
        Socket socket = new Socket(proxy, port);
        Client client = new Client(socket);
        client.chat();
    }
}
