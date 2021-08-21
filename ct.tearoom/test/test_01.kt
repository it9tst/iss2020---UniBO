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
	var waitermind      : ActorBasic? = null    // 1
	var waiterengine    : ActorBasic? = null    // 2
	var smartbell       : ActorBasic? = null 	// 3
	var barman       	: ActorBasic? = null 	// 4
	val initDelayTime   = 1000L
	
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
				if(waitermind != null){
					println("TEST |  --- stato letto Waiter  --- ${waitermind!!.geResourceRep()}")
					assertTrue( waitermind!!.geResourceRep() == "$state")
				}
			}
			
			2 -> {
				if(waiterengine != null){
					println("TEST |  --- stato letto Waiter  --- ${waiterengine!!.geResourceRep()}")
					assertTrue( waiterengine!!.geResourceRep() == "$state")
				}
			}
			
			3 -> {
				if(smartbell != null){
					println("TEST |  --- stato letto Smartbell --- ${smartbell!!.geResourceRep()}")
					assertTrue( smartbell!!.geResourceRep() == "$state")
				}
			}
			
			4 -> {
				if(barman != null){
					println("TEST |  --- stato letto Barman --- ${barman!!.geResourceRep()}")
					assertTrue( barman!!.geResourceRep() == "$state")
				}
			}
		}
		
	}
	
	fun checkPosition(x: String, y: String){		
		if( waitermind != null ){
			println("TEST |  --- stato letto Waiter  --- ${waitermind!!.geResourceRep()}")
			assertTrue( waitermind!!.geResourceRep() == "$x,$y")
		}
	}
	
	@Test
	fun testStateWaiter(){
	 	runBlocking{
 			while(waitermind == null || waiterengine == null || smartbell == null || barman == null){
				println("waits for actors...")
				delay(initDelayTime)  // time to start
				waitermind = it.unibo.kactor.sysUtil.getActor("waitermind")
				waiterengine = it.unibo.kactor.sysUtil.getActor("waiterengine")
				smartbell = it.unibo.kactor.sysUtil.getActor("smartbell")
				barman = it.unibo.kactor.sysUtil.getActor("barman")
 			}
			
			
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","enter_request_client","enter_request_client(36.5)","smartbell"),smartbell!!)
			delay(10000)
			checkPosition("0", "4")
			println("TEST | position entrance checked")
			println("TEST | click enter to continue")
						
			
			MsgUtil.sendMsg(MsgUtil.buildDispatch("waitermind","client_ready_to_order","client_ready_to_order(0, cioccolata)","waitermind"),waitermind!!)
			delay(10000)
			checkPosition("2", "2")
			println("TEST | position table checked")
			println("TEST | click enter to continue")
			
			
			delay(15000)
			checkPosition("6", "0")
			println("TEST | position barman checked")
			println("TEST | click enter to continue")
			
			
			MsgUtil.sendMsg(MsgUtil.buildDispatch("waitermind","client_payment","client_payment","waitermind"),waitermind!!)
			delay(25000)
			checkPosition("2", "2")
			println("TEST | position table checked")
			println("TEST | click enter to continue")
			
			
			delay(25000)
			checkPosition("6", "4")
			println("TEST | position exit checked")
			println("TEST | click enter to continue")
			
			
			delay(25000)
			checkPosition("2", "2")
			println("TEST | position table checked")
			println("TEST | click enter to continue")
						
			
			
			delay(5000)
			MsgUtil.sendMsg("end","end","end",waitermind!!)
			MsgUtil.sendMsg("end","end","end",waiterengine!!)
			MsgUtil.sendMsg("end","end","end",smartbell!!)
			MsgUtil.sendMsg("end","end","end",barman!!)
			if( waitermind != null ) waitermind!!.waitTermination()
  		}
	 	println("TEST | testWaiter BYE  ")  
	}
	
}