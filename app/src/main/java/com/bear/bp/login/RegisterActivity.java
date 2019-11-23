package com.bear.bp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bear.bp.R;
import com.bear.bp.util.LoginRegisterServer;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText newUssername;  // 用户名
    private EditText newPassword;   // 密码
    private EditText againNewPassword;  //再次输入的密码

    private ImageView back;        // 返回
    private Button register;    // 注册

    private String url = "http://182.92.159.2/LoginDemo/AndroidTestDemo/RegisterServlet?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        initView();
        setListener();
    }

    // 绑定控件
    private void initView(){
        newUssername = findViewById(R.id.new_username);
        newPassword = findViewById(R.id.new_password);
        againNewPassword = findViewById(R.id.again_new_password);

        back = findViewById(R.id.back_login);
        register = findViewById(R.id.check_register);
    }
    // 设置监听事件
    private void setListener(){
        back.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_login:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.check_register:
                // 进行注册验证，成功则返回登录界面
                String inputUsername = newUssername.getText().toString();
                String inputPassword = newPassword.getText().toString();
                String againInputPassword = againNewPassword.getText().toString();
                if (TextUtils.isEmpty(inputUsername)){
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(inputPassword)){
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else if (!TextUtils.equals(inputPassword, againInputPassword)){
                    Toast.makeText(this, "输入的密码不一致", Toast.LENGTH_SHORT).show();
                }else {
                    LoginRegisterServer.postRequest(inputUsername, inputPassword, url,
                            RegisterActivity.this, 2);
                }
                break;
            default:
                break;
        }
    }
}
