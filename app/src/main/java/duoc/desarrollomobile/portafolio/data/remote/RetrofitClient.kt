package duoc.desarrollomobile.portafolio.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit Singleton para la API
 */
object RetrofitClient {

    // URL base de tu API
    // Para emulador Android: usa 10.0.2.2 en lugar de localhost
    // Para dispositivo físico: usa la IP de tu computadora (ej: 192.168.1.100)
    private const val BASE_URL = "http://10.0.2.2:5000/api/"

    // Logging interceptor para debug
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente OkHttp
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Instancia de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Servicio de la API
    val apiService: ContactApiService by lazy {
        retrofit.create(ContactApiService::class.java)
    }
}