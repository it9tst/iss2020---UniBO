%====================================================================================
% barman description   
%====================================================================================
context(ctxtearoom, "localhost",  "TCP", "9901").
 qactor( waiter, ctxtearoom, "external").
  qactor( barman, ctxtearoom, "it.unibo.barman.Barman").
