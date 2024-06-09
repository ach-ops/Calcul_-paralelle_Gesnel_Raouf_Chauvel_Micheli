import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


public class Start {
    public static void main(String[] args) {
        try {
            // Création du service
            Distributeur object = new Distributeur();
            ServiceDistributeur rd = (ServiceDistributeur)UnicastRemoteObject.exportObject(object,4553);

            // On crée un registry sur le port donné en argument
            Registry reg = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
            // On bind le service à l'objet
            reg.rebind("distributeur", rd);

            System.out.println("Service central démarré sur le port " + args[0]);
        } catch (AccessException a) {
            System.out.println("Erreur d'accès au registry : " + a.getMessage());
        } catch(RemoteException r){
            System.out.println("Erreur d'obtention du registry" + r.getMessage());
        }
    }
}