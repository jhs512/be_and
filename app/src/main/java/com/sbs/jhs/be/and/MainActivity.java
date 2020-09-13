package com.sbs.jhs.be.and;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private List<Article> articles;
    private RecyclerView1Adapter recyclerView1Adapter;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("게시물 리스트");

        btnAdd = findViewById(R.id.activity_main__btnAdd);
        btnAdd.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddActivity.class));
        });

        articles = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.78.101.245:8085")
                .addConverterFactory(GsonConverterFactory.create()) // GSON 사용
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // RX Java 사용
                .build();

        BeApiService beApiService = retrofit.create(BeApiService.class);
        Observable<ResultData<BeApi__UsrArticle__getArticles__Body>> observable__UsrArticle__getArticlesResultData = beApiService.UsrArticle__getArticles();

        mCompositeDisposable.add(observable__UsrArticle__getArticlesResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
            for (Article article : resultData.body.articles) {
                articles.add(article);
            }

            recyclerView1Adapter.notifyDataSetChanged();
        }, throwable -> {
            Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
            Log.e(TAG, throwable.getMessage(), throwable);
        }));

        RecyclerView recyclerView1 = findViewById(R.id.activity_main__recyclerView1);

        recyclerView1.setHasFixedSize(true);

        recyclerView1Adapter = new RecyclerView1Adapter();
        recyclerView1.setAdapter(recyclerView1Adapter);
    }

    private class RecyclerView1Adapter extends RecyclerView.Adapter<RecyclerView1Adapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_article, parent, false);

            return new ViewHolder(view);
        }

        // 배우와 역할이 만나서 배역이 되는 순간
        // 즉 재분장이 일어날 때
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // position 은 배역번호이다.
            Article Article = articles.get(position);

            // 빠른 재분장을 위해, 뷰 홀더에 꾸며야 하는 부분을 정리해 놓았다.
            holder.textViewId.setText(Article.getId() + "번");
            holder.textViewTitle.setText(Article.getTitle());
            holder.buttonToGoDetail.setTag(Article.getId());
        }

        @Override
        public int getItemCount() {
            return articles.size();
        }

        // 뷰 홀더 클래스
        // 여기서 static을 빼면 RecyclerView1Adapter 객체 내부의 속성에 접근할 수 있다.
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewId;
            public TextView textViewTitle;
            public Button buttonToGoDetail;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewId = itemView.findViewById(R.id.item_article__textViewId);
                textViewTitle = itemView.findViewById(R.id.item_article__textViewTitle);
                buttonToGoDetail = itemView.findViewById(R.id.item_article__buttonToGoDetail);

                buttonToGoDetail.setOnClickListener(view -> {
                    int id = (int) view.getTag();

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }
}