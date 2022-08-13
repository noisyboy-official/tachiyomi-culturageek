package eu.kanade.tachiyomi.extension.all.reaperscans

import eu.kanade.tachiyomi.multisrc.madara.Madara
import eu.kanade.tachiyomi.network.interceptor.rateLimit
import eu.kanade.tachiyomi.source.SourceFactory
import eu.kanade.tachiyomi.source.model.SChapter
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class ReaperScansFactory : SourceFactory {
    override fun createSources() = listOf(
        ReaperScansEn(),
        ReaperScansBr(),
        ReaperScansTr(),
        ReaperScansId(),
        ReaperScansFr()
    )
}

abstract class ReaperScans(
    override val baseUrl: String,
    lang: String,
    dateFormat: SimpleDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
) : Madara("Reaper Scans", baseUrl, lang, dateFormat) {

    override fun chapterFromElement(element: Element): SChapter = SChapter.create().apply {
        val urlElement = element.selectFirst(chapterUrlSelector)!!

        name = urlElement.selectFirst("p.chapter-manhwa-title")?.text()
            ?: urlElement.ownText()
        date_upload = urlElement.selectFirst("span.chapter-release-date > i")?.text()
            .let { parseChapterDate(it) }

        val fixedUrl = urlElement.attr("abs:href").toHttpUrl().newBuilder()
            .removeAllQueryParameters("style")
            .addQueryParameter("style", "list")
            .toString()

        setUrlWithoutDomain(fixedUrl)
    }
}

class ReaperScansEn : ReaperScans(
    "https://reaperscans.com",
    "en",
    SimpleDateFormat("MMM dd,yyyy", Locale.US)
) {

    override val versionId = 2
}

class ReaperScansBr : ReaperScans(
    "https://reaperscans.com.br",
    "pt-BR",
    SimpleDateFormat("dd/MM/yyyy", Locale.US)
) {

    override val id = 7767018058145795388

    override val client: OkHttpClient = super.client.newBuilder()
        .rateLimit(1, 2, TimeUnit.SECONDS)
        .build()
}

class ReaperScansTr : ReaperScans(
    "https://reaperscanstr.com",
    "tr",
    SimpleDateFormat("MMMMM dd, yyyy", Locale("tr"))
) {

    // Tags are useless as they are just SEO keywords.
    override val mangaDetailsSelectorTag = ""
}

class ReaperScansId : ReaperScans("https://reaperscans.id", "id") {

    // Tags are useless as they are just SEO keywords.
    override val mangaDetailsSelectorTag = ""
}

class ReaperScansFr : ReaperScans(
    "https://new.reaperscans.fr",
    "fr",
    SimpleDateFormat("dd/MM/yyyy", Locale.US)
) {

    // Migrated from WpMangaReader to Madara.
    override val versionId = 2
}
