package group_chat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientController implements Runnable{
    private Socket client;
    private BufferedReader in;
    private BufferedWriter out;
    private String username;
    private static List<ClientController> clientControllers = new ArrayList<>();

    public ClientController(Socket socket) {
        client = socket;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            username = in.readLine();
            clientControllers.add(this);
            broadcast("<New Member> {"+username+"} has joined the chat !");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void broadcast(String msg){
        for(ClientController controller : clientControllers){
            controller.sendMessage(msg);
        }
    }

    @Override
    public void run() {
        String msg;
        while (!client.isClosed()){
            try {
                msg = in.readLine();
                broadcast(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
