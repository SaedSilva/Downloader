package br.dev.saed.downloader.controller

import br.dev.saed.downloader.model.entity.Server
import br.dev.saed.downloader.model.repository.ServerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/server"])
class ServerController {

    @Autowired
    private lateinit var serverRepository :ServerRepository

    @PostMapping
    fun insertServer(server: Server): ResponseEntity<Server> {
        return ResponseEntity.ok().body(serverRepository.save(server))
    }

    @GetMapping(path = ["/servers"])
    fun getAllServers(): ResponseEntity<MutableList<Server>> {
        return ResponseEntity.ok().body(serverRepository.findAll())
    }
}