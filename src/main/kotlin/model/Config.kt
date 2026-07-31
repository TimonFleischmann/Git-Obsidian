package model

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val hostUrl: String,
    val projectPathOrId:String,
    val token:String,
    val vaultPath: String
)
