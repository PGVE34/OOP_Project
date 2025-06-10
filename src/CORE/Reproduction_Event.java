package src.CORE;

import src.MODE.Individual;
import src.MODE.Coordenadas;

public class Reproduction_Event implements Event_Strategy {

	public Reproduction_Event() {
		// Nada a fazer no construtor
	}

	@Override
	public void execute(Simulation_Context context, Individual parent) {
		int tempoAtual = context.getTempoAtual();
		int tempoMorte = parent.getDeathTime();
		int tau = context.getTempoFinal();

		// 1. Calcular tempo para próxima reprodução do pai
		int delta = gerarExponencial(context.getRho(), parent, context);
		int novoTempoPai = tempoAtual + delta;

		if (novoTempoPai < tempoMorte && novoTempoPai <= tau) {
			Event proximaReproducao = context.getEventFactory().createReproductionEvent(parent, novoTempoPai);
			context.getPEC().addEvent(proximaReproducao);
		}

		// 2. Criar filho
		Individual filho = parent.reproduz(context.getK());

		// 3. Definir tempo de nascimento e morte do filho
		filho.setBirthTime(tempoAtual);
		int tempoMorteFilho = tempoAtual + gerarExponencial(context.getMu(), filho, context);
		filho.setDeathTime(tempoMorteFilho);

		// 4. Adicionar filho à população
		context.getPopulation().add(filho);

		// 5. Agendar eventos do filho (se ainda dentro do tempo da simulação)
		if (tempoAtual <= tau) {
			// Morte
			if (tempoMorteFilho <= tau) {
				Event morteFilho = context.getEventFactory().createDeathEvent(filho, tempoMorteFilho);
				context.getPEC().addEvent(morteFilho);
			}

			// Primeira reprodução
			int tempoReproducaoFilho = tempoAtual + gerarExponencial(context.getRho(), filho, context);
			if (tempoReproducaoFilho < tempoMorteFilho && tempoReproducaoFilho <= tau) {
				Event reproducaoFilho = context.getEventFactory().createReproductionEvent(filho, tempoReproducaoFilho);
				context.getPEC().addEvent(reproducaoFilho);
			}

			// Primeiro movimento
			int tempoMovimentoFilho = tempoAtual + gerarExponencial(context.getDelta(), filho, context);
			if (tempoMovimentoFilho < tempoMorteFilho && tempoMovimentoFilho <= tau) {
				Event movimentoFilho = context.getEventFactory().createMoveEvent(filho, tempoMovimentoFilho);
				context.getPEC().addEvent(movimentoFilho);
			}
		}
	}

	private int gerarExponencial(double mediaBase, Individual individuo, Simulation_Context ctx) {
		double comfort = individuo.getComfort(ctx.getGrid(), ctx.getDestino(), ctx.getK());
		double lambda = 1.0 / ((1 - Math.log(comfort)) * mediaBase);
		double r = Math.random();
		return (int) Math.ceil(-Math.log(1 - r) / lambda);
	}
}
