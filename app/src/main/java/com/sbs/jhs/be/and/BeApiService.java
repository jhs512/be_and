package com.sbs.jhs.be.and;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface BeApiService {
    @GET("/usr/article/getArticles")
    Observable<ResultData<BeApi__UsrArticle__getArticles__Body>> UsrArticle__getArticles();
}
