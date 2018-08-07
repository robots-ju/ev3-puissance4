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
        //Blocage des moteurs//
mA.setStopAction("hold");

        //Function sleep//

function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}

while(true){
  if (t1.isPressed == true) {
    mA.reset();
    mA.setStopAction("hold");
    mA.runToPosition(10, 250);
    sleep(500);
    positon +=10
    console.log(positon);
  }
}
