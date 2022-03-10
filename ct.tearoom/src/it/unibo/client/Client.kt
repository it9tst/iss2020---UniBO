/* Generated by AN DISI Unibo */ 
package it.unibo.client

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
	
class Client ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var Client_ID = 0
				var Client_Temp = 36.0
				var Client_MaxWaitingTime = 8000L
				var Client_Table = 1
				var Client_Ord: String = ""
				val Menu : Array<String> = arrayOf("the", "acqua", "brioches", "cioccolata")
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("CLIENT | Start")
					}
					 transition( edgeName="goto",targetState="ringBell", cond=doswitch() )
				}	 
				state("ringBell") { //this:State
					action { //it:State
						println("CLIENT | Rings the doorbell")
						
									Client_Temp = Random.nextDouble(35.0, 39.0)
						request("enterRequestClient", "enterRequestClient($Client_Temp)" ,"smartbell" )  
					}
					 transition(edgeName="t00",targetState="enter",cond=whenReply("enterReplyFromSmartbell"))
					transition(edgeName="t01",targetState="goAway",cond=whenReply("enterReplyFromSmartbellNeg"))
					transition(edgeName="t02",targetState="enterWithTime",cond=whenReply("enterReplyFromSmartbellWithTime"))
				}	 
				state("enterWithTime") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("enterReplyFromSmartbellWithTime(ID,MAXWAITINGTIME)"), Term.createTerm("enterReplyFromSmartbellWithTime(ID,MAXWAITINGTIME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  Client_MaxWaitingTime < payloadArg(1).toInt()  
								 ){println("CLIENT | ID: ${payloadArg(0)} | Wait with ${payloadArg(1)} milliseconds")
								}
								else
								 {println("CLIENT | ID: ${payloadArg(0)} | Not Wait")
								 }
						}
					}
					 transition( edgeName="goto",targetState="endWork", cond=doswitch() )
				}	 
				state("goAway") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("enterReplyFromSmartbellNeg(ID)"), Term.createTerm("enterReplyFromSmartbellNeg(ID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("CLIENT | ID: ${payloadArg(0)} | Go away because temperature is KO: $Client_Temp")
						}
					}
					 transition( edgeName="goto",targetState="endWork", cond=doswitch() )
				}	 
				state("enter") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("enterReplyFromSmartbell(ID)"), Term.createTerm("enterReplyFromSmartbell(ID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("CLIENT | ID: ${payloadArg(0)} | Enter")
								 
												Client_ID = payloadArg(0).toInt()
						}
						println("CLIENT | Premi invio per chiedere di ordinare")
						 readLine()  
					}
					 transition( edgeName="goto",targetState="readyToOrder", cond=doswitch() )
				}	 
				state("readyToOrder") { //this:State
					action { //it:State
						println("CLIENT | ID: $Client_ID | Would like to order")
						forward("clientReadyToOrder", "clientReadyToOrder($Client_ID)" ,"waitermind" ) 
						println("CLIENT | Premi invio per ordinare e mangiare")
						 readLine()  
					}
					 transition( edgeName="goto",targetState="order", cond=doswitch() )
				}	 
				state("order") { //this:State
					action { //it:State
						
									Client_Ord = Menu[Random.nextInt(0, 3)]
						println("CLIENT | ID: $Client_ID | Would like to order $Client_Ord")
						forward("clientOrder", "clientOrder($Client_ID,$Client_Ord)" ,"waitermind" ) 
						println("CLIENT | Premi invio per pagare")
						 readLine()  
					}
					 transition( edgeName="goto",targetState="pay", cond=doswitch() )
				}	 
				state("pay") { //this:State
					action { //it:State
						println("CLIENT | ID: $Client_ID | Would like to pay")
						forward("clientPayment", "clientPayment($Client_ID)" ,"waitermind" ) 
					}
					 transition( edgeName="goto",targetState="exit", cond=doswitch() )
				}	 
				state("exit") { //this:State
					action { //it:State
						println("CLIENT || ID: $Client_ID | Exit")
					}
					 transition( edgeName="goto",targetState="endWork", cond=doswitch() )
				}	 
				state("endWork") { //this:State
					action { //it:State
						println("CLIENT | ID: $Client_ID | End work")
						terminate(0)
					}
				}	 
			}
		}
}
