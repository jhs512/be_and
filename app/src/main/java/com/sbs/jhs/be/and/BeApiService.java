package com.sbs.jhs.be.and;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BeApiService {
    @GET("/usr/article/getArticles")
    Observable<ResultData<BeApi__UsrArticle__getArticles__Body>> UsrArticle__getArticles();

    @GET("/usr/article/getArticle")
    Observable<ResultData<BeApi__UsrArticle__getArticle__Body>> UsrArticle__getArticle(@Query("id") int id);

    @POST("/usr/article/doDeleteArticle")
    Observable<ResultData<Map<String, Object>>> UsrArticle__doDeleteArticle(@Query("id") int id);

    @POST("/usr/article/doAddArticle")
    Observable<ResultData<Map<String, Object>>> UsrArticle__doAddArticle(@Query("boardId") int boardId, @Query("title") String title, @Query("body") String body);
}
