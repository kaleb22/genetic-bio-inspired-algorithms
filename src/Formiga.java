import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Formiga {

    private int tamanhoTrilha;
    private int[] trilha;
    private Integer[] vetorSolucao;
    private int custoSolucao;
    private int fitness = 0;
    private int indexSolucao = 0;

    public Formiga(int qtdFabricas, int numCidades, int fabricasAbertas, Boolean populaTrilha) {
        tamanhoTrilha = fabricasAbertas;
        trilha = new int[tamanhoTrilha];
        vetorSolucao = new Integer[numCidades];
        custoSolucao = 0;

        if(populaTrilha) {
            // inicializa um vetor de medianas de 1 até p
            List<Integer> medianas = new ArrayList();
            for (int i = 1; i <= qtdFabricas; i++) {
                medianas.add(i - 1, i);
            }

            for (int i = 0; i < trilha.length; i++) {
                int index = ThreadLocalRandom.current().nextInt(0, medianas.size());
                trilha[i] = medianas.remove(index);
            }
        }
    }

    public int[] getTrilha() {
        return trilha;
    }

    public void setTrilha(int fabrica, int pos) {
        trilha[pos] = fabrica;
    }

    public Integer[] getVetorSolucao() {
        return vetorSolucao;
    }

    public void setVetorSolucao(Integer[] vetorSolucao) {
        this.vetorSolucao = vetorSolucao;
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

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getIndexSolucao() { return indexSolucao;  }

    public void setIndexSolucao(int indexSolucao) { this.indexSolucao = indexSolucao;  }

    public void resetVetorSolucao() {
        Arrays.fill(vetorSolucao, 0);
    }

    public void imprimiTrilha() {
        for(int index : trilha) {
            System.out.print(index + " ");
        }
        System.out.println();
    }
}
