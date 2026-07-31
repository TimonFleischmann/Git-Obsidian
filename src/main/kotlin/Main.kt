package timonff.com

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import commands.Config

class GitObs : CliktCommand(name = "gitObs") {
    override fun run() = Unit
}

fun main(args: Array<String>): Unit =
    GitObs()
    .subcommands(
        Resolve(),
        Config()
    ).main(args)
