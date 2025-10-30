package com.dyma.tennis.service;

import com.dyma.tennis.Player;
import com.dyma.tennis.PlayerList;
import com.dyma.tennis.PlayerToSave;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*
 * PlayerService est le service métier pour gérer les joueurs.
 * Concepts clés :
 * 1️⃣ Ce service lance des exceptions spécifiques (PlayerNotFoundException) pour signaler
 *    les erreurs métier au contrôleur.
 * 2️⃣ Le contrôleur ne gère pas ces exceptions directement, il les laisse à Spring,
 *    qui peut les intercepter via un Handler dédié (PlayerControllerErrorHandler)
 *    pour renvoyer une réponse HTTP structurée (ex : 404 Not Found).
 */
@Service
public class PlayerService {

    /*
      Récupère tous les joueurs et les trie par position (classement).

      Concepts
       Stream API : permet de transformer et filtrer des collections de manière fonctionnelle.
       sorted() : trie les joueurs selon leur rang.
       collect(Collectors.toList()) : récupère le résultat final sous forme de liste.
     */
    public List<Player> getAllPlayers() {
        return PlayerList.ALL.stream()
                .sorted(Comparator.comparing(player -> player.rank().position()))
                .collect(Collectors.toList());
    }

    /*
      Cherche un joueur par nom de famille.

      Concepts :
      - filter() : sélectionne les joueurs correspondant à la condition.
      - findFirst() : récupère le premier joueur trouvé (il devrait être unique ici).
      - orElseThrow() : lance une exception si aucun joueur n'est trouvé.
     */
    public Player getByLastName(String lastName) {
        return PlayerList.ALL.stream()
                .filter(player -> player.lastName().equals(lastName))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(lastName));
    }

    /*
      Crée un nouveau joueur et met à jour le classement.
      Concepts :
       On délègue le recalcul du classement à la méthode getPlayerNewRanking().
       La méthode retourne le joueur nouvellement créé avec son rang recalculé.
     */
    public Player create(PlayerToSave playerToSave) {
        return getPlayerNewRanking(PlayerList.ALL, playerToSave);
    }

    //====================================================================
    // Mettre à jour un joueur existant
    //====================================================================
    public Player update(PlayerToSave playertosave) {
        // 1️⃣ Vérifie si le joueur existe. Si non, PlayerNotFoundException sera levée.
        getByLastName(playertosave.lastName());

        // 2️⃣ Crée une liste temporaire sans le joueur à mettre à jour.
        //    Cela permet de recalculer correctement le classement après modification.
        List<Player> playerswithoutPlayertoupdate = PlayerList.ALL.stream()
                .filter(player -> !player.lastName().equals(playertosave.lastName()))
                .toList();

        // 3️⃣ Recalcule le classement en ajoutant le joueur mis à jour.
        //    La logique :
        //    a) On retire le joueur existant.
        //    b) On ajoute le joueur avec ses nouvelles données.
        //    c) On utilise RankingCalculator pour recalculer les positions.
        //    d) On renvoie le joueur mis à jour.
        return getPlayerNewRanking(playerswithoutPlayertoupdate, playertosave);
    }

    //====================================================================
    // Méthode privée pour calculer le nouveau classement
    //====================================================================
    private Player getPlayerNewRanking(List<Player> existingPlayer, PlayerToSave playerToSave) {
        // Crée un RankingCalculator qui gère le recalcul des positions
        RankingCalculator rankingCalculator = new RankingCalculator(existingPlayer, playerToSave);

        // Récupère la nouvelle liste de joueurs avec les rangs recalculés
        List<Player> players = rankingCalculator.getNewPlayersRanking();

        // Cherche le joueur correspondant au PlayerToSave et le retourne
        return players.stream()
                .filter(player -> player.lastName().equals(playerToSave.lastName()))
                .findFirst()
                .get(); // on suppose que le joueur existe toujours
        /*
         * Résumé
         *  Cette méthode encapsule la logique de recalculer du classement pour un joueur.
         *  Elle utilise RankingCalculator pour garder la logique propre et séparée.
         *  Le flux général : créer/calculer la nouvelle liste → trouver le joueur → le retourner.
         */
    }

    /*
      Supprime un joueur par nom de famille.
      Concepts
       getByLastName() : vérifie si le joueur existe avant suppression.
       Stream + filter() : recrée une liste sans le joueur à supprimer.
       RankingCalculator : recalculer du classement après suppression.

      ⚠️ Remarque : dans une vraie application, il vaut mieux utiliser une liste mutable
      ou removeIf() pour éviter de réassigner une référence finale.
     */
    public void delete(String lastName) {
        Player playerToDelete = getByLastName(lastName);

        // Supprime le joueur en filtrant la liste
        PlayerList.ALL = PlayerList.ALL.stream()
                .filter(player -> !player.lastName().equals(lastName))
                .toList();

        // Recalcule le classement après suppression
        RankingCalculator rankingCalculator = new RankingCalculator(PlayerList.ALL);
        rankingCalculator.getNewPlayersRanking();
    }
}
