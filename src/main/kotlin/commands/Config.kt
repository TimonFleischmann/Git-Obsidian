package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import kotlinx.serialization.json.Json
import model.Config
import java.lang.System.console
import java.nio.file.Files
import java.nio.file.Path

class Config : CliktCommand() {
    override fun run() {
        create()
    }

    private val configPath = Path.of(
        System.getProperty("user.home"),
        ".gitObs",
        "config.json"
    )

    fun loadOrCreate(): Config {
        return if (Files.exists(configPath)) {
            val config = load()
            println("loaded config for project ${config.projectPathOrId}")
            config
        } else {
            create()
        }
    }

    private fun load(): Config {
        val json = Files.readString(configPath)
        return Json.decodeFromString<Config>(json)
    }

    fun create(): Config {
        println("Config not found. Prompting for a new one...")
        println("GitLab hostUrl: ")
        val hostUrl = readln()
        println("Project Path or Id: ")
        val projectPathOrId = readln()
        val token = if (console() != null) {
            String(console().readPassword("GitLab Token: "))
        } else {
            // Fallback (e.g. IntelliJ doesn't provide a Console)
            print("GitLab Token: ")
            readln()
        }
        println("VaultPath: ")
        val vaultPath = readln()

        val config = Config(hostUrl, projectPathOrId, token, vaultPath)

        val json = json.encodeToString(config)

        Files.createDirectories(configPath.parent)
        Files.writeString(configPath, json)
        println("Writing config to $configPath")
        return config
    }
    override fun help(context: Context): String {
        return "set configurations"
    }
    companion object {
        private val json = Json {
            prettyPrint = true
        }
    }
}