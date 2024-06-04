import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Scanner;

public class Start {
    public static final String COMMAND_AIDE = "Erreur dans la commande : Nom incorrect ou nombre de paramètres incorrect";
    public static final String COMMAND_INFO = "Veuillez entrer la commande suivante, choisissez les valeurs des paramètres : 'run [width] [height] [number of part]'";

    public static void main(String[] args) {
        ServiceDistributeur dist = null;
        // Connexion au registre RMI
        try {
            Registry reg = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
            dist = (ServiceDistributeur) reg.lookup("distributeur");
        } catch (AccessException a) {
            System.out.println("Accès échoué");
        } catch (RemoteException r) {
            System.out.println("Erreur lors de la récupération du registre : " + r.getMessage());
            System.exit(0);
        } catch (NotBoundException a) {
            System.out.println("Service nommé " + a.getMessage() + " non trouvé");
            System.exit(0);
        }

        Scanner sc = new Scanner(System.in);
        String carac = "";

        // Boucle principale pour recevoir les commandes de l'utilisateur
        while (!carac.equals("q")) {
            System.out.println(COMMAND_INFO);
            String command = sc.nextLine();
            String[] arguments = command.split(" ");

            for (String item : arguments) {
                System.out.println(item);
            }
            System.out.println("Longueur :" + arguments.length);

            // Vérification de la commande et de ses arguments
            if (Objects.equals(arguments[0], "run") && arguments.length == 4) {
                // Paramètres
                int width = Integer.parseInt(arguments[1]);
                int height = Integer.parseInt(arguments[2]);
                int nbPart = Integer.parseInt(arguments[3]);
                String scenePath = "./simple.txt";

                System.out.println("path");
                
                Client.calculerImage(dist, scenePath, nbPart, width, height);
            } else {
                System.out.println(COMMAND_AIDE);
            }
        }

        System.exit(0);
    }
}
