public class RevMsgForm {

    //创建房间:0个参数.
    public static final String CreateRoom = "000";

    //查询房间:0个参数
    public static final String QueryRoom = "001";

    //加入房间:6个参数.    1.该房间的拥有者   2.己方像素X     3.己方像素Y     4.己方胜场      5.己方总场        6.名字
    public static final String JoinRoom = "002";

    //发送创建房间者的像素信息:6个参数.    1.对手的token      2..创建者像素X    3..创建者像素Y      4.创建者胜场      5.创建者总场    6.名字
    public static final String CreaterPix = "003";

    //离开房间
    public static final String LeaveRoom = "004";

    //下棋:3个参数.        1.对手token     2.己方棋子x   3.己方棋子y
    public static final String PlayChess = "100";

    //投降:1参数           1.输的人的token
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
}
