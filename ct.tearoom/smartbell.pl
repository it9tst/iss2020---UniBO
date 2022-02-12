%====================================================================================
% smartbell description   
%====================================================================================
context(ctxtearoom, "localhost",  "TCP", "9901").
context(ctxclient, "localhost",  "TCP", "9902").
context(ctxsmartbell, "localhost",  "TCP", "9903").
 qactor( client, ctxtearoom, "external").
  qactor( waiter, ctxtearoom, "external").
  qactor( smartbell, ctxsmartbell, "it.unibo.smartbell.Smartbell").
