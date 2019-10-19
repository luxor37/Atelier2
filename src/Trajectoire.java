// * Ce programme génère un fichier JSON formée d'une liste de cameras.
// *
// * Chacune de ces caméras produit une image à partir d'une scène 3D. Les images
// * sont ensuite assemblées en un film.

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;


public class Trajectoire
{

    private static double DISTANCE_OEIL_ECRAN = 1.0;

    /**
     * Classe Vecteur3d
     * 
     * Un vecteur de dimension 3 dont les compsantes sont représentées par trois
     * variables de type double.
     */
    private static class Vecteur3d 
    {
        double x,y,z;

        /**
         * Constructeur à partir des trois composantes du vecteur.
         */
        Vecteur3d(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

//        /**
//         * Constructeur à partir d'un autre vecteur (copie)
//         */
//        public Vecteur3d(Vecteur3d v) {
//            this.x = v.x;
//            this.y = v.y;
//            this.z = v.z;
//        }

        /**
         * Addition de vecteurs.
         *
         * @param v  un vecteur
         * @return  vecteur somme
         */
        Vecteur3d plus(Vecteur3d v) {
            return new Vecteur3d(this.x + v.x, this.y + v.y, this.z + v.z);
        }

//        /**
//         * Soustraction de vecteurs
//         *
//         * @param v  un vecteur
//         * @return  le vecteur this - v
//         */
//        public Vecteur3d moins(Vecteur3d v) {
//            return new Vecteur3d(this.x - v.x, this.y - v.y, this.z - v.z);
//        }

        /**
         * Multiplication par un scalaire.
         *
         * @param d  un scalaire
         * @return  le vecteur d * this.
         */
        Vecteur3d multiplie(double d) {
            return new Vecteur3d(d*this.x, d*this.y, d*this.z);
        }

        /**
         * Calcule le carré de la norme du vecteur.
         *
         * En général, on préfère utiliser cette mesure de longueur plutôt que la
         * norme comme tel. Lorsqu'on cherche uniquement à comparer des vecteurs
         * entre eux, l'utilisation de la norme au carrée permet d'éviter le calcul
         * d'une racine carrée .
         *
         * @return  le carré de la norme de this
         */
        double normeCarree() {
            return this.x*this.x + this.y*this.y + this.z*this.z;
        }

        /**
         * Norme du vecteur
         *
         * return  la norme du vecteur
         */
        double norme() {
            return Math.sqrt(this.normeCarree());
        }

        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }
    }


    /**
     *
     * Classe pour représenter, modifier et afficher une camera.
     *
     */
    private static class Camera 
    {
        private Vecteur3d position;
        private Vecteur3d orientationRegard;
        private Vecteur3d orientationVersLeHaut;
        private double distanceOeilEcran;
        private int largeurImage;
        private int hauteurImage;


        Camera(Vecteur3d position,
               Vecteur3d orientationRegard,
               Vecteur3d orientationVersLeHaut,
               double distanceOeilEcran,
               int largeurImage,
               int hauteurImage) {
            this.position = position;
            this.orientationRegard = orientationRegard;
            this.orientationVersLeHaut = orientationVersLeHaut;
            this.distanceOeilEcran = distanceOeilEcran;
            this.largeurImage = largeurImage;
            this.hauteurImage = hauteurImage;
        }

      void setPosition(Vecteur3d position)
        {
          this.position = position;
        }

      void setOrientationRegard(Vecteur3d orientationRegard)
        {
          this.orientationRegard = orientationRegard;
        }

      void setOrientationVersLeHaut(Vecteur3d orientationVersLeHaut)
        {
          this.orientationVersLeHaut = orientationVersLeHaut;
        }

      public String toString() {
          return "        {\n"
              + "            \"position\"              : \"" + position              + "\",\n"
              + "            \"orientationRegard\"     : \"" + orientationRegard     + "\",\n"
              + "            \"orientationVersLeHaut\" : \"" + orientationVersLeHaut + "\",\n"
              + "            \"distanceOeilEcran\"     : " + distanceOeilEcran       + ",\n"
              + "            \"largeurImage\"          : " + largeurImage            + ",\n"
              + "            \"hauteurImage\"          : " + hauteurImage            + "\n"
              + "        },";
        }

    }

    /**
     * Affiche l'entête du fichier JSON.
     */
    private static void initJSON() 
    {
        System.out.println("{ \"Cameras\" : [");
    }

    /**
     * Affiche la clôture du fichier JSON.
     */
    private static void clotureJSON()
    {
        System.out.println("]\n}");
    }

    private static Vecteur3d GetPosition(double[] abc, double t){
        double x = abc[0]*(1+Math.cos(t))+1;
        double y = abc[1]*(Math.sin(t));
        double z = abc[2]*(1+Math.cos(t));

        return new Vecteur3d(x, y, z);
    }

