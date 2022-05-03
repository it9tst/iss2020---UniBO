package it.unibo.robotWeb2020;
//https://www.baeldung.com/websockets-spring
//https://www.toptal.com/java/stomp-spring-boot-websocket
	
import java.util.Random;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import connQak.configurator;
import connQak.connQakCoap;
import connQak.connQakCoapBasicRobot;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;


@Controller 
public class RobotController { 
	
    String appName ="TeaRoomDashboard";
    String viewModelRep ="startup";
    String htmlPage = "";
    String robotHost = "";
    String robotPort = "";
    
    Random r = new Random();
    
    connQakCoap connQakSupport;
    connQakCoapBasicRobot connQakBasicRobotSupport;
    
    public RobotController() {
    	connQak.configurator.configure();
        htmlPage  = connQak.configurator.getPageTemplate();
        robotHost =	connQak.configurator.getHostAddr();	
        robotPort = connQak.configurator.getPort();

        connQakSupport = new connQakCoap();
        connQakSupport.createConnection();
        connQakBasicRobotSupport = new connQakCoapBasicRobot();
        connQakBasicRobotSupport.createConnection();
     }

    /*
     * Update the page vie socket.io when the application-resource changes.
     * Thanks to Eugenio Cerulo
     */
    
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/")
    public String entry(Model viewmodel) {
    	viewmodel.addAttribute("arg", "Entry page loaded. Please use the buttons");
    	peparePageUpdatingStatemanager();
    	peparePageUpdatingSmartbell();
    	peparePageUpdatingMaxstaytime();
    	return htmlPage;
    }
    
    @GetMapping("/applmodel")
    @ResponseBody
    public String getApplicationModel(Model viewmodel) {
    	ResourceRep rep = getWebPageRep();
    	viewmodel.addAttribute("state",rep.getContent());
    	return rep.getContent();
    }     

	@PostMapping(path = "/move") 
	public String doMove (
		@RequestParam(name = "move", required = false, defaultValue = "h")
		//binds the value of the query string parameter name into the moveName parameter of the  method
		String moveName, Model viewmodel) {
			System.out.println("------------------- RobotController doMove move=" + moveName);
			doBusinessJobMove(moveName, viewmodel);
		return htmlPage;
	}

