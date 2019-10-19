/******************************************************************************
 * Par : Xavier Provençal
 * Date : Septembre 2019
 *
 * Ce programme génère une image à partir de la description d'une scène 3D.
 *
 * La scène est décrite par un fichier au format json. L'image produite peut
 * être au format jpg, png ou gif. Le format gif devrait être évité car il
 * produite des image d'une qualité inférieure. En général, le format jpg
 * produit des fichiers dont la taille est significativement plus petite que
 * ceux au format png. Par contre le format jpg dégrade légèrement les images.
 ******************************************************************************/

package org.xprov.visu3d;

import java.io.FileReader;

import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser;


/**
 * Classe principale du programme.
 *
 * Le programme lit la description d'une scène dans un fichier json et génère
 * l'image correspondante.
 *
 * Redéfinissez la variable `fichierJSON` pour spécifier le fichier utilisé en
 * entrée.  Redéfinissez la variable `fichierImage` pour spécifier le fichier
 * où l'image est écrite. Ce nom de fichier doit obligatoirement terminer une
 * des extensions suivante :
 *   .jpg
 *   .png
 *   .gif
 *
 */
public class Visu3d 
{
    // Fichier JSON contenant la description des objets, RayTracer, Camera et Scene
    private static String fichierJSON = new String("scene.json");

    public static void main(String[] argv)
    {
        Camera    camera    = null;
        Scene     scene     = null;
        RayTracer rayTracer = null;

        // Lecture du fichier JSON
        try (FileReader reader = new FileReader(fichierJSON)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            rayTracer = CustomJSONParser.parseRayTracer((JSONObject) jsonObject.get("RayTracer"));
            camera = CustomJSONParser.parseCamera((JSONObject) jsonObject.get("Camera"));
            scene = CustomJSONParser.parseScene((JSONObject) jsonObject.get("Scene"));
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        // Affichage des données 
        System.out.println(rayTracer);
        System.out.println(scene);
        System.out.println(camera);

        // Le vrai travail se fait ici, c'est la génération de l'image
        rayTracer.genereImage(scene, camera);

        // On sauvegarde l'image
        camera.sauvegarderImage();
        // Confirmation et fin du programme
        System.out.println("Image sauvegardée sous «" + camera.getNomFichierImage() + "»");
        System.exit(0);
    }



}

