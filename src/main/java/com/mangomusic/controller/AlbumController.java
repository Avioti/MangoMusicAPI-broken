package com.mangomusic.controller;

import com.mangomusic.model.Album;
import com.mangomusic.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;
    private static final String RECENT_ALBUMS = "/recent";
    private static final String ID_PATH = "/{id}";
    private static final String ARTIST_ID_PATH = "/artist/{artistId}";
    private static final String GENRE_PATH = "/genre/{genre}";
    private static final String PLAY_COUNT_PATH = "/{id}/play-count";

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums(@RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(albumService.searchAlbums(search));
        }
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

    @GetMapping(ID_PATH)
    public ResponseEntity<Album> getAlbumById(@PathVariable int id) {
        Album album = albumService.getAlbumById(id);
        if (album == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(album);
    }

    @GetMapping(ARTIST_ID_PATH)
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable int artistId) {
        return ResponseEntity.ok(albumService.getAlbumsByArtist(artistId));
    }

    @GetMapping(GENRE_PATH)
    public ResponseEntity<List<Album>> getAlbumsByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(albumService.getAlbumsByGenre(genre));
    }

    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        try {
            Album created = albumService.createAlbum(album);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(ID_PATH)
    public ResponseEntity<Album> updateAlbum(@PathVariable int id, @RequestBody Album album) {
        try {
            Album updated = albumService.updateAlbum(id, album);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(ID_PATH)
    public ResponseEntity<Void> deleteAlbum(@PathVariable int id) {
        boolean deleted = albumService.deleteAlbum(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(PLAY_COUNT_PATH)
    public ResponseEntity<Map<String, Object>> getPlayCount(@PathVariable int id) {
        Album album = albumService.getAlbumById(id);
        if (album == null) {
            return ResponseEntity.notFound().build();
        }
        int playCount = albumService.getAlbumPlayCount(id);
        Map<String, Object> response = new HashMap<>();
        response.put("albumId", id);
        response.put("albumTitle", album.getTitle());
        response.put("artistName", album.getArtistName());
        response.put("playCount", playCount);
        return ResponseEntity.ok(response);
    }

    @GetMapping(RECENT_ALBUMS)
    public ResponseEntity<List<Album>> getRecentAlbums(
            @RequestParam(defaultValue = "10") int limit){
        return ResponseEntity.ok(albumService.getRecentAlbums(limit));
    }



}