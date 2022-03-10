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
			
			// Cliente 0 con temperatura di 38 gradi chiede di entrare
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","enterRequestClient","enterRequestClient(38.0)","smartbell"),smartbell!!)
			
			// Cliente 1 con temperatura di 36.5 gradi chiede di entrare
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","enterRequestClient","enterRequestClient(36.5)","smartbell"),smartbell!!)
			delay(15000)
			println("TEST | start check 0,4")
			checkPosition("0", "4")
			println("TEST | position entrance checked")
			println("TEST | click enter to continue")
						
			// Cliente 1 chiede di ordinare
			MsgUtil.sendMsg(MsgUtil.buildDispatch("waitermind","clientReadyToOrder","clientReadyToOrder(1)","waitermind"),waitermind!!)
			delay(25000)
			println("TEST | start check 2,2")
			checkPosition("2", "2")
			println("TEST | position table checked")
			println("TEST | click enter to continue")
			
			// Cliente 1 ordina una cioccolata
			MsgUtil.sendMsg(MsgUtil.buildDispatch("waitermind","clientReadyToOrder","clientReadyToOrder(1, cioccolata)","waitermind"),waitermind!!)
			
			delay(30000)
			println("TEST | start check 6,0")
			checkPosition("6", "0")
			println("TEST | position barman checked")
			println("TEST | click enter to continue")
			delay(20000)
			
			// Cliente 2 con temperatura di 36.5 gradi chiede di entrare
			MsgUtil.sendMsg(MsgUtil.buildRequest("smartbell","enterRequestClient","enterRequestClient(36.5)","smartbell"),smartbell!!)
			delay(15000)
			println("TEST | start check 0,4")
			checkPosition("0", "4")
			println("TEST | position entrance checked")
			println("TEST | click enter to continue")
			
			// Cliente 1 chiede di pagare
			MsgUtil.sendMsg(MsgUtil.buildDispatch("waitermind","clientPayment","clientPayment(1)","waitermind"),waitermind!!)
			delay(10000)
			println("TEST | start check 2,2")
			checkPosition("2", "2")
			println("TEST | position table checked")
			println("TEST | click enter to continue")
			
			
			delay(15000)
			checkPosition("6", "4")
			println("TEST | position exit checked")
			println("TEST | click enter to continue")
			
			
			delay(20000)
			checkPosition("2", "2")
			println("TEST | position table checked")
			println("TEST | click enter to continue")

			delay(20000)
			MsgUtil.sendMsg("end","end","end",maxstaytime!!)
			MsgUtil.sendMsg("end","end","end",maxstaytimetable1!!)
			MsgUtil.sendMsg("end","end","end",maxstaytimetable2!!)
			MsgUtil.sendMsg("end","end","end",smartbell!!)
			MsgUtil.sendMsg("end","end","end",barman!!)
			MsgUtil.sendMsg("end","end","end",waitermind!!)
			MsgUtil.sendMsg("end","end","end",tearoomstatemanager!!)
			delay(5000)
			MsgUtil.sendMsg("end","end","end",waiterengine!!)
			if(waiterengine != null) waiterengine!!.waitTermination()
  		}
	 	println("TEST | testWaiter BYE  ")  
	}
	
}