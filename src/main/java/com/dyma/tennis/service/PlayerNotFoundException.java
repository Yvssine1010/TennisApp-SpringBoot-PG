package com.dyma.tennis.service;
/*la couche service lève une exception spécifique à ce cas d'erreur. Le contrôleur REST, qui utilise le service métier, traite cette exception grâce à son gestionnaire d'erreurs qui a été mis à jour.
 */
public class PlayerNotFoundException extends RuntimeException {  /*exception lever lors runtime*/
    public PlayerNotFoundException(String lastName) {
        super("Player not found: " + lastName);
    }
}
