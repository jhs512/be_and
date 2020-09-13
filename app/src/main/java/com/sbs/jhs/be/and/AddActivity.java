package com.sbs.jhs.be.and;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private EditText editTextTitle;
    private EditText editTextBody;
    private Button buttonDoAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextTitle = findViewById(R.id.activity_add__editTextTitle);
        editTextBody = findViewById(R.id.activity_add__editTextBody);
        buttonDoAdd = findViewById(R.id.activity_add__buttonDoAdd);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.78.101.245:8085")
                .addConverterFactory(GsonConverterFactory.create()) // GSON 사용
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // RX Java 사용
                .build();

        BeApiService beApiService = retrofit.create(BeApiService.class);

        buttonDoAdd.setOnClickListener(view -> {
            int boardId = 1;
            String title = editTextTitle.getText().toString().trim();

            if ( title.length() == 0 ) {
                Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                editTextTitle.requestFocus();

                return;
            }

            String body = editTextBody.getText().toString().trim();

            if ( body.length() == 0 ) {
                Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                editTextBody.requestFocus();

                return;
            }

            Observable<ResultData> observable__UsrArticle__doAddArticleResultData = beApiService.UsrArticle__doAddArticle(boardId, title, body);

            mCompositeDisposable.add(observable__UsrArticle__doAddArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                Toast.makeText(getApplicationContext(), "글이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }, throwable -> {
                Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
                Log.e(TAG, throwable.getMessage(), throwable);
            }));

        });

        setTitle("게시물 추가");
    }
}