# iss2020ProjectBO | Final Project

### Final Project for exam of Software Systems Engineering M - Ing. Informatica Magistrale - UniBO 2020

Software Lab for Course 72939 - Antonio Natali - DISI - University of Bologna: https://github.com/anatali/iss2020LabBo

### Documentation

[``Sprint 0``](https://htmlview.glitch.me/?https://github.com/it9tst/iss2020ProjectBO/blob/feature/sprint0/doc/sprint0.html)<br />
[``Sprint 1 - Analisi dei Requisiti``](https://htmlview.glitch.me/?https://github.com/it9tst/iss2020ProjectBO/blob/feature/sprint1_an_req/doc/sprint1.html)<br />
[``Sprint 1 - Analisi dei Problemi``](https://htmlview.glitch.me/?https://github.com/it9tst/iss2020ProjectBO/blob/feature/sprint1_an_probl/doc/sprint1.html)<br />
[``Sprint 2 - Analisi dei Requisiti``](https://htmlview.glitch.me/?https://github.com/it9tst/iss2020ProjectBO/blob/feature/sprint2_an_req/doc/sprint2.html)<br />
[``Sprint 2 - Analisi dei Problemi``](https://htmlview.glitch.me/?https://github.com/it9tst/iss2020ProjectBO/blob/feature/sprint2_an_probl/doc/sprint2.html)<br />
[``Sprint 3 - Analisi dei Requisiti``](https://htmlview.glitch.me/?https://github.com/it9tst/iss2020ProjectBO/blob/feature/sprint3_an_req/doc/sprint3.html)<br />
[``Sprint 3 - Analisi dei Problemi``](https://htmlview.glitch.me/?https://github.com/it9tst/iss2020ProjectBO/blob/feature/sprint3_an_probl/doc/sprint3.html)<br />
[``Sprint 4 - Analisi dei Requisiti``](https://htmlview.glitch.me/?https://github.com/it9tst/iss2020ProjectBO/blob/feature/sprint4_an_req/doc/sprint4.html)<br />
[``Sprint 4 - Analisi dei Problemi``](https://htmlview.glitch.me/?https://github.com/it9tst/iss2020ProjectBO/blob/feature/sprint4_an_probl/doc/sprint4.html)

### Requirements

- Eclipse dsl 2020-06
- Kotlin Plugin for Eclipse 0.8.22
- Dropins files that constitute the support to the qak meta-model: [``it.unibo.Qactork.ide_1.2.4.jar``](dropins/it.unibo.Qactork.ide_1.2.4.jar), [``it.unibo.Qactork.ui_1.2.4.jar``](dropins/it.unibo.Qactork.ui_1.2.4.jar), [``it.unibo.Qactork_1.2.4.jar``](dropins/it.unibo.Qactork_1.2.4.jar)
- Gradle 6.2.2
- Java 1.8.0
Linux, Windows and MacOS environments should both work.

### Use (Virtual Robot)

1. Download the latest *distribution.zip* at the [release](https://github.com/it9tst/iss2020ProjectBO/releases) link
2. Extract the distribution folder
3. In the distribution folder there are five zip:
   - ct.tearoom-1.0.zip
   - robotWeb2020-1.0.zip
   - it.unibo.qak20.basicrobot-1.0.zip
   - it.unibo.virtualRobot2020.zip
   - mbot2020.zip
4. Extract the `virtualRobot2020` folder on the PC and follow the instruction to install
5. To run
   ```shell
   cd it.unibo.virtualRobot2020/node/WEnv/server/src
   node main 8999
   ```
6. Extract the `basicrobot` folder on the PC and configure `basicrobotConfig.json` with virtual robot ip
7. To run
   ```shell
   ./it.unibo.qak20.basicrobot-1.0/bin/it.unibo.qak20.basicrobot
   ```
8. Extract the `tearoom` folder on the PC and configure `waiterConfig.json` with stepsize value and `tearoom.pl` with host and port of `ctxbasicrobot`
9. To run
   ```shell
   ./ct.tearoom-1.0/bin/ct.tearoom
   ```
10. Extract the `robotWeb2020` folder on the PC and configure `pageConfig.json` with host and port of `basicrobot`, `tearoom` and `virtualRobot2020`
11. To run
   ```shell
   ./robotWeb2020-1.0/bin/robotWeb2020
   ```
12. Open your browser and type `localhost:50850`
13. Enter the username and password you prefer because a database is not connected
14. Enjoy

### Use (Physical Robot)

1. Download the latest *distribution.zip* at the [release](https://github.com/it9tst/iss2020ProjectBO/releases) link
2. Extract the distribution folder
3. In the distribution folder there are five zip:
   - ct.tearoom-1.0.zip
   - robotWeb2020-1.0.zip
   - it.unibo.qak20.basicrobot-1.0.zip
   - it.unibo.virtualRobot2020.zip
   - mbot2020.zip
4. Extract the `mbot2020` folder and install on the Arduino Physical Robot
5. Extract the `basicrobot` folder on the Raspberry Pi and configure `basicrobotConfig.json` with physical robot ip
6. To run
   ```shell
   ./it.unibo.qak20.basicrobot-1.0/bin/init.sh
   ```
7. Extract the `tearoom` folder on the PC and configure `waiterConfig.json` with stepsize value and `tearoom.pl` with host and port of `ctxbasicrobot`
8. To run
   ```shell
   ./ct.tearoom-1.0/bin/ct.tearoom
   ```
9. Extract the `robotWeb2020` folder on the PC and configure `pageConfig.json` with host and port of `basicrobot`, `tearoom` and `virtualRobot2020`
10. To run
   ```shell
   ./robotWeb2020-1.0/bin/robotWeb2020
   ```
11. Open your browser and type `localhost:50850`
12. Enter the username and password you prefer because a database is not connected
13. Enjoy

[``Video Demo``](/demo/Demo.mp4)

![Logical Architecture](/demo/robot1.jpg)
![Logical Architecture](/demo/robot2.jpg)

![Logical Architecture](/doc/assets/logico.png)

### Authors
[Vittorio Corsale](https://github.com/VittorioCorsale-1)
[Gabriele Tornatore](https://github.com/it9tst)
