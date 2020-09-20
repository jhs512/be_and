package com.sbs.jhs.be.and;

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

public class AddActivity extends BaseActivity {
    private static final String TAG = "AddActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private EditText editTextTitle;
    private EditText editTextBody;
    private Button buttonDoAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setTitle("게시물 추가");

        editTextTitle = findViewById(R.id.activity_add__editTextTitle);
        editTextBody = findViewById(R.id.activity_add__editTextBody);
        buttonDoAdd = findViewById(R.id.activity_add__buttonDoAdd);

        final int boardId = getIntent().getIntExtra("boardId", 0);

        BeApiService beApiService = App.getBeApiService();

        buttonDoAdd.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString().trim();

            if (title.length() == 0) {
                Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                editTextTitle.requestFocus();

                return;
            }

            String body = editTextBody.getText().toString().trim();

            if (body.length() == 0) {
                Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                editTextBody.requestFocus();

                return;
            }

            Observable<ResultData<Map<String, Object>>> observable__UsrArticle__doAddArticleResultData = beApiService.UsrArticle__doAddArticle(App.getLoginAuthKey(), boardId, title, body);

            mCompositeDisposable.add(observable__UsrArticle__doAddArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                int id = Util.getAsInt(resultData.body.get("id"));

                Toast.makeText(getApplicationContext(), id + "번 글이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddActivity.this, ListActivity.class);
                intent.putExtra("boardId", boardId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }, throwable -> {
                Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
                Log.e(TAG, throwable.getMessage(), throwable);
            }));

        });
    }
}