%====================================================================================
% tearoom description   
%====================================================================================
context(ctxtearoom, "localhost",  "TCP", "50810").
 qactor( waiter, ctxtearoom, "it.unibo.waiter.Waiter").
  qactor( smartbell, ctxtearoom, "it.unibo.smartbell.Smartbell").
msglogging.
