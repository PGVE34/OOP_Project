package src;

import src.CORE.*;
import src.io.*;
import src.MODE.*;

public class Main {
    public static void main(String[] args) {
        try {
            Input_Parser parser = new Input_Parser();
            SimulationParameters params;

            if (args.length > 0 && args[0].equals("-f")) {
                // Ex: java -jar projeto.jar -f input.txt
                String path = args[1];
                params = parser.parseFile(path);

            } else if (args.length > 0 && args[0].equals("-r")) {
                // Ex: java -jar projeto.jar -r 5 5 1 1 5 5 1 4 100 10 100 3 10 1 1
                params = parser.parseArgs(args);

            } else {
                System.err.println("Erro: argumentos inválidos. Usa -f <ficheiro> ou -r <valores>");
                return;
            }

            // Criar grid
            Grid grid = params.buildGrid();

            // Criar estruturas base
            PEC pec = new PEC();
            PopulationManager pm = new PopulationManager(null); // contexto ainda não criado
            Simulation_Context context = new Simulation_Context(grid, pec, pm, params);
            pm = new PopulationManager(context); // criar de novo com o contexto correto
            context = new Simulation_Context(grid, pec, pm, params); // atualizar referência com PM certo

            // Criar população inicial e eventos iniciais
            EventFactory factory = context.getEventFactory();
            for (int i = 0; i < params.getNu(); i++) {
                Individual ind = new Individual(params.getStartPoint(), 0, 0);
                ind.setDeathTime((int) (Math.ceil(-Math.log(1 - Math.random()) * params.getMu())));
                pm.add(ind);

                pec.addEvent(factory.createDeathEvent(ind, ind.getDeathTime()));
                pec.addEvent(factory.createMoveEvent(ind, (int) (Math.ceil(-Math.log(1 - Math.random()) * params.getDelta()))));
                pec.addEvent(factory.createReproductionEvent(ind, (int) (Math.ceil(-Math.log(1 - Math.random()) * params.getRho()))));
            }

            // Correr a simulação
            Simulation sim = new Simulation(context);
            sim.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


