package com.sbs.jhs.be.and;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ModifyActivity extends BaseActivity {
    private static final String TAG = "ModifyActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private TextView textViewId;
    private TextView textViewRegDate;
    private EditText editTextTitle;
    private EditText editTextBody;
    private Button buttonDoModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        final int id = getIntent().getIntExtra("id", -1);

        setTitle(id + "번 게시물 수정");

        textViewId = findViewById(R.id.activity_modify__textViewId);
        textViewRegDate = findViewById(R.id.activity_modify__textViewRegDate);
        editTextTitle = findViewById(R.id.activity_modify__editTextTitle);
        editTextBody = findViewById(R.id.activity_modify__editTextBody);
        buttonDoModify = findViewById(R.id.activity_modify__buttonDoModify);

        BeApiService beApiService = App.getBeApiService();

        Observable<ResultData<BeApi__UsrArticle__getArticle__Body>> observable__UsrArticle__getArticleResultData = beApiService.UsrArticle__getArticle(App.getLoginAuthKey(), id);

        mCompositeDisposable.add(observable__UsrArticle__getArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
            Article article = resultData.body.article;

            textViewId.setText(article.getId() + "번");
            textViewRegDate.setText(article.getRegDate());
            editTextTitle.setText(article.getTitle());
            editTextBody.setText(article.getBody());

        }, throwable -> {
            Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
            Log.e(TAG, throwable.getMessage(), throwable);
        }));

        buttonDoModify.setOnClickListener(view -> {
            int boardId = 1;
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

            Observable<ResultData<Map<String, Object>>> observable__UsrArticle__doModifyArticleResultData = beApiService.UsrArticle__doModifyArticle(App.getLoginAuthKey(), id, title, body);

            mCompositeDisposable.add(observable__UsrArticle__doModifyArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                if (resultData.isSuccess()) {
                    int modifiedId = Util.getAsInt(resultData.body.get("id"));

                    Toast.makeText(getApplicationContext(), resultData.msg, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), resultData.msg, Toast.LENGTH_SHORT).show();
                }
            }, throwable -> {
                Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
                Log.e(TAG, throwable.getMessage(), throwable);
            }));

        });
    }
}