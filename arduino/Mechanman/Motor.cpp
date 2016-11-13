#include "Motor.h"

Motor::Motor() {
    dir = 1;
    motor[0] = 0;
    motor[1] = 120;
    motor[2] = 240;
};

void Motor::roll() {
    motor[0] = (motor[0] >= 360) ? 0 : motor[0] + 1;
    motor[1] = (motor[1] >= 360) ? 0 : motor[1] + 1;
    motor[2] = (motor[2] >= 360) ? 0 : motor[2] + 1;
};

void Motor::changeDir() {
    dir *= -1;
};

int Motor::getV(int num) {
    return motor[((dir > 0) ? num : 2 - num)];

}
