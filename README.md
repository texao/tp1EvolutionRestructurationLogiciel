## Description
Cette application permet d'analyser du code source Java et de générer des statistiques sur les classes et méthodes, ainsi qu'un graphe d'appel visuel en format `.dot` et `.png`.

## Prérequis
Avant d'utiliser cette application, assurez-vous d'avoir installé les éléments suivants :

- **Java 11** ou une version plus récente
- **Eclipse IDE** 
- [Autres dépendances] (ex : Maven, etc.)

## Installation

### 1. Décompression du projet
Téléchargez et décompressez l'archive du projet.

### 2. Ouvrir le projet
Ouvrez le projet dans Eclipse ou tout autre IDE compatible. 

## Utilisation

### 1. Démarrage de l'application 
Exécutez le fichier "SimpleSwingApp". Ensuite une interface graphique s'affichera.

### Analyse du code :
- Dans l'interface, entrez le chemin complet du répertoire contenant le code source à analyser.

**Exemple :** `/home/utilisateur/eclipse-workspace/tp2hai913/src/main/java/tp2hai913/tp2hai913/classAanalyser`

*Remplacez la partie* `/home/utilisateur/eclipse-workspace` *par votre propre chemin.*


- Cliquez sur le bouton **"Analyser"** pour lancer l'analyse. Les résultats s'afficheront dans l'interface.


### Générer graphe d'appel
- Entrez le chemin complet du fichier Java à analyser pour générer le graphe d'appel.

**Exemple :** `/home/utilisateur/eclipse-workspace/tp2hai913/src/main/java/tp2hai913/tp2hai913/classAanalyser/Colis.java`
  
*Remplacez la partie* `/home/utilisateur/eclipse-workspace` *par votre propre chemin.*


- Cliquez sur **"Afficher Graphe d'appel"**. Un fichier `.dot` et un fichier `.png` seront créés dans le même répertoire que le fichier analysé.

- Rafraîchissez le répertoire du code source à analyser pour voir les fichiers générés.

- Pour visualiser le fichier `.png`, double-cliquez dessus. Vous pouvez cliquer à nouveau sur l'image pour zoomer.
