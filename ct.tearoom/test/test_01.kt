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
	var client          : ActorBasic? = null    // 3
	val initDelayTime     = 1000L
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxwaiter.main()
			it.unibo.ctxsmartbell.main()
			it.unibo.ctxclient.main()
		}
	}

	@After
	fun terminate() {
		println("%%%  TestState terminate ")
	}
	
	fun checkState(state: String, actor: Int){
		when(actor)	{
			1 -> {
				if( waiter != null ){
					println(" --- stato letto --- ${waiter!!.geResourceRep()}")
					assertTrue( waiter!!.geResourceRep() == "$state")
				}
			}
			
			2 -> {
				if( smartbell != null ){
					println(" --- stato letto --- ${smartbell!!.geResourceRep()}")
					assertTrue( smartbell!!.geResourceRep() == "$state")
				}
			}
			
			3 -> {
				if( client != null ){
					println(" --- stato letto --- ${client!!.geResourceRep()}")
					assertTrue( client!!.geResourceRep() == "$state")
				}
			}
						
			
		}
		
	}
	
	@Test
	fun testStateWaiter(){
	 	runBlocking{
 			while( waiter == null || smartbell == null || client == null ){
				println("waits for waiter ... ")
				delay(initDelayTime)  //time for robot to start
				waiter = it.unibo.kactor.sysUtil.getActor("waiter")
				smartbell = it.unibo.kactor.sysUtil.getActor("smartbell")
				client = it.unibo.kactor.sysUtil.getActor("client")
 			}
			
			MsgUtil.sendMsg(MsgUtil.buildRequest("client","enter_request_client","enter_request_client","smartbell"),smartbell!!)
 			delay(10000)
			checkState("ringBell", 3)
			checkState("checkTempClient", 2)
			println("clicca invio per continuare")
			
			 
			MsgUtil.sendMsg(MsgUtil.buildDispatch("smartbell","smartbell_enter_request","smartbell_enter_request","waiter"),waiter!!)
			delay(25000)
			checkState("clientEnter", 2)
			checkState("accept", 1)
			println("clicca invio per continuare")
			
			
			
			
			
			
			
			
			
			
  		}
	 	println("testWaiter BYE  ")  
	}
	
}