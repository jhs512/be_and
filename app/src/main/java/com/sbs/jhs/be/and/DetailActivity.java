package com.sbs.jhs.be.and;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.BreakIterator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private TextView textViewId;
    private TextView textViewRegDate;
    private TextView textViewTitle;
    private TextView textViewBody;
    private Button buttonDoDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final int id = getIntent().getIntExtra("id", -1);

        textViewId = findViewById(R.id.activity_detail__textViewId);
        textViewRegDate = findViewById(R.id.activity_detail__textViewRegDate);
        textViewTitle = findViewById(R.id.activity_detail__textViewTitle);
        textViewBody = findViewById(R.id.activity_detail__textViewBody);
        buttonDoDelete = findViewById(R.id.activity_detail__buttonDoDelete);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.78.101.245:8085")
                .addConverterFactory(GsonConverterFactory.create()) // GSON 사용
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // RX Java 사용
                .build();

        BeApiService beApiService = retrofit.create(BeApiService.class);

        buttonDoDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(id + "번 글을 삭제합니다.").setMessage("정말 삭제하시겠습니까?");

            builder.setPositiveButton("예", (dialog, viewId) -> {
                Observable<ResultData> observable__UsrArticle__doDeleteArticleResultData = beApiService.UsrArticle__doDeleteArticle(id);

                mCompositeDisposable.add(observable__UsrArticle__doDeleteArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                    Toast.makeText(getApplicationContext(), id + "번 글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }, throwable -> {
                    Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, throwable.getMessage(), throwable);
                }));
            });
            builder.setNegativeButton("아니오", (dialog, viewId) -> {});

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        Observable<ResultData<BeApi__UsrArticle__getArticle__Body>> observable__UsrArticle__getArticleResultData = beApiService.UsrArticle__getArticle(id);

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