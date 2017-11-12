import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

class ClienteThread extends Thread {

    private int idCliente;                          //identificador da thread
    private String nome_arquivo_log;                //nome do arquivo gerado
    private Assento[] assento;                      //array de assentos
    private static Vector log = new Vector();       //vetor do log que sera incrementado ao longo da execucao
    private boolean logAtualizado = true;           //para garantir o padrao produtor/consumidor
    private static int barreira = 0;                //para garantir que o log seja gerado apenas ao final


//    construtor das treads de clientes
    ClienteThread(int id, Assento[] assento) {
        this.idCliente = id;
        System.out.println("Cliente " + idCliente + " criado");
        this.assento = assento;
    }

//    construtor da thread de escritura no arquivo log
    ClienteThread(int id, String nome_arquivo_log) {
        this.idCliente = id;
        this.nome_arquivo_log= nome_arquivo_log;
        System.out.println("Log de ID " + idCliente + " criado");
    }

    synchronized void visualizaAssentos() throws InterruptedException {
        if (logAtualizado) {
            logAtualizado = false;
            Vector estadosAssentos = new Vector();
            for (int i = 1; i < assento.length; i++) {
                if (assento[i].status == 0) {
                    estadosAssentos.add(assento[i].status);
                } else {
                    estadosAssentos.add(assento[i].id_Cliente);
                }
            }

            Vector temp = new Vector();
            temp.add("1, " + String.valueOf(idCliente) + ", " + String.valueOf(estadosAssentos) + "\n");
            log.add(temp);
//            System.out.println("Log:\n" + log);

            System.out.println(estadosAssentos + " visualizado pelo cliente " + idCliente);

            logAtualizado = true;
            notifyAll();
        } else {
            wait();
        }
    }

    synchronized int alocaAssentoLivre() throws InterruptedException {
        if (logAtualizado) {
            logAtualizado = false;
                Vector<Integer> assentosLivres = new Vector<>();
                int qtdAssentosLivres = 0;
                for (int i = 1; i < assento.length; i++) {
                    if (assento[i].status == 0) {
                        assentosLivres.add(assento[i].id_Assento);
                        qtdAssentosLivres++;
                    }
                }

                if (qtdAssentosLivres == 0) {
                    System.out.println("Cliente " + idCliente + " tentou reservar um assento, mas não havia mais assentos livres");
                    logAtualizado = true;
                    notifyAll();
                    return 0;

                } else {
                    int lugarLivreAleatorio = 1 + (int) (Math.random() * qtdAssentosLivres);
                    assento[lugarLivreAleatorio].id_Cliente = idCliente;
                    assento[lugarLivreAleatorio].status = 1;

                    Vector estadosAssentos = new Vector();
                    for (int i = 1; i < assento.length; i++) {
                        if (assento[i].status == 0) {
                            estadosAssentos.add(assento[i].status);
                        } else {
                            estadosAssentos.add(assento[i].id_Cliente);
                        }
                    }

                    Vector temp = new Vector();
                    temp.add("2, " + String.valueOf(idCliente) + ", " + String.valueOf(lugarLivreAleatorio) + ", " + String.valueOf(estadosAssentos) + "\n");
                    log.add(temp);
//                    System.out.println("Log:\n" + log);


                    System.out.println("Cliente " + idCliente + " RESERVOU o " + assento[lugarLivreAleatorio].id_Assento + " com sucesso");
                logAtualizado = true;
                notifyAll();
                }
            } else {
                wait();
        }
        return 1;
    }

