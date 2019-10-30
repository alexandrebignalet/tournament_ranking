# tournament_ranking

## mise en route

Docker et docker-compose doivent être installés sur la machine.
Java doit être présent sur la machine en version 8.

```
git clone git@github.com:alexandrebignalet/tournament_ranking.git
```

```
cd tournament_ranking
./launch.sh
```

Le front est disponible sur [localhost:8082](http://localhost:8082)

Le serveur expose une ressource REST sur [localhost:3000/tournament/competitors](http://localhost:3000/tournament/competitors) consommant et produisant du `application/json`
```js 
POST / avec { "pseudo": "a_pseudo" }
``` 

```js 
PUT /{pseudo} avec { "points": Number }
``` 

```js 
GET /
``` 

```js 
GET /{pseudo}
``` 

```js 
DELETE /
``` 

## sujet
Réalisation d'une application exposant une API REST pour gérer le classement de joueurs lors d'un tournoi.

Les joueurs sont triés en fonction du nombre de points de chacun, du joueur ayant le plus de points à celui qui en a le moins.

L'API devra permettre :
* d'ajouter un joueur (son pseudo)
* de mettre à jour le nombre de points du joueur
* de récupérer les données d'un joueur (pseudo, nombre de points et classement dans le tournoi)
* de retourner les joueurs triés en fonction de leur nombre de points
* de supprimer tous les joueurs à la fin du tournoi

L'application devra être réalisé en Kotlin, pourra utiliser le framework d'injection Dagger2 (Optionnel), et basé sur Dropwizard.
L'application pourra utiliser la technologie de base de donnée de votre choix, de préférence NoSQL, idéalement DynamoDB (difficile).

Le service devra s'initialiser et se lancer par un script bash.

Vous devrez développer un test unitaire.

Bonus :
Fournir une interface d'administration (une UI) permettant de visualiser les joueurs présents dans l'application; en Angular.

Livraison via Git.