#!/usr/bin/env node

//demarrage//

var ev3dev = require('ev3dev-lang');
console.log("Ready");
const net = require('net');

//Serveur//

const socket = net.createServer(function (client) {
    console.log("Client connect");

    client.on('end', function () {
        console.log('Client disconnect');
    });

    client.on("data", function (buffer) {
        console.log('Reçu', buffer);

        if (buffer.length !== 1) {
            console.log('Buffer pas valide !');

            return;
        }

        console.log(buffer.readUInt8(0));

        avancerLeRobot(buffer.readUInt8(0));
        tomberPiece();
        retournerBase();

        client.write(new Buffer([0x01]));
    });
});

socket.listen(8953, function () {
    console.log("Socket ready !!");
});

socket.on('error', function (error) {
    throw error;
});

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

//Function sleep//

function sleep(milliseconds) {
    var start = new Date().getTime();
    for (var i = 0; i < 1e7; i++) {
        if ((new Date().getTime() - start) > milliseconds) {
            break;
        }
    }
}

function avancerLeRobot(colone) {
    mA.reset();
    mA.setStopAction("hold");

    var position = calculPositionColone(colone);

    console.log("Postion ou je dois me rendre " + position + " " + "colone: " + colone);

    sleep(1000);
    mA.runToPosition(position, speedA);
    sleep(2500);

    console.log("J'ai fini d'avancer");

    sleep(1000);
    mA.stop();
}

function tomberPiece() {
    mB.reset();
    mB.setStopAction("brake");
    mB.runToPosition(635, 480);

    sleep(2000);

    mB.runToPosition(0, 480);

    console.log("PION !!");

    sleep(500);
}

function calculPositionColone(colone) {
    var positionRendre = colone * rotationsColones;

    return positionRendre + 675;
}

function retournerBase() {
    console.log("Je retourne à la base");

    mA.runForever(-200);

    while (true) {
        if (t1.isPressed) {
            mA.stop();
            mA.reset();

            break;
        }
    }
}

// Commencer par s'aligner avec la base
retournerBase();
