package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class IOBlockingTcpServer {

    public static final int PORT = 8080;

    // requested maximum length of the queue of incoming connections.)
    public static final int BACKLOG = 100;

    public static final String lineSeparator  = System.lineSeparator();

    public static void main(String[] args) throws IOException {
        startServer();
    }

    private static void startServer() throws IOException {
        try (var serverSocket = new ServerSocket(PORT, BACKLOG)) {
            while (true) {
                // we can add ExecutorService to support multi requests concurrently
                var clientSocket = serverSocket.accept();
                process(clientSocket);
            }
        }
    }


    private static void process(Socket clientSocket) throws IOException {
        // reader
        var bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // output stream
        var out = clientSocket.getOutputStream();

        // say hello to client
        out.write((STR."Hello. I am an echo server\{lineSeparator}")
                .getBytes(StandardCharsets.UTF_8));

        /*
            echo loop
            readLine is a blocking method
         */
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (!line.isEmpty()){
                out.write(STR."\{line}\{lineSeparator}"
                            .getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
