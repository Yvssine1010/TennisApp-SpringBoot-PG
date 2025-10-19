package com.dyma.tennis.service;

import com.dyma.tennis.Player;
import com.dyma.tennis.PlayerList;
import com.dyma.tennis.PlayerToSave;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*Le service lance une exception spécifique.
Le contrôleur ne gère pas l’exception, il laisse Spring la propager.
PlayerControllerErrorHandler l’intercepte et renvoie une réponse HTTP structurée (404 avec message d’erreur).*/
@Service
public class PlayerService {
    public List<Player> getAllPlayers() {
        return PlayerList.ALL.stream()
                .sorted(Comparator.comparing(player -> player.rank().position()))
                .collect(Collectors.toList());
    }

    public Player getByLastName(String lastName) {
        return PlayerList.ALL.stream()
                .filter(player -> player.lastName().equals(lastName))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(lastName));
    }

    public Player create(PlayerToSave playerToSave) {
       return getPlayerNewRanking(PlayerList.ALL, playerToSave);

    }
    //====================================================================mettre à jour un joueur====================================================================//
    public Player update(PlayerToSave playertosave) {
        // 1️⃣ Vérifie si un joueur avec ce nom existe déjà dans la liste.
        getByLastName(playertosave.lastName());

        // 2️⃣ Crée une nouvelle liste de joueurs sans celui qu'on souhaite mettre à jour.
        List<Player> playerswithoutPlayertoupdate = PlayerList.ALL.stream()
                .filter(player -> !player.lastName().equals(playertosave.lastName()))
                .toList();

        // 3️⃣ Recalcule le classement en ajoutant le joueur mis à jour et retourne ce joueur avec son nouveau rang.
        // Exemple : si on veut mettre à jour "Alcaraz" avec 8200 points :
        //    a) On vérifie qu'il existe.
        //    b) On le retire de la liste.
        //    c) On l'ajoute avec ses nouveaux points → recalcul du classement.
        //    d) On renvoie le joueur mis à jour avec son nouveau rang.
        return getPlayerNewRanking(playerswithoutPlayertoupdate, playertosave);
    }

    //====================================================================================================================//
    //La méthode n’est accessible que depuis la classe où elle est définie. pour l'utilise dans des autres methodes
    private Player getPlayerNewRanking(List<Player> existingPlayer, PlayerToSave playerToSave) {
        RankingCalculator rankingCalculator = new RankingCalculator(existingPlayer, playerToSave);//le RankingCalculator va recalculer le nouveau classement des joueurs après avoir ajouté ou mis à jour playerToSave.
        List<Player> players = rankingCalculator.getNewPlayersRanking();//Elle renvoie une nouvelle liste de Player, avec les classements recalculés après la mise à jour de playerToSave.
        return players.stream()
                .filter(player -> player.lastName().equals(playerToSave.lastName()))
                .findFirst()
                .get();
        /*
        * 5️⃣ Résumé de la logique

Cette méthode fait exactement ça :

Crée un calculateur de classement pour la liste de joueurs et le joueur à mettre à jour.

Récupère la nouvelle liste de joueurs avec le classement recalculé.

Cherche dans cette liste le joueur correspondant à playerToSave (via son nom de famille).

Retourne ce joueur avec son nouveau classement.*/
    }


}