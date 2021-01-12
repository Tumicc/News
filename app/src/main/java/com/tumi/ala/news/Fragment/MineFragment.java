package com.tumi.ala.news.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.tumi.ala.news.Activity.EditMineActivity;
import com.tumi.ala.news.Activity.LoginOrRegisterActivity;
import com.tumi.ala.news.R;
import com.tumi.ala.news.Utils.MyDatabaseHelper;
import com.tumi.ala.news.Utils.SharedPreUtil;


public class MineFragment extends Fragment implements View.OnClickListener {
    private static final int CHOSSE_PHOTO = 1;
    private View view;
    private TextView edit_mine, exit_login, mine_user_name;
    private ImageView my_head;
    private MyDatabaseHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();

        helper = new MyDatabaseHelper(getContext(), "UserDB.db", null, 1);
        String username = (String) SharedPreUtil.getParam(getContext(), SharedPreUtil.LOGIN_DATA, "");
        mine_user_name.setText(username);

        edit_mine.setOnClickListener(this);

        exit_login.setOnClickListener(this);

        my_head.setOnClickListener(this);

        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getContext(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initView() {
        mine_user_name = view.findViewById(R.id.mine_user_name);
        edit_mine = view.findViewById(R.id.edit_mine);
        exit_login = view.findViewById(R.id.exit_login);
        my_head = view.findViewById(R.id.my_head);
    }


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOSSE_PHOTO);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_mine: {
                Intent intent = new Intent(getContext(), EditMineActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.exit_login: {
                SharedPreUtil.setParam(getContext(), SharedPreUtil.IS_LOGIN, false);
                SharedPreUtil.removeParam(getContext(), SharedPreUtil.LOGIN_DATA);

                //重新跳转到登录页面
                Intent intent = new Intent(getContext(), LoginOrRegisterActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            break;
            case R.id.my_head: {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
            break;
            default:
                break;

        }
    }
}
