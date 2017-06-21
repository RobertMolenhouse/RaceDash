package application;

import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.engine.ThrottlePositionCommand;
import com.github.pires.obd.commands.fuel.FuelLevelCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
/** 
 * backend Class.  Sets up a socket with an ELM 327 chip.  Also sends commands
 * and receives responses from the ECU.
 * 
 * @author Bob Molenhouse
 *
 */
public class CommandControl{

    private static Socket socket;
    private static InputStream in;
    private static OutputStream out;
    private static String elmAddr = "192.168.0.10";
    private static int elmPort = 35000;

    private static RPMCommand RPM;
    private static SpeedCommand MPH;
    private static ThrottlePositionCommand throttlePos;
    private static EngineCoolantTemperatureCommand coolantTemp;
    private static FuelLevelCommand fuelLevel;
    private static CurrentGearCommand gear;

    private CarData data;

    /**
     * Constructor for CommandControl. Attempts to connect to a ELM 327 socket
     * and set in and out streams.
     *
     * @throws UnknownHostException
     * @throws IOException
     */
    public CommandControl(CarData data) throws UnknownHostException, IOException {
        this.data = data;

        initCommands();

        //attempt a socket connection
        try{
        socket = new Socket(elmAddr, elmPort);
        if (socket != null) {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } 
        }catch (ConnectException e){
        	System.out.println("Could not connect to socket");
        }
    }
    
    /**
     * close that socket dog.
     * @throws IOException
     */
    public void closeSocket() throws IOException{
    	try{
    		socket.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }

    /**
     * runs the commands to the ECU and places the results in the CarData class
     * object.
     * 
     * TODO decide if i want every command in their own try catch, or to group together. 
     */
    public void run() {

        //set up the ELM 327 to be ready to accept the commands how we want
        try {
            new EchoOffCommand().run(in, out);
            new LineFeedOffCommand().run(in, out);
            new TimeoutCommand(125).run(in, out);
            new SelectProtocolCommand(ObdProtocols.AUTO).run(in, out);
        } catch (Exception e) {
        	e.printStackTrace();
        }

        try{
    		// the loop that will send commands and get the goods.
        	// TODO catch errors you loser.
    		while (true) {
    			long start = System.currentTimeMillis();
    				while (System.currentTimeMillis() - start <= 10000) {
    					long start2 = System.currentTimeMillis();
    						while (System.currentTimeMillis() - start2 <= 1000){
    					try {
    						RPM.run(in, out);
    					} catch (IOException | InterruptedException e) {
    						e.printStackTrace();
    					}
    					try {
    						MPH.run(in, out);
    					} catch (IOException | InterruptedException e) {
    						e.printStackTrace();
    					}
    					try {
    						throttlePos.run(in, out);
    					} catch (IOException | InterruptedException e) {
    						e.printStackTrace();
    					}
    					try {
    						gear.run(in, out);
    					} catch (IOException | InterruptedException e) {
    						e.printStackTrace();
    					}
    					data.setRpm(RPM.getRPM());
    					data.setMph(MPH.getImperialSpeed());
    					data.setThrottlePos(throttlePos.getPercentage());
    						}
    						try {
        						gear.run(in, out);
        					} catch (IOException | InterruptedException e) {
        						e.printStackTrace();
        					}
    						data.setGear(gear.getGear());
    				}
    				try {
    					coolantTemp.run(in, out);
    				} catch (IOException | InterruptedException e) {
    					e.printStackTrace();
    				}
    				data.setCoolandTemp(coolantTemp.getImperialUnit());
    				try {
    					fuelLevel.run(in, out);
    				} catch (IOException | InterruptedException e) {
    					e.printStackTrace();
    				}
    				data.setFuelLevel(fuelLevel.getFuelLevel());
    		}	 
 
    		}catch (Exception e) {
    			e.printStackTrace();
    		}
    			finally {
    				try {
    					socket.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}

	}

    /**
     * initialize all the command objects.
     */
    private void initCommands() {
        RPM = new RPMCommand();
        MPH = new SpeedCommand();
        throttlePos = new ThrottlePositionCommand();
        coolantTemp = new EngineCoolantTemperatureCommand();
        fuelLevel = new FuelLevelCommand();
        gear = new CurrentGearCommand();
    }
}
