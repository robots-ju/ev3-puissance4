#!/usr/bin/env node
      //demarrage//
var ev3dev = require('ev3dev-lang');
console.log('Ready !!');

        //Moteur//
var mD = new ev3dev.LargeMotor(ev3dev.OUTPUT_B);
var mG = new ev3dev.LargeMotor(ev3dev.OUTPUT_C);
var directionM = new ev3dev.MediumMotor(ev3dev.OUTPUT_A)
directionM.setStopAction("brake");

        //Capteur//
var ultraObstacle = new ev3dev.UltrasonicSensor(ev3dev.INPUT_3);
var ultraDroite = new ev3dev.UltrasonicSensor(ev3dev.INPUT_1);
var lumiereD = new ev3dev.ColorSensor(ev3dev.INPUT_2);
var lumiereG = new ev3dev.ColorSensor(ev3dev.INPUT_4);

        //déclaration des variables de capteur//

var rotationsens = null;  //1 = droite, 2 = gauche
var vitesse = -200;
var vitesseDirection = 100;

        //déclaration de functions//
function avancer () {
  mG.runForTime(1000, vitesse);
  mD.runForTime(1000, vitesse);
}

function recentrage () {
  if (rotationsens === 2) {
    console.log("RECENTRAGE VERS LA GAUCHE!!");
    directionM.runForTime(2000, vitesseDirection);

  }else if (rotationsens === 1) {
    console.log("RECENTRAGE VERS LA DROITE!!");
    directionM.runForTime(1500, -vitesseDirection);
  }
}

function tournerGauche () {
  directionM.runForTime(2000, -vitesseDirection);
  avancer();
  rotationsens = 2;
  console.log("Gauche !");
  sleep(800);
  console.log("Je dors !!");
  sleep(800);
  recentrage();
}

function tournerDroite () {
  directionM.runForTime(1500, vitesseDirection);
  avancer();
  rotationsens = 1;
  console.log("Droite !");
  sleep(800);
  console.log("Je dors !!");
  sleep(800);
  recentrage();
}

function stopM () {
  mD.stop();
  mG.stop();
}

function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}

        //code//

while (true) {
  console.log(lumiereD.reflectedLightIntensity, lumiereG.reflectedLightIntensity);

  if (lumiereG.reflectedLightIntensity < 30) {
    tournerGauche();
  }
  else if (lumiereD.reflectedLightIntensity < 30) {
    tournerDroite();
  }
  else if (lumiereD.reflectedLightIntensity > 30 && lumiereG.reflectedLightIntensity > 30) {
    console.log("Tout droit !");
    avancer();
  }
  else {
    console.log("Stop !");
    stopM();
  }
}
