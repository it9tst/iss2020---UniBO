%====================================================================================
% client description   
%====================================================================================
context(ctxtearoom, "localhost",  "TCP", "9901").
context(ctxclient, "localhost",  "TCP", "9902").
 qactor( smartbell, ctxtearoom, "external").
  qactor( waiter, ctxtearoom, "external").
  qactor( client, ctxclient, "it.unibo.client.Client").
