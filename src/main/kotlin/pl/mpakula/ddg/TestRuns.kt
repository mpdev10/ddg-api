package pl.mpakula.ddg

import pl.mpakula.ddg.core.model.Either


fun main() {

    val client = DdgClientFactory.createClient(apiUrl = "htt127.0.0.1:80")
    val answer = client.searchInstantAnswer("google")

    when (answer) {
        is Either.Success -> answer.value
        is Either.Error -> answer.exception
    }


}


