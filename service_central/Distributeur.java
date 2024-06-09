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
            System.out.println("Service ajouté");
            System.out.println("Nombre de services présent : " + services.size());
            String host = "";
            try {
                host = RemoteServer.getClientHost();
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }
            System.out.println("Adresse IP du service : " + host);
        }
    }

    @Override
    public synchronized void deleteCalcule(CalculInterface serviceCalcule) throws RemoteException {
        synchronized (services){
            services.remove(serviceCalcule);
            System.out.println("Service supprimé");
            System.out.println("Nombre de services restants : " + services.size());
            String host = "";
            try {
                host = RemoteServer.getClientHost();
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }
            System.out.println("Adresse IP du service qui est parti : " + host);
        }
    }


}
