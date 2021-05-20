/* Generated by AN DISI Unibo */ 
package it.unibo.smartbell

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Smartbell ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				val Temp_max = 37.5
				var Client_temp = 0.0
				var Id_client = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("SMARTBELL | Start")
						updateResourceRep( "s0 smartbell"  
						)
					}
					 transition( edgeName="goto",targetState="waitRing", cond=doswitch() )
				}	 
				state("waitRing") { //this:State
					action { //it:State
						println("SMARTBELL | Wait ring")
						updateResourceRep( "waitRing"  
						)
					}
					 transition(edgeName="t03",targetState="checkTempClient",cond=whenRequest("enter_request_client"))
				}	 
				state("checkTempClient") { //this:State
					action { //it:State
						println("SMARTBELL | Check temp client")
						updateResourceRep( "checkTempClient"  
						)
						if( checkMsgContent( Term.createTerm("enter_request_client(TEMP)"), Term.createTerm("enter_request_client(TEMP)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("SMARTBELL | Entry request from CLIENT with ID: $Id_client and TEMP: ${payloadArg(0)}")
								 Client_temp = payloadArg(0).toDouble()  
						}
						if(  Client_temp < Temp_max  
						 ){println("SMARTBELL | The client can enter")
						request("smartbell_enter_request", "smartbell_enter_request($Id_client)" ,"waiter" )  
						}
						 readLine()  
					}
					 transition(edgeName="t14",targetState="clientEnter",cond=whenReply("client_accept"))
				}	 
				state("clientEnter") { //this:State
					action { //it:State
						updateResourceRep( "clientEnter"  
						)
						answer("enter_request_client", "enter_reply_from_smartbell", "enter_reply_from_smartbell($Id_client)"   )  
						 Id_client++  
					}
				}	 
				state("endWork") { //this:State
					action { //it:State
						println("SMARTBELL | End work")
						updateResourceRep( "endWork"  
						)
						terminate(0)
					}
				}	 
			}
		}
}
