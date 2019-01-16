package com.txl.bin.txl.activity;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.txl.bin.txl.MainActivity;
import com.txl.bin.txl.R;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {

    EditText ed_name;
    EditText ed_phone;
    EditText ed_emai;
    TextView tv_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        initView();
        initClickListener();
        permmission();
    }

    private void permmission() {    //暴力动态获取权限
        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[] { Manifest.permission.READ_CONTACTS }, 1);

        }
    }

    private void initClickListener() {
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {        //获取系统联系人的contentProvider
                String name=ed_name.getText().toString();
                String phone=ed_phone.getText().toString();
                String email=ed_emai.getText().toString();

                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                ContentResolver resolver = getContentResolver();
                ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
                // 向raw_contact表添加一条记录
                //此处.withValue("account_name", null)一定要加，不然会抛NullPointerException
                ContentProviderOperation operation1 = ContentProviderOperation
                        .newInsert(uri).withValue("account_name", null).build();
                operations.add(operation1);
                // 向data添加数据
                uri = Uri.parse("content://com.android.contacts/data");
                //添加姓名
                ContentProviderOperation operation2 = ContentProviderOperation
                        .newInsert(uri).withValueBackReference("raw_contact_id", 0)
                        //withValueBackReference的第二个参数表示引用operations[0]的操作的返回id作为此值
                        .withValue("mimetype", "vnd.android.cursor.item/name")
                        .withValue("data1", name).build();
                operations.add(operation2);
                //添加手机数据
                ContentProviderOperation operation3 = ContentProviderOperation
                        .newInsert(uri).withValueBackReference("raw_contact_id", 0)
                        .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                        .withValue("data1",phone).build();
                operations.add(operation3);

                //添加email数据
                ContentProviderOperation operation4 = ContentProviderOperation
                        .newInsert(uri).withValueBackReference("raw_contact_id", 0)
                        .withValue("mimetype", "vnd.android.cursor.item/email_v2")
                        .withValue("data1",email).build();
                operations.add(operation4);
                try {
                    resolver.applyBatch("com.android.contacts", operations);
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                Toast.makeText(AddContactActivity.this,"添加联系人成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddContactActivity.this,MainActivity.class);
                startActivity(intent);
                finish();   //结束当前acitivity
            }
        });
    }

    private void initView() {
        ed_name=findViewById(R.id.ed_name);
        ed_phone=findViewById(R.id.ed_phone);
        ed_emai=findViewById(R.id.ed_email);
        tv_add=findViewById(R.id.tv_add);

    }
}




//    ContentValues values=new ContentValues();
//    //向 content_uri 插入空值 返回id
//    Uri rawContactUri=getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
//    long rawContactId=ContentUris.parseId(rawContactUri);
//    values.clear();
//    values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId);
//    values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
//    values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name);
//    getContentResolver().insert(ContactsContract.Data.CONTENT_URI,values);  //向联系人uri添加联系人的名字
//    values.clear();
//    //
//    values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId);
//    values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//    values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,phone);
//    values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
//    getContentResolver().insert(ContactsContract.Data.CONTENT_URI,values);  //向联系人电话号码uri添加电话号码
//    values.clear();
//
//    values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId);
//    values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
//    values.put(ContactsContract.CommonDataKinds.Email.DATA,email);
//    values.put(ContactsContract.CommonDataKinds.Email.TYPE,ContactsContract.CommonDataKinds.Email.TYPE_WORK);
//    getContentResolver().insert(ContactsContract.Data.CONTENT_URI,values);//向联系人email url 添加email数据

