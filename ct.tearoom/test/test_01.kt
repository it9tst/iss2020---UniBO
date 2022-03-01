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
	var waitermind			: ActorBasic? = null    // 1
	var waiterengine		: ActorBasic? = null    // 2
	var smartbell			: ActorBasic? = null 	// 3
	var barman				: ActorBasic? = null 	// 4
	var maxstaytime			: ActorBasic? = null 	// 5
	var maxstaytimetable1	: ActorBasic? = null 	// 6
	var maxstaytimetable2	: ActorBasic? = null 	// 7
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
			
			5 -> {
				if(maxstaytime != null){
					println("TEST |  --- stato letto maxstaytime --- ${maxstaytime!!.geResourceRep()}")
					assertTrue( maxstaytime!!.geResourceRep() == "$state")
				}
			}
			
			6 -> {
				if(maxstaytimetable1 != null){
					println("TEST |  --- stato letto maxstaytimetable1 --- ${maxstaytimetable1!!.geResourceRep()}")
					assertTrue( maxstaytimetable1!!.geResourceRep() == "$state")
				}
			}
			
			7 -> {
				if(maxstaytimetable2 != null){
					println("TEST |  --- stato letto maxstaytimetable2 --- ${maxstaytimetable2!!.geResourceRep()}")
					assertTrue( maxstaytimetable2!!.geResourceRep() == "$state")
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
 			while(waitermind == null || waiterengine == null || smartbell == null || barman == null || maxstaytime == null || maxstaytimetable1 == null || maxstaytimetable2 == null){
				println("waits for actors...")
				delay(initDelayTime)  // time to start
				waitermind = it.unibo.kactor.sysUtil.getActor("waitermind")
				waiterengine = it.unibo.kactor.sysUtil.getActor("waiterengine")
				smartbell = it.unibo.kactor.sysUtil.getActor("smartbell")
				barman = it.unibo.kactor.sysUtil.getActor("barman")
				maxstaytime = it.unibo.kactor.sysUtil.getActor("maxstaytime")
				maxstaytimetable1 = it.unibo.kactor.sysUtil.getActor("maxstaytimetable1")
				maxstaytimetable2 = it.unibo.kactor.sysUtil.getActor("maxstaytimetable2")
 			}
			
			delay(5000)
			
			// Entra il primo cliente
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","enter_request_client","enter_request_client(36.5)","smartbell"),smartbell!!)
			delay(15000)
			
			// Entra il secondo cliente
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","enter_request_client","enter_request_client(36.5)","smartbell"),smartbell!!)
			delay(15000)
			
			// Ci sono due tavoli occupati e un terzo cliente chiede di entrare
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","enter_request_client","enter_request_client(36.5)","smartbell"),smartbell!!)
			delay(10000)
			
			println("TEST | start check MaxWaitingTime value")
			println(waitermind!!.geResourceRep())
			println("TEST | MaxWaitingTime value checked")
			println("TEST | click enter to continue")
			
			// Ci sono due tavoli occupati e un quarto cliente chiede di entrare
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","enter_request_client","enter_request_client(36.5)","smartbell"),smartbell!!)
			delay(10000)
			
			println("TEST | start check MaxWaitingTime value")
			println("TEST | MaxWaitingTime value: " + waitermind!!.geResourceRep())
			println("TEST | MaxWaitingTime value checked")
			println("TEST | click enter to continue")

			delay(5000)
			MsgUtil.sendMsg("end","end","end",maxstaytime!!)
			MsgUtil.sendMsg("end","end","end",maxstaytimetable1!!)
			MsgUtil.sendMsg("end","end","end",maxstaytimetable2!!)
			MsgUtil.sendMsg("end","end","end",smartbell!!)
			MsgUtil.sendMsg("end","end","end",barman!!)
			MsgUtil.sendMsg("end","end","end",waitermind!!)
			delay(5000)
			MsgUtil.sendMsg("end","end","end",waiterengine!!)
			if(waiterengine != null) waiterengine!!.waitTermination()
  		}
	 	println("TEST | testWaiter BYE  ")  
	}
	
}