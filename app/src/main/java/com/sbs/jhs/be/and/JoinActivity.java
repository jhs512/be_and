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

public class JoinActivity extends BaseActivity {

    private static final String TAG = "JoinActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private EditText editTextLoginId;
    private EditText editTextLoginPw;
    private EditText editTextLoginPwConfirm;
    private EditText editTextName;
    private EditText editTextNickname;
    private Button buttonDoJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        setTitle("회원가입");

        editTextLoginId = findViewById(R.id.activity_join__editTextLoginId);
        editTextLoginPw = findViewById(R.id.activity_join__editTextLoginPw);
        editTextLoginPwConfirm = findViewById(R.id.activity_join__editTextLoginPwConfirm);
        editTextName = findViewById(R.id.activity_join__editTextName);
        editTextNickname = findViewById(R.id.activity_join__editTextNickname);
        buttonDoJoin = findViewById(R.id.activity_join__buttonDoJoin);

        BeApiService beApiService = App.getBeApiService();

        buttonDoJoin.setOnClickListener(view -> {
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

            String loginPwConfirm = editTextLoginPwConfirm.getText().toString().trim();

            if (loginPwConfirm.length() == 0) {
                Toast.makeText(getApplicationContext(), "로그인 비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
                editTextLoginPwConfirm.requestFocus();

                return;
            }

            if (loginPw.equals(loginPwConfirm) == false) {
                Toast.makeText(getApplicationContext(), "로그인 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                editTextLoginPwConfirm.requestFocus();

                return;
            }

            String name = editTextName.getText().toString().trim();

            if (name.length() == 0) {
                Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                editTextName.requestFocus();

                return;
            }

            String nickname = editTextName.getText().toString().trim();

            if (nickname.length() == 0) {
                Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                editTextNickname.requestFocus();

                return;
            }

            Observable<ResultData<Map<String, Object>>> observable__UsrMember__doJoinResultData = beApiService.UsrMember__doJoin(loginId, loginPw, name, nickname);

            mCompositeDisposable.add(observable__UsrMember__doJoinResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                Toast.makeText(getApplicationContext(), resultData.msg, Toast.LENGTH_SHORT).show();

                if (resultData.isSuccess()) {
                    finish();
                }
            }, throwable -> {
                Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
                Log.e(TAG, throwable.getMessage(), throwable);
            }));

        });
    }
}