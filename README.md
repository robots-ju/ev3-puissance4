# EV3 Puissance 4

Robot qui joue à puissance 4.

## Installation PC

Nécessite un PC (testé uniquement sur Linux) avec java et javacv (sera installé via grudle).

Connecter la webcam au PC.
Les réglages de la webcam et sa grille de scan sont dans `p4.vision.GridCamera`.
`DEVICE_NUMBER` permet de sélectionner l'indice de la webcam à utiliser si plusieurs sont détectées.

Créer le projet Eclipse avec `./gradlew eclipse`.

Exécuter le projet depuis Eclipse.

Le programme doit être lancé sur le robot en premier, puis sur le PC.

## Installation robot

Nécessite ev3dev avec le langage javascript installé.

Copier le programme javascript sur le robot.
Démarrer depuis la ligne de commande ou l'interface.

