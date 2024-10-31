package at.studying;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket server;
    private final ServerApplication app;
    private ExecutorService threadPool;

    public Server(ServerApplication app) {
        this.app = app;
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            this.server = new ServerSocket(10001);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }

        System.out.println("Server started on http://localhost:10001");

        while(true) {
            try {
                Socket socket = this.server.accept();
                RequestHandler handler = new RequestHandler(socket, this.app);
                this.threadPool.submit(() -> {
                    try {
                        handler.handle();
                    } catch (IOException var2) {
                        var2.printStackTrace();
                    }

                });
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        }
    }
}
