package com.dyma.tennis;

import jakarta.validation.constraints.*;

public record Rank(
       @Positive int position,
       @PositiveOrZero int point
) {
}
