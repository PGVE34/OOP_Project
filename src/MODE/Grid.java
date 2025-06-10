package src.MODE;

import java.util.*;

public class Grid {
	private int n; // linhas
	private int m; // colunas
	private Set<Coordenadas> obstacles;
	private List<Special_Cost_Zone> costZones;

	public Grid(int n, int m) {
		this.n = n;
		this.m = m;
		this.obstacles = new HashSet<>();
		this.costZones = new ArrayList<>();
	}

	public int getN() {
		return n;
	}

	public int getM() {
		return m;
	}

	public void addObstacle(Coordenadas c) {
		obstacles.add(c);
	}

	public void addSpecialCostZone(Special_Cost_Zone zone) {
		costZones.add(zone);
	}

	public boolean isObstacle(Coordenadas c) {
		return obstacles.contains(c);
	}

	/**
	 * Retorna o custo de mover de uma coordenada para outra adjacente.
	 * Se estiver numa zona especial de custo, retorna esse custo. Caso contrário, 1.
	 */
	public int custoCaminho(Coordenadas from, Coordenadas to) {
		if (!saoAdjacentes(from, to)) {
			throw new IllegalArgumentException("Coordenadas não são adjacentes.");
		}

		int custoMax = 1;
		for (Special_Cost_Zone zone : costZones) {
			if (zone.afetaAresta(from, to)) {
				custoMax = Math.max(custoMax, zone.getCusto());
			}
		}
		return custoMax;
	}

	public int getMaxCustoAresta() {
		int max = 1;
		for (Special_Cost_Zone zone : costZones) {
			max = Math.max(max, zone.getCusto());
		}
		return max;
	}

	/**
	 * Devolve os movimentos válidos (sem obstáculos) a partir de uma dada coordenada.
	 */
	public List<Coordenadas> getValidMoves(Coordenadas atual) {
		List<Coordenadas> moves = new ArrayList<>();
		int x = atual.getX();
		int y = atual.getY();

		int[][] direcoes = {
				{0, 1},   // Norte
				{1, 0},   // Este
				{0, -1},  // Sul
				{-1, 0}   // Oeste
		};

		for (int[] d : direcoes) {
			Coordenadas nova = new Coordenadas(x + d[0], y + d[1]);
			if (estaDentro(nova) && !isObstacle(nova)) {
				moves.add(nova);
			}
		}

		return moves;
	}

	private boolean estaDentro(Coordenadas c) {
		return c.getX() >= 1 && c.getX() <= n && c.getY() >= 1 && c.getY() <= m;
	}

	private boolean saoAdjacentes(Coordenadas a, Coordenadas b) {
		int dx = Math.abs(a.getX() - b.getX());
		int dy = Math.abs(a.getY() - b.getY());
		return dx + dy == 1;
	}
}
