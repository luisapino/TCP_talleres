import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws UnknownHostException, IOException {

        Socket socket = new Socket("127.0.0.1", 6000);

        Scanner sc = new Scanner(System.in);

        //---Sección para recibir mensajes del server:

        new Thread(()->{
        while(true){ //evita que se cierre la ejecución
            try {
                byte[] bf = new byte[300];//desde -127 a 128, el buffer funciona como un recipiente para los bytes que me lleguen
                socket.getInputStream().read(bf);
                String rec = new String(bf, "UTF-8");
                System.out.println(rec.trim());

            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
       }).start();

       System.out.println("""
        ***Se usa así:
                Para suscribirte: escribe ¨suscribe¨ mas el tema
                Para enviar un mensaje: escribe ¨send¨ mas el tema y lo que quieras enviar
            """);

        while(true){
            String msg = sc.nextLine();
            socket.getOutputStream().write(msg.getBytes("UTF-8"));

        }
    }
}