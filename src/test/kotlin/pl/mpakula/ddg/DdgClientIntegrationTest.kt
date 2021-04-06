package pl.mpakula.ddg

import arrow.core.right
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.marcinziolo.kotlin.wiremock.get
import com.marcinziolo.kotlin.wiremock.like
import com.marcinziolo.kotlin.wiremock.returns
import com.marcinziolo.kotlin.wiremock.returnsJson
import org.junit.Rule
import org.junit.Test
import pl.mpakula.ddg.core.model.*
import pl.mpakula.ddg.infrastructure.icon
import pl.mpakula.ddg.infrastructure.jsonResponse
import pl.mpakula.ddg.infrastructure.link
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.*
import java.net.ServerSocket

internal class DdgClientIntegrationTest {

    private val port = findRandomPort()
    private val url = "http://localhost:$port"
    private val client = DdgClientFactory.createClient(apiUrl = url)

    @Rule
    @JvmField
    var wireMock: WireMockRule = WireMockRule(port)

    private val apiGetCall = wireMock.get { url like ".*" }

    @Test
    fun `when duckDuckGo responds with an error, response should be an error`() {
        //given
        apiGetCall returns { statusCode = 404 }
        //when
        val result = client.searchInstantAnswer("googol")
        //then
        expectThat(result).right().isNotEmpty()
    }

    @Test
    fun `when duckDuckGo response has all empty fields, response should contain nulls or empty values`() {
        //given
        apiGetCall returnsJson {
            body = jsonResponse { }
        }
        //when
        val response = client.searchInstantAnswer("google").orNull()
        //then
        expect {
            that(response).isNotNull()
            that(response?.abstract).isNull()
            that(response?.answer).isNull()
            that(response?.definition).isNull()
            that(response?.relatedTopics).isNotNull().isEmpty()
            that(response?.relatedTopicGroups).isNotNull().isEmpty()
            that(response?.results).isNotNull().isEmpty()
            that(response?.resultGroups).isNotNull().isEmpty()
            that(response?.type).isNotNull().isEqualTo(ResponseType.NONE)
            that(response?.redirect).isNull()
        }
    }

    @Test
    fun `when duckDuckGo response contains non-empty abstract field, should be present in client response`() {
        //given
        val absText = "Flying Spaghetti Monster The deity of the Church of the Flying Spaghetti Monster."
        val absSource = "Wikipedia"
        val absUrl = "https://en.wikipedia.org/wiki/FSM"
        val img = ""
        val head = "Flying Spaghetti Monster"
        apiGetCall returnsJson {
            body = jsonResponse {
                abstract = absText
                abstractText = absText
                abstractSource = absSource
                abstractUrl = absUrl
                image = img
                heading = head
            }
        }
        //when
        val response = client.searchInstantAnswer("fsm").orNull()
        val abstract = response?.abstract
        //then
        expectThat(abstract).isEqualTo(
            Abstract(text = absText, source = absSource, url = absUrl, image = img, heading = head))
    }

    @Test
    fun `when duckDuckGo response contains non-empty answer field, should be present in client response`() {
        //given
        val ansText = "Flying Spaghetti monster is an answer to everything"
        val ansType = "text"
        apiGetCall returnsJson {
            body = jsonResponse {
                answer = ansText
                answerType = ansType
            }
        }
        //when
        val response = client.searchInstantAnswer("how to get rich").orNull()
        val answer = response?.answer
        //then
        expectThat(answer).isEqualTo(Answer(text = ansText, type = ansType))
    }

    @Test
    fun `when duckDuckGo response contains non-empty definition field, should be present in client response`() {
        //given
        val defText = "Kitchen utensil used to strain foods such as pasta or to rinse vegetables"
        val defSource = "Wikipedia"
        val defUrl = "https://en.wikipedia.org/wiki/Colander"
        apiGetCall returnsJson {
            body = jsonResponse {
                definition = defText
                definitionSource = defSource
                definitionUrl = defUrl
            }
        }
        //when
        val response = client.searchInstantAnswer("colander").orNull()
        val definition = response?.definition
        //then
        expectThat(definition).isEqualTo(Definition(text = defText, source = defSource, url = defUrl))
    }

