package com.txl.bin.txl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.txl.bin.txl.R;
import com.txl.bin.txl.db.UsersDb;

public class RegistActivity extends AppCompatActivity {

    EditText ed_name;
    EditText ed_password;
    EditText ed_sure_password;
    TextView tv_regist;
    TextView tv_quit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
        initView();
        initClickListner();

    }

    private void initView() {
        ed_name=(EditText)findViewById(R.id.ed_name);
        ed_password=(EditText)findViewById(R.id.ed_password);
        ed_sure_password=(EditText)findViewById(R.id.ed_sure_password);
        tv_regist=(TextView)findViewById(R.id.tv_regist);
        tv_quit=(TextView)findViewById(R.id.tv_quit);
    }

    private void initClickListner() {
        tv_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=ed_name.getText().toString();
                String password=ed_password.getText().toString();
                String surePassword=ed_sure_password.getText().toString();
                UsersDb usersDb=new UsersDb(RegistActivity.this);
                if(!password.equals(surePassword)){  //密码不相等
                    Toast.makeText(RegistActivity.this,"两次密码不相等请重新输入",Toast.LENGTH_SHORT).show();
                    ed_password.setText("");
                    ed_sure_password.setText("");
                }else if(usersDb.isExit("name")){
                    Toast.makeText(RegistActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                }else{
                    usersDb.insertUser(name,password);
                    Intent intent=new Intent(RegistActivity.this,LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        tv_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
