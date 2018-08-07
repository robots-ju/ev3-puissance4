#!/usr/bin/env node
        //demarrage//
var ev3dev = require('ev3dev-lang');
console.log("Ready");

        //Création des moteurs//

var mA = new ev3dev.LargeMotor(ev3dev.OUTPUT_A);
var mB = new ev3dev.LargeMotor(ev3dev.OUTPUT_B);

        //Création des moteurs//

var t1 = new ev3dev.TouchSensor(ev3dev.INPUT_2);
var positon = 0;
var colone = 6;
var rotationsColones = 180;
var positionRendre = 0;
        //Blocage des moteurs//
mA.setStopAction("hold");

        //CODE//

while (true) {
  positionRendre = colone * rotationsColones;
  positionRendre = positionRendre + 540;
  console.log(positionRendre);
}
