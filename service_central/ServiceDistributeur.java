
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDistributeur extends Remote {

    /**
     * Récupère un service de calcul
     * @return le service de calcul
     * @throws RemoteException
     */
    public CalculInterface demanderService() throws RemoteException;


    /**
     * Ajoute un service de calcul
     * @param serviceCalcule le service de calcul à ajouter
     * @throws RemoteException
     */
    public void addCalcule(CalculInterface serviceCalcule) throws RemoteException;

    /**
     * Supprime un service de calcul
     * @param serviceCalcule le service de calcul à supprimer
     * @throws RemoteException
     */
    public void deleteCalcule(CalculInterface serviceCalcule) throws RemoteException;
}
