package pt.ual.sdp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private final static int PORT = 4242;
    private static ServerSocket serverSocket;
    private final Socket socket;

    public Server(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Object obj = null;

            while (true) {
                obj = in.readObject();
                if (obj instanceof String) {
                    if (((String) obj).equals("Q")) {
                        break;
                    } else if (((String) obj).equals("T")) {
                        serverSocket.close();
                        break;
                    }
                    System.out.println("[payload] Unexpected command.");
                } else if (obj instanceof Payload) {
                    System.out.println("[payload] " + ((Payload) obj).getData());
                    ((Payload) obj).setData("(modification) " + ((Payload) obj).getData());
                    out.writeObject(obj);
                } else {
                    System.out.println("[payload] Unexpected data.");
                }
            }
            socket.close();
            out.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("[started]");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("[connection] " +
                        socket.getInetAddress().getHostName() +
                        "@" +
                        socket.getInetAddress().getHostAddress() +
                        " " +
                        socket.getLocalPort() +
                        ":" +
                        socket.getPort());
                new Server(socket).start();
            } catch (IOException ioe) {
                if (serverSocket.isClosed()) {
                    System.out.println("[terminated]");
                } else {
                    ioe.printStackTrace();
                }
            }
        }
    }
}
