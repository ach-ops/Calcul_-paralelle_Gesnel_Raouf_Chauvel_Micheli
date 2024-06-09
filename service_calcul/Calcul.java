
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
     * @param coor
     *      coordinates of the image part to calculate
     */
    @Override
    public Image calculer(Scene scene, Coordonnees coor) throws RemoteException{
        // Chronométrage du temps de calcul
        Instant debut = Instant.now();
        System.out.println("Calcul de l'image :\n - Coordonnées : "+ coor.x +","+ coor.y +"\n - Taille "+ coor.l + "x" + coor.h);
        Image image = scene.compute(coor.x, coor.y,coor.l, coor.h);
        Instant fin = Instant.now();

        long duree = Duration.between(debut, fin).toMillis();
        System.out.println("Image calculé en " + duree + "ms");
        return image;
    }
}