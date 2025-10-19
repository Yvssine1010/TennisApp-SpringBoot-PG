package com.dyma.tennis.service;

import com.dyma.tennis.Player;
import com.dyma.tennis.PlayerToSave;
import com.dyma.tennis.Rank;

import java.util.*;

/*
 * Service métier qui calcule le classement des joueurs en prenant en compte
 * un nouveau joueur à ajouter ou mettre à jour.
 *
 * Logique :
 * 1️⃣ Ajouter le nouveau joueur à la liste existante.
 * 2️⃣ Trier la liste par points décroissants (le plus de points en premier).
 * 3️⃣ Recalculer le classement ordinal de chaque joueur en fonction de la position dans la liste triée.
 * 4️⃣ Retourner la liste mise à jour avec les nouveaux rangs.
 */
public class RankingCalculator {

    private final List<Player> currentPlayersRanking;
    private final PlayerToSave playerToSave;

    public RankingCalculator(List<Player> currentPlayersRanking, PlayerToSave playerToSave) {
        this.currentPlayersRanking = currentPlayersRanking;
        this.playerToSave = playerToSave;
    }

    public List<Player> getNewPlayersRanking() {
        // 1️⃣ Copie la liste actuelle pour ne pas modifier l'originale
        List<Player> newRankingList = new ArrayList<>(currentPlayersRanking);

        // 2️⃣ Ajoute le nouveau joueur avec un rang temporaire très élevé
        newRankingList.add(new Player(
                playerToSave.firstName(),
                playerToSave.lastName(),
                playerToSave.birthDate(),
                new Rank(999999999, playerToSave.points())
        ));

        // 3️⃣ Trie la liste par points décroissants pour que le joueur avec le plus de points soit premier
        newRankingList.sort((p1, p2) -> Integer.compare(p2.rank().point(), p1.rank().point()));

        // 4️⃣ Recalcule le rang de chaque joueur en fonction de sa position dans la liste triée
        List<Player> updatedPlayers = new ArrayList<>();
        for (int i = 0; i < newRankingList.size(); i++) {
            Player player = newRankingList.get(i);
            Player updatedPlayer = new Player(
                    player.firstName(),
                    player.lastName(),
                    player.birthDate(),
                    new Rank(i + 1, player.rank().point())
            );
            updatedPlayers.add(updatedPlayer);
        }

        // 5️⃣ Retourne la liste finale avec tous les joueurs et leur nouveau rang
        return updatedPlayers;
    }
}
