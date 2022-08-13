package eu.kanade.tachiyomi.extension.pt.dreamunionscan

import eu.kanade.tachiyomi.multisrc.madara.Madara
import eu.kanade.tachiyomi.network.interceptor.rateLimit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class DreamUnionScan : Madara(
    "Dream Union Scan",
    "https://www.dreamunionscan.com",
    "pt-BR",
    SimpleDateFormat("MMMMM dd, yyyy", Locale("pt", "BR"))
) {

    override val client: OkHttpClient = super.client.newBuilder()
        .addInterceptor(::authWarningIntercept)
        .rateLimit(1, 2, TimeUnit.SECONDS)
        .build()

    private fun authWarningIntercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.request.url.toString().contains("wp-login.php")) {
            response.close()
            throw IOException(NEED_LOGIN)
        }

        return response
    }

    companion object {
        private const val NEED_LOGIN = "É necessário realizar o login via WebView para acessar a fonte."
    }
}
