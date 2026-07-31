import timonff.com.model.Discussion

interface Writer {
    fun write(
        mergeId: Long,
        discussions: List<Discussion>
    )
}
