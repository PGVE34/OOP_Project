package src.CORE;

import java.util.PriorityQueue;

public class PEC {
	private PriorityQueue<Event> eventos;

	public PEC() {
		this.eventos = new PriorityQueue<>();
	}

	public void addEvent(Event aEvent) {
		eventos.add(aEvent);
	}

	public Event getNextEvent() {
		return eventos.poll(); // Remove e devolve o evento mais próximo
	}

	public boolean hasEvents() {
		return !eventos.isEmpty();
	}

	public double peekNextEvent() {
		if (eventos.isEmpty()) return -1;
		return eventos.peek().getTime();
	}
}
