# iss2020ProjectBO | SPRINT 4 - Analisi dei Requisiti

### Final Project for exam of Software Systems Engineering M - Ing. Informatica Magistrale - UniBO 2020

Software Lab for Course 72939 - Antonio Natali - DISI - University of Bologna: https://github.com/anatali/iss2020LabBo

### Documentation

[``Sprint 3 - Analisi dei Problemi``](https://htmlview.glitch.me/?https://github.com/it9tst/iss2020ProjectBO/blob/feature/sprint4_an_req/doc/sprint3.html)

### Requirements

- Eclipse dsl 2020-06
- Kotlin Plugin for Eclipse 0.8.22
- Dropins files that constitute the support to the qak meta-model: [``it.unibo.Qactork.ide_1.2.4.jar``](dropins/it.unibo.Qactork.ide_1.2.4.jar), [``it.unibo.Qactork.ui_1.2.4.jar``](dropins/it.unibo.Qactork.ui_1.2.4.jar), [``it.unibo.Qactork_1.2.4.jar``](dropins/it.unibo.Qactork_1.2.4.jar)
- Gradle 6.2.2
- Java 1.8.0

### Use

1. In an empty workspace, create a new project of type Java Project (e.g. ct.tearoom)
2. In workspace folder, copy [``unibolibs``](unibolibs) directory
3. In src folder, copy [``client.qak``](ct.tearoom/src/client.qak) and [``tearoom.qak``](ct.tearoom/src/tearoom.qak)
4. In test folder, copy [``test_01.kt``](ct.tearoom/test/test_01.kt)
5. Open terminal in workspace folder and run command ``gradle -b build_ctxtearoom.gradle eclipse``
6. Select Configure Kotlin -> Add Kotlin nature

### Authors
[Vittorio Corsale](https://github.com/VittorioCorsale-1)
[Gabriele Tornatore](https://github.com/it9tst)