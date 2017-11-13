//package com.linheimx.zimudog.m.net
//
//import com.linheimx.lspider.ParserManager
//import com.linheimx.lspider.god.GodPage
//import com.linheimx.lspider.zimuku.bean.Zimu
//import com.linheimx.zimudog.m.net.api.ShooterApi
//import com.linheimx.zimudog.m.net.api.ZimukuApi
//
//import java.io.IOException
//import java.util.concurrent.TimeUnit
//
//import io.reactivex.Observable
//import io.reactivex.schedulers.Schedulers
//import okhttp3.Interceptor
//import okhttp3.OkHttpClient
//import okhttp3.Response
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import retrofit2.converter.gson.GsonConverterFactory
//
///**
// * Created by x1c on 2017/5/1.
// */
//
//class ApiManager2222 private constructor() {
//
//
//    internal var _ZimukuApi: ZimukuApi? = null
//
//
//    internal var _ShooterApi: ShooterApi? = null
//
//    init {
//
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
//
//        _Client = OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .addInterceptor(HeaderInterceptor())
//                .connectTimeout(5, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .build()
//    }
//
//    /**
//     * 字幕库相关的api
//     *
//     * @return
//     */
//    private fun zimukuApi(): ZimukuApi {
//        if (_ZimukuApi == null) {
//            val retrofit = Retrofit.Builder()
//                    .client(_Client)
//                    .baseUrl(ZimukuApi.BASE_URL)
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//            _ZimukuApi = retrofit.create<ZimukuApi>(ZimukuApi::class.java!!)
//        }
//
//        return _ZimukuApi
//    }
//
//
//    /**
//     * 通过关键词，获得一个页面
//     * ----------------------------
//     * （一个页面下有一堆电影）
//     *
//     * @param movie
//     * @param page
//     * @return
//     */
//    fun getPage_Zimuku(movie: String, page: Int): Observable<GodPage<*>> {
//        return zimukuApi()
//                .getMovieList(movie, page)
//                .flatMap<GodPage> { responseBody ->
//                    val page = ParserManager.getInstance()._PageParserZimuku.parse(responseBody.string())
//                    Observable.just<GodPage>(page!!)
//                }
//                .subscribeOn(Schedulers.io())
//    }
//
//
//    /**
//     * 获取字幕的下载链接
//     *
//     * @param downloadPageUrl
//     * @return
//     */
//    fun getDownloadUrl4Zimu(downloadPageUrl: String): Observable<String> {
//        return zimukuApi()
//                .getHtmlByUrl(downloadPageUrl)
//                .flatMap { responseBody ->
//                    //                        Log.e("--->", "downloadPageUrl:" + downloadPageUrl);
//                    val downloadUrl = ParserManager.getInstance().__ZimuDownloadPageParser.parse(responseBody.string())
//                    //                        Log.e("--->", "downloadUrl:" + downloadUrl);
//                    Observable.just(downloadUrl!!)
//                }
//                .subscribeOn(Schedulers.io())
//    }
//
//     fun getAllZimuFromUrl(allZimuPageUrl: String): Observable<List<Zimu>> {
//        return zimukuApi()
//                .getHtmlByUrl(allZimuPageUrl)
//                .flatMap { responseBody ->
//                    val zimuList = ParserManager.getInstance().__AllZimuParser.parse(responseBody.string())
//                    Observable.just(zimuList!!)
//                }
//                .subscribeOn(Schedulers.io())
//    }
//
//    inner class HeaderInterceptor : Interceptor {
//
//        @Throws(IOException::class)
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val request = chain.request()
//                    .newBuilder()
//                    .header("User-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .build()
//            return chain.proceed(request)
//        }
//    }
//
//    companion object {
//
//        private var _Client: OkHttpClient
//
//        private var _ApiManager2222: ApiManager2222? = null
//
//        val instence: ApiManager2222
//            get() {
//                if (_ApiManager2222 == null) {
//                    synchronized(ApiManager2222::class.java) {
//                        if (_ApiManager2222 == null) {
//                            _ApiManager2222 = ApiManager2222()
//                        }
//                    }
//                }
//                return _ApiManager2222
//            }
//    }
//}
