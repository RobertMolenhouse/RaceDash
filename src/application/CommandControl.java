package application;

//import com.github.pires.obd.commands.SpeedCommand;
//import com.github.pires.obd.commands.engine.RPMCommand;
//import com.github.pires.obd.commands.engine.ThrottlePositionCommand;
//import com.github.pires.obd.commands.fuel.FuelLevelCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
//import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;

import commands.CurrentGearCommand;
import commands.EngineCoolantTemperatureCommand;
import commands.FuelLevelCommand;
import commands.OilTempCommand;
import commands.RPMCommand;
import commands.SpeedCommand;
import commands.ThrottlePositionCommand;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
/** 
 * backend Class.  Sets up a socket with an ELM 327 chip.  Also sends commands
 * and receives responses from the ECU.
 * 
 * @author Bob Molenhouse
 *
 */
public class CommandControl implements SerialPortEventListener{

	SerialPort serialPort;

	private static final String PORT_NAMES[] = { "/dev/ttyUSB0", // Linux
			"COM6", // Windows
	};
	
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 38400;
;
    private InputStream in;
    private OutputStream out;
    private SerialPortEventListener listener;

    private static RPMCommand RPM;
    private static SpeedCommand MPH;
    private static ThrottlePositionCommand throttlePos;
    private static EngineCoolantTemperatureCommand coolantTemp;
    private static FuelLevelCommand fuelLevel;
    private static CurrentGearCommand gear;
    private static OilTempCommand oilTemp;

    private CarData data;

    /**
     * Constructor for CommandControl. Attempts to connect to a ELM 327 socket
     * and set in and out streams.
     *
     * @throws UnknownHostException
     * @throws IOException
     */
    public CommandControl(CarData data)  throws UnknownHostException, IOException {
        this.data = data;

        initCommands();
        initialize(); //connect to serial port
    
    }

    /**
     * runs the commands to the ECU and places the results in the CarData class
     * object.
     * 
     * TODO decide if i want every command in their own try catch, or to group together. 
     * @throws InterruptedException 
     */
    public void run() throws InterruptedException {
        //set up the ELM 327 to be ready to accept the commands how we want
        try {
            new EchoOffCommand().run(in, out);
            new LineFeedOffCommand().run(in, out);
            new TimeoutCommand(125).run(in, out);
            new SelectProtocolCommand(ObdProtocols.AUTO).run(in, out);
        } catch (Exception e) {
        	e.printStackTrace();
        }

		try {
			// the loop that will send commands and get the goods.
			// TODO catch errors you loser.
			while (true) {
				long start = System.currentTimeMillis();
				while (System.currentTimeMillis() - start <= 10000) {
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
						gear.run(in, out);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
					data.setGear(gear.getGear());
					 try {
					 throttlePos.run(in, out);
					 } catch (IOException | InterruptedException e) {
					 e.printStackTrace();
					 }

					if (RPM.getRPM() > 1) {
						data.setRpm(RPM.getRPM());
					}
					if (MPH.getImperialSpeed() > 0) {
						data.setMph(MPH.getImperialSpeed());
					}
					if(throttlePos.getPercentage() > 0){
					 data.setThrottlePos(throttlePos.getPercentage());
					}
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
    				try {
    					oilTemp.run(in, out);
    				} catch (IOException | InterruptedException e) {
    					e.printStackTrace();
    				}
    				data.setOilTemp(oilTemp.getImperialUnit());
    		
    		}}catch (Exception e) {
    			e.printStackTrace();
    		}
    			finally {
    				close();
    			}
    
    }

    /**
     * initialize all the command objects.
     */
    private void initCommands() {
        RPM = new RPMCommand(listener);
        MPH = new SpeedCommand();
        throttlePos = new ThrottlePositionCommand();
        coolantTemp = new EngineCoolantTemperatureCommand();
        fuelLevel = new FuelLevelCommand();
        gear = new CurrentGearCommand();
        oilTemp = new OilTempCommand();
    }
    
    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            in = serialPort.getInputStream();
            out = serialPort.getOutputStream();
            
            serialPort.addEventListener(listener);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            listener.notifyAll();
        }
}}
