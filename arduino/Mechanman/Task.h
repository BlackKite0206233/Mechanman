#include "Motor.h"

#ifndef _Task_h_
#define _Task_h_

class Task {
    private:
        int countTime;
        int totalTime;
        int stopTime;
        int rollingTime;
        int startingTime;

        bool flag;

        Motor motor;

    public:
        Task();
        void setTask(int , int , int );
        void timeCount();
        void tick();
        void getMotorV(int *, int );

        int timeup();
};
#endif
