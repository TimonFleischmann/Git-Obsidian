package timonff.com

import model.Config
import model.Project
import org.gitlab4j.api.GitLabApi
import timonff.com.model.Discussion
import timonff.com.model.Note

class GitApiImpl(
    val config: Config
) : GitApi {
    val api: GitLabApi = GitLabApi(
        config.hostUrl,
        config.token
    )

    override fun getDiscussions(project: Project, mergeId: Long): List<Discussion> {
        val projectPath = project.path
        val discussions = api
            .discussionsApi
            .getMergeRequestDiscussions(project.id, mergeId)
            .map { discussion ->

                val notes = discussion.notes.map { note ->
                    Note(
                        noteId = note.id,
                        author = note.author.name,
                        text = note.body,
                        resolved = note.resolved,
                        resolvable= note.resolvable,
                        file = note.position?.newPath,
                        line = note.position?.newLine,
                        createdAt = note.createdAt.toString()
                    )
                }

                Discussion(
                    discussionId = discussion.id,
                    commentUrl = commentUrl(
                        config.hostUrl,
                        projectPath,
                        mergeId,
                        notes.first().noteId
                    ),
                    notes = notes
                )
            }
        return discussions
    }

    override fun getProject(projectPathOrId: String): Project {
        return Project(
            api.projectApi.getProject(projectPathOrId).id,
            api.projectApi.getProject(projectPathOrId).pathWithNamespace,
            api.projectApi.getProject(projectPathOrId).path
        )

    }
    fun commentUrl(
        hostUrl: String,
        projectPath: String,
        mergeId: Long,
        noteId: Long
    ): String {
        return "$hostUrl/$projectPath/-/merge_requests/$mergeId/diffs#note_$noteId"
    }
}