    private static Vecteur3d GetOrientation(double[] abc, double t){
        // (= vecteur tangent)
        double x = -abc[0]*Math.sin(t);
        double y = abc[1]*Math.cos(t);
        double z = -abc[2]*Math.sin(t);

        return new Vecteur3d(x, y, z);
    }

    /**
     * Réalise une trajectoire toute simple, en 4 phases
     *
     * 1. Avancer de (-10,0,0.75) à (0,0,0)
     * 2. Effecture une rotation d'un demi-tour vers la droite.
     * 3. Effecture une rotation d'un quart de tour du vecteur normal à la trajectore.
     * 4. Reculer de (0,0,0) à (10,0,0.75);
     *
     * Le contenu du fichier JSON correspondant à cette trajectoire est affiché via
     * la sortie standard.
     *
     * @param  n             nombre d'images à générer
     * @param  largeurImage  nombre de colonnes de l'image
     * @param  hauteurImage  nombre de lignes de l'image
     */
    private static void question0(int n, int largeurImage, int hauteurImage)
    {
        initJSON();

        Camera camera = new Camera(
                new Vecteur3d(0, 0, 0), 
                new Vecteur3d(1, 0, 0), 
                new Vecteur3d(0, 0, 1), 
                DISTANCE_OEIL_ECRAN,
                largeurImage,
                hauteurImage
                );


        // Phase 1 : la caméra passe de (-10,0,0.75) à (0,0,0)
        Vecteur3d depart      = new Vecteur3d(-10, 0, 0.75);
        Vecteur3d destination = new Vecteur3d(  0, 0, 0);
        for (int i = 0; i< n; ++i)
        {
            double t = (i*1.0) / (n - 1.0); // 0.0 <= t <= 1.0 (croissant)
            // Calcul de la position via une interpolation linéaire
            // calcule effectué : position = (1-t)*depart + t*destination
            Vecteur3d position = depart.multiplie(1.0-t).plus(destination.multiplie(t));
            camera.setPosition(position);
            System.out.println(camera);
        }

        // Phase 2 : la caméra fait un demi-tour sur elle même dans le sens
        // anti-horaire
        for (int i = 0; i< n; ++i)
        {
            double t = (1.0 * i) / (n - 1.0); // 0.0 <= t <= 1.0 (croissant)
            double angle = Math.PI * t;
            Vecteur3d orientationRegard = new Vecteur3d(Math.cos(angle), Math.sin(angle), 0.0);
            camera.setOrientationRegard(orientationRegard);
            System.out.println(camera);
        }

        // Phase 3 : la caméra fait un quart de tour de haut en bas
        for (int i = 0; i< n; ++i)
        {
            double t = (1.0 * i) / (n - 1.0); // 0.0 <= t <= 1.0 (croissant)
            double angle = Math.PI*(1.0-t)/2.0;
            Vecteur3d orientationVersLeHaut = new Vecteur3d(0, Math.cos(angle), Math.sin(angle));
            camera.setOrientationVersLeHaut(orientationVersLeHaut);
            System.out.println(camera);
        }
       
        // Phase 4 : la caméra se déplace en marche arrière de (0,0,0) à (10,0,0)
        depart = new Vecteur3d(0,0,0);
        destination = new Vecteur3d(10,0,0.75);
        for (int i = 0; i< n; ++i)
        {
            double t = (1.0 * i) / (n - 1.0); // 0.0 <= t <= 1. (croissant)
            // Calcul de la position via une interpolation linéaire
            // calcule effectué : position = (1-t)*depart + t*destination
            Vecteur3d position = depart.multiplie(1.0-t).plus(destination.multiplie(t));
            camera.setPosition(position);
            System.out.println(camera);
        }

        clotureJSON();
    }

    private static void question1(int n, int largeurImage, int hauteurImage, double[] abc)
    {
        initJSON();

        Camera camera = new Camera(
                new Vecteur3d(1, 0, 0),
                new Vecteur3d(-1, 0, 0),
                new Vecteur3d(0, 0, 1),
                DISTANCE_OEIL_ECRAN,
                largeurImage,
                hauteurImage
        );

        int nbPasParPhase = n/3;
        for (int i=0; i<nbPasParPhase; ++i)
        {
            double t = (2*3.1416*i)/(nbPasParPhase-1); // 0.0 <= t <= 1.0 (croissant)

            camera.setPosition(GetPosition(abc, t));
            System.out.println(camera);
        }

        clotureJSON();
    }

