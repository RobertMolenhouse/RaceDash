package commands;

import com.github.pires.obd.enums.AvailableCommandNames;

/**
 * will get the current gear the transmission is in.
 * This IS specific to Mazda Skyactiv vehicles.
 *
 */
public class CurrentGearCommand extends ObdCommand {

    private String gear;

    /**
     * Default constructor.
     */
    public CurrentGearCommand() {
        super("22 1e1f1"); //Hopefully this is right
    }


    /** reads raw data returned to determine the current gear */
    @Override
    protected void performCalculations() {
 
    	if(!rawData.isEmpty() && rawData.length() >= 7){
    	String byte4 = rawData.substring(6);
        
    	switch (byte4){
    	case "46" : gear = "P";
    		break;
    	case "82" : gear = "R";
    		break;
    	case "80" : gear = "N";
    		break;
    	case "01" : gear = "1";
    		break;
    	case "02" : gear = "2";
			break;
    	case "03" : gear = "3";
			break;
    	case "04" : gear = "4";
			break;
    	case "05" : gear = "5";
			break;
    		
    	}
    	}
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return String.format("%d%s", gear, getResultUnit());
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(gear);
    }

    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return "Gear";
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_RPM.getValue();
    }

    /**
     * <p>getRPM.</p>
     *
     * @return a int.
     */
    public String getGear() {
        return gear;
    }

}