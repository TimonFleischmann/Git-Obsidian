package timonff.com.model

data class Discussion(
    val discussionId: String,
    val commentUrl: String,
    val notes: List<Note>
)

data class Note(
    val noteId: Long,
    val author: String,
    val text: String,
    val resolved: Boolean?,
    val resolvable: Boolean,
    val file: String?,
    val line: Int?,
    val createdAt: String
)