package timonff.com

import ObsidianWriter
import Writer
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.long
import commands.Config
import kotlin.io.path.Path


class Resolve : CliktCommand() {
    val mergeId by argument().long()
    override fun run() {
        val config = Config().loadOrCreate()
        val api: GitApi = GitApiImpl(config)
        val project = api.getProject(config.projectPathOrId)
        val discussions = api.getDiscussions(project, mergeId)

        echo("${discussions.size} Threads found")
        val writer:Writer=ObsidianWriter(
            Path(config.vaultPath),
            config,
            project
        )
        writer.write(
            mergeId,
            discussions
        )
    }

    override fun help(context: Context): String {
        return "Writes a interactive summary of unresolved Threads into your Obsidian Vault"
    }
}