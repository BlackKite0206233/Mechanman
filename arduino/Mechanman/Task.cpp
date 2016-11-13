#include "Task.h"
#include "Motor.h"

Task::Task() {
    motor = Motor();
};

void Task::setTask(int totalTime, int stopTime, int rollingTime) {
    this->totalTime = totalTime;
    this->stopTime = stopTime;
    this->rollingTime = rollingTime;
    this->startingTime = 60;
    flag = true;
};

void Task::timeCount() {
    countTime++;
};

void Task::tick() {
    if((startingTime - countTime > 0 ||
       (countTime - 60) % (stopTime + rollingTime) < rollingTime) &&
        totalTime - countTime >= 60) {
        motor.roll();

        if(!flag)
            flag = true;
    } else {
        if(flag) {
            motor.changeDir();
            flag = false;
        }
    }
};

void Task::getMotorV(int *motorArr, int pinNum) {
    for(int i = 0; i < pinNum; i++)
        motorArr[i] = motor.getV(i);
}

int Task::timeup() {
    return totalTime - countTime;
};
