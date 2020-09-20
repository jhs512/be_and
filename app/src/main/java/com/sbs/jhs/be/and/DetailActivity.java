package com.sbs.jhs.be.and;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailActivity extends BaseActivity {
    private static final String TAG = "DetailActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private TextView textViewId;
    private TextView textViewRegDate;
    private TextView textViewTitle;
    private TextView textViewBody;
    private Button buttonDoDelete;
    private Button buttonModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final int id = getIntent().getIntExtra("id", -1);

        setTitle(id + "번 게시물");

        textViewId = findViewById(R.id.activity_detail__textViewId);
        textViewRegDate = findViewById(R.id.activity_detail__textViewRegDate);
        textViewTitle = findViewById(R.id.activity_detail__textViewTitle);
        textViewBody = findViewById(R.id.activity_detail__textViewBody);
        buttonDoDelete = findViewById(R.id.activity_detail__buttonDoDelete);
        buttonModify = findViewById(R.id.activity_detail__buttonModify);

        BeApiService beApiService = App.getBeApiService();

        buttonModify.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, ModifyActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        buttonDoDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(id + "번 글을 삭제합니다.").setMessage("정말 삭제하시겠습니까?");

            builder.setPositiveButton("예", (dialog, viewId) -> {
                Observable<ResultData<Map<String, Object>>> observable__UsrArticle__doDeleteArticleResultData = beApiService.UsrArticle__doDeleteArticle(App.getLoginAuthKey(), id);

                mCompositeDisposable.add(observable__UsrArticle__doDeleteArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                    if (resultData.isSuccess()) {
                        int deletedId = Util.getAsInt(resultData.body.get("id"));

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
            builder.setNegativeButton("아니오", (dialog, viewId) -> {
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        Observable<ResultData<BeApi__UsrArticle__getArticle__Body>> observable__UsrArticle__getArticleResultData = beApiService.UsrArticle__getArticle(App.getLoginAuthKey(), id);

        mCompositeDisposable.add(observable__UsrArticle__getArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
            Article article = resultData.body.article;

            textViewId.setText(article.getId() + "번");
            textViewRegDate.setText(article.getRegDate());
            textViewTitle.setText(article.getTitle());
            textViewBody.setText(article.getBody());

        }, throwable -> {
            Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
            Log.e(TAG, throwable.getMessage(), throwable);
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }
}