package src.CORE;

import src.MODE.*;
import src.io.SimulationParameters;
import java.util.*;

public class Simulation {
	private final Simulation_Context context;

	public Simulation(Simulation_Context context) {
		this.context = context;
	}

	public void run() {
		// Print input parameters at the start
		printInputParameters(context.getParameters());

		int tau = context.getParameters().getTau();
		int obsInterval = tau / 20;
		int nextObsTime = 0;
		int obsNum = 0;
		int events = 0;
		int currentTime = 0;

		while (context.getPEC().hasEvents()) {
			Event event = context.getPEC().getNextEvent();
			if (event == null) break;
			currentTime = event.getTime();

			// Print observations at the right times
			while (currentTime >= nextObsTime && obsNum <= 20) {
				Individual best = findBestIndividual();
				boolean hit = best != null && best.getLastPosition().equals(context.getDestino());
				printObservation(obsNum, nextObsTime, events, context.getPopulation().getALL().size(), hit, best);
				obsNum++;
				nextObsTime = obsNum * obsInterval;
			}

			// Process event
			event.execute(context);
			events++;

			// Stop if simulation time exceeded
			if (currentTime > tau) break;
		}

		// Print any remaining observations up to tau
		while (obsNum <= 20) {
			Individual best = findBestIndividual();
			boolean hit = best != null && best.getLastPosition().equals(context.getDestino());
			printObservation(obsNum, obsNum * obsInterval, events, context.getPopulation().getALL().size(), hit, best);
			obsNum++;
		}

		// Print final best fit individual
		Individual best = findBestIndividual();
		printBestFitIndividual(best);
	}

	private void printInputParameters(SimulationParameters params) {
		System.out.printf("%d %d %d %d %d %d %d %d %d %d %d %d %d %d %d%n",
				params.getN(), params.getM(),
				params.getStartPoint().getX(), params.getStartPoint().getY(),
				params.getEndPoint().getX(), params.getEndPoint().getY(),
				params.getSpecialCostZones().size(), params.getObstacles().size(),
				params.getTau(), params.getNu(), params.getNuMax(),
				params.getK(), (int) params.getMu(), (int) params.getDelta(), (int) params.getRho());

		if (!params.getSpecialCostZones().isEmpty()) {
			System.out.println("special cost zones:");
			for (Special_Cost_Zone zone : params.getSpecialCostZones()) {
				System.out.printf("%d %d %d %d %d%n",
						zone.getInf().getX(), zone.getInf().getY(),
						zone.getSup().getX(), zone.getSup().getY(),
						zone.getCusto());
			}
		}
		if (!params.getObstacles().isEmpty()) {
			System.out.println("obstacles:");
			for (Coordenadas c : params.getObstacles()) {
				System.out.printf("%d %d%n", c.getX(), c.getY());
			}
		}
		System.out.println();
		System.out.println();
	}

	private void printObservation(int obsNum, int time, int events, int popSize, boolean hit, Individual best) {
		System.out.println("Observation " + obsNum + ":");
		System.out.println("Present time: " + time);
		System.out.println("Number of realized events: " + events);
		System.out.println("Population size: " + popSize);
		System.out.println("Final point has been hit: " + (hit ? "yes" : "no"));
		System.out.print("Path of the best fit individual: ");
		if (best != null) {
			System.out.print("[");
			List<Coordenadas> path = best.getPath();
			for (int i = 0; i < path.size(); i++) {
				System.out.print(path.get(i));
				if (i < path.size() - 1) System.out.print(", ");
			}
			System.out.println("]");
			if (hit) {
				System.out.println("Cost/Comfort: " + best.getCost(context.getGrid()));
			} else {
				System.out.println("Cost/Comfort: " + String.format("%.6f", best.getComfort(context.getGrid(), context.getDestino(), context.getK())));
			}
		} else {
			System.out.println("[]");
			System.out.println("Cost/Comfort: 0");
		}
		System.out.println();
	}

	private void printBestFitIndividual(Individual best) {
		System.out.print("Best fit individual: ");
		if (best != null) {
			System.out.print("[");
			List<Coordenadas> path = best.getPath();
			for (int i = 0; i < path.size(); i++) {
				System.out.print(path.get(i));
				if (i < path.size() - 1) System.out.print(", ");
			}
			System.out.println("] with cost " + best.getCost(context.getGrid()));
		} else {
			System.out.println("[] with cost 0");
		}
	}

	private Individual findBestIndividual() {
		List<Individual> all = context.getPopulation().getALL();
		Coordenadas destino = context.getDestino();
		Grid grid = context.getGrid();
		int k = context.getK();

		// First, check if any individual reached the final point
		Individual best = null;
		int minCost = Integer.MAX_VALUE;
		for (Individual ind : all) {
			if (ind.getLastPosition().equals(destino)) {
				int cost = ind.getCost(grid);
				if (cost < minCost) {
					minCost = cost;
					best = ind;
				}
			}
		}
		if (best != null) return best;

		// Otherwise, pick the one with highest comfort
		double maxComfort = -1;
		for (Individual ind : all) {
			double comfort = ind.getComfort(grid, destino, k);
			if (comfort > maxComfort) {
				maxComfort = comfort;
				best = ind;
			}
		}
		return best;
	}
}