package pl.mpakula.ddg.infrastructure.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SerializableResponse(
    @SerialName("Abstract") val abstract: String = "",
    @SerialName("AbstractText") val abstractText: String = "",
    @SerialName("AbstractSource") val abstractSource: String = "",
    @SerialName("AbstractURL") val abstractUrl: String = "",
    @SerialName("Image") val image: String = "",
    @SerialName("Heading") val heading: String = "",
    @SerialName("Answer") val answer: String = "",
    @SerialName("AnswerType") val answerType: String = "",
    @SerialName("Definition") val definition: String = "",
    @SerialName("DefinitionSource") val definitionSource: String = "",
    @SerialName("DefinitionURL") val definitionUrl: String = "",
    @SerialName("RelatedTopics") val relatedTopics: List<SerializableLink> = emptyList(),
    @SerialName("Results") val results: List<SerializableLink> = emptyList(),
    @SerialName("Type") val type: String,
    @SerialName("Redirect") val redirect: String
)