package src.CORE;

import java.util.List;
import src.MODE.Grid;
import src.MODE.Coordenadas;
import src.MODE.Individual;
import src.io.SimulationParameters;

public class Simulation_Context {
	private Grid grid;
	private PEC pec;
	private PopulationManager population;
	private SimulationParameters parameters;
	private EventFactory eventFactory;
	private int tempoAtual;

	public Simulation_Context(Grid grid, PEC pec, PopulationManager population, SimulationParameters parameters) {
		this.grid = grid;
		this.pec = pec;
		this.population = population;
		this.parameters = parameters;
		this.eventFactory = new EventFactory();
		this.tempoAtual = 0;
	}

	public Grid getGrid() { return grid; }
	public PEC getPEC() { return pec; }
	public PopulationManager getPopulation() { return population; }
	public SimulationParameters getParameters() { return parameters; }
	public EventFactory getEventFactory() { return eventFactory; }
	public int getTempoAtual() { return tempoAtual; }
	public void setTempoAtual(int tempo) { this.tempoAtual = tempo; }

	public Coordenadas getDestino() { return parameters.getEndPoint(); }
	public int getK() { return parameters.getK(); }
	public int getN() { return parameters.getN(); }
	public int getM() { return parameters.getM(); }
	public int getTempoFinal() { return parameters.getTau(); }
	public double getMu() { return parameters.getMu(); }
	public double getRho() { return parameters.getRho(); }
	public double getDelta() { return parameters.getDelta(); }

	public Individual getBestFitIndividual() {
		Coordenadas destino = getDestino();
		List<Individual> all = population.getALL();
		Individual melhorComDestino = null;
		int melhorCusto = Integer.MAX_VALUE;
		for (Individual i : all) {
			if (i.getLastPosition().equals(destino)) {
				int custo = i.getCost(grid);
				if (custo < melhorCusto) {
					melhorCusto = custo;
					melhorComDestino = i;
				}
			}
		}
		if (melhorComDestino != null) return melhorComDestino;
		return all.stream()
				.max((a, b) -> Double.compare(
						a.getComfort(grid, destino, getK()),
						b.getComfort(grid, destino, getK())))
				.orElse(null);
	}

	}
