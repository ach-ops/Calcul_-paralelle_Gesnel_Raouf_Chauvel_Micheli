import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Scanner;

public class Start {

    public static final String COMMAND_HELP = "Erreur de commande : Nom incorrect ou nombre de paramètres incorrect";
    public static final String COMMAND_INFO = "Veuillez saisir la commande suivante, en choisissant les valeurs des paramètres : 'run [largeur] [hauteur] [nombre de parties]' ou 'sync [largeur] [hauteur] [nombre de parties]'";

    public static void main(String[] args) {

        ServiceDistributeur dist = null; // Initialisation du service distributeur à null

        try {
            Registry reg = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1])); // Récupération du registre RMI
            dist = (ServiceDistributeur) reg.lookup("distributeur"); // Récupération du service distributeur depuis le registre
        } catch (AccessException a) {
            System.out.println("Échec de l'accès "); // Affichage en cas d'échec d'accès
        } catch(RemoteException r) {
            System.out.println("Erreur lors de la récupération du registre" + r.getMessage()); // Affichage en cas d'erreur lors de la récupération du registre
            System.exit(0); // Sortie du programme
        } catch (NotBoundException a) {
            System.out.println("Service nommé "+ a.getMessage() + " non trouvé"); // Affichage en cas de service non trouvé dans le registre
            System.exit(0); // Sortie du programme
        }

        Scanner sc = new Scanner(System.in); // Création d'un scanner pour la saisie

        String carac = "";

        while (!carac.equals("q")){
            System.out.println(COMMAND_INFO); // Affichage du message d'information sur la commande à saisir
            String command = sc.nextLine(); // Saisie de la commande par l'utilisateur

            String[] arguments = command.split(" "); // Découpage de la commande en arguments

            if (Objects.equals(arguments[0], "run") && arguments.length == 4) {
                // Paramètres
                int width = Integer.parseInt(arguments[1]); // Largeur de l'image
                int height = Integer.parseInt(arguments[2]); // Hauteur de l'image
                int nbPart = Integer.parseInt(arguments[3]); // Nombre de parties de l'image
                String scenePath = "./simple.txt"; // Chemin de la scène

                // Démarrage du calcul de l'image
                Client.calculerImage(dist, scenePath, nbPart, width, height);
            } else if (Objects.equals(arguments[0], "sync") && arguments.length == 4) {
                // Paramètres pour sync
                int width = Integer.parseInt(arguments[1]);
                int height = Integer.parseInt(arguments[2]);
                int nbPart = Integer.parseInt(arguments[3]);
                String scenePath = "./simple.txt";
                // Démarrage du calcul de l'image avec synchronisation
                Client.calculerImageSync(dist, scenePath, nbPart, width, height);
            } else {
                System.out.println(COMMAND_HELP);
            }
        }

        System.exit(0);
    }
}
