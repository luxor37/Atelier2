///******************************************************************************
// * Par : Xavier Provençal
// * Date : Septembre 2019
// *
// * Cette classe implémente une sphère dans une scène 3D.
// *
// * Une sphère est définie par :
// *  - un centre
// *  - un rayon
// *  - une couleur
// *  - un coefficient de réflexivité
// *
// */
//
//import org.xprov.visu3d.linalg.Vecteur3d;
//import org.xprov.visu3d.linalg.Point3d;
//
///**
// * Classe sphère
// *
// * Une sphère est définie par l'ensemble des points à une distance fixée d'un
// * centre.
// */
//public class Sphere implements Visible
//{
//    /**
//     * Constructeur
//     *
//     * Définit une sphère à partir d'un centre, un rayon et une couleur.
//     * Par défaut, le paramètre de réflexivité est 0.0.
//     *
//     * @param centre  le entre de la sphère
//     * @param rayon   le rayon de la sphère
//     * @param couleur  la couleur de la sphère
//     */
//    public Sphere(Point3d centre, double rayon, Couleur couleur) {
//        this.centre = centre;
//        this.rayon = rayon;
//        this.couleur = couleur;
//        this.reflexivite = 0.0; // default
//    }
//
//    /**
//     * Spécifie ou remplace le paramètre de réflexivité.
//     *
//     * @param reflexivite  le paramètre de réflexivité
//     */
//    public void setReflexivite(double reflexivite) {
//        this.reflexivite = reflexivite;
//    }
//
//    public Point3d getCenter() {
//        return this.centre;
//    }
//
//    public double getRayon() {
//        return this.rayon;
//    }
//
//    public Couleur getCouleur() {
//        return this.couleur;
//    }
//
//    public double getReflexivite() {
//        return this.reflexivite;
//    }
//
//    /**
//     * Calcule l'intersection d'un rayon avec la sphère.
//     *
//     * Étant donné un rayon lancé depuis le point `origine` dans l'orientation
//     * `orientation`, le point d'intersection de ce rayon sur la sphère est
//     * calculé, s'il existe.
//     *
//     * @param origine   Le point depuis lequel le rayon est lancé.
//     * @param orientation Point3d normalisé indiquant l'orientation du rayon.
//     * @return          Un object `Impact` décrivant le point d'impact s'il
//     *                  existe, null sinon.
//     *
//     *
//     */
//    public Impact intersectionRayon(Point3d origine, Vecteur3d orientation) {
//        Point3d z = this.getCenter(); //centre de la sphere
//        var r = this.getRayon();//rayon de la sphere
//        //
//        //                                        X ------ (orientation)
//        //                             Q     -------
//        //                           -------        \
//        //                    -------                \ minDist
//        //             -------    B                 A \
//        //     --------                   C            \
//        //   O ---------------------------------------- Z
//        //
//        //  origine = origine du rayon
//        //  Z = centre de la sphère
//        //  X = projection orthogonale du centre de la sphère sur le rayon
//        //
//        //  Note : l'angle OXZ est est un angle droit.
//        //
//        //  OXZ est un triangle-rectangle, on pose :
//        //  norm_a la longueur du côté OX
//        //  norm_b la longueur du côté XZ
//        //  norm_c la longueur du côté OZ (l'hypoténuse)
//
//        var norm_c = Math.sqrt((Math.pow(z.getX()-origine.getX(), 2)) +
//                (Math.pow(z.getY()-origine.getY(), 2)) +
//                (Math.pow(z.getZ()-origine.getZ(), 2)));
//
//        var c = origine.moins(z);
//
//        var norm_b = c.prodScal(orientation);
//
//        var norm_a = Math.sqrt(Math.pow(norm_c, 2) - Math.pow(norm_b, 2));
//
//        if(norm_a > r){
//            return null;
//        }
//
////        if(norm_a <= r){
////            return Impact.impactAvecJusteCouleur(this.couleur);
////        }
//
//        //Q = distance entre le point d'intersection et le point X
//        var Q = Math.sqrt(Math.pow(r, 2) - Math.pow(norm_a, 2));
//
//        //respectivement, la distance entre l'origine et le pt d'intersection 1 et 2
//        var norm_i1 = norm_c - Q;
//        //var norm_i2 = norm_c + Q;
//
//        //coordonnées des intersection 1 et 2
//        var i1 = origine.plus(orientation.2(norm_i1));
//        //var i2 = origine.plus(orientation.multiplie(norm_i2));
//
//        Vecteur3d normale = i1.moins(z).normalise();
//
//        return new Impact(i1, Math.pow(norm_i1, 2), normale, this.couleur, this.reflexivite);
//    }
//
//
//
//    public String toString() {
//        return "Sphere\n"
//            + "  centre      : " + centre      + "\n"
//            + "  rayon       : " + rayon       + "\n"
//            + "  couleur     : " + couleur      + "\n"
//            + "  reflexivite : " + reflexivite;
//    }
//
//    private Point3d centre; // position du centre de la sphère
//    private double rayon;   // rayon de la sphère
//    private Couleur couleur; // couleur de la sphère
//    private double reflexivite; // indice de réflexivité
//}
