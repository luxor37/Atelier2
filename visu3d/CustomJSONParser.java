package org.xprov.visu3d;

import java.util.ArrayList;

import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.xprov.visu3d.linalg.Vecteur3d;
import org.xprov.visu3d.linalg.Point3d;


public class CustomJSONParser {

    public static RayTracer parseRayTracer(JSONObject jsonObject) {

        // Initialisation avec les valeurs par défaut
        int nbMaxReflexions = 0;
        boolean multithread = true;

        try {
            Long value = new Long((long) jsonObject.get("nbMaxReflexions"));
            nbMaxReflexions = value.intValue();
        } catch (NullPointerException e) {
        }

        try {
            multithread = (boolean) jsonObject.get("multithread");
        } catch (NullPointerException e) {
        }

        return new RayTracer(nbMaxReflexions, multithread);
    }

    public static Camera parseCamera(JSONObject jsonObject) {
        Point3d position = null;
        Vecteur3d orientationRegard = null;
        Vecteur3d orientationVersLeHaut = null;
        double distanceOeilEcran = 0.0;
        int largeurImage  = 0;
        int hauteurImage = 0;
        String nomFichierImage = null;

        // Read required values
        try {
            position = new Point3d((String) jsonObject.get("position"));
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseCamera error." 
                    + " Invalid required field `position`.");
            System.exit(-1);
        }

