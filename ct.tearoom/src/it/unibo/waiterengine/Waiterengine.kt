/* Generated by AN DISI Unibo */ 
package it.unibo.waiterengine

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Waiterengine ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				// Robot
				var StepTime = 260L
				val BackTime = 2 * StepTime / 3
				
				// Map
				val mapRoom = "teaRoomExplored"
				var XPoint = "0"
				var YPoint = "0"
		
				// Table state delay
				val TableClearTime = 6000L
				val TableCleanTime = 6000L
				val TableSanitizedTime = 5000L
		
				var CmdToMove = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("WAITERENGINE | Start")
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.plannerUtil.loadRoomMap( mapRoom  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						forward("engineReady", "engineReady(PAYLOAD)" ,"waitermind" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						println("WAITERENGINE | waitCmd")
					}
					 transition(edgeName="t046",targetState="planDestination",cond=whenDispatch("moveTo"))
					transition(edgeName="t047",targetState="waitCmd",cond=whenDispatch("stopMove"))
					transition(edgeName="t048",targetState="cleanTable",cond=whenRequest("clean"))
					transition(edgeName="t049",targetState="endWork",cond=whenDispatch("end"))
				}	 
				state("planDestination") { //this:State
					action { //it:State
						println("WAITERENGINE | planDestination")
						if( checkMsgContent( Term.createTerm("moveTo(X,Y)"), Term.createTerm("moveTo(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												XPoint = payloadArg(0)
											    YPoint = payloadArg(1)			  
						}
						println("WAITERENGINE | Start moveTo ($XPoint, $YPoint)")
						itunibo.planner.plannerUtil.planForGoal( "$XPoint", "$YPoint"  )
					}
					 transition( edgeName="goto",targetState="readStep", cond=doswitch() )
				}	 
				state("readStep") { //this:State
					action { //it:State
						println("WAITERENGINE | readStep")
						 
									CmdToMove = itunibo.planner.plannerUtil.getNextPlannedMove()
					}
					 transition( edgeName="goto",targetState="execStep", cond=doswitchGuarded({ CmdToMove == "w"  
					}) )
					transition( edgeName="goto",targetState="execMove", cond=doswitchGuarded({! ( CmdToMove == "w"  
					) }) )
				}	 
				state("execStep") { //this:State
					action { //it:State
						println("WAITERENGINE | execStep")
						request("step", "step($StepTime)" ,"basicrobot" )  
					}
					 transition(edgeName="t150",targetState="updateMap",cond=whenReply("stepdone"))
					transition(edgeName="t151",targetState="errorHandler",cond=whenReply("stepfail"))
				}	 
				state("execMove") { //this:State
					action { //it:State
						println("WAITERENGINE | Exec Move")
						forward("cmd", "cmd($CmdToMove)" ,"basicrobot" ) 
						delay(200) 
					}
					 transition( edgeName="goto",targetState="updateMap", cond=doswitch() )
				}	 
				state("updateMap") { //this:State
					action { //it:State
						println("WAITERENGINE | updateMap")
						updateResourceRep( itunibo.planner.plannerUtil.getMapOneLine()  
						)
						itunibo.planner.plannerUtil.updateMap( "$CmdToMove"  )
					}
					 transition( edgeName="goto",targetState="checkStopEngine", cond=doswitchGuarded({ CmdToMove.length > 0  
					}) )
					transition( edgeName="goto",targetState="endDestination", cond=doswitchGuarded({! ( CmdToMove.length > 0  
					) }) )
				}	 
				state("checkStopEngine") { //this:State
					action { //it:State
						println("WAITERENGINE | checkStopEngine")
						stateTimer = TimerActor("timer_checkStopEngine", 
							scope, context!!, "local_tout_waiterengine_checkStopEngine", 100.toLong() )
					}
					 transition(edgeName="t252",targetState="readStep",cond=whenTimeout("local_tout_waiterengine_checkStopEngine"))   
					transition(edgeName="t253",targetState="stopEngine",cond=whenDispatch("stopMove"))
				}	 
				state("stopEngine") { //this:State
					action { //it:State
						println("WAITERENGINE | stopEngine")
						itunibo.planner.plannerUtil.resetActions(  )
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("errorHandler") { //this:State
					action { //it:State
						println("WAITERENGINE | errorHandler")
						if( checkMsgContent( Term.createTerm("stepfail(DURATION,CAUSE)"), Term.createTerm("stepfail(DURATION,CAUSE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												val D = payloadArg(0).toLong()
												val Dt = Math.abs(StepTime - D)
												val BackT = D/2
								println("WAITERENGINE | Robotmapper stepFail - D = $D, BackTime = ${BackTime}, BackT = $BackT")
								if(  D > BackTime  
								 ){forward("cmd", "cmd(s)" ,"basicrobot" ) 
								delay(BackT)
								forward("cmd", "cmd(h)" ,"basicrobot" ) 
								}
								itunibo.planner.plannerUtil.showCurrentRobotState(  )
								delay(500) 
						}
					}
					 transition( edgeName="goto",targetState="updateMap", cond=doswitch() )
				}	 
				state("cleanTable") { //this:State
					action { //it:State
						println("WAITERENGINE | cleanTable")
						if( checkMsgContent( Term.createTerm("clean(TABLE,STATE)"), Term.createTerm("clean(TABLE,STATE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												val TableSelectedToClean = payloadArg(0).toInt()
												
												when(payloadArg(1).toInt()) {
													1 -> {
								delay(TableClearTime)
								println("WAITERENGINE | tableCleared")
								forward("setTableState", "setTableState($TableSelectedToClean,tableCleared)" ,"tearoomstatemanager" ) 
								
													}
													
													2 -> {
								delay(TableCleanTime)
								println("WAITERENGINE | tableCleaned")
								forward("setTableState", "setTableState($TableSelectedToClean,tableCleaned)" ,"tearoomstatemanager" ) 
								
													}
													
													3 -> {
								delay(TableSanitizedTime)
								println("WAITERENGINE | tableSanitized")
								forward("setTableState", "setTableState($TableSelectedToClean,tableSanitized)" ,"tearoomstatemanager" ) 
								
													}
												}
						}
						answer("clean", "cleanDone", "cleanDone(PAYLOAD)"   )  
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("endDestination") { //this:State
					action { //it:State
						println("WAITERENGINE | endDestination")
						println("WAITERENGINE | Done moveTo ($XPoint, $YPoint)")
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						forward("done", "done($XPoint,$YPoint)" ,"waitermind" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("endWork") { //this:State
					action { //it:State
						println("WAITERENGINE | End work")
						terminate(0)
					}
				}	 
			}
		}
}
