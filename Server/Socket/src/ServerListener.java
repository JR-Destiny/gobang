import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread {
    @Override
    public void run() {
        try {
            // 1、创建ServerScoket，设置端口
            String IdToken;//id令牌
            ServerSocket serverSocket = new ServerSocket(8899);
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:"+addr);
            System.out.println("等待与客户端连接.....");
            while (true) {
                // 2、accept方法将导致程序阻塞
                Socket socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                // 读取id令牌
                IdToken = br.readLine();
                System.out.println("接受数据******" + IdToken);
                // 3、将socket传递给新线程
                ChatSocket cs = new ChatSocket(socket,IdToken);
                cs.start();
                // 4、使用Chatmanager进行管理
                ChatManager.getInstance().add(cs);
            }
        } catch (IOException e) {e.printStackTrace();}
    }
}