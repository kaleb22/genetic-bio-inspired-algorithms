import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        BaseDados base01 = new BaseDados();
        base01.carregaBaseDados();
        base01.imprimiMatriz();
        base01.imprimiVetorDemandas();
        List<Integer> vetorDeCustos = new ArrayList<>();

        for(int i = 0; i < 20; i++) {
            System.out.println("Solução " + AlgoritmoGenetico.run(50, base01, 6) );
        }

        System.out.println("----- Execução ACO ---- ");

        for(int i = 0; i < 20; i++) {
            AcoAlgoritmo.run(50, base01, 6);
        }
    }
}
