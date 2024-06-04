import raytracer.Disp;
import raytracer.Scene;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Client {

    // Méthode pour diviser l'image en plusieurs parties
    public static List<Coordonnees> splitImage(int nbPart, int width, int heigth) {
        int nbLongPart = (int) (Math.sqrt(nbPart));
        int partwidth = width / nbLongPart;
        int partHeigth = heigth / nbLongPart;
        int x;
        int y = 0;
        List<Coordonnees> res = new ArrayList<>();
        for (int i = 0; i < nbLongPart; i++) {
            x = 0;
            for (int j = 0; j < nbLongPart; j++) {
                res.add(new Coordonnees(x, y, partwidth, partHeigth));
                x += partwidth;
            }
            y += partHeigth;
        }
        return res;
    }

    // Méthode pour calculer l'image
    public static void calculerImage(ServiceDistributeur distributeur, String scenePath, int nbpart, int width, int heigth) {
        List<Coordonnees> list = Client.splitImage(nbpart, width, heigth);
        // Récupération de la scène
        Scene scene = new Scene(scenePath, width, heigth);
        // Création d'une fenêtre
        Disp disp = new Disp("Raytracer", width, heigth);
        // Pour chaque coordonnées
        for (Coordonnees coor : list) {
            Thread t = new Thread() {
                public void run() {
                    boolean calc = false;

                    // Tant que le calcul n'est pas terminé
                    while (!calc) {
                        // Récupération d'une instance de 'calcule'
                        CalculInterface calcService = null;
                        try {
                            calcService = distributeur.demanderService();
                        } catch (RemoteException e) {
                            System.out.println("Serveur pas disponible, veuillez patienter...");
                            e.printStackTrace();
                            break;
                        }

                        if (calcService != null) {
                            // Calcul de l'image
                            try {
                                raytracer.Image image = calcService.calculer(scene, coor);
                                System.out.printf(coor.x + " " + coor.y);
                                System.out.printf(image.toString() + "\n");
                                disp.setImage(image, coor.x, coor.y);
                                calc = true;
                            } catch (RemoteException r) {
                                System.out.println("Erreur lors du calcul, veuillez patienter...");
                                r.printStackTrace();
                                try {
                                    distributeur.deleteCalcule(calcService);
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println("Service effacé");
                            } catch (ServerNotActiveException e) {
                                try {
                                    distributeur.deleteCalcule(calcService);
                                } catch (RemoteException ex) {
                                    throw new RuntimeException(ex);
                                }
                                System.out.println("Service effacé");
                            }
                        } else {
                            System.out.println("Serveur pas disponible, veuillez patienter...");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            };
            t.start();
        }
    }
}
