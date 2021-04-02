package pl.mpakula.ddg.infrastructure.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SerializableIcon(
    @SerialName("URL") val url: String = "",
    @SerialName("Height") val height: String = "",
    @SerialName("Width") val width: String = "",
)
