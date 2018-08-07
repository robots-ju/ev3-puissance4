#!/usr/bin/env node
        //demarrage//
var ev3dev = require('ev3dev-lang');
console.log("Ready");

        //Création des moteurs//

var mA = new ev3dev.LargeMotor(ev3dev.OUTPUT_A);
var mB = new ev3dev.MediumMotor(ev3dev.OUTPUT_B);

        //Création des moteurs//

var t1 = new ev3dev.TouchSensor(ev3dev.INPUT_2);

        //Blocage des moteurs//

mA.setStopAction("hold");

        //Déclaration de variables//

const speedA = 500;
const rotationsColones = 175;
  var finPartie = false;

        //Function sleep//

function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}

        //Cest ici que tu raconte ta vie au robot//
var distance = 0;

function avancerLeRobot(colone) {
  mA.reset();
  mA.setStopAction("hold");
  var position = calculPositionColone(colone);
  console.log("Postion ou je dois me rendre " + position + " " + "colone: " + colone);
  sleep(1000);
  mA.runToPosition(position , speedA);
  sleep(2500);
  console.log("J'ai fini d'avancer");
  sleep(1000);
  mA.stop();

}

function tomberPiece () {
  mB.reset();
  mB.setStopAction("brake");
  mB.runToPosition(635, 480);
  sleep(2000);
  mB.runToPosition(-635, 480)
  console.log("PION !!");
  sleep(500);
}

function calculPositionColone (colone) {
  var positionRendre = colone * rotationsColones;
  positionRendre = positionRendre + 675;
  return positionRendre
  console.log("Je dois tourner de " + rotationsColones);
}

function retournerBase() {
  while (t1.isPressed == false) {
  mA.runForever(- 200)
  console.log("Je retourne à la base");
    if (t1.isPressed == true) {
      mA.stop();
      mA.reset();
    }
  }
}

while(true) {
  avancerLeRobot(6);
  sleep(500);
  tomberPiece();
  retournerBase();
}
