package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import kotlinx.serialization.json.Json
import model.Config
import java.nio.file.Files
import java.nio.file.Path

class Config : CliktCommand() {
    override fun run() {
        create()
    }

    private val configPath = Path.of("config.json")

    fun loadOrCreate(): Config {
        return if (Files.exists(configPath)) {
            load()
        } else {
            create()
        }
    }

    private fun load(): Config {
        val json = Files.readString(configPath)
        return Json.decodeFromString<Config>(json)
    }

    fun create(): Config {
        echo("GitLab hostUrl: ")
        val hostUrl = readln()
        echo("Project Path or Id: ")
        val projectPathOrId = readln()
        echo("GitLab Token: ")
        val token = readln()
        echo("VaultPath: ")
        val vaultPath = readln()

        val config = Config(hostUrl, projectPathOrId, token, vaultPath)

        val json = json.encodeToString(config)

        Files.writeString(configPath, json)

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