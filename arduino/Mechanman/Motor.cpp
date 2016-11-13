#include "Motor.h"

Motor::Motor() {
    this.dir = 1;
    this.Motor[0] = 0;
    this.Motor[1] = 120;
    this.Motor[2] = 240;
};

void Motor::roll() {
    this.Motor[0] = (this.Motor[0] >= 360) ? 0 : this.Motor[0] + 1;
    this.Motor[1] = (this.Motor[1] >= 360) ? 0 : this.Motor[1] + 1;
    this.Motor[2] = (this.Motor[2] >= 360) ? 0 : this.Motor[2] + 1;
};

void Motor::changeDir() {
    this.dir *= -1;
};

int Motor::getV(int num) {
    return this.Motor[((dir > 0) ? num : 2 - num)];

}
