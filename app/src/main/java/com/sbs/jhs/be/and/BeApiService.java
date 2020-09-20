package com.sbs.jhs.be.and;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BeApiService {
    @GET("/usr/article/getArticles")
    Observable<ResultData<BeApi__UsrArticle__getArticles__Body>> UsrArticle__getArticles(@Query("loginAuthKey") String loginAuthKey, @Query("boardId") int boardId);

    @GET("/usr/article/getArticle")
    Observable<ResultData<BeApi__UsrArticle__getArticle__Body>> UsrArticle__getArticle(@Query("loginAuthKey") String loginAuthKey, @Query("id") int id);

    @POST("/usr/article/doDeleteArticle")
    Observable<ResultData<Map<String, Object>>> UsrArticle__doDeleteArticle(@Query("loginAuthKey") String loginAuthKey, @Query("id") int id);

    @POST("/usr/article/doAddArticle")
    Observable<ResultData<Map<String, Object>>> UsrArticle__doAddArticle(@Query("loginAuthKey") String loginAuthKey, @Query("boardId") int boardId, @Query("title") String title, @Query("body") String body);

    @POST("/usr/article/doModifyArticle")
    Observable<ResultData<Map<String, Object>>> UsrArticle__doModifyArticle(@Query("loginAuthKey") String loginAuthKey, @Query("id") int id, @Query("title") String title, @Query("body") String body);

    @POST("/usr/member/doLogin")
    Observable<ResultData<Map<String, Object>>> UsrMember__doLogin(@Query("loginId") String loginId, @Query("loginPw") String loginPw);

    @POST("/usr/member/doJoin")
    Observable<ResultData<Map<String, Object>>> UsrMember__doJoin(@Query("loginId") String loginId, @Query("loginPw") String loginPw, @Query("name") String name, @Query("nickname") String nickname);
}