    @Test
    fun `when duckDuckGo response contains single related topic, should be present in client response`() {
        //given
        val tUrl = "https://duckduckgo.com/Googolplex"
        val tResult = "<a href=\\\"https://duckduckgo.com/Googolplex\\\">Googolplex</a>"
        val tText = "Googolplex"
        apiGetCall returnsJson {
            body = jsonResponse {
                relatedTopics(
                    link {
                        firstUrl = tUrl
                        icon = icon {
                            url = ""
                            height = ""
                            width = ""
                        }
                        result = tResult
                        text = tText
                    }
                )
            }
        }
        //when
        val response = client.searchInstantAnswer("googol").orNull()
        val relatedTopics = response?.relatedTopics
        val relatedTopicGroups = response?.relatedTopicGroups
        //then
        expect {
            that(relatedTopics).isNotNull()
                .containsExactly(Topic(firstUrl = tUrl, icon = Icon("", "", ""), result = tResult, text = tText))
            that(relatedTopicGroups).isNotNull()
                .isEmpty()
        }
    }

    @Test
    fun `when duckDuckGo response contains related topic group, should be present in client response`() {
        //given
        val tUrl = "https://duckduckgo.com/Googolplex"
        val tResult = "<a href=\\\"https://duckduckgo.com/Googolplex\\\">Googolplex</a>"
        val tText = "Googolplex"
        val gName = "See also"
        apiGetCall returnsJson {
            body = jsonResponse {
                relatedTopics(
                    link {
                        name = gName
                        topics = listOf(
                            link {
                                firstUrl = tUrl
                                icon = icon {
                                    url = ""
                                    height = ""
                                    width = ""
                                }
                                result = tResult
                                text = tText
                            }
                        )
                    }
                )
            }
        }
        //when
        val response = client.searchInstantAnswer("googol").orNull()
        val relatedTopics = response?.relatedTopics
        val relatedTopicGroups = response?.relatedTopicGroups
        //then
        expect {
            that(relatedTopicGroups).isNotNull()
                .containsExactly(
                    Group(name = gName,
                        elements = listOf(
                            Topic(firstUrl = tUrl,
                                icon = Icon("", "", ""),
                                result = tResult,
                                text = tText)
                        )
                    )
                )
            that(relatedTopics).isNotNull()
                .isEmpty()
        }
    }

    @Test
    fun `when duckDuckGo response contains single result, should be present in client response`() {
        //given
        val rUrl = "https://duckduckgo.com/Googolplex"
        val rResult = "<a href=\\\"https://duckduckgo.com/Googolplex\\\">Googolplex</a>"
        val rText = "Googolplex"
        apiGetCall returnsJson {
            body = jsonResponse {
                results(
                    link {
                        firstUrl = rUrl
                        icon = icon {
                            url = ""
                            height = ""
                            width = ""
                        }
                        result = rResult
                        text = rText
                    }
                )
            }
        }
        //when
        val response = client.searchInstantAnswer("googol").orNull()
        val results = response?.results
        val resultGroups = response?.resultGroups
        //then
        expect {
            that(results).isNotNull()
                .containsExactly(Result(firstUrl = rUrl, icon = Icon("", "", ""), result = rResult, text = rText))
            that(resultGroups).isNotNull()
                .isEmpty()
        }
    }

    @Test
    fun `when duckDuckGo response contains result group, should be present in client response`() {
        //given
        val rUrl = "https://duckduckgo.com/Googolplex"
        val rResult = "<a href=\\\"https://duckduckgo.com/Googolplex\\\">Googolplex</a>"
        val rText = "Googolplex"
        val gName = "See also"
        apiGetCall returnsJson {
            body = jsonResponse {
                results(
                    link {
                        name = gName
                        results = listOf(
                            link {
                                firstUrl = rUrl
                                icon = icon {
                                    url = ""
                                    height = ""
                                    width = ""
                                }
                                result = rResult
                                text = rText
                            }
                        )
                    }
                )
            }
        }
        //when
        val response = client.searchInstantAnswer("googol").orNull()
        val results = response?.results
        val resultGroups = response?.resultGroups
        //then
        expect {
            that(resultGroups).isNotNull()
                .containsExactly(
                    Group(name = gName,
                        elements = listOf(
                            Result(firstUrl = rUrl,
                                icon = Icon("", "", ""),
                                result = rResult,
                                text = rText))

                    )
                )
            that(results).isNotNull()
                .isEmpty()
        }
    }

    @Test
    fun `when duckDuckGo response contains non-empty type field, should be present in client response`() {
        //given
        val responseType = "c"
        apiGetCall returnsJson { body = jsonResponse { type = responseType } }
        //when
        val response = client.searchInstantAnswer("any").orNull()
        val type = response?.type
        //then
        expectThat(type).isNotNull().isEqualTo(ResponseType.CATEGORY)
    }

    private fun findRandomPort() = ServerSocket(0).use { it.localPort }

}