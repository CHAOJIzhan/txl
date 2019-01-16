package com.txl.bin.txl;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.txl.bin.txl.activity.AddContactActivity;
import com.txl.bin.txl.activity.LoginActivity;
import com.txl.bin.txl.adapater.RecyclerviewAdapter;
import com.txl.bin.txl.bean.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv_quit;
    TextView tv_add;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//        startActivity(intent);
        initView();
        getContnet();
        iniClickListener();
    }

    private void iniClickListener() {
        tv_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除login 标值 并且跳转 loginactivity
                SharedPreferences preference=getSharedPreferences("user",MODE_PRIVATE);
                SharedPreferences.Editor editor=preference.edit();
                editor.remove("login");
                editor.commit();
                finish();
//                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent);
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddContactActivity.class);
                startActivity(intent);
                finish();   //结束当前activity

            }
        });
    }

    private void getContnet() {

        List<Contact> contactsList=new ArrayList<Contact>();

        Uri uri = Uri.parse("content://com.android.contacts/contacts"); //访问raw_contacts表
        ContentResolver resolver = getContentResolver();
        //获得_id属性
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID}, null, null, null);
        while(cursor.moveToNext()){
            Contact contact = new Contact();
            //获得id并且在data中寻找数据
            int id = cursor.getInt(0);
            uri = Uri.parse("content://com.android.contacts/contacts/"+id+"/data");
            //data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
            Cursor cursor2 = resolver.query(uri, new String[]{ContactsContract.Data.DATA1,ContactsContract.Data.MIMETYPE}, null,null, null);
            while(cursor2.moveToNext()){
                String data = cursor2.getString(cursor2.getColumnIndex("data1"));
                if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")){       //如果是名字
                   contact.setName(data);
                }
                else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")){  //如果是电话
                    contact.setPhone(data);
                }
                else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/email_v2")){  //如果是email
                    contact.setEmail(data);
                }
            }
            contactsList.add(contact);
        }

        RecyclerviewAdapter adapter=new RecyclerviewAdapter(this,contactsList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }

    private void initView() {
        tv_quit=findViewById(R.id.tv_quit);
        tv_add=findViewById(R.id.tv_add);
        rv=findViewById(R.id.rv);
    }
}
