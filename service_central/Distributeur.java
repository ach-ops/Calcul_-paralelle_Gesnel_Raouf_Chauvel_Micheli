import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.List;

public class Distributeur implements ServiceDistributeur {
    // Liste des services
    List<CalculInterface> services = new ArrayList<>();
    // Indice du service
    int indice = 0;

    @Override
    public synchronized CalculInterface demanderService() throws RemoteException {
        // On vérifie si la liste des services est vide
        if (services.isEmpty()) {
            return null;
        }

        System.out.println();
        // On ajoute 1 à l'indice
        indice++;
        if (indice >= services.size()) {
            indice = 0;
        }

        return services.get(indice);
    }

    @Override
    public synchronized void addCalcule(CalculInterface serviceCalcule) throws RemoteException {
        synchronized (services) {
            services.add(serviceCalcule);
            String host = "";
            try {
                host = RemoteServer.getClientHost();
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }
            System.out.println("Service ajouté : " + host);
        }
    }

    @Override
    public synchronized void deleteCalcule(CalculInterface serviceCalcule) throws RemoteException {
        synchronized (services) {
            services.remove(serviceCalcule);

            String host = "";
            try {
                host = RemoteServer.getClientHost();
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }
            System.out.println("Service supprimé : " + host);
        }
    }

}
