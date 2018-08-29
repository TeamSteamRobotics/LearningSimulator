package amrelk.simulation.learning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RobotServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    RobotServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    void stop() throws IOException{
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

    void send(String message) {
        out.println(message);
    }

    boolean ready() {
        try {
            return in.ready();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    String readLine() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "0,0,disabled";
        }
    }
}
