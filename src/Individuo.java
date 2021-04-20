import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Individuo {

    private Integer[] cromossomo;
    private int fitness = 0;
    private Integer[] vetorSolucao;
    private int custoSolucao = 0;

    public Individuo(int qtdFabricas, int qtdCidades, int tamanhoCromossomo) {
        cromossomo = new Integer[tamanhoCromossomo];
        vetorSolucao = new Integer[qtdCidades - 1];
        // inicializa um vetor de medianas de 1 até p
        List<Integer> medianas = new ArrayList();
        for (int i = 1; i <= qtdFabricas; i++) {
            medianas.add(i - 1, i);
        }

        for (int i = 0; i < cromossomo.length; i++) {
            int index = ThreadLocalRandom.current().nextInt(0, medianas.size());
            cromossomo[i] = medianas.remove(index);
        }
    }

    protected Integer[] getCromossomo() {
        return cromossomo;
    }

    protected void copiaCromossomo(Integer[] novoCromossomo) {
        int index = 0;
        for(Integer gene : novoCromossomo) {
            cromossomo[index++] = gene;
        }
    }

    protected void imprimiCromossomo() {
        for(int i = 0; i < cromossomo.length; i++) {
            System.out.print(cromossomo[i] + " ");
        }
        System.out.println();
    }

    protected void setUnicoGene(int index, int value) {
        cromossomo[index] = value;
        fitness = 0;
    }
    public Integer[] getVetorSolucao() {
        return vetorSolucao;
    }

    public void setVetorSolucao(Integer[] vetorSolucao) {
        this.vetorSolucao = vetorSolucao;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getCustoSolucao() {
        return custoSolucao;
    }

    public void setCustoSolucao(int custoSolucao) {
        this.custoSolucao = custoSolucao;
    }

    public int getFitness() {
        return fitness;
    }
}
