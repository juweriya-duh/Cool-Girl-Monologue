package com.coolgirl.monologue;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {
    private final QuoteRepository repo;
    public QuoteController(QuoteRepository repo) { this.repo = repo; }

    // Published by default; ?all=true includes drafts (for the "My drafts" shelf)
    @GetMapping
    public List<Quote> list(@RequestParam(defaultValue = "false") boolean all) {
        return all ? repo.findAllByOrderByCreatedAtDesc() : repo.findByStatusOrderByCreatedAtDesc("PUBLISHED");
    }

    @PostMapping
    public Quote create(@Valid @RequestBody Quote q) { q.id = null; return repo.save(q); }

    @PutMapping("/{id}")
    public ResponseEntity<Quote> update(@PathVariable Long id, @Valid @RequestBody Quote in) {
        return repo.findById(id).map(q -> {
            q.text = in.text; q.author = in.author; q.source = in.source;
            q.imageUrl = in.imageUrl; q.mine = in.mine;
            return ResponseEntity.ok(repo.save(q));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<Quote> publish(@PathVariable Long id, @RequestParam(defaultValue = "true") boolean on) {
        return repo.findById(id).map(q -> { q.status = on ? "PUBLISHED" : "DRAFT"; return ResponseEntity.ok(repo.save(q)); })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
