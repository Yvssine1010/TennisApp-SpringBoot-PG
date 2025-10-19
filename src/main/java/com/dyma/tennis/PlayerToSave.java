package com.dyma.tennis;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
/* comme si on inscrivait un nouveau joueur au classement mondial. On peut imaginer que ce joueur a
gagné ses premiers tournois et a donc marqué des points. On inscrit donc ce joueur avec son total de points, et la
fonctionnalité de création de joueur va non seulement ajouter ce joueur au classement mondial mais automatiquement
mettre à jour le classement.*/
public record PlayerToSave(
        @NotBlank(message = "First name is mandatory") String firstName,
        @NotBlank(message = "Last name is mandatory") String lastName,
        @NotNull(message = "Birth date is mandatory") @PastOrPresent(message = "Birth date must be past or present") LocalDate birthDate,
        @PositiveOrZero(message = "Points must be more than zero") int points) {
}