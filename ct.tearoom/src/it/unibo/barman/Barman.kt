/* Generated by AN DISI Unibo */ 
package it.unibo.barman

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Barman ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var List = arrayListOf<Int, arrayListOf<String>()>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("Barman start")
					}
					 transition( edgeName="goto",targetState="waitOrder", cond=doswitch() )
				}	 
				state("waitOrder") { //this:State
					action { //it:State
						println("Barman wait order")
					}
					 transition(edgeName="t00",targetState="getOrder",cond=whenDispatch("takeOrder"))
					transition(edgeName="t01",targetState="endWork",cond=whenDispatch("end"))
				}	 
				state("getOrder") { //this:State
					action { //it:State
						println("Barman is preparing order... ")
						if( checkMsgContent( Term.createTerm("takeOrder(ID,ORD)"), Term.createTerm("takeOrder(ID,ORD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 List.add($payloadArg(0), $payloadArg(1))  
						}
					}
					 transition( edgeName="goto",targetState="orderFinish", cond=doswitch() )
				}	 
				state("orderFinish") { //this:State
					action { //it:State
						println("The order is ready!")
						forward("orderReady", "orderReady(READY)" ,"waiter" ) 
						 List.remove()  
					}
					 transition(edgeName="t12",targetState="waitOrder",cond=whenDispatch("orderReady"))
				}	 
				state("endWork") { //this:State
					action { //it:State
						println("Barman end work")
						terminate(0)
					}
				}	 
			}
		}
}
