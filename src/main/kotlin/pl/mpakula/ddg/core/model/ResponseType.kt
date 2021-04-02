package pl.mpakula.ddg.core.model

enum class ResponseType(private val value: String) {
    ARTICLE("a"), DISAMBIGUATION("d"), CATEGORY("c"), NAME("n"), EXCLUSIVE("e"), NONE("");

    companion object {
        fun fromString(string: String) = typeMap.getOrDefault(string.toLowerCase(), NONE)
        private val typeMap: Map<String, ResponseType> = values().map { it.value to it }.toMap()
    }

}