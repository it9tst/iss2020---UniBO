%====================================================================================
% client description   
%====================================================================================
context(ctxclient, "127.0.0.1",  "TCP", "50820").
context(ctxtearoom, "localhost",  "TCP", "50810").
 qactor( waitermind, ctxtearoom, "external").
  qactor( smartbell, ctxtearoom, "external").
  qactor( client, ctxclient, "it.unibo.client.Client").
msglogging.
