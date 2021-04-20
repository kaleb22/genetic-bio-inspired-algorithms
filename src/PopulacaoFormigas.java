import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopulacaoFormigas {

    private List<Formiga> listaFormigas;
    static int numCidades;

    public PopulacaoFormigas() { listaFormigas = new ArrayList<>(); }

    public PopulacaoFormigas(int tamanhoPopulacao, int qtdFabricas, int numCidades, int fabricasAbertas, Boolean populaTrilha) {
        listaFormigas = new ArrayList<>();
        this.numCidades = numCidades;
        inicializaNovaPopulacao(tamanhoPopulacao, qtdFabricas, numCidades, fabricasAbertas, populaTrilha);
    }

    private void inicializaNovaPopulacao(int tamanhoPopulacao,int qtdFabricas, int numCidades, int fabricasAbertas, Boolean populaTrilha) {
        for (int i = 0; i < tamanhoPopulacao; i++) {
            Formiga novaFormiga = new Formiga(qtdFabricas, numCidades, fabricasAbertas, populaTrilha);
            listaFormigas.add(i, novaFormiga);
        }
    }

    public void constroiSolucao(BaseDados baseDados) {

        int[][] matrizP = baseDados.getMatrizP();

        for (Formiga formiga : listaFormigas) {
            int[] demandaPorCidadeCopia = Arrays.stream( baseDados.getDemandaPorCidade() ).toArray();
            for (int fabrica : formiga.getTrilha()) {

                formiga.setFitness( calculaFitness(baseDados, matrizP, fabrica, formiga, demandaPorCidadeCopia) );

                //System.out.print(fabrica + " ");
            }
            //System.out.print("fitness = " + formiga.getFitness() + " custo solucao " + formiga.getCustoSolucao() + " ");
            for (Integer valor : formiga.getVetorSolucao()) {
                //System.out.print(valor + " ");
            }
            //System.out.println();

        }
    }

    private int calculaFitness(BaseDados baseDados, int[][] matrizP, Integer fabrica, Formiga formiga, int[] demandaPorCidadeCopia) {
        int[][] matrizCopia = Arrays.stream(matrizP)
                .map((int[] row) -> row.clone())
                .toArray((int length) -> new int[length][]);


        int posFabrica = fabrica - 1;
        int capacidadeFabrica = matrizCopia[posFabrica][baseDados.getNumCidades() - 1];
        int custo = matrizCopia[posFabrica][0];
        int indiceCusto = 0;

        int fitness = formiga.getFitness();
        Integer[] vetorDeSolucao = formiga.getVetorSolucao();
        int menorDemanda = demandaPorCidadeCopia[0];

        while(capacidadeFabrica > 0) {

            if(fitness == numCidades) break;

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
        formiga.setVetorSolucao(vetorDeSolucao);
        int custoParcial = formiga.getCustoSolucao();
        custoParcial += custo;
        formiga.setCustoSolucao(custoParcial);
        return fitness;
    }

    public List<Formiga> getListaFormigas() {
        return listaFormigas;
    }

    public void setListaFormigas(List<Formiga> listaFormigas) {
        this.listaFormigas = listaFormigas;
    }

    public void setFormigaPorIndex(int index, Formiga formiga) {
        this.listaFormigas.set(index, formiga);
    }

    public Formiga getMelhorSolucao(int numCidades) {
        Formiga melhorFormiga = listaFormigas.get(0);
        for(int i = 0; i < listaFormigas.size() - 1; i++ ) {
            Formiga formiga = listaFormigas.get(i);
            if( formiga.getFitness() == numCidades )
                if( formiga.getCustoSolucao() < melhorFormiga.getCustoSolucao() ) {
                    melhorFormiga = formiga;
                    melhorFormiga.setIndexSolucao(i);
                }
        }

        if(melhorFormiga.getFitness() == numCidades) {
            return melhorFormiga;
        } else {
            return null;
        }
    }
}
