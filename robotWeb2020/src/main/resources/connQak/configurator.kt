package connQak

import org.json.JSONObject
import java.io.File
import java.net.InetAddress
//import java.nio.charset.Charset
//import org.apache.commons.io.Charsets


object configurator {
	// Page
	@JvmStatic public var pageTemplate		= "login"
	
	// MQTT broker	
	//@JvmStatic var mqtthostAddr    	= "broker.hivemq.com"
	@JvmStatic var mqtthostAddr    	= "localhost"
	@JvmStatic var mqttport    		= "1883"

	// Domains application
	@JvmStatic var hostAddr   	    = ""
	@JvmStatic var port    			= ""
	@JvmStatic var qakdest	     	= ""
	@JvmStatic var ctxqadest 		= ""
	
	// Basicrobot application
	@JvmStatic var hostAddrBasicRobot	= ""
	@JvmStatic var portBasicRobot		= ""
	@JvmStatic var qakdestBasicRobot	= ""
	@JvmStatic var ctxqadestBasicRobot	= ""
	@JvmStatic var stepsize				= ""
	
	// Virtualrobot application
	@JvmStatic var hostAddrVirtualRobot	= ""
	
	@JvmStatic	//to be used by Java
	fun configure(){
		try {
			val configfile =   File("pageConfig.json")
			val config     =   configfile.readText()	//charset: Charset = Charsets.UTF_8
			//println( "		--- configurator | config=$config" )
			val jsonObject	=  JSONObject(config)			
			pageTemplate 		 =  jsonObject.getString("page")
			
			hostAddr    		 =  jsonObject.getString("host")
			port    			 =  jsonObject.getString("port")
			qakdest         	 =  jsonObject.getString("qakdest")
			ctxqadest			 =  jsonObject.getString("ctxqadest")
			
			hostAddrBasicRobot	 =  jsonObject.getString("hostAddrBasicRobot")
			portBasicRobot		 =  jsonObject.getString("portBasicRobot")
			qakdestBasicRobot	 =  jsonObject.getString("qakdestBasicRobot")
			ctxqadestBasicRobot	 =  jsonObject.getString("ctxqadestBasicRobot")
			stepsize			 =  jsonObject.getString("stepsize")
			
			hostAddrVirtualRobot =  jsonObject.getString("hostAddrVirtualRobot")
			
			System.out.println("System IP Address : " + (InetAddress.getLocalHost().getHostAddress()).trim());
			System.out.println("--- configurator | configfile path=${configfile.getPath()} pageTemplate=$pageTemplate hostAddr=$hostAddr port=$port qakdest=$qakdest ctxqadest=$ctxqadest hostAddrBasicRobot=$hostAddrBasicRobot portBasicRobot=$portBasicRobot qakdestBasicRobot=$qakdestBasicRobot ctxqadestBasicRobot=$ctxqadestBasicRobot stepsize=$stepsize hostAddrVirtualRobot=$hostAddrVirtualRobot")
		
		} catch(e:Exception) {
			System.out.println("&&& SORRY pageConfig.json NOT FOUND")
			pageTemplate 	= "login"
			
			hostAddr    	= "localhost"
			port    		= "50810"
			qakdest         = "waitermind"
			ctxqadest		= "ctxtearoom"
			
			hostAddrBasicRobot   	= "192.168.10.160"
			portBasicRobot    		= "50800"
			qakdestBasicRobot	    = "basicrobot"
			ctxqadestBasicRobot 	= "ctxbasicrobot"
			stepsize				= "260"
			
			hostAddrVirtualRobot   	= "localhost"
			
			System.out.println("--- configurator | pageTemplate=$pageTemplate hostAddr=$hostAddr port=$port qakdest=$qakdest ctxqadest=$ctxqadest hostAddrBasicRobot=$hostAddrBasicRobot portBasicRobot=$portBasicRobot qakdestBasicRobot=$qakdestBasicRobot ctxqadestBasicRobot=$ctxqadestBasicRobot stepsize=$stepsize hostAddrVirtualRobot=$hostAddrVirtualRobot")
		}
	}//configure
}