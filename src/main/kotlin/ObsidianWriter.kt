import model.Config
import model.Project
import timonff.com.model.Discussion
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime

class ObsidianWriter(
    private val vault: Path,
    private val config: Config,
    private val project: Project
) : Writer {

    override fun write(
        mergeId: Long,
        discussions: List<Discussion>
    ) {
        val folder = vault.resolve(
            project.shortPath.replace("/", "-")
        )

        Files.createDirectories(folder)

        val file = nextFile(folder, "MR-$mergeId")

        val open = discussions.filter {
            it.notes.any { note ->
                note.resolvable && note.resolved == false
            }
        }

        val resolved = discussions - open.toSet()

        val content = buildString {

            appendLine("---")
            appendLine("type: gitlab-review")
            appendLine("project: ${project.shortPath}")
            appendLine("project_url: ${config.hostUrl}/${project.path}")
            appendLine("merge_request: $mergeId")
            appendLine("merge_request_url: ${config.hostUrl}/${project.path}/-/merge_requests/$mergeId")
            appendLine("status: ${if (open.isEmpty()) "resolved" else "open"}")
            appendLine("open_threads: ${open.size}")
            appendLine("resolved_threads: ${resolved.size}")
            appendLine("generated: ${LocalDateTime.now()}")
            appendLine("tags:")
            appendLine("  - gitlab")
            appendLine("  - code-review")
            appendLine("  - merge-request")
            appendLine("---")
            appendLine()
            appendLine("## ✅ Resolved Threads (${resolved.size}) |🔴 Open Threads (${open.size})")
            appendLine()

            open.groupBy { it.notes.firstOrNull()?.file ?: "Other" }
                .forEach { (file, unsortedDiscussions) ->
                    val sorted = unsortedDiscussions.sortedWith(
                        compareBy(
                            { it.notes.firstOrNull()?.line != null },
                            { it.notes.firstOrNull()?.line ?: 0 }
                        )
                    )
                    appendForFile(file, sorted)
                }

        }

        Files.writeString(file, content)
    }

    private fun StringBuilder.appendForFile(file: String, discussions: List<Discussion>) {
        appendLine("---")
        appendLine("# \uD83D\uDCC4 $file")
        appendLine()
        discussions.forEach {
            appendDiscussion( file, it)
        }
    }

    private fun StringBuilder.appendDiscussion(
        file: String,
        discussion: Discussion
    ) {
        val line = discussion.notes.firstOrNull()?.line
        if (line == null) {
            appendLine("## [$file general](${discussion.commentUrl})")
        } else {
            appendLine("## [$file:$line](${discussion.commentUrl})")
        }
        appendLine("> [!example] Status")
        appendLine(">  - [ ] Started resolving")
        appendLine(">  - [ ] Resolve GitLab thread")
        appendLine()
        discussion.notes.forEach { note ->
            appendLine("> **${note.author}:** ")
            appendLine("> `${note.text}`")
            appendLine()
        }

        appendLine("```")
        appendLine("ref: ${discussion.commentUrl}")
        appendLine("```")
        appendLine()
    }

    fun nextFile(folder: Path, prefix: String): Path {
        var index = 1

        while (true) {
            val file = folder.resolve("$prefix#$index.md")

            if (!Files.exists(file)) {
                return file
            }

            index++
        }
    }
}