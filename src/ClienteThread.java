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
    private int qtd_assentos;




    //    construtor das treads de clientes
    ClienteThread(int id, Assento[] assento) {
        this.idCliente = id;
        System.out.println("Cliente " + idCliente + " criado");
        this.assento = assento;
    }

//    construtor da thread de escritura no arquivo log
    ClienteThread(int id, String nome_arquivo_log, int qtd_assentos) {
        this.idCliente = id;
        this.nome_arquivo_log= nome_arquivo_log;
        System.out.println("\nLog de ID " + idCliente + " criado");
        this.qtd_assentos = qtd_assentos;

    }

    void checaLog(){
        synchronized (ClienteThread.class) {
            System.out.println("----- THREAD " + idCliente + " = CHECOU O LOG: " + logAtualizado);
            logAtualizado = !logAtualizado;
        }
    }
    
    void mudaLog(boolean log){
            System.out.println("----- THREAD " + idCliente + " = O ESTADO DO LOG ERA " + log + " E VAI MUDAR PARA: " + !log);
            logAtualizado = !logAtualizado;
    }

    synchronized void visualizaAssentos() throws InterruptedException {
        synchronized (this){
        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA VISUALIZA: " + logAtualizado);


        if (logAtualizado) {
            mudaLog(logAtualizado);
            System.out.println("----- THREAD " + idCliente + " = ENTROU NO VISUALIZA E O LOG ESTÁ " + logAtualizado);



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
            temp.add("mapa = " + String.valueOf(estadosAssentos) + "\n" +
                    "fop.op1("+ String.valueOf(idCliente) +",mapa)\n");
            log.add(temp);
//      ===============================================================



            System.out.println(estadosAssentos + " visualizado pelo cliente " + idCliente);

            mudaLog(logAtualizado);
            notify();
            System.out.println("----- THREAD " + idCliente + " = NOTIFY DO VISUALIZA COM O LOG: " + logAtualizado);



        } else {
            System.out.println("----- THREAD " + idCliente + " = WAIT DO VISUALIZA");
            while (!logAtualizado){
                Thread.sleep(1000);
            }
        }
    }
    }

    synchronized int alocaAssentoLivre() throws InterruptedException {
        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA ASSENTO LIVRE: " + logAtualizado);

        if (logAtualizado) {
            mudaLog(logAtualizado);




            System.out.println("----- THREAD " + idCliente + " = ENTROU NO ASSENTO LIVRE E O LOG ESTÁ " + logAtualizado);

            Vector<Integer> assentosLivres = new Vector<>();
            int qtdAssentosLivres = 0;
            for (int i = 1; i < assento.length; i++) {
                if (assento[i].status == 0) {
                    assentosLivres.add(assento[i].id_Assento);
                    qtdAssentosLivres++;
                }
            }

            System.out.println("ASSENTOS LIVRES: " + assentosLivres);

            if (qtdAssentosLivres == 0) {
                System.out.println("Cliente " + idCliente + " tentou reservar um assento, mas não havia mais assentos livres");
                mudaLog(logAtualizado);
                notifyAll();
                return 0;

            } else {
                synchronized (ClienteThread.class) {
                    int assentoLivre = assentosLivres.get(0);
                    assento[assentoLivre].id_Cliente = idCliente;
                    assento[assentoLivre].status = 1;


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
                    temp.add("mapa = " + String.valueOf(estadosAssentos) + "\n" +
                            "fop.op2(" + String.valueOf(idCliente) + "," + String.valueOf(assentoLivre) + ",mapa)\n");

                    log.add(temp);
//      ===============================================================


                    System.out.println("Cliente " + idCliente + " RESERVOU o assento " + assento[assentoLivre].id_Assento + " com sucesso");
                }
                mudaLog(logAtualizado);
                notifyAll();
                System.out.println("----- THREAD " + idCliente + " = NOTIFY DO ASSENTO LIVRE COM O LOG: " + logAtualizado);

            }

        } else {
            System.out.println("----- THREAD " + idCliente + " = WAIT DO VISUALIZA ASSENTO LIVRE");
            while (!logAtualizado){
                Thread.sleep(1000);
            };
        }
        return 1;
    }

    synchronized int alocaAssentoDado(int id_Assento) throws InterruptedException {
        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA ASSENTO DADO: " + logAtualizado);

        if(logAtualizado){
            mudaLog(logAtualizado);




            System.out.println("----- THREAD " + idCliente + " = ENTROU NO ASSENTO DADO E O LOG ESTÁ " + logAtualizado);

            synchronized (ClienteThread.class) {

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
                    temp.add("mapa = " + String.valueOf(estadosAssentos) + "\n" +
                            "fop.op3(" + String.valueOf(idCliente) + "," + String.valueOf(id_Assento) + ",mapa)\n");

                    log.add(temp);
//      ===============================================================


                System.out.println("Cliente " + idCliente + " RESERVOU o assento " + assento[id_Assento].id_Assento + " como desejado");
                mudaLog(logAtualizado);
                notifyAll();
                System.out.println("----- THREAD " + idCliente + " = NOTIFY DO ASSENTO DADO COM O LOG: " + logAtualizado);
                return 1;

                } else {
                    System.out.println("Cliente " + idCliente + " tentou reservar o assento desejado " + assento[id_Assento].id_Assento + " mas não conseguiu");
                    System.out.println("----- THREAD " + idCliente + " = NOTIFY DO ASSENTO DADO TRETA");

                    mudaLog(logAtualizado);
                    notifyAll();
                }
            }

        } else {
            System.out.println("----- THREAD " + idCliente + " = WAIT DO ASSENTO DADO");
            while (!logAtualizado){
                Thread.sleep(1000);
            }
        }
        return 0;
    }

    synchronized void liberaAssento(int id_Assento) throws InterruptedException {
        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA LIBERA: " + logAtualizado);

        if (logAtualizado){
            mudaLog(logAtualizado);




            System.out.println("----- THREAD " + idCliente + " = ENTROU NO LIBERA E O LOG ESTÁ " + logAtualizado);

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
                    temp.add("mapa = " + String.valueOf(estadosAssentos) + "\n" +
                            "fop.op4(" + String.valueOf(idCliente) + "," + String.valueOf(id_Assento) + ",mapa)\n");

                    log.add(temp);
//      ===============================================================



                System.out.println("Cliente " + idCliente + " LIBEROU o " + assento[id_Assento].id_Assento + " com sucesso");

            } else if (assento[id_Assento].id_Cliente != idCliente) {
                System.out.println("Cliente " + idCliente + " tentou liberar o assento " + assento[id_Assento].id_Assento + " que não era dele");
            } else if (assento[id_Assento].status == 0) {
                System.out.println("Cliente " + idCliente + " tentou liberar o assento " + assento[id_Assento].id_Assento + " que já estava vazio");
            }

            mudaLog(logAtualizado);
            notifyAll();
            System.out.println("----- THREAD " + idCliente + " = NOTIFY DO LIBERA COM O LOG: " + logAtualizado);

        } else {
            System.out.println("WAIT DO LIBERA");
            while (!logAtualizado){
                Thread.sleep(1000);
            }
        }
    }

    public void run() {
        if (idCliente == 1) {
            int sucesso = 0;
            try {
                esperaAleatoria();
                visualizaAssentos();
                esperaAleatoria();
                sucesso = alocaAssentoLivre();
                esperaAleatoria();
                visualizaAssentos();

                synchronized (this) {
                    barreira++;             // esta thread terminou sua execucao
                    System.out.println("VALOR DA BARREIRA: " + barreira);
                    notifyAll();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (idCliente == 2) {
            int sucesso = 0;
            try {
                esperaAleatoria();
                visualizaAssentos();
                esperaAleatoria();
                sucesso = alocaAssentoDado(1 + (int) (Math.random() * (assento.length - 1)));
                esperaAleatoria();
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
                esperaAleatoria();
                visualizaAssentos();
                esperaAleatoria();
                sucesso = alocaAssentoLivre();
                esperaAleatoria();
                visualizaAssentos();
                esperaAleatoria();
                liberaAssento(1 + (int) (Math.random() * (assento.length - 1)));
                esperaAleatoria();
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
            imprimeLog(this.qtd_assentos);

        }

    }

    private void esperaAleatoria() throws InterruptedException {
        int tempo = (int)(Math.random()*300)+200;
        Thread.sleep(tempo);
    }

    private void imprimeLog(int qtd_assentos) {
        try {
            String logString = String.valueOf(log);
            logString = logString.replace("[[","");
            logString = logString.replace("]]","");
            logString = logString.replace("], [","");
            logString = logString.replace(" ","");

            String inicio_log ="import fop\nfop.geraMapa("+ qtd_assentos + ")\n";
            String fim_log ="fop.verificaCorretude()";
            logString = inicio_log + logString + fim_log;

            FileWriter fw;
            File arquivo = new File(nome_arquivo_log+".py");
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