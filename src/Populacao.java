import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Populacao {

    private List<Individuo> individuos;
    static int numCidades;

    public Populacao(int tamanho, int qtdFabricas, int qtdCidades, int tamanhoCromossomo) {
        individuos = new ArrayList<>();
        numCidades = qtdCidades;
        criarNovaPopulacao(tamanho, qtdFabricas, qtdCidades, tamanhoCromossomo);
    }

    public Populacao() {
        individuos = new ArrayList<>();
    }

    private void criarNovaPopulacao(int tamanho, int qtdFabricas, int qtdCidades, int tamanhoCromossomo) {
        for (int i = 0; i < tamanho; i++) {
            Individuo novoIndividuo = new Individuo(qtdFabricas, qtdCidades, tamanhoCromossomo);
            individuos.add(i, novoIndividuo);
        }
    }

    public List<Individuo> getListaIndividuos() {
        return individuos;
    }

    public void calcularFitness(BaseDados baseDados) {

        int[][] matrizP = baseDados.getMatrizP();

        for (Individuo individuo : individuos) {
            int[] demandaPorCidadeCopia = Arrays.stream( baseDados.getDemandaPorCidade() ).toArray();
            for (Integer fabrica : individuo.getCromossomo()) {

                individuo.setFitness( calculaFitness(baseDados, matrizP, fabrica, individuo, demandaPorCidadeCopia) );

                //System.out.print(fabrica + " ");
            }
            //System.out.print("fitness = " + individuo.getFitness() + " custo solucao " + individuo.getCustoSolucao() + " ");
            for (Integer valor : individuo.getVetorSolucao()) {
                //System.out.print(valor + " ");
            }
            //System.out.println();

        }
    }

    private int calculaFitness(BaseDados baseDados, int[][] matrizP, Integer fabrica, Individuo individuo, int[] demandaPorCidadeCopia) {
        int[][] matrizCopia = Arrays.stream(matrizP)
                .map((int[] row) -> row.clone())
                .toArray((int length) -> new int[length][]);


        int posFabrica = fabrica - 1;
        int capacidadeFabrica = matrizCopia[posFabrica][baseDados.getNumCidades() - 1];
        int custo = matrizCopia[posFabrica][0];
        int indiceCusto = 0;

        int fitness = individuo.getFitness();
        Integer[] vetorDeSolucao = individuo.getVetorSolucao();
        int menorDemanda = demandaPorCidadeCopia[0];

        while(capacidadeFabrica > 0) {

            if(fitness == numCidades - 1) break;

            for(int j = 1; j < baseDados.getNumCidades() - 1; j++ ) {
                if( menorDemanda > demandaPorCidadeCopia[j] ) {
                    menorDemanda = demandaPorCidadeCopia[j];
                    custo = matrizCopia[posFabrica][j];
                    indiceCusto = j;
                }
            }

            if(vetorDeSolucao[indiceCusto] == null || vetorDeSolucao[indiceCusto] == 0) {
                //cidade n foi atendida
                fitness += 1;
                capacidadeFabrica -= demandaPorCidadeCopia[indiceCusto];

                if( capacidadeFabrica < 0) {
                    fitness -= 1;
                    vetorDeSolucao[indiceCusto] = 0;
                } else {
                    vetorDeSolucao[indiceCusto] = 1;
                    custo += matrizCopia[posFabrica][indiceCusto];
                    demandaPorCidadeCopia[indiceCusto] = 99999;
                    menorDemanda = demandaPorCidadeCopia[0];
                    indiceCusto = 0;
                }
            } else {
                demandaPorCidadeCopia[indiceCusto] = 99999;
                menorDemanda = demandaPorCidadeCopia[0];
                custo = matrizCopia[posFabrica][0];
                vetorDeSolucao[indiceCusto] = 0;
            }
        }
        individuo.setVetorSolucao(vetorDeSolucao);
        int custoParcial = individuo.getCustoSolucao();
        custoParcial += custo;
        individuo.setCustoSolucao(custoParcial);
        return fitness;
    }

    public Individuo getMelhorFitness(int numCidades) {
        Individuo melhorIndividuo = individuos.get(0);
        for(int i = 0; i < individuos.size() - 1; i++ ) {
            Individuo ind = individuos.get(i);
            if( ind.getFitness() == numCidades - 1 )
                if( ind.getCustoSolucao() < melhorIndividuo.getCustoSolucao() )
                    melhorIndividuo = ind;
        }

        return melhorIndividuo;
    }
}
