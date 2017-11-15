public class Main {

    public static void main(String[] args) throws InterruptedException {

        String nome_arquivo_log = args[0];
        int qtd_assentos = Integer.parseInt(args[1]);


        // gerando assentos a partir do parâmetro dado
        Assento[] assento = new Assento[(qtd_assentos + 1)];
        for (int i = 1; i < (qtd_assentos + 1); i++) {
            assento[i] = new Assento(i, 0, 0);
        }

        // criando 3 clientes a partir do id 1
        ClienteThread[] cliente = new ClienteThread[4];
        for (int i = 1; i < 4; i++) {
            cliente[i] = new ClienteThread(i, assento);
            cliente[i].start();
        }

        // criando a thread de log
//        while (cliente[1].isAlive() || cliente[2].isAlive() || cliente[3].isAlive()) {
//            Thread.sleep(200);
//        }
        ClienteThread log = new ClienteThread(0, nome_arquivo_log, qtd_assentos,assento);
        log.start();
    }
}