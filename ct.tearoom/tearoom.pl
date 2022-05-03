%====================================================================================
% tearoom description   
%====================================================================================
context(ctxtearoom, "localhost",  "TCP", "50810").
context(ctxbasicrobot, "192.168.10.160",  "TCP", "50800").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( waitermind, ctxtearoom, "it.unibo.waitermind.Waitermind").
  qactor( waiterengine, ctxtearoom, "it.unibo.waiterengine.Waiterengine").
  qactor( smartbell, ctxtearoom, "it.unibo.smartbell.Smartbell").
  qactor( barman, ctxtearoom, "it.unibo.barman.Barman").
  qactor( maxstaytime, ctxtearoom, "it.unibo.maxstaytime.Maxstaytime").
  qactor( maxstaytimetable1, ctxtearoom, "it.unibo.maxstaytimetable1.Maxstaytimetable1").
  qactor( maxstaytimetable2, ctxtearoom, "it.unibo.maxstaytimetable2.Maxstaytimetable2").
  qactor( tearoomstatemanager, ctxtearoom, "it.unibo.tearoomstatemanager.Tearoomstatemanager").
msglogging.
