package mr.ncu.com.client;

public class SendMsgForm {

    //创建房间:0个参数.
    public static final String CreateRoom = "000";
    //查询房间:0个参数
    public static final String QueryRoom = "001";
    //加入房间:6个参数.    1.该房间的拥有者   2.己方像素X     3.己方像素Y     4.己方胜场      5.己方总场      6.昵称
    public static final String JoinRoom = "002";
    //发送创建房间者的像素信息:6个参数.    1.对手的token      2..创建者像素X    3..创建者像素Y      4.创建者胜场      5.创建者总场       6.昵称
    public static final String CreaterPix = "003";
    //离开房间
    public static final String LeaveRoom = "004";
    //下棋:3个参数.        1.对手token     2.己方棋子x   3.己方棋子y
    public static final String PlayChess = "100";
    //离开:1参数           1.对手token
    public static final String LeaveGame ="101";
    //胜利:2参数            1.赢的人的token     2.对手token
    public static final String WinGame = "102";
    //失败:2参数            1.输的人token       2.对手token
    public static final String LoseGame = "103";
    //再来一局:1参数         1.对手token
    public static final String PlayAgain = "104";
    //同意或拒绝:2参数         1.对手token   2.同意或拒绝
    public static final String AgreeOrRefuse = "105";
    //创建玩家:2个参数.    1.昵称    2.密码
    public static final String CreatePlayer = "200";
    //玩家登录:2个参数     1.昵称    2.密码
    public static final String PlayerLogin = "201";

    public static final String ERROR = "404";

    SendMsgForm(){}

    public String createRoom (){
        String msg = CreateRoom   ;
        return msg;
    }

    public String queryRoom(){
        String msg = QueryRoom  ;
        return msg;
    }

    public String joinRoom(String playername ,String pixX ,String pixY,String win,String total,String name){
        String msg = JoinRoom + "&" + playername + "&" + pixX + "&" +pixY +"&"+ win +"&" +total + "&" +name;
        return msg;
    }

    public String createrPix(String token,String pixX ,String pixY,String win,String total,String name){
        String msg = CreaterPix +  "&" + token +"&" +pixX + "&" +pixY + "&" + win +"&" +total+ "&" +name;
        return msg;
    }

    public String playChess(String token ,String x,String y){
        String msg = PlayChess + "&" + token + "&" +x + "&" +y  ;
        return msg;
    }

    public String leaveGame(String token){
        String msg = LeaveGame + "&" + token  ;
        return msg;
    }

    public String winGame(String token ,String e_token){
        String msg = WinGame + "&" + token + "&" + e_token;
        return msg;
    }

    public String loseGame(String token,String e_token){
        String msg = LoseGame + "&" + token + "&" + e_token;
        return msg;
    }

    public String createPlayer(String name ,String psw){
        String msg = CreatePlayer + "&" +name+"&" +psw  ;
        return msg;
    }

    public String playerLogin(String name,String psw){
        String msg = PlayerLogin + "&" + name + "&" + psw ;
        return msg;
    }

    public String leaveRoom(){
        String msg = LeaveRoom;
        return msg;
    }

    public String playAgain(String token){
        String msg = PlayAgain + "&" + token;
        return msg;
    }

    public String agreeOrrefuse(String token , String tag){
        String msg = AgreeOrRefuse + "&" + token + "&" + tag;
        return msg;
    }
}
