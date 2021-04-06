package pl.mpakula.ddg.core

import arrow.core.Either
import pl.mpakula.ddg.core.exception.DdgClientException
import pl.mpakula.ddg.core.model.DdgResponse

interface DdgClient {
    fun searchInstantAnswer(query: String): Either<DdgClientException, DdgResponse>
}