package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOTcpServer {
    public static void main(String[] args) throws IOException {

        // Open a selector
        // Open a server socket channel
        try (var selector = Selector.open();
             var serverSocketChannel = ServerSocketChannel.open()) {

            // Bind the server socket channel to a specific port
            serverSocketChannel.bind(new InetSocketAddress(8080));

            // Configure the server socket channel to be non-blocking
            serverSocketChannel.configureBlocking(false);

            // Register the server socket channel with the selector for accept operations
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // Select channels with events
                selector.select();

                // Get selected keys
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

                while (keyIterator.hasNext()) {
                    var key = keyIterator.next();

                    if (key.isAcceptable()) {
                        // Accept the connection from the client
                        ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);

                        // Register the client channel with the selector for read operations
                        clientChannel.register(selector, SelectionKey.OP_READ);
                    }else if (key.isReadable()) {
                        // Read data from the client
                        var clientChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int bytesRead = clientChannel.read(buffer);

                        // Client disconnected
                        if (bytesRead == -1) {
                            clientChannel.close();
                        }else {
                            buffer.flip();
                            clientChannel.write(buffer);
                        }
                    }
                    keyIterator.remove();
                }
            }

        }


    }
}
