
import raytracer.Image;
import raytracer.Scene;
import java.rmi.RemoteException;
import java.time.Duration;
import java.time.Instant;

public class Calcul implements CalculInterface
{
    /**
     * @param scene
     *      Image scene
     * @param coordonnees
     *      Coordonnées de l'image à calculer
     */
    @Override
    public Image calculer(Scene scene, Coordonnees coordonnees) throws RemoteException{
        // Chronométrage du temps de calcul
        Instant debut = Instant.now();
        System.out.println("Calcul de l'image :\n - Coordonnées : "+ coordonnees.x +","+ coordonnees.y +"\n - Taille "+ coordonnees.l + "x" + coordonnees.h);
        Image image = scene.compute(coordonnees.x, coordonnees.y,coordonnees.l, coordonnees.h);
        Instant fin = Instant.now();

        long duree = Duration.between(debut, fin).toMillis();
        System.out.println("Image calculé en " + duree + "ms");
        return image;
    }
}