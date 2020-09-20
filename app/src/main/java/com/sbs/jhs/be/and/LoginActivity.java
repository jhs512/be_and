package com.sbs.jhs.be.and;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private EditText editTextLoginId;
    private EditText editTextLoginPw;
    private Button buttonDoLogin;
    private Button buttonJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("로그인");

        if (App.isLogined()) {
            moveToList();
            return;
        }

        editTextLoginId = findViewById(R.id.activity_login__editTextLoginId);
        editTextLoginPw = findViewById(R.id.activity_login__editTextLoginPw);
        buttonDoLogin = findViewById(R.id.activity_login__buttonDoLogin);
        buttonJoin = findViewById(R.id.activity_login__buttonJoin);

        buttonJoin.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
            startActivity(intent);
        });

        BeApiService beApiService = App.getBeApiService();

        buttonDoLogin.setOnClickListener(view -> {
            String loginId = editTextLoginId.getText().toString().trim();

            if (loginId.length() == 0) {
                Toast.makeText(getApplicationContext(), "로그인 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                editTextLoginId.requestFocus();

                return;
            }

            String loginPw = editTextLoginPw.getText().toString().trim();

            if (loginPw.length() == 0) {
                Toast.makeText(getApplicationContext(), "로그인 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                editTextLoginPw.requestFocus();

                return;
            }

            Observable<ResultData<Map<String, Object>>> observable__UsrMember__doLoginResultData = beApiService.UsrMember__doLogin(loginId, loginPw);

            mCompositeDisposable.add(observable__UsrMember__doLoginResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                if (resultData.isSuccess()) {
                    String loginIdInResultDataBody = (String) resultData.body.get("loginId");
                    String authKey = (String) resultData.body.get("authKey");

                    Toast.makeText(getApplicationContext(), resultData.msg, Toast.LENGTH_SHORT).show();

                    App.login(loginIdInResultDataBody, authKey);

                    moveToList();
                } else {
                    Toast.makeText(getApplicationContext(), resultData.msg, Toast.LENGTH_SHORT).show();

                    if (resultData.resultCode.equals("F-1")) {
                        editTextLoginId.requestFocus();
                    } else if (resultData.resultCode.equals("F-2")) {
                        editTextLoginPw.requestFocus();
                    }
                }
            }, throwable -> {
                Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
                Log.e(TAG, throwable.getMessage(), throwable);
            }));

        });
    }

    private void moveToList() {
        Intent intent = new Intent(LoginActivity.this, ListActivity.class);
        intent.putExtra("boardId", 1);
        startActivity(intent);
    }
}