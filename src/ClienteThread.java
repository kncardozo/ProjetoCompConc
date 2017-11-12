import java.util.Vector;

class ClienteThread extends Thread {

    private int idCliente;
    private String nome_arquivo_log;
    private Assento[] assento;
    private static Vector log = new Vector();

    ClienteThread(int id, Assento[] assento) {
        this.idCliente = id;
        System.out.println("Cliente " + idCliente + " criado");
        this.assento = assento;
    }

    ClienteThread(int id, String nome_arquivo_log) {
        this.idCliente = id;
        this.nome_arquivo_log= nome_arquivo_log;
        System.out.println("Log de ID " + idCliente + " criado");
    }

    synchronized void visualizaAssentos() {
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
        System.out.println("Log:\n" + log);

        System.out.println(estadosAssentos + " visualizado pelo cliente " + idCliente);

    }

    synchronized int alocaAssentoLivre() {

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
            System.out.println("Log:" + log);


            System.out.println("Cliente " + idCliente + " RESERVOU o " + assento[lugarLivreAleatorio].id_Assento + " com sucesso");

        }

        return 1;

    }

    synchronized int alocaAssentoDado(int id_Assento) {
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
            System.out.println("Log:" + log);


            System.out.println("Cliente " + idCliente + " RESERVOU o assento " + assento[id_Assento].id_Assento + " como desejado");

            return 1;

        } else {
            System.out.println("Cliente " + idCliente + " tentou reservar o assento desejado " + assento[id_Assento].id_Assento + " mas não conseguiu");
        }
        return 0;
    }

    synchronized void liberaAssento(int id_Assento) {
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
            System.out.println("Log:" + log);



            System.out.println("Cliente " + idCliente + " LIBEROU o " + assento[id_Assento].id_Assento + " com sucesso");

        } else if (assento[id_Assento].id_Cliente != idCliente) {
            System.out.println("Cliente " + idCliente + " tentou liberar o assento " + assento[id_Assento].id_Assento + " que não era dele");
        } else if (assento[id_Assento].status == 0) {
            System.out.println("Cliente " + idCliente + " tentou liberar o assento " + assento[id_Assento].id_Assento + " que já estava vazio");
        }
    }

    public void run() {
        if (idCliente == 1) {
            int sucesso = 0;
            visualizaAssentos();
            sucesso = alocaAssentoLivre();
            visualizaAssentos();
        }
        if (idCliente == 2) {
            int sucesso = 0;
            visualizaAssentos();
            sucesso = alocaAssentoDado(1 + (int) (Math.random() * (assento.length - 1)));
            visualizaAssentos();
        }
        if (idCliente == 3) {
            int sucesso = 0;
            visualizaAssentos();
            sucesso = alocaAssentoLivre();
            visualizaAssentos();
            liberaAssento(1 + (int) (Math.random() * (assento.length - 1)));
            visualizaAssentos();
        }

    }
}