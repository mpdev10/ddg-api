package pl.mpakula.ddg.infrastructure

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pl.mpakula.ddg.core.model.*
import pl.mpakula.ddg.infrastructure.model.SerializableIcon
import pl.mpakula.ddg.infrastructure.model.SerializableLink
import pl.mpakula.ddg.infrastructure.model.SerializableResponse

internal fun deserialize(decodedObj: String): SerializableResponse =
    Json { ignoreUnknownKeys = true }.decodeFromString(decodedObj)

internal fun toDomain(response: SerializableResponse) = DdgResponse(
    toAbstract(response),
    toAnswer(response),
    toDefinition(response),
    toRelatedTopics(response),
    toRelatedTopicGroups(response),
    toResults(response),
    toResultGroups(response),
    ResponseType.fromString(response.type),
    response.redirect.takeUnless { it.isBlank() }
)

private fun toAbstract(response: SerializableResponse): Abstract? = when (response.abstract) {
    "" -> null
    else -> Abstract(response.abstractText,
        response.abstractSource,
        response.abstractUrl,
        response.image,
        response.heading)
}

private fun toAnswer(response: SerializableResponse): Answer? = when (response.answer) {
    "" -> null
    else -> Answer(response.answer, response.answerType)
}

private fun toDefinition(response: SerializableResponse): Definition? = when (response.definition) {
    "" -> null
    else -> Definition(response.definition, response.definitionSource, response.definitionUrl)
}

private fun toTopic(link: SerializableLink) = Topic(link.firstUrl, toIcon(link.icon), link.result, link.text)

private fun toResult(link: SerializableLink) = Result(link.firstUrl, toIcon(link.icon), link.result, link.text)

private fun toRelatedTopics(response: SerializableResponse): List<Topic> =
    response.relatedTopics.filter(::isSingleLink)
        .map(::toTopic)

private fun toTopicGroup(link: SerializableLink) = Group(link.name, link.topics.map(::toTopic))

private fun toRelatedTopicGroups(response: SerializableResponse): List<Group<Topic>> =
    response.relatedTopics.filter { !isSingleLink(it) }
        .map(::toTopicGroup)

private fun toResults(response: SerializableResponse): List<Result> =
    response.results.filter(::isSingleLink)
        .map(::toResult)

private fun toResultGroup(link: SerializableLink) = Group(link.name, link.results.map(::toResult))

private fun toResultGroups(response: SerializableResponse): List<Group<Result>> =
    response.results.filter { !isSingleLink(it) }
        .map(::toResultGroup)


private fun toIcon(icon: SerializableIcon): Icon = Icon(icon.height, icon.width, icon.url)

private fun isSingleLink(link: SerializableLink) = link.text.isNotEmpty()