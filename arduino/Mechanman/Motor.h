#ifndef _Motor_h_
#define _Motor_h_

class Motor {
    public:
        Motor();

        void roll();
        void changeDir();

        int getV(int );
        
        int dir;
        int motor[3];
        int num = 3;
};
#endif
