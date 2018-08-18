package amrelk.simulation.learning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RobotServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    RobotServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    void stop() throws IOException{
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    void send(String message) {
        out.println(message);
    }
}
