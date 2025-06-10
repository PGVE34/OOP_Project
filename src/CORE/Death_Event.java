package src.CORE;
import src.MODE.*;

public class Death_Event implements Event_Strategy {

	public Death_Event() {
		// No setup needed
	}

	@Override
	public void execute(Simulation_Context context, Individual individual) {
		context.getPopulation().remove(individual);
	}
}