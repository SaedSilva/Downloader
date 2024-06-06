package br.dev.saed.silverdownloader.model

import java.time.LocalDateTime

data class Download(
    var name: String,
    var path: String,
    var date: LocalDateTime
)
