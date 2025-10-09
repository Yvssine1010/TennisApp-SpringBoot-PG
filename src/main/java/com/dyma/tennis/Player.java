package com.dyma.tennis;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record Player(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @PastOrPresent LocalDate birthDate, /* permet de s'assurer que la donnée est bien une date et qu'elle se situe dans le passé ou le présent : on veut éviter une date de naissance dans le futur.*/
        @Valid Rank rank
) {
}


