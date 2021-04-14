%====================================================================================
% tearoom description   
%====================================================================================
context(ctxrobotboundary, "localhost",  "TCP", "8018").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
context(ctxclient, "localhost",  "TCP", "8060").
 qactor( barman, ctxrobotboundary, "external").
  qactor( basicrobot, ctxbasicrobot, "external").
