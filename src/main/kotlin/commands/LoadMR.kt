package timonff.com

import ObsidianWriter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import commands.Config
import kotlin.io.path.Path


class LoadMR : CliktCommand() {
    override fun run() {
        val config = Config().loadOrCreate()
        val api: GitApi = GitApiImpl(config)
        val project = api.getProject(config.projectPathOrId)
        val mergeId = 1L
        val discussions = api.getDiscussions(project, mergeId)
        ObsidianWriter(Path(config.vaultPath), config, project)
            .write(
                mergeId,
                discussions
            )

        echo("${discussions.size} Threads found")
    }

    override fun help(context: Context): String {
        return "helps"
    }
}