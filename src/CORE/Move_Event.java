package src.CORE;

import src.MODE.Grid;
import src.MODE.Individual;
import src.MODE.Coordenadas;
import java.util.List;
import java.util.stream.Collectors;

public class Move_Event implements Event_Strategy {

	@Override
	public void execute(Simulation_Context context, Individual individual) {
		Grid grid = context.getGrid();
		Coordenadas current = individual.getCurrentPosition();

		// Only consider moves to cells not already in the path
		List<Coordenadas> validMoves = grid.getValidMoves(current).stream()
				.filter(c -> !individual.getPath().contains(c))
				.collect(Collectors.toList());

		if (!validMoves.isEmpty()) {
			int n = validMoves.size();
			double u = Math.random();
			int idx = (int) Math.floor(u * n);
			if (idx == n) idx = n - 1;

			Coordenadas next = validMoves.get(idx);
			individual.moveTo(next);

			// Schedule next move
			int tempoAtual = context.getTempoAtual();
			int tempoMorte = individual.getDeathTime();
			int tau = context.getTempoFinal();

			int delta = (int) Math.ceil(-Math.log(1 - Math.random()) * context.getDelta());
			int proximoTempo = tempoAtual + delta;

			if (proximoTempo < tempoMorte && proximoTempo <= tau) {
				Event proximoMove = context.getEventFactory().createMoveEvent(individual, proximoTempo);
				context.getPEC().addEvent(proximoMove);
			}
		}
		// If no valid moves, do not schedule another move event
	}
}