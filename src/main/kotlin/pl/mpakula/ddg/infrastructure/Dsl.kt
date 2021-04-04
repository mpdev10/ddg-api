package pl.mpakula.ddg.infrastructure

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.mpakula.ddg.infrastructure.model.SerializableIcon
import pl.mpakula.ddg.infrastructure.model.SerializableLink
import pl.mpakula.ddg.infrastructure.model.SerializableResponse

internal class SerializableResponseBuilder {
    var abstract: String = ""
    var abstractText: String = ""
    var abstractSource: String = ""
    var abstractUrl: String = ""
    var image: String = ""
    var heading: String = ""
    var answer: String = ""
    var answerType: String = ""
    var definition: String = ""
    var definitionSource: String = ""
    var definitionUrl: String = ""
    var relatedTopics: List<SerializableLink> = emptyList()
    var results: List<SerializableLink> = emptyList()
    var type: String = ""
    var redirect: String = ""

    fun relatedTopics(vararg topics: SerializableLink) {
        relatedTopics = topics.toList()
    }

    fun results(vararg topics: SerializableLink) {
        results = topics.toList()
    }

    fun build() =
        SerializableResponse(abstract,
            abstractText,
            abstractSource,
            abstractUrl,
            image,
            heading,
            answer,
            answerType,
            definition,
            definitionSource,
            definitionUrl,
            relatedTopics,
            results,
            type,
            redirect)
}

internal class SerializableIconBuilder {
    var url: String = ""
    var height: String = ""
    var width: String = ""

    fun build() = SerializableIcon(url, height, width)
}

internal class SerializableLinkBuilder {
    var result: String = ""
    var firstUrl: String = ""
    var icon: SerializableIcon = SerializableIcon()
    var text: String = ""
    var name: String = ""
    var topics: List<SerializableLink> = emptyList()
    var results: List<SerializableLink> = emptyList()

    fun build() = SerializableLink(result, firstUrl, icon, text, name, topics, results)
}

internal fun jsonResponse(init: SerializableResponseBuilder.() -> Unit): String {
    val builder = SerializableResponseBuilder()
    builder.init()
    return Json.encodeToString(builder.build())
}

internal fun serializableResponse(init: SerializableResponseBuilder.() -> Unit): SerializableResponse {
    val builder = SerializableResponseBuilder()
    builder.init()
    return builder.build()
}

internal fun icon(init: SerializableIconBuilder.() -> Unit): SerializableIcon {
    val builder = SerializableIconBuilder()
    builder.init()
    return builder.build()
}

internal fun link(init: SerializableLinkBuilder.() -> Unit): SerializableLink {
    val builder = SerializableLinkBuilder()
    builder.init()
    return builder.build()
}