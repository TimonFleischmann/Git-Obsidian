package timonff.com

import model.Project
import timonff.com.model.Discussion

interface GitApi {
    fun getDiscussions(project: Project, mergeId: Long): List<Discussion>
    fun getProject(projectPathOrId: String): Project
}