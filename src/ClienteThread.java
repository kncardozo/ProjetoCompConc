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
    private static boolean logAtualizado = true;    //para garantir o padrao produtor/consumidor
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
        System.out.println("\nLog de ID " + idCliente + " criado");
    }

    boolean checaLog(){
        synchronized (ClienteThread.class) {
            System.out.println("----- THREAD " + idCliente + " = CHECOU O LOG: " + logAtualizado);
            return logAtualizado;
        }
    }
    
    synchronized boolean mudaLog(){
            System.out.println("----- THREAD " + idCliente + " = MUDOU O ESTADO DO LOG PARA: " + !logAtualizado);
            return !logAtualizado;
    }

    synchronized void visualizaAssentos() throws InterruptedException {

        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA VISUALIZA: " + logAtualizado);
        if (checaLog()) {

            System.out.println("----- THREAD " + idCliente + " = ENTROU NO VISUALIZA");



//      ====================== ATIVIDADES DO LOG ======================
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
//      ===============================================================



            System.out.println(estadosAssentos + " visualizado pelo cliente " + idCliente);

            System.out.println("----- THREAD " + idCliente + " = NOTIFY DO VISUALIZA");
            notifyAll();



        } else {
            System.out.println("----- THREAD " + idCliente + " = WAIT DO VISUALIZA");
            wait();
        }
    }

    synchronized int alocaAssentoLivre() throws InterruptedException {
        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA ASSENTO LIVRE: " + logAtualizado);

        if (checaLog()) {




            System.out.println("----- THREAD " + idCliente + " = ENTROU NO ASSENTO LIVRE");

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
                notifyAll();
                return 0;

            } else {
                int lugarLivreAleatorio = 1 + (int) (Math.random() * qtdAssentosLivres);
                assento[lugarLivreAleatorio].id_Cliente = idCliente;
                assento[lugarLivreAleatorio].status = 1;



//      ====================== ATIVIDADES DO LOG ======================
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
//      ===============================================================



                    System.out.println("Cliente " + idCliente + " RESERVOU o " + assento[lugarLivreAleatorio].id_Assento + " com sucesso");

                    System.out.println("----- THREAD " + idCliente + " = NOTIFY DO ASSENTO LIVRE");
                    notifyAll();
                }

            } else {
                System.out.println("----- THREAD " + idCliente + " = WAIT DO VISUALIZA ASSENTO LIVRE");
                wait();
        }
        return 1;
    }

    synchronized int alocaAssentoDado(int id_Assento) throws InterruptedException {
        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA ASSENTO DADO: " + logAtualizado);

        if(checaLog()){




            System.out.println("----- THREAD " + idCliente + " = ENTROU NO ASSENTO DADO");

            if (assento[id_Assento].status == 0) {
                    assento[id_Assento].status = 1;
                    assento[id_Assento].id_Cliente = idCliente;


//      ====================== ATIVIDADES DO LOG ======================
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
//      ===============================================================


                    System.out.println("Cliente " + idCliente + " RESERVOU o assento " + assento[id_Assento].id_Assento + " como desejado");
                    System.out.println("----- THREAD " + idCliente + " = NOTIFY DO ASSENTO DADO");

                    notifyAll();
                return 1;

                } else {
                    System.out.println("Cliente " + idCliente + " tentou reservar o assento desejado " + assento[id_Assento].id_Assento + " mas não conseguiu");
                    System.out.println("----- THREAD " + idCliente + " = NOTIFY DO ASSENTO DADO TRETA");

                    notifyAll();
                }
        } else {
            System.out.println("----- THREAD " + idCliente + " = WAIT DO ASSENTO DADO");
            wait();
        }
        return 0;
    }

    synchronized void liberaAssento(int id_Assento) throws InterruptedException {
        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA LIBERA: " + logAtualizado);

        if (checaLog()){




            System.out.println("----- THREAD " + idCliente + " = ENTROU NO LIBERA");

            if (assento[id_Assento].id_Cliente == idCliente && assento[id_Assento].status == 1) {
                assento[id_Assento].status = 0;
                assento[id_Assento].id_Cliente = 0;



//      ====================== ATIVIDADES DO LOG ======================
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
//      ===============================================================



                System.out.println("Cliente " + idCliente + " LIBEROU o " + assento[id_Assento].id_Assento + " com sucesso");

            } else if (assento[id_Assento].id_Cliente != idCliente) {
                System.out.println("Cliente " + idCliente + " tentou liberar o assento " + assento[id_Assento].id_Assento + " que não era dele");
            } else if (assento[id_Assento].status == 0) {
                System.out.println("Cliente " + idCliente + " tentou liberar o assento " + assento[id_Assento].id_Assento + " que já estava vazio");
            }

            System.out.println("----- THREAD " + idCliente + " = NOTIFY DO LIBERA");
            notifyAll();

        } else {
            System.out.println("WAIT DO LIBERA");
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
                    System.out.println("VALOR DA BARREIRA: " + barreira);
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
                    System.out.println("VALOR DA BARREIRA: " + barreira);
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
                    System.out.println("VALOR DA BARREIRA: " + barreira);
                    this.notifyAll();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(idCliente == 0){
            System.out.println("----- SOU O LOG E ME ACORDARAM");
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