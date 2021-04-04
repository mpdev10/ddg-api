package pl.mpakula.ddg.infrastructure

import pl.mpakula.ddg.core.DdgClient
import pl.mpakula.ddg.core.exception.DdgClientException
import pl.mpakula.ddg.core.model.DdgResponse
import pl.mpakula.ddg.core.model.Either
import pl.mpakula.ddg.core.model.Either.Success
import pl.mpakula.ddg.core.model.HtmlInText


internal class DefaultDdgClient(
    private val htmlInText: HtmlInText,
    private val apiUrl: String,
) : DdgClient {

    override fun searchInstantAnswer(query: String): Either<DdgResponse> {
        return try {
            doSearch(query)
                .jsonObject.toString()
                .let(::deserialize)
                .let(::toDomain)
                .let(::Success)
        } catch (e: Exception) {
            Either.Error(DdgClientException(e))
        }
    }

    private fun doSearch(query: String) = khttp.get(
        apiUrl,
        params = mapOf("q" to query, "format" to "json", "no_redirect" to "1", "no_html" to htmlInText.value.toString())
    )

}