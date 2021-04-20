import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class AcoAlgoritmo {

    private static double taxaEvaporacao = 0.5;
    private static int qtdFabricasAbertas;
    private static int tamanhoPopulacao;
    private static int quantidadeCidades;
    private static int quantidadeFabricas;
    private static final int QUANTIDADE_EXECUCOES = 100;

    public static void run(int populacaoSize, BaseDados baseDados, int fabricasAbertas) {

        int[] vetorFeromonio = new int[baseDados.getNumFabricas()];
        qtdFabricasAbertas = fabricasAbertas;
        tamanhoPopulacao = populacaoSize;
        quantidadeCidades = baseDados.getNumCidades() - 1;
        quantidadeFabricas = baseDados.getNumFabricas();

        IntStream.rangeClosed(0, vetorFeromonio.length - 1)
                 .forEach(i -> {
                    vetorFeromonio[i] = 1;
                 });

        AtomicReference<PopulacaoFormigas> populacao = new AtomicReference<>(new PopulacaoFormigas(tamanhoPopulacao, quantidadeFabricas, quantidadeCidades, qtdFabricasAbertas, true));

        IntStream.rangeClosed(1, QUANTIDADE_EXECUCOES)
                .forEach(i -> {
                    //System.out.println("Iteracao #" + i);
                    iniciaAlgoritmo(populacao.get(), baseDados, vetorFeromonio, i);
                    populacao.set(preparaFormigas(vetorFeromonio));
                });
    }

    private static void iniciaAlgoritmo(PopulacaoFormigas populacao, BaseDados baseDados, int[] vetorFeromonio, int indexExecucao) {
        populacao.constroiSolucao(baseDados);
        atualizaFeromonio(vetorFeromonio, populacao, indexExecucao);
        //imprimiVetorFeromonio(vetorFeromonio);
    }

    private static void atualizaFeromonio(int[] vetorFeromonio, PopulacaoFormigas populacao, int indexExecucao) {

        if(indexExecucao == QUANTIDADE_EXECUCOES) {
            System.out.println("Melhor Solucao = " + populacao.getMelhorSolucao(quantidadeCidades).getCustoSolucao());
        }

        int tamanhoPopulacao = populacao.getListaFormigas().size();
        for(int i = 0; i < tamanhoPopulacao; i++) {
            List<Formiga> listaFormigas = populacao.getListaFormigas();
            Formiga melhorFormiga = populacao.getMelhorSolucao(quantidadeCidades);

            if(melhorFormiga != null) {
                int[] arraySolucao = melhorFormiga.getTrilha();
                double taxaAtualizacaoFeromonio = 4.0;

                for(Integer fabrica : arraySolucao) {
                    double custoSolucao = melhorFormiga.getCustoSolucao();
                    custoSolucao = custoSolucao * taxaEvaporacao;
                    vetorFeromonio[fabrica - 1] += custoSolucao * taxaAtualizacaoFeromonio;
                    taxaAtualizacaoFeromonio -= 0.2;
                }

                listaFormigas.remove(melhorFormiga.getIndexSolucao());
                populacao.setListaFormigas(listaFormigas);
            } else {
                break;
            }
        }
    }

    private static PopulacaoFormigas preparaFormigas(int[] vetorFeromonio) {

        PopulacaoFormigas populacao = new PopulacaoFormigas(tamanhoPopulacao, quantidadeFabricas, quantidadeCidades, qtdFabricasAbertas, false);
        List<Formiga> listaFormigas = populacao.getListaFormigas();

        for (int i = 0; i < tamanhoPopulacao; i++) {

            Formiga formiga = listaFormigas.get(i);
            List<Integer> melhoresFabricas = new ArrayList<>();
            List<Integer> vetorFeromonioCopia = new ArrayList<>();

            for (int valor : vetorFeromonio) {
                vetorFeromonioCopia.add(valor);
            }

            escolheMelhoresTresFabricas(vetorFeromonioCopia, melhoresFabricas, false);

            int index = ThreadLocalRandom.current().nextInt(0, melhoresFabricas.size());
            int indexFabrica = melhoresFabricas.get(index);
            formiga.setTrilha(indexFabrica, 0);
            melhoresFabricas.remove(index);

            for (int j = 1; j < qtdFabricasAbertas; j++) {
                escolheMelhoresTresFabricas(vetorFeromonioCopia, melhoresFabricas, true);
                index = ThreadLocalRandom.current().nextInt(0, melhoresFabricas.size());
                indexFabrica = melhoresFabricas.get(index);
                formiga.setTrilha(indexFabrica, j);
                melhoresFabricas.remove(index);
            }
        }
        return populacao;
    }

    private static void escolheMelhoresTresFabricas(List<Integer> vetorFeromonioCopia, List<Integer> melhoresFabricas, Boolean selecionaApenasUma) {
        int custo = 0;
        int maiorCusto = 0;

        for(int i = 0; i < 3; i++) {

            if(selecionaApenasUma && i == 1) break;

            maiorCusto = vetorFeromonioCopia.get(0);
            int index = 1;
            int fabricaMaiorCusto = 1;
            for (int custoFabrica : vetorFeromonioCopia) {
                custo = custoFabrica;
                if (maiorCusto < custo ) {
                    maiorCusto = custo;
                    fabricaMaiorCusto = index;
                }
                index++;
            }
            melhoresFabricas.add(fabricaMaiorCusto);
            vetorFeromonioCopia.set(fabricaMaiorCusto - 1, 0);
        }
    }

    private static void imprimiVetorFeromonio(int[] vetorFeromonio) {

        IntStream.rangeClosed(0, vetorFeromonio.length - 1)
                .forEach(i -> {
                    int index = i + 1;
                    System.out.println("taxa feromonio pos# "+ index + " = " + vetorFeromonio[i]);
                });
    }
}
