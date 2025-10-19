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
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Tennis Players")
@RestController
@RequestMapping("/players")
public class PlayerController {
    @Autowired
    private PlayerService playerserv;
    String a ;;
    @Operation(summary = "Finds players", description = "Finds players")//décrit ce que fait ton endpoint
    //les differentes reponses http possible(200, 400, 401, 404, 500, etc.)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "pLaYeRs lIsT",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Player.class))),
                            @Content(mediaType = "application/xml",
                                    array = @ArraySchema(schema = @Schema(implementation = Player.class)))}
            )
    })
    @GetMapping
    public List<Player> list() {
        return playerserv.getAllPlayers();
    }
    @Operation(summary = "Finds a player", description = "Finds a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Player.class))}),
            @ApiResponse(responseCode = "404", description = "Player with specified last name not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))})
    })

    @GetMapping("{lastName}")
    public Player getByLastName(@PathVariable("lastName") String lastName) {
        return playerserv.getByLastName(lastName);
    }

    @Operation(summary = "Create player", description = "Create player")//décrit ce que fait ton endpoint
    //les differentes reponses http possible(200, 400, 401, 404, 500, etc.)
    /*met à jour le contrôleurREST qui va traiter les requêtes de création*/
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "pLaYeR Creation",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = Player.class))}
            )
    })
    @PostMapping
    public Player create(@RequestBody @Valid PlayerToSave newPlayer) {
        return playerserv.create(newPlayer);
    }

    @Operation(summary = "Update a  player", description = "Update a  player")//décrit ce que fait ton endpoint
    //les differentes reponses http possible(200, 400, 401, 404, 500, etc.)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "pLaYeR Update",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = Player.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Player with specified last name not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))})
    })


    @PutMapping
    public Player updatePlayer(@RequestBody @Valid PlayerToSave playerToSave) {
        return playerserv.update(playerToSave);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Player deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Player not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{lastName}")
    public void delete(@PathVariable("lastName") String lastName) {
        // logique de suppression ici
    }

}
