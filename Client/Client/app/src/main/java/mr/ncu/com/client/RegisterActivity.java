package mr.ncu.com.client;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static mr.ncu.com.client.MainActivity.net;

public class RegisterActivity extends AppCompatActivity {

    private ImageView Register_Confirm;
    private SendMsgForm smf;
    private EditText Register_Name;
    private EditText Register_Password;
    private String rev = "null";
    public static Handler RegisterHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegisterHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                rev = msg.obj.toString();
                String temp[] = rev.split("&");
                if (temp[1].equals("注册的用户名已存在")){
                    Toast.makeText(RegisterActivity.this,temp[1],Toast.LENGTH_SHORT).show();
                }else if (temp[1].equals("注册成功")){
                    Toast.makeText(RegisterActivity.this,temp[1],Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("relogin","false");
                    intent.setClass(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else if (temp[1].equals("注册失败")){
                    Toast.makeText(RegisterActivity.this,temp[1],Toast.LENGTH_SHORT).show();
                }
            }
        } ;

        smf = new SendMsgForm();
        Register_Name = (EditText) findViewById(R.id.Register_Name);
        Register_Password = (EditText) findViewById(R.id.Register_Password);
        Register_Confirm = (ImageView)findViewById(R.id.Register_Confirm);

        Register_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Register_Name.getText().toString();
                String psw = Register_Password.getText().toString();
                net.sendMsg(smf.createPlayer(name,psw));
            }
        });
    }
}
