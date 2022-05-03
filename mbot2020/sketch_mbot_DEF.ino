#include <Arduino.h>
#include <Wire.h>
#include <NewPing.h>
#include <Servo.h>


/*
 * -----------------------------------
 * Connections and settings
 * -----------------------------------
 */
//SONAR PIN
#define trigPin 13
#define echoPin 12
#define MAX_DISTANCE 350          // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm
#define MIN_DISTANCE 15           // The distance (in cm) at which we want sonar to signal the collision

//PWM CAPABLE PIN TO CONTROL MOTOR SPEED: 3, 5, 6, 9, 10, 11
//Motor DX
#define motorPin1EN 5
#define motorPin1A 7
#define motorPin1B 8

//Motor SX
#define motorPin2EN 6
#define motorPin2A 9
#define motorPin2B 10

//LED PIN
#define ledGREEN A0
#define ledRED A1

// ----------------------

int last_speed_DX = 0;
int last_speed_SX = 0;
 
void runDX(int speed);
void runSX(int speed);
void moveBot(int direction, int speed);

NewPing sonar(trigPin, echoPin, MAX_DISTANCE); // NewPing setup of pins and maximum distance.

int input;
int count;

float rotLeftTime  = 0.7;
float rotRightTime = 0.7;
float rotStepTime  = 0.07;

void remoteCmdExecutor();

Servo myservo;
int rightDistance = 0;
int leftDistance = 0;

/*
 * -----------------------------------
 * setup
 * -----------------------------------
 */
void setup(){
    Serial.begin(115200);
    Serial.println("uniboControl start");
    myservo.attach(3);
}

void loop(){
    setLed('G');
    myservo.write(90);
    remoteCmdExecutor();
//    lookAtSonar();
    //lookAtSonarTwo();
}


/*
 * -----------------------------------
 * Obstacles
 * -----------------------------------
 */
float calcDistance(){
    int iterations = 5;
    float duration = sonar.ping_median(iterations);
    return (duration / 2) * 0.0343;
}

void lookAtSonar(){
    float sonar = calcDistance();
    //emit sonar data but with a reduced frequency
    if( count++ > 10 ){
        Serial.println(sonar);
        count = 0;
    }

    if(sonar <= MIN_DISTANCE){ //very near
        moveBot(1,0);  //Stop
        setLed('R');
        Serial.println("OBSTACLE FROM ARDUINO");

        _delay(0.3);
        moveBot(2,100); //backward
        _delay(0.5);
        moveBot(2,0); //Stop
    }
}

void lookAtSonarTwo(){
    float sonar = calcDistance();

    if( count++ > 10 ){
        Serial.println(sonar);
        count = 0;
    }

    if(sonar <= MIN_DISTANCE){
        moveBot(1,0);  //Stop
        setLed('R');
        Serial.println("OBSTACLE FROM ARDUINO");

        _delay(0.3);

        myservo.write(0);
        _delay(1);
        rightDistance = calcDistance();
        _delay(0.5);
        myservo.write(90);
        _delay(1);
        myservo.write(180);
        _delay(1);
        leftDistance = calcDistance();
        _delay(0.5);
        myservo.write(90);
        _delay(1);

        moveBot(2,100); //backward
        _delay(0.5);

        if(rightDistance > leftDistance) {
            rotateRight90();
        } else if(rightDistance < leftDistance) {
            rotateLeft90();
        } else if((rightDistance <= MIN_DISTANCE) || (leftDistance <= MIN_DISTANCE)) {
            moveBot(2,100); //backward
            _delay(0.5);
        } else {
            moveBot(2,0); //Stop
        }
    }
}


/*
 * -----------------------------------
 * delay
 * -----------------------------------
 */
void _loop(){
}

void _delay(float seconds){
    long endTime = millis() + seconds * 1000;
    while(millis() < endTime) _loop();
}


/*
 * -----------------------------------
 * Interpreter
 * -----------------------------------
 */

 /*
  * WARNING: the modification is not permanent
  * This is useful just for tuning
  */
