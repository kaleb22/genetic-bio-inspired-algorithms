import java.io.BufferedReader;
import java.io.FileReader;

public class BaseDados {

    private int numFabricas = 0;
    private int numCidades = 0;
    private int matrizP[][];
    private int[] demandaPorCidade;

    public void carregaBaseDados() {
        try {
            BufferedReader leitor = new BufferedReader(new FileReader("K:/UFABC/18 QUAD/Comp Bio Inspirada/cap51.txt"));
            String linha = leitor.readLine();
            System.out.println(linha);

            inicializaArrays(linha);
            String[] valores;

            int i = 0;
            while(i < numFabricas) {
                linha = leitor.readLine();

                valores = linha.split("\\s+");
                matrizP[i][numCidades - 1] = Integer.parseInt(valores[1]);
                i++;
            }
            int index_coluna = 0;
            int index_linha = 0;

            linha = leitor.readLine();
            valores = linha.split("\\s+");
            demandaPorCidade[index_coluna] = Integer.parseInt(valores[1]);

            while(true) {
                linha = leitor.readLine();

                if(linha == null) {
                    break;
                }

                if(linha.length() <= 6) {
                    valores = linha.split("\\s+");
                    index_coluna++;
                    demandaPorCidade[index_coluna] = Integer.parseInt(valores[1]);
                    index_linha = 0;
                    continue;
                }

                valores = linha.split("\\s+");

                for (String valor : valores) {
                    String[] itens = valor.split("\\.");
                    if(itens.length != 1) {
                        matrizP[index_linha][index_coluna] = Integer.parseInt(itens[1]);
                        index_linha++;
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void inicializaArrays(String linha) {

        String[] valores = linha.split("\\s+");
        System.out.println(valores[1]);
        System.out.println(valores[2]);
        numFabricas = Integer.parseInt(valores[1]);
        numCidades = Integer.parseInt(valores[2]);

        numCidades++; // coluna p/ armazenar capacidade das fabricas
        matrizP = new int[numFabricas][numCidades];
        demandaPorCidade = new int[numCidades - 1];
    }

    public void imprimiMatriz() {
        int i, j;

        System.out.println("num_fab = " + numFabricas);
        System.out.println("num_cidades = " + numCidades);

        for(i = 0; i < numCidades; i++) {
            for(j = 0; j < numFabricas; j++) {
                System.out.print(matrizP[j][i]);
                System.out.print(" ");
            }
            System.out.println("");
        }
    }

    public void imprimiVetorDemandas() {
        int i;
        int soma = 0;

        for (int demanda : demandaPorCidade) {
            System.out.print(demanda);
            System.out.print(" ");
            soma += demanda;
        }

        System.out.println("soma total das demandas " + soma);
    }
    public int getNumFabricas() {
        return numFabricas;
    }

    public int getNumCidades() {
        return numCidades;
    }

    public int[][] getMatrizP() {
        return matrizP;
    }

    public int[] getDemandaPorCidade() {
        return demandaPorCidade;
    }
}
