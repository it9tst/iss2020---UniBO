%====================================================================================
% mappingwalker description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/boundarywalker").
context(ctxmappingwalker, "localhost",  "TCP", "8030").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "50800").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( mappingwalker, ctxmappingwalker, "it.unibo.mappingwalker.Mappingwalker").
