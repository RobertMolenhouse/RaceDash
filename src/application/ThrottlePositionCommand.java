package application;

import com.github.pires.obd.commands.PercentageObdCommand;
import com.github.pires.obd.enums.AvailableCommandNames;

public class ThrottlePositionCommand extends PercentageObdCommand {

    /**
     * Default ctor.
     */
    public ThrottlePositionCommand() {
        super("01 11");
    }

    /**
     * Copy ctor.
     *
     * @param other a {@link com.github.pires.obd.commands.engine.ThrottlePositionCommand} object.
     */
    public ThrottlePositionCommand(ThrottlePositionCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.THROTTLE_POS.getValue();
    }

}
