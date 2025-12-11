package com.mangomusic.controller;

import com.mangomusic.model.Album;
import com.mangomusic.model.Artist;
import com.mangomusic.service.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;

    private static final String TOP_ALBUM = "/{id}/top-album";
    private static final String GENRES = "/genres";
    private static final String ID_PATH = "/{id}";

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists(@RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(artistService.searchArtists(search));
        }
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping(ID_PATH)
    public ResponseEntity<Artist> getArtistById(@PathVariable int id) {
        Artist artist = artistService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(artist);
    }

    @GetMapping(GENRES)
    public ResponseEntity<List<String>> getAllGenres() {
        return ResponseEntity.ok(artistService.getAllGenres());
    }

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        try {
            Artist created = artistService.createArtist(artist);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(ID_PATH)
    public ResponseEntity<Artist> updateArtist(@PathVariable int id, @RequestBody Artist artist) {
        try {
            Artist updated = artistService.updateArtist(id, artist);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(ID_PATH)
    public ResponseEntity<Void> deleteArtist(@PathVariable int id) {
        boolean deleted = artistService.deleteArtist(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(TOP_ALBUM)
    public ResponseEntity<?> getTopAlbum(@PathVariable int id){
        Artist artist = artistService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.notFound().build();
        }
        Album topAlbum = artistService.getTopAlbumByArtist(id);
        if (topAlbum == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(topAlbum);
    }
}