package src.CORE;

import src.MODE.Individual;
import src.MODE.Grid;
import src.MODE.Coordenadas;

import java.util.*;
import java.util.stream.Collectors;

public class PopulationManager {
	private List<Individual> individuals = new ArrayList<>();
	private Simulation_Context context;

	public PopulationManager(Simulation_Context context) {
		this.context = context;
	}

	public void add(Individual individual) {
		individuals.add(individual);
	}

	public void remove(Individual individual) {
		individuals.remove(individual);
	}

	public List<Individual> getALL() {
		return new ArrayList<>(individuals);
	}

	public List<Individual> getALIVE() {
		int tempoAtual = context.getTempoAtual();
		return individuals.stream()
				.filter(i -> i.getDeathTime() > tempoAtual)
				.collect(Collectors.toList());
	}

	public List<Individual> getTopK(int k) {
		return getALIVE().stream()
				.sorted(Comparator.comparingDouble(
						i -> -i.getComfort(context.getGrid(), context.getDestino(), context.getK())
				))
				.limit(k)
				.collect(Collectors.toList());
	}

	public void applyEpidemic() {
		List<Individual> vivos = getALIVE();
		if (vivos.size() <= 5) return; // nada a fazer

		List<Individual> top5 = getTopK(5);
		Set<Individual> sobreviventes = new HashSet<>(top5);

		for (Individual ind : vivos) {
			if (sobreviventes.contains(ind)) continue;

			double comfort = ind.getComfort(context.getGrid(), context.getDestino(), context.getK());
			double sorte = Math.random(); // [0, 1)

			if (sorte <= comfort) {
				sobreviventes.add(ind);
			} else {
				remove(ind);
			}
		}
	}
}
