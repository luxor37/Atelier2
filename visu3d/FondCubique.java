/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Cette classe implémente fond statique de forme cubique.
 *
 * Chacun des 6 côtés du cube possède une texture spécifiée à l'initialisation.
 *
 ******************************************************************************/

package org.xprov.visu3d;

import org.xprov.visu3d.linalg.Vecteur3d;

/**
 * Classe FondCubique
 *
 * Chacune des 6 faces du cube couvre un sixième de l'horizon et sont alignés
 * de manière à faire face aux axes. 
 *  - L'orientation ( 1, 0, 0) pointe au centre de la face `devant`
 *  - L'orientation (-1, 0, 0) pointe au centre de la face `derriere`
 *  - L'orientation ( 0, 1, 0) pointe au centre de la face `droite`
 *  - L'orientation ( 0,-1, 0) pointe au centre de la face `gauche`
 *  - L'orientation ( 0, 0, 1) pointe au centre de la face `haut`
 *  - L'orientation ( 0, 0,-1) pointe au centre de la face `bas`
 */
public class FondCubique implements Fond 
{
    /**
     * Constructeur à partir d'une seule image.
     * Cette image est utilisée dans toutes les orientations.
     *
     * @param fichierImage  fichier de la texture utilisée sur les 6 faces du cube
     */
    public FondCubique(String fichierImage) {
        Image image = new Image(fichierImage);

        fichierDevant   = fichierImage;
        fichierDerriere = fichierImage;
        fichierGauche   = fichierImage;
        fichierDroite   = fichierImage;
        fichierHaut     = fichierImage;
        fichierBas      = fichierImage;

        imgDevant   = image;
        imgDerriere = image;
        imgGauche   = image;
        imgDroite   = image;
        imgHaut     = image;
        imgBas      = image;
    }

    /**
     * Constructeur à partir d'un tableau de six images.
     *
     * Les images sont spécifiées dans l'ordre suivant :
     * devant, derriere, gauche, droite, haut, bas.
     *
     * Note : devant correspont à l'orientation (1,0,0) et le haut à (0,0,1).
     *
     * @param devant   fichier de la texture utilisée sur la face de devant
     * @param derriere fichier de la texture utilisée sur la face de derriere
     * @param gauche   fichier de la texture utilisée sur la face de gauche
     * @param droite   fichier de la texture utilisée sur la face de droite
     * @param haut     fichier de la texture utilisée sur la face du haut
     * @param bas      fichier de la texture utilisée sur la face du bas
     */
    public FondCubique(String devant, String derriere, 
            String gauche, String droite, 
            String haut, String bas) 
    {
        imgDevant   = new Image(devant);
        imgDerriere = new Image(derriere);
        imgGauche   = new Image(gauche);
        imgDroite   = new Image(droite);
        imgHaut     = new Image(haut);
        imgBas      = new Image(bas);

        fichierDevant   = devant;
        fichierDerriere = derriere;
        fichierGauche   = gauche;
        fichierDroite   = droite;
        fichierHaut     = haut;
        fichierBas      = bas;
    }

    /**
     * Calcule la couleur observée dans l'image spécifiée. Le vecteur orientation
     * est tel que :
     *
     *  x >= 0
     *  x >= |y|
     *  x >= |z|
     *
     * Le vecteur (1,0,0) pointe au centre de l'image. La coordonnée y indique
     * le décalage de gauche à droite. La coordonnée z indique la décalage de
     * bas en haut.
     *
     * y = -x correspond à la colonne de gauche.
     * y = x-1 correspond à la colonne de droite.
     * z = x-1 correspond à la ligne du haut.
     * z = -x correspond à la ligne du bas.
     *
     * @param img  image utilisée comme texture
     * @param orientation  orientation du rayon
     * @return  la couleur de l'iamge dans l'orientation spécifiée
     */
    private Couleur getCouleurDansImage(Image img, Vecteur3d orientation) {
        double x = orientation.getX();
        double y = orientation.getY();
        double z = orientation.getZ();
        double ratioHorizontal = (y/x + 1.0) / 2.0;
        double ratioVertical = (1.0 - z/x) / 2.0;

        if (ratioHorizontal == 1.0) {
            ratioHorizontal -= Constantes.EPSILON;
        }
        if (ratioVertical == 1.0) {
            ratioVertical -= Constantes.EPSILON;
        }

        assert (0.0 <= ratioHorizontal && ratioHorizontal < 1.0
                && 0.0 <= ratioVertical && ratioVertical < 1.0);


        return img.getCouleurCoordsRelatives(ratioHorizontal, ratioVertical);
    }

