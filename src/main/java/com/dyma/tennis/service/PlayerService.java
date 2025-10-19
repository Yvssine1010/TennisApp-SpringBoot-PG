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
        getByLastName(playertosave.lastName());//Cette méthode vérifie que le joueur existe déjà dans la liste.

        List<Player> playerswithoutPlayertoupdate=PlayerList.ALL.stream() //Création de la liste sans le joueur à mettre à jour
                .filter(player -> !player.lastName().equals(playertosave.lastName()))
                .toList();

        RankingCalculator rankingCalculator = new RankingCalculator(playerswithoutPlayertoupdate, playertosave);
        List<Player> players = rankingCalculator.getNewPlayersRanking();

        return players.stream() //Une fois le classement recalculé, on cherche dans la nouvelle liste le joueur correspondant et On retourne donc le joueur mis à jour avec son nouveau classement.
                .filter(player -> player.lastName().equals(playertosave.lastName()))
                .findFirst().get();
        /*Tu veux mettre à jour Alcaraz avec 8200 points :

1️⃣ On vérifie qu’il existe.
2️⃣ On retire Alcaraz de la liste.
3️⃣ On ajoute le nouveau Alcaraz (avec 8200 pts) → recalcul du classement.
4️⃣ On renvoie le nouvel Alcaraz avec son nouveau rang.*/
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