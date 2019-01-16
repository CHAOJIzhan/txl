package com.txl.bin.txl.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.txl.bin.txl.MainActivity;
import com.txl.bin.txl.R;
import com.txl.bin.txl.db.UsersDb;

public class LoginActivity extends AppCompatActivity {

    EditText ed_name;
    EditText ed_password;
    TextView tv_login;
    TextView tv_regist;
    CheckBox cb_remerber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        isLogin();
        initView();
        initPassword();
        initClickListener();
        permmission();


    }

    private void isLogin() {    //读取 shareferPrefernce 判断是否已经登陆
        SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
        String login=preferences.getString("login","");
        if(!login.equals("")){  //已经登陆 直接跳转 mainactivity
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }

    }

    private void initPassword() {   //从sharedPrefrence读取账号密码
        SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
        String name=preferences.getString("name","");
        String password=preferences.getString("password","");
        ed_name.setText(name);
        ed_password.setText(password);
        if(!name.equals("")){
            cb_remerber.setChecked(true);
        }
    }


    private void initClickListener() {
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=ed_name.getText().toString();
                String password=ed_password.getText().toString();
                UsersDb usersDb=new UsersDb(LoginActivity.this);
                if(usersDb.isRight(name,password)){
                    //登陆成功 如果 勾选记住密码 就将 账号密码写入  prefrence
                    if(cb_remerber.isChecked()){
                        SharedPreferences preference=getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preference.edit();
                        editor.putString("name",name);
                        editor.putString("password",password);
                        editor.putString("login",name);
                        editor.commit();
                    }



                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);


                }else{
                    Toast.makeText(LoginActivity.this,"账号密码不正确 请重新输入账号密码",Toast.LENGTH_SHORT).show();
                    ed_name.setText("");
                    ed_password.setText("");
                }

            }
        });


        tv_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        ed_name=findViewById(R.id.ed_name);
        ed_password=findViewById(R.id.ed_password);
        tv_login=findViewById(R.id.tv_login);
        tv_regist=findViewById(R.id.tv_regist);
        cb_remerber=findViewById(R.id.cb_remmeber);
    }

    private void permmission() {    //暴力动态获取权限
        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[] { Manifest.permission.READ_CONTACTS ,Manifest.permission.WRITE_CONTACTS}, 1);

        }
    }
}
