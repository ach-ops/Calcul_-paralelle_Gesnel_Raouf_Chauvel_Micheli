import raytracer.Image;
import raytracer.Scene;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface CalculInterface extends Remote
{
    /**
     * @param scene
     * @param coordonnees
     */
    public Image calculer(Scene scene, Coordonnees coordonnees) throws RemoteException, ServerNotActiveException;
}