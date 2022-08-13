package eu.kanade.tachiyomi.extension.pt.shinzooscan

import eu.kanade.tachiyomi.multisrc.madara.Madara
import eu.kanade.tachiyomi.network.interceptor.rateLimit
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class ShinzooScan : Madara(
    "Shinzoo Scan",
    "https://shinzooscan.xyz",
    "pt-BR",
    SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
) {

    override val client: OkHttpClient = super.client.newBuilder()
        .rateLimit(1, 1, TimeUnit.SECONDS)
        .build()
}
