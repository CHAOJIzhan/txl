package com.txl.bin.txl.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.txl.bin.txl.MainActivity;
import com.txl.bin.txl.R;

public class DetailActivity extends AppCompatActivity {

    TextView ed_name;
    EditText ed_phone;
    EditText ed_emai;
    TextView tv_save;
    TextView tv_delete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        initView();
        getExtra();
        initClickListner();

    }

    private void getExtra() {
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String phone=intent.getStringExtra("phone");
        String email=intent.getStringExtra("email");
        ed_name.setText(name);
        ed_phone.setText(phone);
        ed_emai.setText(email);
    }

    private void initClickListner() {
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed_name.getText().toString();
                //根据姓名求id
                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID},"display_name=?", new String[]{name}, null);
                if(cursor.moveToFirst()){
                    int id = cursor.getInt(0);
                    //根据id修改data中的相应数据
                    Uri u = Uri.parse("content://com.android.contacts/data");//对data表的所有数据操作
                    ContentResolver r = getContentResolver();
                    ContentValues values = new ContentValues();
                    String phone=ed_phone.getText().toString();
                    values.put("data1", phone);
                    r.update(u, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/phone_v2",id+""});

                    String email=ed_emai.getText().toString();
                    values.clear();
                    values.put("data1", email);
                    r.update(u, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/email_v2",id+""});


                }
                Intent intent=new Intent(DetailActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(DetailActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                finish();   //结束当前activity

            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed_name.getText().toString();
                //根据姓名求id
                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID},"display_name=?", new String[]{name}, null);
                if(cursor.moveToFirst()){
                    int id = cursor.getInt(0);
                    //根据id删除data中的相应数据
                    resolver.delete(uri, "display_name=?", new String[]{name});
                    uri = Uri.parse("content://com.android.contacts/data");
                    resolver.delete(uri, "raw_contact_id=?", new String[]{id+""});
                }
                Intent intent=new Intent(DetailActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(DetailActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                finish();   //结束当前activity
            }
        });


    }

    private void initView() {
        ed_name=findViewById(R.id.ed_name);
        ed_phone=findViewById(R.id.ed_phone);
        ed_emai=findViewById(R.id.ed_email);
        tv_delete=findViewById(R.id.tv_delete);
        tv_save=findViewById(R.id.tv_save);

    }


}
