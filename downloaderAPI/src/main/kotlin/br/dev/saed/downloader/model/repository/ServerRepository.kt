package br.dev.saed.downloader.model.repository;

import br.dev.saed.downloader.model.entity.Server
import org.springframework.data.repository.ListCrudRepository

interface ServerRepository : ListCrudRepository<Server, Int> {
}