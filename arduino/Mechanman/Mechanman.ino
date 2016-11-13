#include <SoftwareSerial.h>
#include <Wire.h>
#include <string.h>
#include <math.h>

#include "Task.h"

#define SPEED(speed) 1000000 / (36 * (60 + speed))

#define RX 5
#define TX 4

#define checkCode "$2a$04$NcywggFZq1ktjuQ7n73l4.Q4KLVp6mGC8kNr7bALqBj2DJXNScFi2"

#define BUZZER 12

#define MOTOR1 9
#define MOTOR2 10
#define MOTOR3 11

#define V(motorV) 127 * (1 + sin(motorV * M_PI / 180);

SoftwareSerial BT(RX, TX);

boolean isConnect = false;

String ctrl[6] = {""};
char c;

boolean isAct = true;
boolean isRead = true;

Task task = Task();

int motorV[3] = {0};
int delayTime;
unsigned long long tickTime = 0;
unsigned long long countTime = 0;

void setup() {
  pinMode(BUZZER, OUTPUT);
  pinMode(MOTOR1, OUTPUT);
  pinMode(MOTOR2, OUTPUT);
  pinMode(MOTOR3, OUTPUT);

  Serial.begin(9600);
  Serial.println("bluetooth begin");
  BT.begin(9600);

  dir = 0;
}

void loop() {
    if(isConnect) {
        readCtrlCode();
        if(isRead) {
            isRead = false;
            if(!ctrl[0].toInt()) {
                setAct(ctrl[1].toInt());
                isAct = true;
            } else
                isAct = false;
        }
        if(isAct) {
            if(!timeCount)
                tickTime = countTime = micros();

            if(micros() - tickTime >= delayTime) {
                tickTime = micros();
                task.tick();
            }

            if(micros() - countTime >= 1000000) {
                task.timeCount();
                countTime = micros();
            }

            if(task.timeup() <= 10 && micros() % 1000000 < 50)
                digitalWrite(BUZZER, HIGH);
            else
                digitalWrite(BUZZER, LOW);

            if(!task.timeup())
                isAct = false;

            task.getMotorV(motorV, 3);

            analogWrite(MOTOR1, V(motorV[0]));
            analogWrite(MOTOR2, V(motorV[1]));
            analogWrite(MOTOR3, V(motorV[2]));
        }
    } else
        checkConnect();
}

void setAct(int type) {
    switch(type) {
        case 1:
            task.setTask(ctrl[2], ctrl[3], ctrl[4]);
            delayTime = SPEED(ctrl[5].toInt());
            break;
        case 2:
            task.setTask(ctrl[2], 50, 10);
            delayTime = SPEED(0);
            break;
        default:
            task.setTask(ctrl[2], 0, ctrl[2]);
            delayTime = SPEED(0);
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
