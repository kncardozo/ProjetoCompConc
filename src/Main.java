public class Main {

    public static void main(String[] args) {

//        String nome_arquivo_log = args[0];
//        int qtd_assentos = Integer.parseInt(args[1]);
        String nome_arquivo_log = "arquivo.txt";
        int qtd_assentos = 5;



        // gerando assentos a partir do parâmetro dado
        Assento[] assentos = new Assento[qtd_assentos];
        for (int i=0; i < qtd_assentos; i++ ){

            assentos[i] = new Assento(i,0);

        }

        // criando 3 clientes a partir do id 1
        ClienteThread[] cliente = new ClienteThread[3];
        for (int i=1; i < 4; i++ ){
            cliente[i] = new ClienteThread(i);
            cliente[i].start();
        }


        cliente[1].visualizaAssentos();
    
        System.out.println(qtd_assentos);
        System.out.println(nome_arquivo_log);


    }
}