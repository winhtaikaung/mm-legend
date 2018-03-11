package com.winhtaikaung.devweekly

import android.app.Application
import android.util.Log
import com.winhtaikaung.devweekly.repository.api.ArticleApi
import com.winhtaikaung.devweekly.repository.api.IssueApi
import com.winhtaikaung.devweekly.repository.api.SourceApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class App : Application() {
    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var sourceApi: SourceApi
        private lateinit var issueApi: IssueApi
        private lateinit var articleApi: ArticleApi


    }

    private var disposable: Disposable? = null
    private var sourceApi: SourceApi? = null

    override fun onCreate() {
        super.onCreate()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        // TODO Dependencies Injection should be replaced with existing code
        retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(" https://92694f5c.ap.ngrok.io/")
                .build()

        sourceApi = retrofit.create(SourceApi::class.java)
        issueApi = retrofit.create(IssueApi::class.java)
        articleApi = retrofit.create(ArticleApi::class.java)
        val sourceId = "f80e8357ee7e4b9093762d3b8e75497e"
        getSource(sourceId)


    }

    //TODO the following method will be placed in Repository
    private fun getSource(sourceId: String) {
        val graphql = "{\n" +
                "  source(sourceId: \"" + sourceId + "\") {\n" +
                "    id\n" +
                "    objectId\n" +
                "    tag\n" +
                "    img\n" +
                "    name\n" +
                "    baseUrl\n" +
                "    createdDate\n" +
                "    updatedDate\n" +
                "  }\n" +
                "}"
        disposable = sourceApi?.getSource(graphql)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ result ->
                    Log.e("result",
                            result.toString())
                }, { error -> error.message })
    }

    private fun getIssue(issueId: String) {
        var graphql = "{\n" +
                "  issue(issueId:\"" + issueId + "\"){\n" +
                "    id\n" +
                "    objectId\n" +
                "    url\n" +
                "    issueNumber\n" +
                "    sourceId\n" +
                "    createdDate\n" +
                "    updatedDate\n" +
                "  }\n" +
                "}"
        disposable = issueApi?.getIssue(graphql)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ result ->
                    Log.e("result",
                            result.toString())
                }, { error -> error.message })
    }

    private fun getArticle(articleId: String) {
        var graphql = "{\n" +
                "  article(articleId:\"" + articleId + "\"){\n" +
                "    id\n" +
                "    objectId\n" +
                "    url\n" +
                "    img\n" +
                "    mainUrl\n" +
                "    title\n" +
                "    preContent\n" +
                "    sourceId\n" +
                "    issueId\n" +
                "    createdDate\n" +
                "    updatedDate\n" +
                "  }\n" +
                "}"
        disposable = articleApi?.getArticle(graphql)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ result ->
                    Log.e("result",
                            result.toString())
                }, { error -> error.message })
    }


}
