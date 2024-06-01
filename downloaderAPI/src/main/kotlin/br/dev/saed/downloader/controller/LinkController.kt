package br.dev.saed.downloader.controller

import br.dev.saed.downloader.model.entity.Link
import br.dev.saed.downloader.model.repository.LinkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/link"])
class LinkController {

    @Autowired
    private lateinit var linkRepository: LinkRepository

    @PostMapping
    fun insertLink(link: Link): ResponseEntity<Link> {
        val response = linkRepository.save(link)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping(path = ["/code/{code}"])
    fun getLinkByCode(@PathVariable code: Int): ResponseEntity<Link> {
        val response = linkRepository.findLinkByCodeEquals(code)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping(path = ["/links"])
    fun getAllLinks(): ResponseEntity<MutableIterable<Link>> {
        return ResponseEntity.ok().body(linkRepository.findAll())
    }
}