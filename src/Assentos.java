class Assento{
    public int id_Assento;
    public int id_Cliente;
    public int status;

    private ClienteThread cliente;
    private Assento[] assentos;

    public Assento (int id, ClienteThread cliente, int status){
        this.id_Assento = id;
        this.id_Cliente = cliente.getClienteId();
        this.status = status;
    }
    public Assento (int id, int status){
        this.id_Assento = id;
        this.status = status;
    }

}