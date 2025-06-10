package src.MODE;

/**
 * Representa um ponto no plano (i.e., numa célula da grelha).
 */
public class Coordenadas {
	private int x;
	private int y;

	/**
	 * Construtor que cria uma coordenada com os valores indicados.
	 */
	public Coordenadas(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Acessores para x.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Acessores para y.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Calcula a distância de Manhattan até outro ponto,
	 * ignorando obstáculos (distância mínima em arestas).
	 */
	public int getDistancia(Coordenadas outro) {
		return Math.abs(this.x - outro.x) + Math.abs(this.y - outro.y);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Coordenadas)) return false;
		Coordenadas other = (Coordenadas) obj;
		return this.x == other.x && this.y == other.y;
	}

	@Override
	public int hashCode() {
		return 31 * x + y;
	}
}