    synchronized int alocaAssentoDado(int id_Assento) throws InterruptedException {
        if(logAtualizado){
            logAtualizado=false;
                if (assento[id_Assento].status == 0) {
                    assento[id_Assento].status = 1;
                    assento[id_Assento].id_Cliente = idCliente;

                    Vector estadosAssentos = new Vector();
                    for (int i = 1; i < assento.length; i++) {
                        if (assento[i].status == 0) {
                            estadosAssentos.add(assento[i].status);
                        } else {
                            estadosAssentos.add(assento[i].id_Cliente);
                        }
                    }

                    Vector temp = new Vector();
                    temp.add("3, " + String.valueOf(idCliente) + ", " + String.valueOf(id_Assento) + ", " + String.valueOf(estadosAssentos) + "\n");
                    log.add(temp);
//                    System.out.println("Log:\n" + log);


                    System.out.println("Cliente " + idCliente + " RESERVOU o assento " + assento[id_Assento].id_Assento + " como desejado");
                logAtualizado = true;
                notifyAll();
                return 1;

                } else {
                    System.out.println("Cliente " + idCliente + " tentou reservar o assento desejado " + assento[id_Assento].id_Assento + " mas não conseguiu");
                    logAtualizado = true;
                    notifyAll();
                }
        } else {
            wait();
        }
        return 0;
    }

    synchronized void liberaAssento(int id_Assento) throws InterruptedException {
        if (logAtualizado){
            logAtualizado = false;
                if (assento[id_Assento].id_Cliente == idCliente && assento[id_Assento].status == 1) {
                    assento[id_Assento].status = 0;
                    assento[id_Assento].id_Cliente = 0;

                    Vector estadosAssentos = new Vector();
                    for (int i = 1; i < assento.length; i++) {
                        if (assento[i].status == 0) {
                            estadosAssentos.add(assento[i].status);
                        } else {
                            estadosAssentos.add(assento[i].id_Cliente);
                        }
                    }

                    Vector temp = new Vector();
                    temp.add("4, " + String.valueOf(idCliente) + ", " + String.valueOf(id_Assento) + ", " + String.valueOf(estadosAssentos) + "\n");
                    log.add(temp);
//                    System.out.println("Log:\n" + log);



                    System.out.println("Cliente " + idCliente + " LIBEROU o " + assento[id_Assento].id_Assento + " com sucesso");

                } else if (assento[id_Assento].id_Cliente != idCliente) {
                    System.out.println("Cliente " + idCliente + " tentou liberar o assento " + assento[id_Assento].id_Assento + " que não era dele");
                } else if (assento[id_Assento].status == 0) {
                    System.out.println("Cliente " + idCliente + " tentou liberar o assento " + assento[id_Assento].id_Assento + " que já estava vazio");
                }
                logAtualizado=true;
                notifyAll();
        }else {
            wait();
        }
    }

    public void run() {
        if (idCliente == 1) {
            int sucesso = 0;
            try {
                visualizaAssentos();
                sucesso = alocaAssentoLivre();
                visualizaAssentos();
                synchronized (this) {
                    barreira++;             // esta thread terminou sua execucao
                    this.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        if (idCliente == 2) {
            int sucesso = 0;
            try {
                visualizaAssentos();
                sucesso = alocaAssentoDado(1 + (int) (Math.random() * (assento.length - 1)));
                visualizaAssentos();
                synchronized (this) {
                    barreira++;             // esta thread terminou sua execucao
                    this.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (idCliente == 3) {
            int sucesso = 0;
            try {
                visualizaAssentos();
                sucesso = alocaAssentoLivre();
                visualizaAssentos();
                liberaAssento(1 + (int) (Math.random() * (assento.length - 1)));
                visualizaAssentos();
                synchronized (this) {
                    barreira++;             // esta thread terminou sua execucao
                    this.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(idCliente == 0){
            while (barreira != 3){
                try {
                    synchronized (this){
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("\n TODAS AS OUTRAS THREADS TERMINARAM");
            imprimeLog();

        }

    }

    private void imprimeLog() {
        try {
            String logString = String.valueOf(log);
            logString = logString.replace("[[","");
            logString = logString.replace("]]","");
            logString = logString.replace("], [","");

            FileWriter fw;
            File arquivo = new File(nome_arquivo_log);
            fw = new FileWriter(arquivo);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(logString);
            bw.close();
            fw.close();
            System.out.println(" Arquivo de Log " + nome_arquivo_log + " gerado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}