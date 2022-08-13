package eu.kanade.tachiyomi.extension.tr.webtoontr

import eu.kanade.tachiyomi.multisrc.madara.Madara
import java.text.SimpleDateFormat
import java.util.Locale

class WebtoonTR : Madara(
    "Webtoon TR",
    "https://webtoon-tr.com",
    "tr",
    SimpleDateFormat("dd/MM/yyy", Locale("tr"))
) {
    override val useNewChapterEndpoint = true
}
