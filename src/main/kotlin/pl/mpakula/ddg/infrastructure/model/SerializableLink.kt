package pl.mpakula.ddg.infrastructure.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SerializableLink(
    @SerialName("Result") val result: String = "",
    @SerialName("FirstURL") val firstUrl: String = "",
    @SerialName("Icon") val icon: SerializableIcon = SerializableIcon(),
    @SerialName("Text") val text: String = "",
    @SerialName("Name") val name: String = "",
    @SerialName("Topics") val topics: List<SerializableLink> = emptyList(),
    @SerialName("Results") val results: List<SerializableLink> = emptyList()
)