    private static void question2(int n, int largeurImage, int hauteurImage, double[] abc)
    {
        initJSON();

        Camera camera = new Camera(
                new Vecteur3d(1, 0, 0),
                new Vecteur3d(-1, 0, 0),
                new Vecteur3d(0, 0, 1),
                DISTANCE_OEIL_ECRAN,
                largeurImage,
                hauteurImage
        );

        int nbPasParPhase = n/3;
        for (int i=0; i<nbPasParPhase; ++i)
        {
            double t = (2*3.1416*i)/(nbPasParPhase-1); // 0.0 <= t <= 1.0 (croissant)

            //verslehaut
            // Formule des instruction
            // T.I. -> solve(crossP([-a*sin(t) b*cos(t) -c*sin(t)], []) = [0 z -y], {y, z})
            // => (x, y, z)=[0 c*sin(t) b*cos(t)]
            double xVersLeHaut = 0;
            double yVersLeHaut = abc[2]*Math.sin(t);
            double zVersLeHaut = abc[1]*Math.cos(t);

            Vecteur3d newVersLeHaut = new Vecteur3d(xVersLeHaut, yVersLeHaut, zVersLeHaut);

            camera.setPosition(GetPosition(abc, t));
            camera.setOrientationRegard(GetOrientation(abc, t));
            camera.setOrientationVersLeHaut(newVersLeHaut);
            System.out.println(camera);
        }

        clotureJSON();
    }

    private static void question3(int n, int largeurImage, int hauteurImage, double[] abc)
    {
        initJSON();

        Camera camera = new Camera(
                new Vecteur3d(1, 0, 0),
                new Vecteur3d(-1, 0, 0),
                new Vecteur3d(0, 0, 1),
                DISTANCE_OEIL_ECRAN,
                largeurImage,
                hauteurImage
        );

        int nbPasParPhase = n/3;
        for (int i=0; i<nbPasParPhase; ++i)
        {
            double t = (2*3.1416*i)/(nbPasParPhase-1); // 0.0 <= t <= 1.0 (croissant)

            //double norm1 = Math.sqrt((abc[0]*abc[0]-abc[1]*abc[1]+abc[2]*abc[2])*(Math.cos(t)*Math.cos(t))+(abc[1]*abc[1]));

            //verslehaut
            double xTangentDerived = (-abc[0]*Math.cos(t));
            double zTangentDerived = (-abc[2]*Math.cos(t));
            double yTangentDerived = (-abc[1]*Math.sin(t));
            var norm = new Vecteur3d(xTangentDerived, yTangentDerived, zTangentDerived).norme();

            Vecteur3d newVersLeHaut = new Vecteur3d(xTangentDerived/norm, yTangentDerived/norm, zTangentDerived/norm);

            camera.setPosition(GetPosition(abc, t));
            camera.setOrientationRegard(GetOrientation(abc, t));
            camera.setOrientationVersLeHaut(newVersLeHaut);
            System.out.println(camera);
        }

        clotureJSON();
    }

    private static void question4(int n, int largeurImage, int hauteurImage, double[] abc)
    {
        initJSON();

        Camera camera = new Camera(
                new Vecteur3d(1, 0, 0),
                new Vecteur3d(-1, 0, 0),
                new Vecteur3d(0, 0, 1),
                DISTANCE_OEIL_ECRAN,
                largeurImage,
                hauteurImage
        );

        int nbPasParPhase = n/3;
        for (int i=0; i<nbPasParPhase; ++i)
        {
            double t = (2*3.1416*i)/(nbPasParPhase-1); // 0.0 <= t <= 1.0 (croissant)

            //vecteur normal principal
            double xDerived2 = -abc[0]*Math.cos(t);
            double yDerived2 = -abc[1]*Math.sin(t);
            double zDerived2 = -abc[2]*Math.cos(t);
            var norm = new Vecteur3d(xDerived2, yDerived2, zDerived2).norme();
            Vecteur3d normalPrincipal = new Vecteur3d(xDerived2/norm, yDerived2/norm, zDerived2/norm);

            //verslehaut
            // (calculer vec. perpenticulaire au vecteur normal
            // principal en utilisant la formule de la question 2)
            // T.I. -> solve(crossP([-a*cos(t) -b*sin(t) -c*cos(t)], []) = [0 z -y], {y, z})
            // => (x, y, z) = [0 c*cos(t) -b*sin(t)]
            double xVersLeHaut = 0;
            double yVersLeHaut = abc[2]*Math.cos(t);
            double zVersLeHaut = -abc[1]*Math.sin(t);

            Vecteur3d newVersLeHaut = new Vecteur3d(xVersLeHaut, yVersLeHaut, zVersLeHaut);

            camera.setPosition(GetPosition(abc, t));
            camera.setOrientationRegard(GetOrientation(abc, t));
            camera.setOrientationVersLeHaut(newVersLeHaut);
            System.out.println(camera);
        }

        clotureJSON();
    }

    public static void main(String[] args) {
        try {
            // Creating a File object that represents the disk file.
            PrintStream o = new PrintStream(new File("test/cameras.json"));
            // Assign o to output stream
            System.setOut(o);

            double[] abc = {15.0, 5.0, 3.0};

            //(160, 400, 340) -> low settings | (1440, 1600, 900) -> high settings
            //question0(900, 1600, 900);
            //question1(900, 1600, 900, abc);
            //question2(900, 1600, 900, abc);
            question3(160, 300, 200, abc);
            //question4(160, 300, 200, abc);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
