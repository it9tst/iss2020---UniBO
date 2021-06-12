package test

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.MqttUtils
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.ApplMessageType

class Test03 {
	var waiter          : ActorBasic? = null    // 1
	var smartbell       : ActorBasic? = null 	// 2
	var barman       	: ActorBasic? = null 		// 3
	val initDelayTime     = 1000L
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxtearoom.main()
		}
	}
	
	@After
	fun terminate() {
		println("TEST | %%%  TestState terminate")
	}
	
	fun checkState(state: String, actor: Int){
		when(actor)	{
			1 -> {
				if(waiter != null){
					println("TEST |  --- stato letto Waiter  --- ${waiter!!.geResourceRep()}")
					assertTrue( waiter!!.geResourceRep() == "$state")
				}
			}
			
			2 -> {
				if(smartbell != null){
					println("TEST |  --- stato letto Smartbell --- ${smartbell!!.geResourceRep()}")
					assertTrue( smartbell!!.geResourceRep() == "$state")
				}
			}
			
			3 -> {
				if(barman != null){
					println("TEST |  --- stato letto Barman --- ${barman!!.geResourceRep()}")
					assertTrue( barman!!.geResourceRep() == "$state")
				}
			}
		}
		
	}
	
	@Test
	fun testStateWaiter(){
	 	runBlocking{
 			while(waiter == null || smartbell == null || barman == null){
				println("waits for actors...")
				delay(initDelayTime)  // time to start
				waiter = it.unibo.kactor.sysUtil.getActor("waiter")
				smartbell = it.unibo.kactor.sysUtil.getActor("smartbell")
				barman = it.unibo.kactor.sysUtil.getActor("barman")
 			}
			
			
			MsgUtil.sendMsg(MsgUtil.buildRequest("waiter","smartbell_enter_request","smartbell_enter_request","waiter"),waiter!!)
			delay(5000)
			checkState("accept", 1)
			println("TEST | accept checked")
			println("TEST | click enter to continue")
			
			MsgUtil.sendMsg(MsgUtil.buildRequest("waiter","smartbell_enter_request","smartbell_enter_request","waiter"),waiter!!)
			delay(5000)
			checkState("accept", 1)
			println("TEST | accept checked")
			println("TEST | click enter to continue")
			
			

			
			MsgUtil.sendMsg(MsgUtil.buildRequest("waiter","smartbell_enter_request","smartbell_enter_request","waiter"),waiter!!)
			delay(5000)
			checkState("convoyTable", 1)
			println("TEST | accept checked")
			println("TEST | click enter to continue")
			
			MsgUtil.sendMsg(MsgUtil.buildRequest("waiter","inform_maxwaittime","inform_maxwaittime","waiter"),waiter!!)
			delay(5000)
			checkState("inform", 1)
			println("TEST | inform checked")
			println("TEST | click enter to continue")
					
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","client_accept_with_time","client_accept_with_time","smartbell"),waiter!!)
			delay(5000)
			checkState("clientEnterWithTime", 2)
			println("TEST | clientEnterWithTime checked")
			println("TEST | click enter to continue")
			

			delay(5000)
			MsgUtil.sendMsg("end","end","end",waiter!!)
			MsgUtil.sendMsg("end","end","end",smartbell!!)
			MsgUtil.sendMsg("end","end","end",barman!!)
			if( waiter != null ) waiter!!.waitTermination()
  		}
	 	println("TEST | testWaiter BYE  ")  
	}	
}