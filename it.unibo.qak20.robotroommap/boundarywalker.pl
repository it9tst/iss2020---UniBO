%====================================================================================
% boundarywalker description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/boundarywalker").
context(ctxboundarywalker, "localhost",  "TCP", "8068").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "50800").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( boundarywalker, ctxboundarywalker, "it.unibo.boundarywalker.Boundarywalker").
