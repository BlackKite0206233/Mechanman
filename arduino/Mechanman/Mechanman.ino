#include <SoftwareSerial.h>
#include <Wire.h>
#include <string.h>

#include "Task.h"

SoftwareSerial BT(5, 4);

const char checkCode[] = "$2a$04$NcywggFZq1ktjuQ7n73l4.Q4KLVp6mGC8kNr7bALqBj2DJXNScFi2";
boolean isConnect = false;

const int buzzer = 12;

const int motor1 = 9;
const int motor2 = 10;
const int motor3 = 11;

int delayTime;

String ctrl[5] = {"", "", "", "", ""};
char c;

boolean isAct = true;
boolean isRead = true;

void setup() {
  // put your setup code here, to run once:

  Serial.begin(9600);
  Serial.println("bluetooth begin");
  BT.begin(9600);

}

void loop() {
  // put your main code here, to run repeatedly:

    if(isConnect) {
        readCtrlCode();
        if(isRead) {
            isRead = false;
            switch(ctrl[0].toInt()) {
                case 0:
                    Serial.println("stop");
                    actStop();
                    break;
                case 1:
                    Serial.println("start");
                    act();
                    break;
                case 2:
                    Serial.println("restart");
                    restart();
                    break;
            }
        }
        if(isAct) {

        } else {
            continue;
        }
    } else {
        checkConnect();
    }
}

void actStop() {
    isAct = false;
}

void act() {
    switch(ctrl[1].toInt())
}

void restart() {

}

void readCtrlCode() {
    int i = 0;
    while(BT.available()) {
        isRead = true;
        delay(5);
        c = BT.read();
        if(c == ':') {
            i++;
            continue;
        }
        ctrl[i] += c;
    }
}

void checkConnect() {
    String code;
    while(BT.available()) {
        delay(5);
        c = BT.read();
        code += c;
    }

    if(code.equals(checkCode)) {
        Serial.println("connect");
        isConnect = true;
    }
    code = "";
}
