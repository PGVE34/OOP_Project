package src.CORE;

import src.MODE.Individual;

public class EventFactory {

	public EventFactory() {
		// Nenhuma inicialização necessária neste caso
	}

	public Event createMoveEvent(Individual individual, int time) {
		return new Event(time, individual, new Move_Event());
	}

	public Event createDeathEvent(Individual individual, int time) {
		return new Event(time, individual, new Death_Event());
	}

	public Event createReproductionEvent(Individual individual, int time) {
		return new Event(time, individual, new Reproduction_Event());
	}
}
