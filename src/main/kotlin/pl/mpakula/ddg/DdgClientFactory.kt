package pl.mpakula.ddg

import pl.mpakula.ddg.core.DdgClient
import pl.mpakula.ddg.core.model.HtmlInText
import pl.mpakula.ddg.infrastructure.DefaultDdgClient

class DdgClientFactory {

    companion object {
        fun createClient(
            htmlInText: HtmlInText = HtmlInText.NONE,
            apiUrl: String = "https://api.duckduckgo.com/",
        ): DdgClient = DefaultDdgClient(htmlInText, apiUrl)
    }

}
