package mr.ncu.com.client;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class GameChess extends View  {
    private int myPanelWidth ;        //棋盘宽度
    private float myLineHeight;    //行宽
    private int maxLine = 12;        //行数

    private Paint myPaint;         //画笔
    private Bitmap myWhitePice;    //白棋子
    private Bitmap myBlackPice;    //黑棋子
    private float ratioPieceOfLineHight = 3 * 1.0f / 4;  //棋子为行宽的3/4；
    private boolean isGemOver=false;        //游戏结束
    public static int WHITE_WIN = 0;  //胜利为白方标志
    public static int BLACK_WIN = 1;  //胜利为黑方标志
    private boolean isWhite = true;  //判断是否是白棋先手，或当前为白棋下子

    private boolean whitewin=false;

    private List<Point> myWhiteArray = new ArrayList<Point>();  //白棋子位置信息
    private List<Point> myBlackArray = new ArrayList<Point>();  //黑棋子位置信息
    //游戏ai
    private AI ai;
    private Handler mHandlerUI ;
    private onGameListener onGameListener;  //回调接口
    private int mUnder;        //dialog的Y坐标
    private int[][] chessArray;
    private boolean isUserBout=true;
    private static SoundManager AI_service = null;
    private static SoundManager.MyBinder AI_manager = null;
    public GameChess(Context context) {
        this(context, null);
    }
    public GameChess(Context context ,AttributeSet attributeSet){            //构造函数
        super(context , attributeSet);
        init();
    }
    // 用于回调的接口
    public interface onGameListener {
        void onGameOver(int i);
    }
    //自定义接口，用于显示dialog
    public void setOnGameListener(GameChess.onGameListener onGameListener) {
        this.onGameListener = onGameListener;
    }
    //初始化函数
    private void init() {
        chessArray=new int[12][12];
        for(int i=0;i<12;i++){
            for(int j=0;j<12;j++){
                chessArray[i][j]=0;
            }
        }
        ai = new AI(chessArray);
        mHandlerUI=new Handler() {
            public void handleMessage(Message msg) {
                if(!isGemOver)
                {
                    isUserBout = true;
                    Point p=new Point(ai.getPos().getX(),ai.getPos().getY());
                    myBlackArray.add(p);
                    AI_manager.PlaySound();
                    invalidate();
                }
            }
        };
        ai.setAICallBack(new AI.AICallBack(){
            @Override
            public void aiAtTheBell() {
                Message msg = new Message();
                mHandlerUI.sendMessage(msg);
            }
        });
        myPaint = new Paint();
        myPaint.setColor(0x6E6E6E);     //给画笔设置颜色
        myPaint.setAntiAlias(true);      //设置画笔是否使用抗锯齿
        myPaint.setDither(true);            //设置画笔是否防抖动
        myPaint.setAlpha(120);
        myPaint.setStyle(Paint.Style.STROKE);        //设置画笔样式，这里使用描边
        myWhitePice = BitmapFactory.decodeResource(getResources(),R.drawable.whitechess); //设置棋子图片
        myBlackPice = BitmapFactory.decodeResource(getResources(), R.drawable.blackchess);
    }
    //触发事件
    public boolean onTouchEvent(MotionEvent event){
        if (isGemOver||!isUserBout) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {   //判断触摸动作，ACTION_UP为单点触摸离开
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getVaLidPoint(x,y);

            if (myWhiteArray.contains(p)|| myBlackArray.contains(p)) {
                return false;
            }
            if (isWhite) {
                myWhiteArray.add(p);
                chessArray[p.x][p.y]=1;
                ai.setChessArray(chessArray);
                isUserBout=false;
            }else {
                myBlackArray.add(p);
            }
            if(!isGemOver) {
                ai.aiBout();
            }
            AI_manager.PlaySound();
            invalidate();         //invalidate()是用来刷新View的，必须在UI线程中使用
            //isWhite = !isWhite;
        }
        return true;
    }

    public void SetMusicManager(SoundManager.MyBinder mManager){
        AI_manager = mManager;
        AI_service = AI_manager.getService();
        AI_service.InitSound();
    }

    protected Point getVaLidPoint(int x , int y){
        return new Point((int)(x/myLineHeight),(int)(y/myLineHeight));
    }
    //计算布局大小
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {            //MeasureSpec.UNSPECIFIED表示未知大小
            width = heightSize;
        }else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    protected void onSizeChanged(int w, int h ,int oldw , int oldh) {         //当View大小发生改变的时候会被系统自动回调
        super.onSizeChanged(w, h, oldw, oldh);
        myPanelWidth = w;
        myLineHeight = myPanelWidth*1.0f/maxLine;
        mUnder = h - (h - myPanelWidth) / 2;
        int pieceWidth = (int) (myLineHeight*ratioPieceOfLineHight);  //棋子大小占行宽的3/4
        myWhitePice = Bitmap.createScaledBitmap(myWhitePice, pieceWidth, pieceWidth, false);    //以src为原图，创建新的图像，指定新图像的高宽以及是否可变。
        myBlackPice = Bitmap.createScaledBitmap(myBlackPice, pieceWidth, pieceWidth, false);
    }

    protected void  onDraw(Canvas canvas) {       //Canvas类相当于一块画布
        super.onDraw(canvas);
        drawBroad(canvas);
        drawPiece(canvas);
        checkGameOver();
    }

    public void child(Canvas canvas){
        drawBroad(canvas);
        drawPiece(canvas);
        checkGameOver();
    }

    private void drawBroad(Canvas canvas){            //画出棋盘线
        canvas.drawColor(Color.parseColor("#f8d7b4"));
        int w = myPanelWidth;
        float lineHeight = myLineHeight;
        int startX = (int) (lineHeight/2);            //棋盘线起始X坐标
        int endX = (int)(w-lineHeight/2);            //棋盘终止X坐标
        myPaint.setStrokeWidth(5);
        canvas.drawLine(startX, lineHeight/2, endX, lineHeight/2, myPaint);
        canvas.drawLine(lineHeight/2, startX, lineHeight/2, endX, myPaint);
        for(int i = 0; i< maxLine; i++){
            int y = (int)((i+1.5)*lineHeight);        //y坐标
            if(i<maxLine-2){
                myPaint.setStrokeWidth(2);
                canvas.drawLine(startX, y, endX, y, myPaint);        //画棋盘横向线
                canvas.drawLine(y, startX, y, endX, myPaint);
            }else {
                myPaint.setStrokeWidth(5);
                canvas.drawLine(startX, y, endX, y,myPaint);        //画棋盘横向线
                canvas.drawLine(y,startX,y,endX,myPaint);       //画棋盘纵向线
            }
        }
    }
    //画棋子
    private void drawPiece(Canvas canvas) {
        int n1 = myWhiteArray.size();
        int n2 = myBlackArray.size();
        for(int i =0; i< n1 ;i++){
            Point whitePoint = myWhiteArray.get(i);
            canvas.drawBitmap(myWhitePice, (whitePoint.x+(1-ratioPieceOfLineHight)/2)*myLineHeight,
                    (whitePoint.y+(1-ratioPieceOfLineHight)/2)*myLineHeight, null);
            //drawBitmap(Bitmap bitmap, float left, float top, Paint paint);Bitmap：图片对象，left:偏移左边的位置，top： 偏移顶部的位置
        }
        for(int i =0; i< n2 ;i++){
            Point blackPoint = myBlackArray.get(i);
            canvas.drawBitmap(myBlackPice, (blackPoint.x+(1-ratioPieceOfLineHight)/2)*myLineHeight,
                    (blackPoint.y+(1-ratioPieceOfLineHight)/2)*myLineHeight, null);
        }
    }

    //检测游戏是否结束
    private void checkGameOver(){
        boolean whiteWin = checkFiveInLine(myWhiteArray);
        boolean blackWin = checkFiveInLine(myBlackArray);
        if (whiteWin || blackWin) {
            isGemOver = true;
            whitewin=whiteWin;
            Log.i("已执行","游戏结束Gamechess");
            if (onGameListener != null) {
                onGameListener.onGameOver(whiteWin ? WHITE_WIN : BLACK_WIN);
            }
        }
    }
    //回调一个int数据用于设置Dialog的位置
    public int getUnder() {
        return mUnder;
    }
    //检测是否存在五棋子相连的情况
    private boolean checkFiveInLine(List<Point> myArray){
        for(Point p : myArray){
            int x = p.x;
            int y = p.y;
            boolean win_flag =                             //判断是否存在五子相连情况
                    checkHorizontal(x , y ,myArray)||checkVertical(x,y,myArray)
                            ||checkLeftDiagonal(x,y,myArray)||checkRightDiagonal(x,y,myArray);
            if (win_flag) {
                return true;
            }
        }
        return false;
    }
    //横向检查是否满足五子相连
    private boolean checkHorizontal(int x ,int y ,List<Point> myArray){
        int count = 1;
        for(int i = 1;i < 5; i++){
            if (myArray.contains(new Point(x+i,y))) {
                count++;
            }else {
                break;
            }
        }
        if (count == 5) {
            return true;
        }
        for(int i = 1;i < 5; i++){
            if (myArray.contains(new Point(x-i,y))) {
                count++;
            }else {
                break;
            }
            if (count == 5) {
                return true;
            }
        }
        return false;
    }
    //纵向检查是否满足五子相连
    private boolean checkVertical(int x ,int y ,List<Point> myArray){
        int count = 1;
        for(int i = 1;i < 5; i++){
            if (myArray.contains(new Point(x,y+i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == 5) {
            return true;
        }
        for(int i = 1;i < 5; i++){
            if (myArray.contains(new Point(x,y-i))) {
                count++;
            }else {
                break;
            }
            if (count == 5) {
                return true;
            }
        }
        return false;
    }
    //左斜向检查是否满足五子相连
    private boolean checkLeftDiagonal(int x ,int y ,List<Point> myArray){
        int count = 1;
        for(int i = 1;i < 5; i++){
            if (myArray.contains(new Point(x-i,y+i))) {
                count++;
            }else {
                break;
            }

        }
        if (count == 5) {
            return true;
        }
        for(int i = 1;i < 5; i++){
            if (myArray.contains(new Point(x+i,y-i))) {
                count++;
            }else {
                break;
            }
            if (count == 5) {
                return true;
            }
        }
        return false;
    }
    //右斜向检查是否满足五子相连
    private boolean checkRightDiagonal(int x ,int y ,List<Point> myArray){
        int count = 1;
        for(int i = 1;i < 5; i++){            //切记，i = 1 开始，否则就会只检测到三个子相连就结束了
            if (myArray.contains(new Point(x-i,y-i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == 5) {
            return true;
        }
        for(int i = 1;i < 5; i++){
            if (myArray.contains(new Point(x+i,y+i))) {
                count++;
            }else {
                break;
            }
            if (count == 5) {
                return true;
            }
        }
        return false;
    }
    public void regret() {
        if (myBlackArray.size() > 0 || myWhiteArray.size() > 0) {
                int bx=myBlackArray.get(myBlackArray.size() - 1).x;
                int by=myBlackArray.get(myBlackArray.size() - 1).y;
                int wx=myWhiteArray.get(myWhiteArray.size() - 1).x;
                int wy=myWhiteArray.get(myWhiteArray.size() - 1).y;
                chessArray[bx][by]=0;
                chessArray[wx][wy]=0;
                myBlackArray.remove(myBlackArray.size() - 1);
                myWhiteArray.remove(myWhiteArray.size() - 1);
            }
            invalidate();
        }
    //重新开始游戏
    public void restartGame(){
        myWhiteArray.clear();
        myBlackArray.clear();
        isGemOver = false;
        isWhite = true;
        for (int i = 0; i < maxLine; i++) {
            for (int j = 0; j < maxLine; j++) {
                chessArray[i][j] = 0;
            }
        }
        invalidate();
    }


    public void setMyWhiteArray(List<Point> myWhiteArray) {
        this.myWhiteArray = myWhiteArray;
    }


    public void setMyBlackArray(List<Point> myBlackArray) {
        this.myBlackArray = myBlackArray;
    }

    public boolean isGemOver() {
        return isGemOver;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isWhitewin() { return whitewin; }
}
