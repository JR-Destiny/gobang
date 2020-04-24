package mr.ncu.com.client;

public class PlayerInfo {
    private int win;
    private int total;
    private int percent;
    private String token;       //对手的token
    private String pixX;        //对手的像素X
    private String pixY;        //对手的像素Y

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPercent() {
        percent = (int)((float)win  /(float)total * 100);
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPixX() {
        return pixX;
    }

    public void setPixX(String pixX) {
        this.pixX = pixX;
    }

    public String getPixY() {
        return pixY;
    }

    public void setPixY(String pixY) {
        this.pixY = pixY;
    }
}
