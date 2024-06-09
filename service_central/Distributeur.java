import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.List;

public class Distributeur implements ServiceDistributeur {
    List<CalculInterface> services = new ArrayList<>();
    int indice = 0;

    @Override
    public synchronized CalculInterface demanderService() throws RemoteException {
        // check if any services exist
        if (services.isEmpty()) {
            return null;
        }

        System.out.println();
        indice++;
        if (indice >= services.size()){
            indice = 0;
        }

        return services.get(indice);
    }

    @Override
    public synchronized void  addCalcule(CalculInterface serviceCalcule) throws RemoteException {
        synchronized (services){
            services.add(serviceCalcule);
            System.out.println("Noeud ajouté");
            String host = "";
            try {
                host = RemoteServer.getClientHost();
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }
            System.out.println("Enregistrement du nouveau noeud: " + host);
            System.out.println("Nombre de noeuds présents : " + services.size());
        }
    }

    @Override
    public synchronized void deleteCalcule(CalculInterface serviceCalcule) throws RemoteException {
        synchronized (services){
            services.remove(serviceCalcule);
            System.out.println("Noeud supprimé");
            String host = "";
            try {
                host = RemoteServer.getClientHost();
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }
            System.out.println("Adresse IP du noeud qui est parti : " + host);
            System.out.println("Nombre de noeuds restants : " + services.size());
        }
    }


}
