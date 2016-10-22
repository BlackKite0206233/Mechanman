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

String ctrl[5] = {""};
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
            if(!ctrl[0].toInt()) {
                act();
                isAct = true;
            } else
                isAct = false;
        }
        if(isAct) {

        }
    } else
        checkConnect();
}

void act() {
    switch(ctrl[1].toInt()) {
        case 0:
            break;
        case 1:
            break;
        case 2:
            break;
        case 3:
            break;
    }
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
