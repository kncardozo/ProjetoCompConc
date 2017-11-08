public class ClienteThread extends Thread {

    public int idThread;

    public ClienteThread(int id) {
        this.idThread = id;
    }

    public void run(){
        if(this.idThread == 1){
            cliente[idThread].visualizaAssentos();
            cliente[idThread].alocaAssentoLivre();

        } else if(this.idThread == 2){
            cliente[idThread].visualizaAssentos();
        } else if(this.idThread == 3){
            cliente[idThread].visualizaAssentos();
        }

    }
}
