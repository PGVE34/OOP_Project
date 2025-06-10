package src.MODE;

public class Special_Cost_Zone {
	private Coordenadas cantoInferior;
	private Coordenadas cantoSuperior;
	private int custo;

	public Special_Cost_Zone(Coordenadas inf, Coordenadas sup, int custo) {
		this.cantoInferior = inf;
		this.cantoSuperior = sup;
		this.custo = custo;
	}

	public int getCusto() {
		return custo;
	}

	public Coordenadas getCantoInferior() {
		return cantoInferior;
	}

	public Coordenadas getCantoSuperior() {
		return cantoSuperior;
	}

	// Add these for compatibility with the rest of the codebase
	public Coordenadas getInf() {
		return getCantoInferior();
	}

	public Coordenadas getSup() {
		return getCantoSuperior();
	}

	public boolean afetaAresta(Coordenadas from, Coordenadas to) {
		if (Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY()) != 1) {
			return false;
		}
		return isOnPerimeter(from) && isOnPerimeter(to);
	}

	private boolean isOnPerimeter(Coordenadas c) {
		int x = c.getX();
		int y = c.getY();
		int x1 = cantoInferior.getX();
		int y1 = cantoInferior.getY();
		int x2 = cantoSuperior.getX();
		int y2 = cantoSuperior.getY();

		boolean dentroHorizontal = x >= x1 && x <= x2;
		boolean dentroVertical = y >= y1 && y <= y2;

		boolean naBordaHorizontal = (y == y1 || y == y2) && dentroHorizontal;
		boolean naBordaVertical = (x == x1 || x == x2) && dentroVertical;

		return naBordaHorizontal || naBordaVertical;
	}

	@Override
	public String toString() {
		return "Zona especial: [" + cantoInferior + " -> " + cantoSuperior + "] custo = " + custo;
	}
}