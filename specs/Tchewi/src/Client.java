import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A very simple example of TCP server. When the server starts, it binds a
 * server socket on any of the available network interfaces and on port 2205. It
 * then waits until one (only one!) client makes a connection request. When the
 * client arrives, the server does not even check if the client sends data. It
 * simply writes the current time, every second, during 15 seconds.
 *
 * To test the server, simply open a terminal, do a "telnet localhost 2205" and
 * see what you get back. Use Wireshark to have a look at the transmitted TCP
 * segments.
 *
 * @author Olivier Liechti
 */
public class Client {

    static final Logger LOG = Logger.getLogger(Client.class.getName());

    //private final int TEST_DURATION = 15000;
    //private final int PAUSE_DURATION = 1000;
    //private final int NUMBER_OF_ITERATIONS = TEST_DURATION / PAUSE_DURATION;
    private final int REQUEST_PORT = 2000;

    /**
     * This method does the entire processing.
     */
    public void start() {
        LOG.info("Starting server...");

        Socket serverSocket = null;
        //Socket clientSocket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        Scanner scan = new Scanner(System.in);
        String operation = "";
        String result;

        try {
            LOG.log(Level.INFO, "Creating a client socket and binding it on any of the available network interfaces and on port {0}", new Object[]{Integer.toString(REQUEST_PORT)});
            serverSocket = new Socket("127.0.0.1", REQUEST_PORT);
            logSocketAddress(serverSocket);

            while (operation != "none") {

                System.out.println("Quelle opération voulez-vous effectuer ? (DoOp x op y)");
                reader = new BufferedReader(new InputStreamReader(System.in));
                operation = reader.readLine();
                reader.close();

                //LOG.log(Level.INFO, "Waiting (blocking) for a connection request on {0} : {1}", new Object[]{serverSocket.getInetAddress(), Integer.toString(serverSocket.getLocalPort())});
                //clientSocket = serverSocket.accept();

                //InputStream fromServer = socket.getInputStream();
                //OutputStream toServer = socket.getOutputStream();

                LOG.log(Level.INFO, "Getting a Reader and a Writer connected to the client socket...");

                writer = new PrintWriter(serverSocket.getOutputStream());

                reader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));




                //LOG.log(Level.INFO, "A client has arrived. We now have a client socket with following attributes:");
                //logSocketAddress(clientSocket);

                writer.println(operation);
                writer.flush();


                result = reader.readLine();

                System.out.println("Résultat : " + result);

//                LOG.log(Level.INFO, "Starting my job... sending current time to the client for {0} ms", TEST_DURATION);
//                for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
//                    writer.println(String.format("{'time' : '%s'}", new Date()));
//                    writer.flush();
//                    LOG.log(Level.INFO, "Sent data to client, doing a pause...");
//                    Thread.sleep(PAUSE_DURATION);
//                }

                reader.close();
                writer.close();
                serverSocket.close();


            }

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } finally {
            LOG.log(Level.INFO, "We are done. Cleaning up resources, closing streams and sockets...");
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.close();
            try {
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * A utility method to print server socket information
     *
     * @param serverSocket the socket that we want to log
     */
    private void logServerSocketAddress(ServerSocket serverSocket) {
        LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{serverSocket.getLocalSocketAddress()});
        LOG.log(Level.INFO, "             Local port: {0}", new Object[]{Integer.toString(serverSocket.getLocalPort())});
        LOG.log(Level.INFO, "               is bound: {0}", new Object[]{serverSocket.isBound()});
    }

    /**
     * A utility method to print socket information
     *
     * @param clientSocket the socket that we want to log
     */
    private void logSocketAddress(Socket clientSocket) {
        LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{clientSocket.getLocalAddress()});
        LOG.log(Level.INFO, "             Local port: {0}", new Object[]{Integer.toString(clientSocket.getLocalPort())});
        LOG.log(Level.INFO, "  Remote Socket address: {0}", new Object[]{clientSocket.getRemoteSocketAddress()});
        LOG.log(Level.INFO, "            Remote port: {0}", new Object[]{Integer.toString(clientSocket.getPort())});
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");

        Client client = new Client();
        client.start();
    }

}