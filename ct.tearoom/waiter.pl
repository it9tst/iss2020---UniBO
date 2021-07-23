%====================================================================================
% waiter description   
%====================================================================================
context(ctxtearoom, "localhost",  "TCP", "9901").
context(ctxclient, "localhost",  "TCP", "9902").
context(ctxsmartbell, "localhost",  "TCP", "9903").
context(ctxbarman, "localhost",  "TCP", "9904").
context(ctxwaiter, "localhost",  "TCP", "9905").
 qactor( client, ctxtearoom, "external").
  qactor( smartbell, ctxtearoom, "external").
  qactor( barman, ctxtearoom, "external").
  qactor( waiter, ctxwaiter, "it.unibo.waiter.Waiter").
