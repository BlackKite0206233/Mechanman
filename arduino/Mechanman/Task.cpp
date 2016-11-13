#include "Task_h"
#include "Motor.h"

Task::Task() {
    this.motor = Motor();
};

void Task::setTask(int totalTime, int stopTime, int rollingTime) {
    this.totalTime = totalTime;
    this.stopTime = stopTime;
    this.rollingTime = rollingTime;
    this.startingTime = 60;
    flag = true;
};

void Task::timeCount() {
    this.countTime++;
};

void Task::tick() {
    if((this.startingTime - this.countTime > 0 ||
       (this.countTime - 60) % (this.stopTime + this.rollingTime) < this.rollingTime) &&
        this.totalTime - this.countTime >= 60) {
        motor.roll();

        if(!flag)
            flag = true;
    } else {
        if(flag) {
            this.motor.changeDir();
            flag = false;
        }
    }
};

void Task::getMotorV(int *motorArr, int pinNum) {
    for(int i = 0; i < pinNum; i++)
        motorArr[i] = this.motor.getV(i);
}

int Task::timeup() {
    return this.totalTime - this.countTime;
};
