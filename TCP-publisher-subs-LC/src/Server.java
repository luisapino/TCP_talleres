import java.io.IOException;
import java.net.*;
import java.util.*;

public class Server {
    private static ArrayList<Sessions> sesiones = new ArrayList<>();
    private static ArrayList<String> temas = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6000);
        while(true){
            System.out.println("Esperando Cliente");
            Socket clientSocket=serverSocket.accept();
            System.out.println("Cliente conectado");
            Sessions session=new Sessions(clientSocket);
            new Thread(session).start();
            sesiones.add(session);
        }
    }

    public static boolean existeTema(String tema){
        boolean existe=false;
        for (int i=0; i<temas.size();i++){
            if (tema.equalsIgnoreCase(temas.get(i))){
                existe=true;
            }
        }
        return existe;
    }

    public static void addTema(String tema){
        temas.add(tema);
    }

      //metodo para enviar mensajes
    public static boolean enviarMensaje(String msg, String tema){

         if (existeTema(tema)) {
            sesiones.stream()
                    .filter(sesiones -> sesiones.estaSuscrito(tema))
                    .forEach(sesiones -> sesiones.mensaje(msg, tema));
            return true;
        }
        return false;
    }
}