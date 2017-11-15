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
    private static int id_operacao_para_log;
    private static int id_thread_para_log;
    private static int id_assento_para_log;


    //    construtor das treads de clientes
    ClienteThread(int id, Assento[] assento) {
        this.idCliente = id;
        System.out.println("Cliente " + idCliente + " criado");
        this.assento = assento;
    }

    //    construtor da thread de escritura no arquivo log
    ClienteThread(int id, String nome_arquivo_log, int qtd_assentos, Assento[] assento) {
        this.idCliente = id;
        this.nome_arquivo_log = nome_arquivo_log;
        System.out.println("\nLog de ID " + idCliente + " criado");
        this.qtd_assentos = qtd_assentos;
        this.assento = assento;

    }


    private void setaLogTrue(boolean log) {
        System.out.println("----- THREAD " + idCliente + " = O ESTADO DO LOG ERA " + log + " E VAI MUDAR PARA: " + !log);
        logAtualizado = true;
    }

    private void setaLogFalse(boolean log) {
        System.out.println("----- THREAD " + idCliente + " = O ESTADO DO LOG ERA " + log + " E VAI MUDAR PARA: " + !log);
        logAtualizado = false;
    }


    synchronized void visualizaAssentos() throws InterruptedException {
        synchronized (this) {
            System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA VISUALIZA: " + logAtualizado);


            while (!logAtualizado) {
                System.out.println("----- THREAD " + idCliente + " = WAIT DO VISUALIZA");
                wait(200);
            }


            setaLogFalse(logAtualizado);
            System.out.println("----- THREAD " + idCliente + " = ENTROU NO VISUALIZA E O LOG ESTÁ " + logAtualizado);
            id_operacao_para_log = 1;
            id_assento_para_log = 0;
            id_thread_para_log = idCliente;


            notifyAll();
//            System.out.println("----- THREAD " + idCliente + " = NOTIFY DO VISUALIZA COM O LOG: " + logAtualizado);
        }
    }


    synchronized int alocaAssentoLivre() throws InterruptedException {
        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA ASSENTO LIVRE: " + logAtualizado);

        while (!logAtualizado) {
            System.out.println("----- THREAD " + idCliente + " = WAIT DO VISUALIZA ASSENTO LIVRE");
            wait(200);
        }

        setaLogFalse(logAtualizado);


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

            id_operacao_para_log = 2;
            id_assento_para_log = 0;
            id_thread_para_log = idCliente;

            notifyAll();
            return 0;

        } else {
            synchronized (ClienteThread.class) {
                int assentoLivre = assentosLivres.get(0);
                assento[assentoLivre].id_Cliente = idCliente;
                assento[assentoLivre].status = 1;

                id_operacao_para_log = 2;
                id_assento_para_log = assento[assentoLivre].id_Assento;
                id_thread_para_log = idCliente;

                System.out.println("Cliente " + idCliente + " RESERVOU o assento " + assento[assentoLivre].id_Assento + " com sucesso");
            }
            notifyAll();
//                System.out.println("----- THREAD " + idCliente + " = NOTIFY DO ASSENTO LIVRE COM O LOG: " + logAtualizado);

        }

        return 1;
    }

    synchronized int alocaAssentoDado(int id_Assento) throws InterruptedException {
        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA ASSENTO DADO: " + logAtualizado);

        while (!logAtualizado) {
            System.out.println("----- THREAD " + idCliente + " = WAIT DO ASSENTO DADO");
            wait(200);
        }
        setaLogFalse(logAtualizado);


        System.out.println("----- THREAD " + idCliente + " = ENTROU NO ASSENTO DADO E O LOG ESTÁ " + logAtualizado);

        synchronized (ClienteThread.class) {

            if (assento[id_Assento].status == 0) {
                assento[id_Assento].status = 1;
                assento[id_Assento].id_Cliente = idCliente;

                id_operacao_para_log = 3;
                id_assento_para_log = assento[id_Assento].id_Assento;
                id_thread_para_log = idCliente;


                System.out.println("Cliente " + idCliente + " RESERVOU o assento " + assento[id_Assento].id_Assento + " como desejado");
                notifyAll();
//                System.out.println("----- THREAD " + idCliente + " = NOTIFY DO ASSENTO DADO COM O LOG: " + logAtualizado);
                return 1;

            } else {
                System.out.println("Cliente " + idCliente + " tentou reservar o assento desejado " + assento[id_Assento].id_Assento + " mas não conseguiu");
                id_operacao_para_log = 3;
                id_assento_para_log = assento[id_Assento].id_Assento;
                id_thread_para_log = idCliente;

                System.out.println("----- THREAD " + idCliente + " = NOTIFY DO ASSENTO DADO TRETA");

                notifyAll();
            }
        }

        return 0;
    }

    synchronized void liberaAssento(int id_Assento) throws InterruptedException {
        System.out.println("----- THREAD " + idCliente + " = VAI CHECAR O LOG PELA LIBERA: " + logAtualizado);

        while (!logAtualizado) {
            System.out.println("WAIT DO LIBERA");
            wait(200);
        }
        setaLogFalse(logAtualizado);


        System.out.println("----- THREAD " + idCliente + " = ENTROU NO LIBERA E O LOG ESTÁ " + logAtualizado);

        if (assento[id_Assento].id_Cliente == idCliente && assento[id_Assento].status == 1) {
            assento[id_Assento].status = 0;
            assento[id_Assento].id_Cliente = 0;

            id_operacao_para_log = 4;
            id_assento_para_log = assento[id_Assento].id_Assento;
            id_thread_para_log = idCliente;


            System.out.println("Cliente " + idCliente + " LIBEROU o " + assento[id_Assento].id_Assento + " com sucesso");

        } else if (assento[id_Assento].id_Cliente != idCliente) {
            System.out.println("Cliente " + idCliente + " tentou liberar o assento " + assento[id_Assento].id_Assento + " que não era dele");

            id_operacao_para_log = 4;
            id_assento_para_log = assento[id_Assento].id_Assento;
            id_thread_para_log = idCliente;

//            setaLogTrue(logAtualizado);

        }

        notifyAll();
//            System.out.println("----- THREAD " + idCliente + " = NOTIFY DO LIBERA COM O LOG: " + logAtualizado);

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

        if (idCliente == 0) {
            while (barreira != 3) {
                while (logAtualizado) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!logAtualizado) {
                    System.out.println("----- SOU O LOG E ACORDEI PARA ATUALIZAR O LOG");
                    atualizaLog(id_operacao_para_log, id_thread_para_log, id_assento_para_log);
                }
            }
            System.out.println("\n TODAS AS OUTRAS THREADS TERMINARAM");
            imprimeLog(this.qtd_assentos);
        }

    }

    private void atualizaLog(int id_operacao, int id_thread, int id_assento) {
        System.out.println("----- SOU O LOG E ATUALIZEI O LOG");

        Vector estadosAssentos = new Vector();
        for (int i = 1; i < assento.length; i++) {
            if (assento[i].status == 0) {
                estadosAssentos.add(assento[i].status);
            } else {
                estadosAssentos.add(assento[i].id_Cliente);
            }
        }
        Vector temp = new Vector();

        if (id_operacao == 1) {
            temp.add("mapa = " + String.valueOf(estadosAssentos) + "\n" +
                    "fop.op1(" + String.valueOf(id_thread) + ",mapa)\n");
            log.add(temp);
            setaLogTrue(logAtualizado);
            synchronized (this) {
                System.out.println("----- SOU O LOG E NOTIFIQUEI A GALERA");
                this.notify();
            }

        } else {
            temp.add("mapa = " + String.valueOf(estadosAssentos) + "\n" +
                    "fop.op" + id_operacao + "(" + String.valueOf(id_thread) + "," + String.valueOf(id_assento) + ",mapa)\n");

            log.add(temp);
            setaLogTrue(logAtualizado);
            synchronized (this) {
                System.out.println("----- SOU O LOG E NOTIFIQUEI A GALERA");
                this.notifyAll();
            }
        }
    }

    private void esperaAleatoria() throws InterruptedException {
        int tempo = (int) (Math.random() * 300) + 200;
        Thread.sleep(tempo);
    }

    private void imprimeLog(int qtd_assentos) {
        try {
            String logString = String.valueOf(log);
            logString = logString.replace("[[", "");
            logString = logString.replace("]]", "");
            logString = logString.replace("], [", "");
            logString = logString.replace(" ", "");

            String inicio_log = "import fop\nfop.geraMapa(" + qtd_assentos + ")\n";
            String fim_log = "fop.verificaCorretude()";
            logString = inicio_log + logString + fim_log;

            nome_arquivo_log = nome_arquivo_log + ".py";

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