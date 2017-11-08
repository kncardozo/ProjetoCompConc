public class Assento{
    public int id_Assento;
    public int id_Thread;
    public int status;

    private ClienteThread cliente;

    public Assento (int id, ClienteThread cliente, int status){
        this.id_Assento = id;
        this.id_Thread = cliente.idThread;
        this.status = status;
    }
    public Assento (int id, int status){
        this.id_Assento = id;
        this.status = status;
    }

    public void visualizaAssentos(){

        for (int i=0; i < assentos.length(); i++ )
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
}