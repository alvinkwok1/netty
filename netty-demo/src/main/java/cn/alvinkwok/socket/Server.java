package cn.alvinkwok.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {

    private static final int coreSize = 10;

    private static final int maxSize = 20;

    private static final int keepAliveTime = 0;

    private static final int queueSize = 100;

    private static final int revBufferSize = 2048;

    ExecutorService threadPool = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize));


    public void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress socketAddress = new InetSocketAddress(port);
            serverSocket.bind(socketAddress);
            // 开始监听
            Socket socket = serverSocket.accept();

            threadPool.execute(() -> startTask(socket));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startTask(Socket socket) {
        try {
            String ip = socket.getInetAddress().getHostAddress();
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[revBufferSize];
            while (true) {
                int len = is.read(buffer);
                System.out.printf("接收[%s]的数据，共[%d]字节,内容为[%s]%n", ip, len, new String(buffer));
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
