package src.MODE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Representa um indivíduo que caminha, reproduz e morre.
 */
public class Individual {
	private List<Coordenadas> path;
	private int x;
	private int y;
	private int birthTime;
	private int deathTime;
	private Individual parent;
	private boolean reproduced;

	public Individual(Coordenadas start, int birth, int death) {
		this.path = new ArrayList<>();
		this.x = start.getX();
		this.y = start.getY();
		this.path.add(start);
		this.birthTime = birth;
		this.deathTime = death;
		this.parent = null;
		this.reproduced = false;
	}

	public Individual(Coordenadas start, int birth, int death, Individual parent, List<Coordenadas> inheritedPath) {
		this.path = new ArrayList<>(inheritedPath);
		this.x = start.getX();
		this.y = start.getY();
		if (this.path.isEmpty() || !this.path.get(this.path.size() - 1).equals(start)) {
			this.path.add(start);
		}
		this.birthTime = birth;
		this.deathTime = death;
		this.parent = parent;
		this.reproduced = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Coordenadas getCurrentPosition() {
		return new Coordenadas(x, y);
	}

	public Coordenadas getLastPosition() {
		return path.get(path.size() - 1);
	}

	public List<Coordenadas> getPath() {
		return path;
	}

	public int getLength() {
		return path.size() - 1;
	}

	public int getDeathTime() {
		return deathTime;
	}

	public int getBirthTime() {
		return birthTime;
	}

	public void setDeathTime(int t) {
		this.deathTime = t;
	}

	public void setBirthTime(int t) {
		this.birthTime = t;
	}

	public void moveTo(Coordenadas next) {
		this.x = next.getX();
		this.y = next.getY();
		this.path.add(next);
		removeCycles();
	}

	private void removeCycles() {
		Set<Coordenadas> seen = new HashSet<>();
		List<Coordenadas> newPath = new ArrayList<>();
		for (Coordenadas coord : path) {
			if (seen.contains(coord)) {
				int idx = newPath.indexOf(coord);
				newPath = new ArrayList<>(newPath.subList(0, idx + 1));
			} else {
				newPath.add(coord);
				seen.add(coord);
			}
		}
		this.path = newPath;
	}

	public int getCost(Grid grid) {
		int cost = 0;
		for (int i = 1; i < path.size(); i++) {
			Coordenadas a = path.get(i - 1);
			Coordenadas b = path.get(i);
			cost += grid.custoCaminho(a, b);
		}
		return cost;
	}

	public double getComfort(Grid grid, Coordenadas target, int k) {
		int length = getLength();
		int cost = getCost(grid);
		int maxEdgeCost = grid.getMaxCustoAresta();
		int distToEnd = getLastPosition().getDistancia(target);

		double part1 = (1.0 - cost - length + 2) / ((maxEdgeCost - 1.0) * length + 3);
		double part2 = 1.0 - ((double) distToEnd / (grid.getN() + grid.getM() + 1));

		part1 = Math.max(0.001, Math.min(0.999, part1));
		part2 = Math.max(0.001, Math.min(0.999, part2));

		return Math.pow(part1, k) * Math.pow(part2, k);
	}

	public double getComfortAtPosition(Grid grid, Coordenadas pos, int targetIndex, int k) {
		int m = grid.getM();
		int tx = (targetIndex / m) + 1;
		int ty = (targetIndex % m) + 1;
		Coordenadas target = new Coordenadas(tx, ty);

		Coordenadas oldLast = getLastPosition();
		path.add(pos);
		int oldX = this.x;
		int oldY = this.y;
		this.x = pos.getX();
		this.y = pos.getY();

		double comfort = getComfort(grid, target, k);

		path.remove(path.size() - 1);
		this.x = oldX;
		this.y = oldY;
		return comfort;
	}

	public Individual reproduz(int k) {
		int inheritLength = Math.max(1, path.size() / 2);
		List<Coordenadas> inheritedPath = new ArrayList<>(path.subList(0, inheritLength));
		Coordenadas start = inheritedPath.get(inheritedPath.size() - 1);
		return new Individual(start, 0, 0, this, inheritedPath);
	}
}