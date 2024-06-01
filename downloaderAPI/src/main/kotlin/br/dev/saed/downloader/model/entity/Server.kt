package br.dev.saed.downloader.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class Server(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Int = 0,
    @Column(unique = true)
    val name: String,

) {

    @OneToMany(mappedBy = "server")
    val links: Set<Link> = mutableSetOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Server

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}