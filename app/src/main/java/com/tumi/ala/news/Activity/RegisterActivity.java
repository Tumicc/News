package com.tumi.ala.news.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.tumi.ala.news.R;
import com.tumi.ala.news.Utils.ApplicationUtil;
import com.tumi.ala.news.Utils.MyDatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private static final int CHOSSE_PHOTO = 1;
    private MyDatabaseHelper dbHelper;
    private TextView save_user;
    private EditText username, userpassword, repassword;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new MyDatabaseHelper(this, "UserDB.db", null, 1);

        save_user = findViewById(R.id.save_user);

        username = findViewById(R.id.register_username);
        userpassword = findViewById(R.id.register_password);
        repassword = findViewById(R.id.register_repassword);
        checkBox = findViewById(R.id.checkbox_tiaokuan);


        save_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    String username_str = username.getText().toString();
                    String userpassword_str = userpassword.getText().toString();
                    String repassword_str = repassword.getText().toString();

                    if (userpassword_str.equals(repassword_str)) {
                        ContentValues values = new ContentValues();
                        //组装数据
                        values.put("name", username_str);
                        values.put("password", userpassword_str);

                        db.insert("User", null, values);

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                } else {
                    Toast.makeText(RegisterActivity.this, "请勾选同意使用条款", Toast.LENGTH_SHORT).show();
                }

            }
        });


        ApplicationUtil.getInstance().addActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOSSE_PHOTO);
    }

}
