import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Sessions implements Runnable{
    private Socket socket;

    private ArrayList<String> temaSuscripcion = new ArrayList<>();

    public Sessions(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        while (true) {
        try {
            byte[] bf = new byte[500]; //lo lee y lo convierte en cadena de texto
            socket.getInputStream().read(bf);
            String rec = new String(bf, "UTF-8");
            rec = rec.trim();
            String[] partes = rec.split(" ");
            String comando = partes[0];
            String tema;
            String msg = "";

            if (comando.equalsIgnoreCase("suscribe")) {
                try {
                tema = partes[1];
                if (Server.existeTema(tema)) {
                    if (estaSuscrito(tema)) {
                        socket.getOutputStream().write("Esta ya suscrito a un tema".getBytes());
                    } else {
                        suscribe(tema);
                    }
                } else {
                    Server.addTema(tema);
                    suscribe(tema);
                }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    socket.getOutputStream().write("Se suscribe escribiendo: suscribe *ingresa tema aqui* ".getBytes());
                }
            }
            
            
            else if(comando.equalsIgnoreCase("send")){
                try {
                    tema = partes[1];
                    for(int i = 2; i <partes.length; i++){
                        msg = msg+" "+partes[i];
                    }

                    if(!Server.enviarMensaje(msg, tema)){
                        socket.getOutputStream().write("El mensaje no fue enviado".getBytes());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    socket.getOutputStream().write("Se envia un mensaje escribiendo: send *ingresa el tema y luego ingresa el mensaje".getBytes());
                }

            }
        } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //agrega un tema al arreglo de temas para suscribirse
    public void suscribe(String topic){
        temaSuscripcion.add(topic);
    }

    //veririfa si está suscrito a un tema
    public boolean estaSuscrito(String tipo){
        boolean aux=false;
        for (int i = 0; i< temaSuscripcion.size(); i++){
            if(tipo.equalsIgnoreCase(temaSuscripcion.get(i))){
                aux=true;
            }
        }
        return aux;
    }


    public void mensaje(String msg, String tema){
        try {
            socket.getOutputStream().write((tema+ ", te envian el mensaje: "+msg).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
