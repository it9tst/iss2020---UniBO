%====================================================================================
% barman description   
%====================================================================================
context(ctxtearoom, "localhost",  "TCP", "9901").
context(ctxbarman, "localhost",  "TCP", "9904").
 qactor( waiter, ctxtearoom, "external").
  qactor( barman, ctxbarman, "it.unibo.barman.Barman").
