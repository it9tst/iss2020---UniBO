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

class Test01 {
	var waiter          : ActorBasic? = null    // 1
	var smartbell       : ActorBasic? = null 	// 2
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
		}
		
	}
	
	@Test
	fun testStateWaiter(){
	 	runBlocking{
 			while(waiter == null || smartbell == null){
				println("waits for waiter...")
				delay(initDelayTime)  // time for robot to start
				waiter = it.unibo.kactor.sysUtil.getActor("waiter")
				smartbell = it.unibo.kactor.sysUtil.getActor("smartbell")
 			}
			
			
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","enter_request_client","enter_request_client","smartbell"),smartbell!!)
			delay(15000)
			checkState("checkTempClient", 2)
			println("TEST | checkTempClient checked")
			println("TEST | click enter to continue")
			 
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","smartbell_enter_request","smartbell_enter_request","waiter"),waiter!!)
			delay(15000)
			checkState("convoyTable", 1)
			println("TEST | convoyTable checked")
			println("TEST | click enter to continue")
			
			MsgUtil.sendMsg(MsgUtil.buildDispatch("waiter","client_ready_to_order","client_ready_to_order","waiter"),waiter!!)
			delay(10000)
			checkState("takeOrder", 1)
			println("TEST | takeOrder checked")
			println("TEST | click enter to continue")
			 
			MsgUtil.sendMsg(MsgUtil.buildDispatch("waiter","client_payment","client_payment","waiter"),waiter!!)
			delay(10000)
			checkState("collectPayment", 1)
			println("TEST | collectPayment checked")
			println("TEST | click enter to continue")
			
			
			if( waiter != null ) waiter!!.waitTermination()
  		}
	 	println("TEST | testWaiter BYE  ")  
	}
	
}