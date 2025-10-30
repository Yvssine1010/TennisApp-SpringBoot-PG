package com.dyma.tennis.rest;

import com.dyma.tennis.*;
import com.dyma.tennis.Error;
import com.dyma.tennis.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tennis Players")
@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerserv;

    // ✅ GET - Liste de tous les joueurs
    @Operation(summary = "Finds players", description = "Finds all players")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Players list",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Player.class)))
                    }
            )
    })
    @GetMapping
    public List<Player> list() {
        return playerserv.getAllPlayers();
    }

    // ✅ GET - Trouver un joueur par nom
    @Operation(summary = "Find a player", description = "Find a player by last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "404", description = "Player not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/{lastName}")
    public Player getByLastName(@PathVariable("lastName") String lastName) {
        return playerserv.getByLastName(lastName);
    }

    // ✅ POST - Créer un joueur
    @Operation(summary = "Create player", description = "Create a new player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Player.class)))
    })
    @PostMapping
    public Player create(@RequestBody @Valid PlayerToSave newPlayer) {
        return playerserv.create(newPlayer);
    }

    // ✅ PUT - Mettre à jour un joueur
    @Operation(summary = "Update a player", description = "Update an existing player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "404", description = "Player not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class)))
    })
    @PutMapping
    public Player updatePlayer(@RequestBody @Valid PlayerToSave playerToSave) {
        return playerserv.update(playerToSave);
    } // ✅ ← tu avais oublié cette accolade

    // ✅ DELETE - Supprimer un joueur
    @Operation(summary = "Delete a player by last name", description = "Delete a player by their last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Player deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Player not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lastName}") // ✅ corrigé ici
    public void delete(@PathVariable("lastName") String lastName) {
        playerserv.delete(lastName);
    }
}
