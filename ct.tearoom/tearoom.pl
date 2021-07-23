%====================================================================================
% tearoom description   
%====================================================================================
context(ctxtearoom, "localhost",  "TCP", "50810").
context(ctxbasicrobot, "192.168.1.20",  "TCP", "50800").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( waitermind, ctxtearoom, "it.unibo.waitermind.Waitermind").
  qactor( waiterengine, ctxtearoom, "it.unibo.waiterengine.Waiterengine").
  qactor( smartbell, ctxtearoom, "it.unibo.smartbell.Smartbell").
  qactor( barman, ctxtearoom, "it.unibo.barman.Barman").
msglogging.
