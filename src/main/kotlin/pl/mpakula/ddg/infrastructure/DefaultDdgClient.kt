package pl.mpakula.ddg.infrastructure

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.getOrElse
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
                .let(::deserialize)
                .let(::toDomain)
                .let(::Success)
        } catch (e: Exception) {
            Either.Error(DdgClientException(e))
        }
    }

    private fun doSearch(query: String) = apiUrl.httpGet(
        listOf(
            "q" to query,
            "format" to "json",
            "no_redirect" to "1",
            "no_html" to htmlInText.value.toString()
        )
    ).responseString().third.getOrElse { throw it }

}