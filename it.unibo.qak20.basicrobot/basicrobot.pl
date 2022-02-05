%====================================================================================
% basicrobot description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/boundarywalker").
context(ctxbasicrobot, "localhost",  "TCP", "50800").
 qactor( datacleaner, ctxbasicrobot, "rx.dataCleaner").
  qactor( distancefilter, ctxbasicrobot, "rx.distanceFilter").
  qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
msglogging.
