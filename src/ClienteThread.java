class ClienteThread extends Thread {

    private int idCliente;
    private Assento[] assentos;
    private ClienteThread[] cliente;


    public ClienteThread(int id) {
        this.idCliente = id;
    }

    public int getClienteId(){
        return this.idCliente;
    }

    public void visualizaAssentos(){

        for (int i=0; i < assentos.length; i++ )
            System.out.println(assentos[i].id_Assento + assentos[i].status);
    }

    public int alocaAssentoLivre (Assento assento, int id){

        return 0;
    }
    public int alocaAssentoDado (Assento assento, int id){

        return 0;
    }

    public int liberaAssento(Assento assento, int id){

        return 0;
    }

    public void run() {

        if (this.getClienteId() == 1) {
            cliente[idCliente].visualizaAssentos();

        } else if (this.getClienteId() == 2) {
            cliente[idCliente].visualizaAssentos();

        } else if (this.getClienteId() == 3) {
            cliente[idCliente].visualizaAssentos();
        }
    }
}
