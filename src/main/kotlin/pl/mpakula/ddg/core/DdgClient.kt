package pl.mpakula.ddg.core

import pl.mpakula.ddg.core.model.DdgResponse
import pl.mpakula.ddg.core.model.Either

interface DdgClient {
    fun searchInstantAnswer(query: String): Either<DdgResponse>
}