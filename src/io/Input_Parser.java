package src.io;

import src.MODE.*;

import java.io.*;
import java.util.*;

public class Input_Parser {

	public Input_Parser() {
		// Construtor vazio
	}

	/**
	 * Lê parâmetros a partir de um ficheiro no formato descrito no enunciado.
	 */
	public SimulationParameters parseFile(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();

		// Primeira linha: parâmetros principais
		String[] parts = line.trim().split("\\s+");
		int n = Integer.parseInt(parts[0]);
		int m = Integer.parseInt(parts[1]);
		int xi = Integer.parseInt(parts[2]);
		int yi = Integer.parseInt(parts[3]);
		int xf = Integer.parseInt(parts[4]);
		int yf = Integer.parseInt(parts[5]);
		int nscz = Integer.parseInt(parts[6]);
		int nobst = Integer.parseInt(parts[7]);
		int tau = Integer.parseInt(parts[8]);
		int nu = Integer.parseInt(parts[9]);
		int nuMax = Integer.parseInt(parts[10]);
		int k = Integer.parseInt(parts[11]);
		double mu = Double.parseDouble(parts[12]);
		double delta = Double.parseDouble(parts[13]);
		double rho = Double.parseDouble(parts[14]);

		List<Special_Cost_Zone> zonas = new ArrayList<>();
		if (nscz > 0) {
			br.readLine(); // linha: "special cost zones:"
			for (int i = 0; i < nscz; i++) {
				String[] z = br.readLine().trim().split("\\s+");
				Coordenadas inf = new Coordenadas(Integer.parseInt(z[0]), Integer.parseInt(z[1]));
				Coordenadas sup = new Coordenadas(Integer.parseInt(z[2]), Integer.parseInt(z[3]));
				int custo = Integer.parseInt(z[4]);
				zonas.add(new Special_Cost_Zone(inf, sup, custo));
			}
		}

		List<Coordenadas> obstaculos = new ArrayList<>();
		if (nobst > 0) {
			br.readLine(); // linha: "obstacles:"
			for (int i = 0; i < nobst; i++) {
				String[] o = br.readLine().trim().split("\\s+");
				obstaculos.add(new Coordenadas(Integer.parseInt(o[0]), Integer.parseInt(o[1])));
			}
		}

		br.close();

		return new SimulationParameters(n, m, new Coordenadas(xi, yi), new Coordenadas(xf, yf), zonas, obstaculos,
				tau, nu, nuMax, k, mu, delta, rho);
	}

	/**
	 * Lê parâmetros da linha de comandos, modo aleatório (-r).
	 */
	public SimulationParameters parseArgs(String[] args) {
		if (!args[0].equals("-r") || args.length != 17) {
			throw new IllegalArgumentException("Formato inválido para o modo -r.");
		}

		int n = Integer.parseInt(args[1]);
		int m = Integer.parseInt(args[2]);
		int xi = Integer.parseInt(args[3]);
		int yi = Integer.parseInt(args[4]);
		int xf = Integer.parseInt(args[5]);
		int yf = Integer.parseInt(args[6]);
		int nscz = Integer.parseInt(args[7]);
		int nobst = Integer.parseInt(args[8]);
		int tau = Integer.parseInt(args[9]);
		int nu = Integer.parseInt(args[10]);
		int nuMax = Integer.parseInt(args[11]);
		int k = Integer.parseInt(args[12]);
		double mu = Double.parseDouble(args[13]);
		double delta = Double.parseDouble(args[14]);
		double rho = Double.parseDouble(args[15]);

		Random rand = new Random();
		List<Special_Cost_Zone> zonas = new ArrayList<>();
		List<Coordenadas> obstaculos = new ArrayList<>();

		for (int i = 0; i < nscz; i++) {
			int x1 = 1 + rand.nextInt(n);
			int y1 = 1 + rand.nextInt(m);
			int x2 = x1 + rand.nextInt(Math.max(1, n - x1));
			int y2 = y1 + rand.nextInt(Math.max(1, m - y1));
			int cost = 2 + rand.nextInt(4); // custo entre 2 e 5
			zonas.add(new Special_Cost_Zone(new Coordenadas(x1, y1), new Coordenadas(x2, y2), cost));
		}

		for (int i = 0; i < nobst; i++) {
			obstaculos.add(new Coordenadas(1 + rand.nextInt(n), 1 + rand.nextInt(m)));
		}

		return new SimulationParameters(n, m, new Coordenadas(xi, yi), new Coordenadas(xf, yf), zonas, obstaculos,
				tau, nu, nuMax, k, mu, delta, rho);
	}
}
