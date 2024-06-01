package br.dev.saed.downloader.model.repository

import br.dev.saed.downloader.model.entity.Link
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface LinkRepository :CrudRepository<Link, Int> {
    @Query
    fun findLinkByCodeEquals(code: Int) :Link
}