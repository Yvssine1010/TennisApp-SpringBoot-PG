package com.dyma.tennis.service;

import com.dyma.tennis.Player;
import com.dyma.tennis.PlayerToSave;
import com.dyma.tennis.Rank;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
//service métier utilise un composant dedié pour déterminer le classement intégrant le nouveau joueur, puis retourne le joueur inscrit avec son classement qui a été calculé.
/*on ajoute le nouveau joueur à la liste des joueurs existants
on trie cette liste en fonction du nombre de points de chaque joueur
la position dans la liste triée détermine le nouveau classement
les joueurs sont mis à jour
le nouveau classement est renvoyé*/
public class RankingCalculator {
        private final List<Player> currentPlayersRanking;
        private final PlayerToSave playerToSave;

        public RankingCalculator(List<Player> currentPlayersRanking, PlayerToSave playerToSave) {
            this.currentPlayersRanking = currentPlayersRanking;
            this.playerToSave = playerToSave;
        }

        public List<Player> getNewPlayersRanking() {
            List<Player> newRankingList = new ArrayList<>(currentPlayersRanking);
            newRankingList.add(new Player(
                    playerToSave.firstName(),
                    playerToSave.lastName(),
                    playerToSave.birthDate(),
                    new Rank(999999999, playerToSave.points())
            ));

            List<Player> sortedPlayers = newRankingList.stream()
                    .sorted(Comparator.comparing(player -> player.rank().point()))
                    .toList();

            List<Player> updatedPlayers = new ArrayList<>();

            for (int i = 0; i < sortedPlayers.size(); i++) {
                Player player = sortedPlayers.get(i);
                Player updatedPlayer = new Player(
                        player.firstName(),
                        player.lastName(),
                        player.birthDate(),
                        new Rank(i+1, player.rank().point())
                );
                updatedPlayers.add(updatedPlayer);
            }
            return updatedPlayers;
        }
    }

