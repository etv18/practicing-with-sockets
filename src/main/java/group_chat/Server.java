package group_chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {
    private static final int port = 9001;
    private static final String host = "localhost";
    private BufferedReader in;
    private BufferedWriter out;
    private ServerSocket server;

    Server(ServerSocket serverSocket) {
        server = serverSocket;
    }

    public void startServer(){
        System.out.println("***** SERVER IS UP *****");
        try {
            while(!server.isClosed()) {
                Socket socket = server.accept(); //The socket is a client
                ClientController clientController = new ClientController(socket);
                System.out.println("Client connected...");

                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(clientController);
            }
        } catch (IOException e) {

        }

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(new ServerSocket(port));
        server.startServer();
    }

}
