package com.raywenderlich.spacedaily

import com.raywenderlich.spacedaily.di.networkModule
import com.raywenderlich.spacedaily.network.NASAAPIInterface
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import retrofit2.Retrofit

class NetworkTest: KoinTest {
    private val retrofit: Retrofit by inject()
    private val api: NASAAPIInterface by inject()
    private val moshi: Moshi by inject()
    private val okHttpClient: OkHttpClient by inject()
    private val baseUrl : String by lazy { get(named("BASE_URL")) as String }
    @Before
    fun setup(){
        startKoin {
            modules(listOf(networkModule))
        }
    }
    @After
    fun tearDown(){
        stopKoin()
    }
    @Test
    fun `Retrofit Instance Created and BASE_URL is not null` (){
        assertNotNull(retrofit)
        assertNotNull(baseUrl)
    }
    @Test
    fun `Test Moshi Instance creation` (){
        assertNotNull(moshi)
    }
    @Test
    fun `Test api Instance creation` (){
        assertNotNull(api)
    }
    @Test
    fun `Test OkHttpClient and interceptors` (){
        assertNotNull(okHttpClient)
        assert(okHttpClient.connectTimeoutMillis == 30000)
        assert(okHttpClient.readTimeoutMillis == 30000)
        assert(okHttpClient.writeTimeoutMillis == 30000)
        assert(okHttpClient.interceptors.size == 1)
    }
}