void configureRotationTime(){
    char dir  = Serial.read();
    float v   = Serial.parseFloat();  
    Serial.println( dir == 'l' );
    if( dir == 'l' ) rotLeftTime  = v;
    if( dir == 'r' ) rotRightTime = v;
    if( dir == 'z' ) rotStepTime  = v;
    if( dir == 'x' ) rotStepTime  = v;
    Serial.println( rotLeftTime );
}

void remoteCmdExecutor(){
    if(Serial.available() > 0){
        input = Serial.read();
        //Serial.println(input);
        switch(input){
            case 99  : configureRotationTime(); break;  //c... | cl0.59 or cr0.59  or cx0.005 or cz0.005
            case 119 : moveBot(1,150); break;  //w
            case 115 : moveBot(2,150); break;  //s
            case 97  : moveBot(3,150); break;  //a
            case 100 : moveBot(4,150); break;  //d
            case 114 : rotateRight90();  break;  //r
            case 108 : rotateLeft90(); break;    //l
            case 120 : rotateRightStep();  break; //x
            case 122 : rotateLeftStep();  break;  //z
            case 104 : moveBot(1,0);  break;  //h
            case 102 : moveBot(1,0); break;  //f
            default  : Serial.flush(); //moveBot(1,0);
        }
    }
}

void rotateRight90(){ 
    //Serial.println("rotateRight90");
    moveBot(4,150);
    _delay( rotRightTime );
    moveBot(1,0); //Stop
}

void rotateLeft90(){
    //Serial.println("rotateLeft90");
    moveBot(3,150);
    _delay( rotLeftTime );
    moveBot(1,0); //Stop
}

void rotateRightStep(){ 
    //Serial.println("rotateRightStep");
    moveBot(4,150);
    _delay( rotStepTime );
    moveBot(1,0); //Stop
}

void rotateLeftStep(){
    //Serial.println("rotateRightStep");
    moveBot(3,150);
    _delay( rotStepTime );
    moveBot(1,0); //Stop
}


/*
 * -----------------------------------
 * alternative run DX and SX
 * -----------------------------------
 */
void runDX(int motorSpeed){
    if(motorSpeed >= 0){
          //Set DX motor forward (clockwise)
          digitalWrite(motorPin1A, LOW);
          digitalWrite(motorPin1B, HIGH);

          //Set speed
          analogWrite(motorPin1EN, motorSpeed);

    } else {
          //Set DX motor backward (counter-clockwise)
          digitalWrite(motorPin1A, HIGH);
          digitalWrite(motorPin1B, LOW);

          //Set speed
          analogWrite(motorPin1EN, -motorSpeed);
    }
}

void runSX(int motorSpeed){
    if(motorSpeed >= 0){
        //Set SX motor forward (counter-clockwise)
        digitalWrite(motorPin2A, LOW);
        digitalWrite(motorPin2B, HIGH);

        //Set speed
        analogWrite(motorPin2EN, motorSpeed);
    } else {
        //Set DX motor backward (clockwise)
        digitalWrite(motorPin2A, HIGH);
        digitalWrite(motorPin2B, LOW);

        //Set speed
        analogWrite(motorPin2EN, -motorSpeed);
    }
}


/*
 * -----------------------------------
 * Moving
 * -----------------------------------
 */
void moveBot(int direction, int speed){
      int rightSpeed = 0;
      int leftSpeed  = 0;

      if(direction == 1){ //forward
          rightSpeed = speed;
          leftSpeed = speed;
      }else if(direction == 2){ //backward
          rightSpeed = -speed;
          leftSpeed = -speed;
      }else if(direction == 3){ //left
          rightSpeed = speed;
          leftSpeed = -speed;
      }else if(direction == 4){ //right
          rightSpeed = -speed;
          leftSpeed = speed;
      }

      runDX(rightSpeed);
      runSX(leftSpeed);
}


/*
 * -----------------------------------
 * LED Stuff
 * -----------------------------------
 */
void setLed(char cmd){
    switch(cmd){
        case 'O': analogWrite(ledRED, 0); analogWrite(ledGREEN, 0); break;
        case 'R': analogWrite(ledRED, 1024); analogWrite(ledGREEN, 0); break;
        case 'G': analogWrite(ledRED, 0); analogWrite(ledGREEN, 1024); break;
        default: analogWrite(ledRED, 0); analogWrite(ledGREEN, 0);
    }
}
