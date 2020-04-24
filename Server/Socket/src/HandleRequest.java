
public class HandleRequest {
    private String[] order;
    private ChatSocket chatSocket;
    private DAO dao;
    private String msg;

    HandleRequest(){}

    public void handle(String msg,ChatSocket cs){
        String buffer[] = msg.split("&");
        this.chatSocket = cs;
        this.order = buffer;
        dao = new DAO();
        matchOrder();
    }

    private void matchOrder(){
        switch (order[0]){
            case RevMsgForm.CreateRoom:
                String name = dao.queryPlayerName(chatSocket.IdToken);
                msg = RevMsgForm.CreateRoom + "&" + dao.createRoom(name);
                ChatManager.getInstance().publishToOne(chatSocket.IdToken,msg);    //给自己发送操作是否成功
                break;
            case RevMsgForm.QueryRoom:
                String roomlist = dao.queryRoom();
                if (roomlist != null) {
                    roomlist = roomlist.substring(4);
                    msg = RevMsgForm.QueryRoom + roomlist;
                }else {
                    msg = RevMsgForm.QueryRoom + "&" +"null";
                }
                ChatManager.getInstance().publishToOne(chatSocket.IdToken,msg);    //给自己发送创建房间的玩家名称
                break;
            case RevMsgForm.JoinRoom:
                String token = dao.queryPlayerToken(order[1]);
                if (dao.deleteRoom(order[1]).equals("true")) {
                    msg = RevMsgForm.JoinRoom + "&" + chatSocket.IdToken + "&" + order[2] + "&" +order[3] + "&" + order[4] + "&" +order[5] +"&" +order[6];
                    ChatManager.getInstance().publishToOne(token,msg); //给对方发送我的token，我的像素信息,我的胜场，总场,名字
                }
                else {
                    msg = RevMsgForm.JoinRoom + "&" +"false";
                    ChatManager.getInstance().publishToOne(chatSocket.IdToken,msg);   //给自己发送操作失败
                }
                break;
            case RevMsgForm.CreaterPix:
                msg = RevMsgForm.CreaterPix + "&" +chatSocket.IdToken + "&" + order[2] + "&" + order[3] + "&" + order[4] + "&" +order[5]+"&" +order[6];
                ChatManager.getInstance().publishToOne(order[1],msg);           //给加入房间的人发送自己的token，像素信息,我的胜场，总场,名字
                break;
            case RevMsgForm.LeaveRoom:
                String myname = dao.queryPlayerName(chatSocket.IdToken);
                dao.deleteRoom(myname);
                break;
            case RevMsgForm.PlayChess:
                msg = RevMsgForm.PlayChess + "&" + order[2] + "&" + order[3];
                ChatManager.getInstance().publishToOne(order[1],msg);           //给对手发送自己的坐标
                break;
            case RevMsgForm.LeaveGame:
                msg = RevMsgForm.LeaveGame + "&" + "true";
                ChatManager.getInstance().publishToOne(order[1],msg);           //给对手发送自己离开房间
                break;
            case RevMsgForm.WinGame:
                dao.updatePlayerInfo("win",order[1]);
                dao.updatePlayerInfo("total",order[1]);

                dao.updatePlayerInfo("total",order[2]);

                msg = RevMsgForm.WinGame + "&" +dao.queryPlayerInfo(order[1]) + "&" + dao.queryPlayerInfo(order[2]);
                ChatManager.getInstance().publishToOne(order[1],msg);             //给自己发送胜场 ，总场,对手的胜场总场。

                msg = RevMsgForm.WinGame + "&" +dao.queryPlayerInfo(order[2]) + "&" + dao.queryPlayerInfo(order[1]);
                ChatManager.getInstance().publishToOne(order[2],msg);
                break;
            case RevMsgForm.LoseGame:
                dao.updatePlayerInfo("total",order[1]);
                msg = RevMsgForm.LoseGame + "&" +dao.queryPlayerInfo(order[1]) + "&" + dao.queryPlayerInfo(order[2]);
                ChatManager.getInstance().publishToOne(order[1],msg);             //给自己发送胜场 ，总场
                break;
            case RevMsgForm.PlayAgain:
                msg = RevMsgForm.PlayAgain + "&" +"again";
                ChatManager.getInstance().publishToOne(order[1],msg);
                break;
            case RevMsgForm.AgreeOrRefuse:
                msg = RevMsgForm.AgreeOrRefuse + "&" + order[2];
                ChatManager.getInstance().publishToOne(order[1],msg);
                break;
            case RevMsgForm.CreatePlayer:
                if (dao.repetition(order[1]).equals("true"))
                    msg= RevMsgForm.CreatePlayer + "&" +"注册的用户名已存在";
                else if (dao.createPlayer(chatSocket.IdToken,order[1],order[2]).equals("true")) {
                    msg = RevMsgForm.CreatePlayer + "&" +"注册成功";
                }else msg = RevMsgForm.CreatePlayer + "&" +"注册失败";
                ChatManager.getInstance().publishToOne(chatSocket.IdToken,msg);    //给自己发送操作是否成功
                break;
            case RevMsgForm.PlayerLogin:
                if (dao.playerLogin(order[1],order[2]).equals("true")){
                    dao.updatePlayerToken(chatSocket.IdToken,order[1]);
                    msg = RevMsgForm.PlayerLogin + "&" +dao.queryPlayerInfo(chatSocket.IdToken);
                }else msg = RevMsgForm.PlayerLogin + "&" +"false";
                ChatManager.getInstance().publishToOne(chatSocket.IdToken,msg);         //给自己发送胜场 ，总场
                break;
            default:
                msg = RevMsgForm.ERROR + "&" + "error";
                ChatManager.getInstance().publishToOne(chatSocket.IdToken,msg);
                break;
        }
    }


}
