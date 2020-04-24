import java.io.*;
import java.net.Socket;

class ChatSocket extends Thread{
    public Socket socket;
    public String IdToken;
    public ChatSocket(Socket socket ,String idToken) {
        this.socket = socket;
        this.IdToken = idToken;
    }
    // 服务端传值给客户端
    public void out(String out) {
        try {
            if (socket.isConnected() && !socket.isClosed()) {
                BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(out+"\n");
                bw.flush();
                System.out.println("转发数据******" + out);
            } else {
                // 链接已关闭
                ChatManager.getInstance().remove(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            if (socket.isConnected() && !socket.isClosed()) {
                // 接受客户端数据
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                // 读取数据
                String line = null;
                HandleRequest rq = new HandleRequest();
                while ((line = br.readLine()) != null) {
                    System.out.println("接受数据******" + line);
                    rq.handle(line,this);
                }
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
                ChatManager.getInstance().remove(this);
            } else {
                // 链接已关闭
                ChatManager.getInstance().remove(this);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