	private void peparePageUpdatingStatemanager() {
		CoapClient client = new CoapClient();
		String url =  "coap://" + configurator.getHostAddr() + ":" + configurator.getPort() + "/" + configurator.getCtxqadest() + "/tearoomstatemanager";
		System.out.println("CoapObserver | url=" + url.toString());
		client.setURI(url.toString());
		client.setTimeout(1000L);
	    
		CoapResponse respGet = client.get(); //CoapResponse
		if(respGet != null) {
			System.out.println("CoapObserver | createConnection doing get | CODE= ${respGet.code} content=${respGet.getResponseText()}");
		} else {
			System.out.println("CoapObserver | url=" + url.toString() + " FAILURE");
		}
		
		client.observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				System.out.println("Tearoomstatemanager changed!" + response.getResponseText());
				simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForTearoomstatemanager, new ResourceRep("" + HtmlUtils.htmlEscape(response.getResponseText())));
			}
			
			@Override
			public void onError() {
				System.out.println("Tearoomstatemanager error!");
			}
		});
	}
	
	private void peparePageUpdatingSmartbell() {
		CoapClient client = new CoapClient();
		String url =  "coap://" + configurator.getHostAddr() + ":" + configurator.getPort() + "/" + configurator.getCtxqadest() + "/smartbell";
		System.out.println("CoapObserver | url=" + url.toString());
		client.setURI(url.toString());
		client.setTimeout(1000L);
	    
		CoapResponse respGet = client.get(); //CoapResponse
		if(respGet != null) {
			System.out.println("CoapObserver | createConnection doing get | CODE= ${respGet.code} content=${respGet.getResponseText()}");
		} else {
			System.out.println("CoapObserver | url=" + url.toString() + " FAILURE");
		}
		
		client.observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				System.out.println("Smartbell changed!" + response.getResponseText());
				simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForSmartbell, new ResourceRep("" + HtmlUtils.htmlEscape(response.getResponseText())));
			}
			
			@Override
			public void onError() {
				System.out.println("Smartbell error!");
			}
		});
	}
	
	private void peparePageUpdatingMaxstaytime() {
		CoapClient client = new CoapClient();
		String url =  "coap://" + configurator.getHostAddr() + ":" + configurator.getPort() + "/" + configurator.getCtxqadest() + "/maxstaytime";
		System.out.println("CoapObserver | url=" + url.toString());
		client.setURI(url.toString());
		client.setTimeout(1000L);
	    
		CoapResponse respGet = client.get(); //CoapResponse
		if(respGet != null) {
			System.out.println("CoapObserver | createConnection doing get | CODE= ${respGet.code} content=${respGet.getResponseText()}");
		} else {
			System.out.println("CoapObserver | url=" + url.toString() + " FAILURE");
		}
		
		client.observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				System.out.println("Maxstaytime changed!" + response.getResponseText());
				simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForMaxstaytime, new ResourceRep("" + HtmlUtils.htmlEscape(response.getResponseText())));
			}
			
			@Override
			public void onError() {
				System.out.println("Maxstaytime error!");
			}
		});
	}
	
	/*
	 * INTERACTION WITH THE BUSINESS LOGIC			
	 */
	protected void doBusinessJobNewClient(String message, Model viewmodel) {
		try {
			
			if (message.equals("enterRequestClient")) {
				double randomValue = 35.0 + (39.0 - 35.0) * r.nextDouble();
				ApplMessage msg = MsgUtil.buildRequest("web", "enterRequestClient", "enterRequestClient(" + randomValue + ")", "smartbell");
				connQakSupport.forwardEnter(msg);
			}
			
			//WAIT for command completion ...
			Thread.sleep(400);  //QUITE A LONG TIME ...
			
			if(viewmodel != null) {
				ResourceRep rep = getWebPageRep();
				viewmodel.addAttribute("arg", "Current Robot State:  "+rep.getContent());
			}
		} catch (Exception e) {
			System.out.println("------------------- RobotController doBusinessJobNewClient ERROR=" + e.getMessage());
			//e.printStackTrace();
		}		
	}
	
	protected void doBusinessJobStepClient(int id, String message, Model viewmodel) {
		try {
			
			if (message.equals("clientReadyToOrder")) {
				ApplMessage msg = MsgUtil.buildDispatch("web", "clientReadyToOrder", "clientReadyToOrder(" + id + ")", configurator.getQakdest());
				connQakSupport.forward(msg);
			} else if (message.equals("clientOrder")) {
				String[] menu = {"the", "acqua", "brioches", "cioccolata"};
				ApplMessage msg = MsgUtil.buildDispatch("web", "clientOrder", "clientOrder(" + id + ", " + menu[r.nextInt(4)] + ")", configurator.getQakdest());
				connQakSupport.forward(msg);
			} else if (message.equals("clientPayment")) {
				ApplMessage msg = MsgUtil.buildDispatch("web", "clientPayment", "clientPayment(" + id + ")", configurator.getQakdest());
				connQakSupport.forward(msg);
			}
			
			//WAIT for command completion ...
			Thread.sleep(400);  //QUITE A LONG TIME ...
			
			if(viewmodel != null) {
				ResourceRep rep = getWebPageRep();
				viewmodel.addAttribute("arg", "Current Robot State:  "+rep.getContent());
			}
		} catch (Exception e) {
			System.out.println("------------------- RobotController doBusinessJobStepClient ERROR=" + e.getMessage());
			//e.printStackTrace();
		}		
	}
	
	protected void doBusinessJobMove(String moveName, Model viewmodel) {
		try {
			ApplMessage msg;
			
			if (moveName.equals("w")) {
				msg = MsgUtil.buildRequest("web", "step", "step("+configurator.getStepsize()+")", configurator.getQakdestBasicRobot());
			} else {
				msg = MsgUtil.buildDispatch("web", "cmd", "cmd("+moveName+")", configurator.getQakdestBasicRobot());
			}
			
			connQakBasicRobotSupport.forward(msg);
			
			//WAIT for command completion ...
			Thread.sleep(400);  //QUITE A LONG TIME ...
			
			if(viewmodel != null) {
				ResourceRep rep = getWebPageRep();
				viewmodel.addAttribute("arg", "Current Robot State:  "+rep.getContent());
			}
		} catch (Exception e) {
			System.out.println("------------------- RobotController doBusinessJobMove ERROR=" + e.getMessage());
			//e.printStackTrace();
		}		
	}

    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String> ("RobotController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }
    
	/* ----------------------------------------------------------
	 * Message-handling Controller
	 * ----------------------------------------------------------
	 */
	
	@MessageMapping("/newclient")
 	@SendTo("/topic/display")
 	public ResourceRep client1(RequestMessageOnSock message) throws Exception {
		doBusinessJobNewClient(message.getName(), null);
		return getWebPageRep();
 	}
	
	@MessageMapping("/stepclient")
 	@SendTo("/topic/display")
 	public ResourceRep client2(RequestMessageOnSock message) throws Exception {
		doBusinessJobStepClient(message.getID(), message.getName(), null);
		return getWebPageRep();
 	}
	
	@MessageMapping("/move")
 	@SendTo("/topic/display")
 	public ResourceRep backtoclient(RequestMessageOnSock message) throws Exception {
		doBusinessJobMove(message.getName(), null);
		return getWebPageRep();
 	}
	
	@MessageMapping("/update")
	@SendTo("/topic/display")
	public ResourceRep updateTheMap(@Payload String message) {
		ResourceRep rep = getWebPageRep();
		return rep;
	}

	public ResourceRep getWebPageRep() {
		String resourceRep = connQakSupport.readRep();
		System.out.println("------------------- RobotController resourceRep=" + resourceRep);
		return new ResourceRep("" + HtmlUtils.htmlEscape(resourceRep));
	}

	/*
	 * The @MessageMapping annotation ensures that, 
	 * if a message is sent to the /hello destination, the greeting() method is called.    
	 * The payload of the message is bound to a HelloMessage object, which is passed into greeting().
	 * 
	 * Internally, the implementation of the method simulates a processing delay by causing 
	 * the thread to sleep for one second. 
	 * This is to demonstrate that, after the client sends a message, 
	 * the server can take as long as it needs to asynchronously process the message. 
	 * The client can continue with whatever work it needs to do without waiting for the response.
	 * 
	 * After the one-second delay, the greeting() method creates a Greeting object and returns it. 
	 * The return value is broadcast to all subscribers of /topic/display, 
	 * as specified in the @SendTo annotation. 
	 * Note that the name from the input message is sanitized, since, in this case, 
	 * it will be echoed back and re-rendered in the browser DOM on the client side.
	 */
		
	/*
	 * curl --location --request POST 'http://localhost:8080/move' --header 'Content-Type: text/plain' --form 'move=l'	
	 * curl -d move=r localhost:8080/move
	 */
}

