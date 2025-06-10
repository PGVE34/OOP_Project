package src.CORE;

import src.MODE.Individual;

public class Event implements Comparable<Event> {
	private int time;
	private Individual individual;
	private Event_Strategy strategy;

	public Event(int time, Individual individual, Event_Strategy strategy) {
		this.time = time;
		this.individual = individual;
		this.strategy = strategy;
	}

	public int getTime() {
		return time;
	}

	public Individual getIndividual() {
		return individual;
	}

	public void execute(Simulation_Context simContext) {
		strategy.execute(simContext, individual);
	}

	@Override
	public int compareTo(Event other) {
		return Integer.compare(this.time, other.time);
	}

	@Override
	public String toString() {
		return "Event at t=" + time + " for individual=" + individual;
	}
}
