import java.rmi.NotBoundException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Start
{
    public static void main(String[]args) throws RemoteException
    {
        //--- Gestion des numéros de ports en paramètres du programme ---
        int portService = 0;

        try {
            //On crée une instance du service
            Calcul service = new Calcul();

            //On met le service à disposition via un port de la machine
            CalculInterface serviceInterface = (CalculInterface) UnicastRemoteObject.exportObject(service, portService);

            // Getting central service
            Registry reg = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
            ServiceDistributeur dist = (ServiceDistributeur) reg.lookup("distributeur");

            // Register to the Central service
            dist.addCalcule(serviceInterface);
            System.out.println("Service noeudCalcul démarré");

            // Cas ou le noeudCalcul est arrêté
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    dist.deleteCalcule(serviceInterface);
                    System.out.println("Service noeudCalcul arrêté");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }));

        }catch (java.rmi.server.ExportException e) {
            System.err.println("Port " + portService + " déjà utilisé : impossible d'y affecter le service");
            System.exit(-1);
        }catch (RemoteException r){
            System.out.println("Erreur lors de l'ajout du service au service central");
            r.printStackTrace();
        }
         catch (NotBoundException e) {
            System.out.println("Erreur lors de la récupération du service central ");
        }
    }
}