import java.util.Vector;

public class ChatManager {
    private static ChatManager cm;
    // 单例
    public static ChatManager getInstance() {
        if (cm == null) {
            cm = new ChatManager();
        }
        return cm;
    }
    // ChatSocket集合
    Vector vector = new Vector<>();
    // 添加ChatSocket
    public void add(ChatSocket chatSocket) {
        vector.add(chatSocket);
        System.out.println("已有人连接,ip:"+chatSocket.socket.getInetAddress());
        System.out.println("当前连接人数："+ChatManager.getInstance().vector.size());
    }
    // 移除ChatSocket
    public void remove(ChatSocket chatSocket) {
        vector.remove(chatSocket);
        System.out.println("有人断开连接");
        System.out.println("当前连接人数："+ChatManager.getInstance().vector.size());
    }
    // 定向发送
    public void publishToOne(String to, String out) {
        for (int i = 0; i < vector.size(); i++) {
            ChatSocket chatSocket = (ChatSocket) vector.get(i);
            if (to.equals(chatSocket.IdToken)){
                chatSocket.out(out);
            }
        }
    }
    //广播发送
    public void publishToAll(ChatSocket cs, String out){
        for (int i = 0; i < vector.size(); i++) {
            ChatSocket chatSocket = (ChatSocket) vector.get(i);
            if (!cs.equals(chatSocket)) {
                chatSocket.out(out);
            }
        }
    }
}
