package br.dev.saed.downloader.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class Link(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Int = 0,

    val name: String,
    val url: String,

    @Column(unique = true)
    val code: Int,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "server_id")
    val server: Server
) {



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Link

        return id == other.id
    }

    override fun hashCode(): Int {
        return id ?: 0
    }


}