        try {
            orientationRegard = new Vecteur3d((String) jsonObject.get("orientationRegard"));
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseCamera error." 
                    + " Invalid required field `orientationRegard`.");
            System.exit(-1);
        }

        try {
            orientationVersLeHaut = new Vecteur3d((String) jsonObject.get("orientationVersLeHaut"));
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseCamera error." 
                    + " Invalid required field `orientationVersLeHaut`.");
            System.exit(-1);
        }

        try {
            distanceOeilEcran = (double) jsonObject.get("distanceOeilEcran");
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseCamera error." 
                    + " Invalid required field `distanceOeilEcran`.");
            System.exit(-1);
        }

        try {
            largeurImage = ((Long) jsonObject.get("largeurImage")).intValue();
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseCamera error." 
                    + " Invalid required field `largeurImage`.");
            System.exit(-1);
        }

        try {
            hauteurImage = ((Long) jsonObject.get("hauteurImage")).intValue();
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseCamera error." 
                    + " Invalid required field `hauteurImage`.");
            System.exit(-1);
        }

        try {
            nomFichierImage = (String) jsonObject.get("fichier");
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseCamera error." 
                    + " Invalid required field `image`.");
            System.exit(-1);
        }

        return new Camera(position,
                orientationRegard,
                orientationVersLeHaut,
                distanceOeilEcran,
                largeurImage,
                hauteurImage,
                nomFichierImage);
    }

    public static Scene parseScene(JSONObject jsonObject) {

        Scene scene = new Scene();

        JSONArray jsonArray;
        JSONObject jsonSubObject;

        jsonArray  = (JSONArray) jsonObject.get("Visibles");
        if (jsonArray != null) {
            for (Object o : jsonArray) {
                Visible v = parseVisible((JSONObject) o);
                scene.ajoute(v);
            }
        }

        jsonSubObject  = (JSONObject) jsonObject.get("Fond");
        if (jsonSubObject != null) {
            Fond fond = parseFond(jsonSubObject);
            scene.setFond(fond);
        }


        jsonArray = (JSONArray) jsonObject.get("Lumière");
        if (jsonArray != null) {
            for (Object o : jsonArray) {
                Lumiere l = parseLumiere((JSONObject) o);
                scene.ajoute(l);
            }
        }
        return scene;
    }

    public static Visible parseVisible(JSONObject jsonObject) {
        try {
            String type = (String) jsonObject.get("type");
            if (type.equals("Sphere"))
                return parseSphere(jsonObject);
            if (type.equals("Cube"))
                return parseCube(jsonObject);
            if (type.equals("Rectangle"))
                return parseRectangle(jsonObject);
            throw new IllegalArgumentException("Unknown visible type (=" + type + ")");
        } 
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseVisible error." 
                    + " Invalid required field `type`.");
            System.exit(-1);
        }
        return null;
    }

    public static Sphere parseSphere(JSONObject jsonObject) {

        // Required fields
        Point3d centre = null;
        double rayon = -1.0;
        Couleur couleur = null;

        // Read required fields and raise error if there is a problem
        try {
            centre = new Point3d((String) jsonObject.get("centre"));
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseSphere."
                    + " Invalid required field `centre`.");
            System.exit(-1);
        }

        try {
            rayon = (double) jsonObject.get("rayon");
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseSphere."
                    + " Invalid required field `rayon`.");
            System.exit(-1);
        }

        try {
            couleur = new Couleur((String) jsonObject.get("couleur"));
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseSphere."
                    + " Invalid required field `couleur`.");
            System.exit(-1);
        }

        // Build object
        Sphere sphere = new Sphere(centre, rayon, couleur);

        // Read optionnal properties
        double reflexivite;

        try {
            reflexivite = (double) jsonObject.get("reflexivite");
            sphere.setReflexivite(reflexivite);
        }
        catch (NullPointerException e) {
        }

        return sphere;
    }

    public static ArrayList<Point3d> parseListeDePoints3d(String texte) {
        ArrayList<Point3d> l = new ArrayList<Point3d>();
        if (!texte.startsWith("[") || !texte.endsWith("]")) {
            System.err.println("CustomJSONParser.parseListeDePoints3d"
                    + " Liste invalide, doit commencer par '[' et terminer par ']'");
        }
        int posOuvrante = 0;
        int posFermante = 0;
        posOuvrante = texte.indexOf("(");
        while (posOuvrante != -1) {
            posFermante = texte.indexOf(")", posOuvrante + 1);
            l.add(new Point3d(texte.substring(posOuvrante, posFermante+1)));
            posOuvrante = texte.indexOf("(", posFermante + 1);
        }
        return l;
    }

    public static Cube parseCube(JSONObject jsonObject) {
        // Required fields
        Point3d centre = null;
        Couleur couleur = null;
        double size = 0.0;

        // Read required fields and raise error if there is a problem
        try {
            centre = new Point3d((String) jsonObject.get("centre"));
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseCube."
                    + " Invalid required field `centre`.");
            System.exit(-1);
        }

        try {
            size = (double) jsonObject.get("size");
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseCube."
                    + " Invalid required field `size`.");
            System.exit(-1);
        }

        // Build object
        Cube c = new Cube(centre, size);

        // Read optionnal properties
        // todo

        return c;
    }

    public static Rectangle parseRectangle(JSONObject jsonObject) {
        // Required fields
        ArrayList<Point3d> points = null;
        Couleur couleur = null;


        // Read required fields and raise error if there is a problem
        try {
            points = parseListeDePoints3d((String) jsonObject.get("points"));
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseRectangle."
                    + " Invalid required field `points`.");
            System.exit(-1);
        }
        if (points.size() != 4) {
            System.err.println("CustomJSONParser.parseRectangle."
                    + " Nombre de points invalide, doit être 4.");
            System.exit(-1);
        }

        try {
            couleur = new Couleur((String) jsonObject.get("couleur"));
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseCube."
                    + " Invalid required field `size`.");
            System.exit(-1);
        }

        // Build object
        Rectangle rectangle = new Rectangle(
                points.get(0), points.get(1), 
                points.get(2), points.get(3), 
                couleur);

        // Read optionnal properties
        double reflexivite;
        String fichierImageTexture = null;

        try {
            reflexivite = (double) jsonObject.get("reflexivite");
            rectangle.setReflexivite(reflexivite);
        }
        catch (NullPointerException e) {
        }


        try {
            fichierImageTexture = (String) jsonObject.get("texture");
            rectangle.setTexture(fichierImageTexture);
        }
        catch (NullPointerException e) {
        }

        return rectangle;
    }

    public static Lumiere parseLumiere(JSONObject jsonObject) {
        return null;
    }

    public static Fond parseFond(JSONObject jsonObject) {
        try {
            String type = (String) jsonObject.get("type");
            if (type.equals("Cubique"))
                return parseFondCubique(jsonObject);
            else if (type.equals("Cylindrique"))
                return parseFondCylindrique(jsonObject);
            else if (type.equals("Spherique"))
                return parseFondSpherique(jsonObject);
            throw new IllegalArgumentException("Type de fond inconnu (=" + type + ")");
        } 
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseFond erreur." 
                    + " Champs requis `type` invalide.");
            System.exit(-1);
        }
        return null;
    }


    public static Fond parseFondCubique(JSONObject jsonObject) {
        FondCubique fond = null;
        String fichier  = null; // si un seul fichier fourni

        String devant   = null; // si six fichiers sont fournis
        String derriere = null; 
        String gauche   = null;
        String droite   = null; 
        String haut     = null;
        String bas      = null;
        try {
            fichier = (String) jsonObject.get("image");
        }
        catch (NullPointerException e) {
        }
        try {
            devant = (String) jsonObject.get("devant");
            derriere = (String) jsonObject.get("derriere");
            gauche = (String) jsonObject.get("gauche");
            droite = (String) jsonObject.get("droite");
            haut = (String) jsonObject.get("haut");
            bas = (String) jsonObject.get("bas");
        }
        catch (NullPointerException e) {
        }

        if (fichier != null) {
            fond = new FondCubique(fichier);
        }

        if (devant != null && derriere != null 
                && gauche != null && droite != null 
                && haut != null && bas != null)
        {
            fond = new FondCubique(devant, derriere, gauche, droite, haut, bas);
        }
        
        if (fond == null) {
            System.err.println("CustomJSONParser.parseFondCubique erreur." 
                    + " Le champ `image` ou les champs `devant/derriere/gauche/droite/haut/bas`"
                   + " sont requis.");
            System.exit(-1);
        }

        return fond;
    }



    public static Fond parseFondCylindrique(JSONObject jsonObject) {
        String fichier = null;
        try {
            fichier = (String) jsonObject.get("image");
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseFondCylindrique erreur."
                    + " Champs requis `image` invalide");
            System.exit(-1);
        }
        Fond fond = new FondCylindrique(fichier);
        return fond;
    }

    public static Fond parseFondSpherique(JSONObject jsonObject) {
        String fichierImageDessus = null;
        String fichierImageDessous = null;
        try {
            fichierImageDessus = (String) jsonObject.get("dessus");
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseFondSpherique erreur."
                    + " Champs requis `dessus` invalide");
            System.exit(-1);
        }
        try {
            fichierImageDessous = (String) jsonObject.get("dessous");
        }
        catch (NullPointerException e) {
            System.err.println("CustomJSONParser.parseFondSpherique erreur."
                    + " Champs requis `dessous` invalide");
            System.exit(-1);
        }
        Fond fond = new FondSpherique(fichierImageDessus, fichierImageDessous);
        return fond;
    }

}