    /**
     * Détermine la couleur observée dans une orientation donnée.
     *
     * @param orientation  vecteur normalisé dans l'orientation du regard
     * @return  un impact contenant une couleur et une normale
     */
    public Impact getImpact(Vecteur3d orientation) {
        // Composantes du vecteur orientation
        double x = orientation.getX();
        double y = orientation.getY();
        double z = orientation.getZ();

        // Valeurs absolues des comporantes
        double va_x = Math.abs(x);
        double va_y = Math.abs(y);
        double va_z = Math.abs(z);

        // Il y a six cas à considérer : devant, derrière, gauche, droite, haut, bas.
        // On commence par déterminer dans lequel de ces six cas on se trouve.
        // Ensuite, on effectue une rotation du vecteur de manière à considérer
        // chaque côté comme s'il était devant nous, dans l'orientation (1,0,0).
        Couleur couleur = null;
        Vecteur3d normale = null;

        // Si |x| > max( |y|, |z| ) alors l'orientation est soit vers l'avant, soit vers l'arrie
        if (va_x >= Math.max(va_y, va_z)) {
            if (x > 0.0) {
                couleur = getCouleurDansImage(imgDevant, orientation);
                normale = new Vecteur3d(-1,0,0);
            } else {
                couleur = getCouleurDansImage(imgDerriere, new Vecteur3d(-x, y, z));
                normale = new Vecteur3d(1,0,0);
            }

        // Si |y| > max( |x|, |z| ) alors l'orientation est soit vers la gauche, soit vers la droite
        } else if (va_y >= Math.max(va_x, va_z)) {
            if (y > 0.0) {
                couleur = getCouleurDansImage(imgDroite, new Vecteur3d(y, -x, z));
                normale = new Vecteur3d(0,-1,0);
            } else {
                couleur = getCouleurDansImage(imgGauche, new Vecteur3d(-y, x, z));
                normale = new Vecteur3d(0,1,0);
            }

        // Si |z| > max( |x|, |y| ) alors l'orientation est soit vers le haut, soit vers le bas.
        } else if (va_z >= Math.max(va_x, va_y)) {
            if (z > 0.0) {
                couleur = getCouleurDansImage(imgHaut, new Vecteur3d(z, y, -x));
                normale = new Vecteur3d(0,0,-1);
            } else {
                couleur = getCouleurDansImage(imgBas, new Vecteur3d(-z, y, x));
                normale = new Vecteur3d(0,0,1);
            }
        }

        assert(couleur != null && normale != null);
        return Impact.impactAvecJusteCouleurEtNormale(couleur, normale);
    }


    public String toString() {
        return "Fond cubique\n" 
            + "  Image devant   : \"" + fichierDevant + "\"\n"
            + "  Image derriere : \"" + fichierDerriere + "\"\n"
            + "  Image gauche   : \"" + fichierGauche + "\"\n"
            + "  Image droite   : \"" + fichierDroite + "\"\n"
            + "  Image haut     : \"" + fichierHaut + "\"\n"
            + "  Image bas      : \"" + fichierBas + "\"";
    }

    private String fichierDevant;   // nom du fichier de l'image de devant
    private String fichierDerriere; // nom du fichier de l'image de derrière
    private String fichierGauche;   // nom du fichier de l'image de gauche
    private String fichierDroite;   // nom du fichier de l'image de droite
    private String fichierHaut;     // nom du fichier de l'image du haut
    private String fichierBas;      // nom du fichier de l'image du bas

    private Image imgDevant;   // Image devant
    private Image imgDerriere; // Image derriere
    private Image imgGauche;   // Image à gauche
    private Image imgDroite;   // Image à droite
    private Image imgHaut;     // Image en haut
    private Image imgBas;      // Image en bas
}
