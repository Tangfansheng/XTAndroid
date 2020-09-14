package com.example.XTproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.XTproject.R;
import com.example.XTproject.base.BaseActivity;
import com.example.XTproject.model.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "login";
    private EditText _username;
    private EditText _password;
    private Button login;
    //查数据库
    private String queryLoginURL;
    private String userName;
    private String password;
    private String queryResult;

    //存储
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        _username = bindViewId(R.id.username);
        _password = bindViewId(R.id.password);
        login = bindViewId(R.id.login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userName = pref.getString("username", "");
        password = pref.getString("password", "");
        _username.setText(userName);
        _password.setText(password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = _username.getText().toString();
                password = _password.getText().toString();
                new LoginTask(LoginActivity.this, userName, password).execute();
            }
        });
    }

    @Override
    protected void initData() {

    }


    class LoginTask extends AsyncTask<Void, String, Boolean> {
        private Context context;
        private String password;
        private String username;
        User user;

        public LoginTask(Context context, String username, String password) {
            this.context = context;
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            queryLoginURL = "http://120.26.187.166:8080/XTBridge/check?username=" + userName;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            final Request request = new Request.Builder().url(queryLoginURL).get().build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                queryResult = response.body().string();
                Log.d(TAG, queryResult);
                user = JSON.parseObject(queryResult, User.class);
                if(user!=null && user.getPassword().equals(password)){
                    editor= pref.edit();
                    editor.putString("username", this.username);
                    editor.putString("password", this.password);
                    editor.apply();
                    return true;
                }else{
                    if(user == null){
                        Log.d(TAG, "user = null");
                    }else{
                        Log.d(TAG, "password wrong");
                    }
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean flag) {
            if(flag){
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("permission", user.getPermission());
                context.startActivity(intent);
            }else{
                Toast.makeText(LoginActivity.this, "登录失败，请重新登录", Toast.LENGTH_SHORT).show();
            }
        }





    }
}