package commands;

import com.github.pires.obd.enums.AvailableCommandNames;

/**
 * Displays the current engine Oil temperature.
 *
 */
public class OilTempCommand extends TemperatureCommand {

    /**
     * Default ctor.
     */
    public OilTempCommand() {
        super("01 5C");
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_OIL_TEMP.getValue();
    }

}