import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlgoritmoGenetico {

    private static final int TAMANHO_TORNEIO = 5;
    private static final boolean ELITISMO = false;
    private static final int NUMERO_GERACOES = 100;
    private static int TAMANHO_CROMOSSOMO = 0;

    public static int run(int tamanhoPopulacao, BaseDados baseDados, int tamanhoCromossomo) {

        Populacao myPop = new Populacao(tamanhoPopulacao, baseDados.getNumFabricas(), baseDados.getNumCidades(), tamanhoCromossomo);
        TAMANHO_CROMOSSOMO = tamanhoCromossomo;

        int contadorGeracoes = 1;
        while ( contadorGeracoes <= NUMERO_GERACOES) {
            myPop.calcularFitness(baseDados);
            if(contadorGeracoes == NUMERO_GERACOES) break;
            myPop = evoluiPopulacao(myPop, baseDados);
            contadorGeracoes++;
        }
        return myPop.getMelhorFitness(baseDados.getNumCidades()).getCustoSolucao();
    }

    private static Populacao evoluiPopulacao(Populacao myPop, BaseDados baseDados) {

        int elitismOffset;
        Populacao novaPopulacao = new Populacao();

        if (ELITISMO) {
            Individuo melhorIndividuo = new Individuo(baseDados.getNumFabricas(), baseDados.getNumCidades(), TAMANHO_CROMOSSOMO);
            melhorIndividuo.copiaCromossomo( myPop.getMelhorFitness(Populacao.numCidades).getCromossomo() );
            novaPopulacao.getListaIndividuos().add(0, melhorIndividuo);
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }

        for (int i = elitismOffset; i < myPop.getListaIndividuos().size(); i++) {
            Individuo indiv1 = selecaoPorTorneio(myPop);
            Individuo indiv2 = selecaoPorTorneio(myPop);
            List<Individuo> listaFilhos = crossover(indiv1, indiv2, baseDados);

            for(Individuo ind : listaFilhos) {
                if (novaPopulacao.getListaIndividuos().size() < myPop.getListaIndividuos().size() )
                    novaPopulacao.getListaIndividuos().add(ind);
            }

            if( novaPopulacao.getListaIndividuos().size() == myPop.getListaIndividuos().size() ) break;
        }

        for (int i = elitismOffset; i < novaPopulacao.getListaIndividuos().size(); i++) {
            mutacao(novaPopulacao.getListaIndividuos().get(i), baseDados);
        }

        return  novaPopulacao;
    }

    private static void mutacao(Individuo individuo, BaseDados baseDados) {
        List<Integer> medianas = new ArrayList();
        for (int i = 1; i <= baseDados.getNumFabricas(); i++) {
            medianas.add(i - 1, i);
        }

        Integer[] cromossomo = individuo.getCromossomo();
        for(int i = 0; i < cromossomo.length; i++ ) {
            if ( medianas.indexOf(cromossomo[i]) != -1 ) {
                //encontrou o gene e iremos remove-lo
                int index = medianas.indexOf(cromossomo[i]);
                medianas.remove(index);
            }
        }

        int indexGeneOriginal = (int) (Math.random() * cromossomo.length);
        int indexGeneMutacao  =  (int) (Math.random() * medianas.size());
        individuo.setUnicoGene(indexGeneOriginal, medianas.get(indexGeneMutacao));
    }

    private static Individuo selecaoPorTorneio(Populacao pop) {
        Populacao tournament = new Populacao();
        for (int i = 0; i < TAMANHO_TORNEIO; i++) {
            int randomId = (int) (Math.random() * pop.getListaIndividuos().size());
            tournament.getListaIndividuos().add(i, pop.getListaIndividuos().get(randomId));
        }
        Individuo fittest = tournament.getMelhorFitness(Populacao.numCidades);
        return fittest;
    }

    private static List<Individuo> crossover(Individuo indiv1, Individuo indiv2, BaseDados baseDados) {
        List<Individuo> listaFilhos = new ArrayList<>();

        List<Integer> vetorTroca1 = new ArrayList<>();
        List<Integer> vetorTroca2 = new ArrayList<>();

        Integer[] pai = indiv1.getCromossomo();
        Integer[] mae = indiv2.getCromossomo();

        populaVetorTroca(vetorTroca1, pai, mae);
        populaVetorTroca(vetorTroca2, mae, pai);

        if(vetorTroca1.isEmpty() && vetorTroca2.isEmpty()) {
            // pai e mae sao iguais
            vetorTroca1 = Arrays.stream( pai ).collect(Collectors.toList());
            Individuo filho = new Individuo(baseDados.getNumFabricas(), baseDados.getNumCidades(), TAMANHO_CROMOSSOMO);
            int index = 0;
            for (Integer gene : vetorTroca1) {
                filho.setUnicoGene(index++, gene);
            }
            listaFilhos.add(filho);
        } else {
            // insere genes pai na mae
            int genesAseremTrocados = (int) (Math.random() * vetorTroca1.size() - 1);
            for(int i = 0; i < genesAseremTrocados; i++) {
                mae[i] = vetorTroca1.get(i);
            }
            Individuo filho1 = new Individuo(baseDados.getNumFabricas(), baseDados.getNumCidades(), TAMANHO_CROMOSSOMO);
            filho1.copiaCromossomo(mae);

            // insere genes mae no pai
            genesAseremTrocados = (int) (Math.random() * vetorTroca2.size() - 1);
            for(int i = 0; i < genesAseremTrocados; i++) {
                pai[i] = vetorTroca2.get(i);
            }
            Individuo filho2 = new Individuo(baseDados.getNumFabricas(), baseDados.getNumCidades(), TAMANHO_CROMOSSOMO);
            filho2.copiaCromossomo(pai);

            listaFilhos.add(filho1);
            listaFilhos.add(filho2);
        }
        return listaFilhos;
    }

    private static void populaVetorTroca(List<Integer> vetorTroca, Integer[] cromossomo1, Integer[] cromossomo2) {
        for (Integer gene : cromossomo1) {
            boolean geneRepetido = false;
            for (int j = 0; j < cromossomo1.length; j++) {
                if (geneRepetido) break;

                if (gene.equals(cromossomo2[j])) geneRepetido = true;

                if (j == cromossomo1.length - 1 && !geneRepetido) {
                    vetorTroca.add(gene);
                }
            }
        }
    }
}
