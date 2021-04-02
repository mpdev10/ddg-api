package pl.mpakula.ddg.core.model

data class DdgResponse(
    val abstract: Abstract?,
    val answer: Answer?,
    val definition: Definition?,
    val relatedTopics: List<Topic>,
    val relatedTopicGroups: List<Group<Topic>>,
    val results: List<Result>,
    val resultGroups: List<Group<Result>>,
    val type: ResponseType,
    val redirect: String?
)