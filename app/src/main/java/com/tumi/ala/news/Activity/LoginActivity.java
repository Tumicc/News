package com.tumi.ala.news.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.tumi.ala.news.R;
import com.tumi.ala.news.Utils.*;

public class LoginActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private Button check_user;
    private EditText username, userpassword;
    private ImageView login_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        dbHelper = new MyDatabaseHelper(this, "UserDB.db", null, 1);

        check_user = findViewById(R.id.check_user);
        username = findViewById(R.id.login_username);
        userpassword = findViewById(R.id.login_password);
        login_head = findViewById(R.id.login_head);

        check_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String username_str = username.getText().toString();
                String userpassword_str = userpassword.getText().toString();

                Cursor cursor = db.rawQuery("select * from User where name=?", new String[]{username_str});
                if (cursor.getCount() == 0) {
                    Toast.makeText(LoginActivity.this, "用户名不存在！", Toast.LENGTH_SHORT).show();
                } else {
                    if (cursor.moveToFirst()) {
                        String userpassword_db = cursor.getString(cursor.getColumnIndex("password"));
                        if (userpassword_str.equals(userpassword_db)) {
                            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.IS_LOGIN, true);
                            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.LOGIN_DATA, username_str);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "密码错误，请重新登录", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                cursor.close();
                db.close();
            }
        });
        ApplicationUtil.getInstance().addActivity(this);
    }
}
