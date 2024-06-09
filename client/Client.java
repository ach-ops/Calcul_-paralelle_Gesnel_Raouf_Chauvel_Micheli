import raytracer.Disp;
import raytracer.Scene;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Client {

    // Méthode pour diviser une image en parties
    public static List<Coordonnees> splitImage(int nbPart, int width, int height) {
        int nbLongPart = (int) (Math.sqrt(nbPart));
        int partwidth = width / nbLongPart;
        int partHeight = height / nbLongPart;
        int x;
        int y = 0;
        List<Coordonnees> res = new ArrayList<>(); // Initialisation d'une liste de Coordonnees

        // Boucle pour parcourir les parties de l'image
        for (int i = 0; i < nbLongPart; i++) {
            x = 0;
            for (int j = 0; j < nbLongPart; j++) {
                res.add(new Coordonnees(x, y, partwidth, partHeight)); // Ajout des coordonnées d'une partie à la liste
                x += partwidth;
            }
            y += partHeight;
        }
        return res; // Retourne la liste des coordonnées des parties de l'image
    }

    // Méthode pour calculer une image en utilisant un service de distribution
    public static void calculerImage(ServiceDistributeur distributeur, String scenePath, int nbPart, int width, int height) {
        // Récupération de la liste des coordonnées des parties de l'image
        List<Coordonnees> list = Client.splitImage(nbPart, width, height);
        // Récupération de la scène
        Scene scene = new Scene(scenePath, width, height);

        // Création d'une fenêtre
        Disp disp = new Disp("Raytracer", width, height);
        long startTime = System.currentTimeMillis();
        for (Coordonnees coor : list) {
            // Création d'un nouveau thread
            Thread t = new Thread() {
                public void run() {
                    boolean calc = false;

                    while (!calc) {

                        CalculInterface calcService = null;

                        try {
                            calcService = distributeur.demanderService();
                        } catch (RemoteException e) {
                            System.out.println("Serveur non disponible");
                            e.printStackTrace();
                            break;
                        }

                        if (calcService != null) {
                            try {
                                raytracer.Image image = calcService.calculer(scene, coor);
                                System.out.printf("cordonnée x : " + coor.x + " " + "cordonnée y :" + coor.y + "\n");
                                System.out.printf(image.toString() + "\n");
                                disp.setImage(image, coor.x, coor.y);
                                calc = true;
                            } catch (RemoteException r) {
                                System.out.println("Échec du calcul");
                                r.printStackTrace();
                                try {
                                    distributeur.deleteCalcule(calcService);
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println("Service supprimé");
                            } catch (ServerNotActiveException e) {
                                try {
                                    distributeur.deleteCalcule(calcService);
                                } catch (RemoteException ex) {
                                    throw new RuntimeException(ex);
                                }
                                System.out.println("Service supprimé");
                            }
                        } else {
                            System.out.println("Aucun service disponible pour le moment, veuillez patienter...");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    long endTime = System.currentTimeMillis();
                    long totalTime = endTime - startTime;
                    System.out.println("Temps total de calcul : " + totalTime + " ms");
                }

            };
            // Démarrage du thread
            t.start();
        }
    }
    // Méthode pour calculer une image de manière synchrone
    public static void calculerImageSync(ServiceDistributeur distributeur , String scenePath, int nbPart, int width, int height) {
        // Récupération de la liste des coordonnées des parties de l'image
        List<Coordonnees> list = Client.splitImage(nbPart, width, height);
        // Récupération de la scène
        Scene scene = new Scene(scenePath, width, height);
        // Création d'une fenêtre
        Disp disp = new Disp("Raytracer", width, height);

        for (Coordonnees coor : list) {
            boolean calc = false;

            while (!calc) {
                CalculInterface calcService = null;

                try {
                    calcService = distributeur.demanderService();
                } catch (RemoteException e) {
                    System.out.println("Serveur non disponible");
                    e.printStackTrace();
                    break;
                }

                if (calcService != null) {
                    try {
                        raytracer.Image image = calcService.calculer(scene, coor);
                        System.out.printf("cordonnée x : " + coor.x + " " + "cordonnée y :" + coor.y + "\n");
                        System.out.printf(image.toString() + "\n");
                        disp.setImage(image, coor.x, coor.y);
                        calc = true;
                    } catch (RemoteException r) {
                        System.out.println("Échec du calcul");
                        r.printStackTrace();
                        try {
                            distributeur.deleteCalcule(calcService);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Service supprimé");
                    } catch (ServerNotActiveException e) {
                        try {
                            distributeur.deleteCalcule(calcService);
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println("Service supprimé");
                    }
                } else {
                    System.out.println("Aucun service disponible pour le moment, veuillez patienter...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
