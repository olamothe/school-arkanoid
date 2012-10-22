
import javax.microedition.lcdui.Graphics;


/**
 * @author Olivier Lamothe
 */
public class Level implements IDessinable
{
    public Brick [] briques;
    private final int NB_BRICK_LARGEUR = 8;
    private final int ESPACE_HAUT = 30;
    private final int HAUTEUR_Y = 9;
    private final int LARGEUR_X = (240 - (NB_BRICK_LARGEUR*24))/(NB_BRICK_LARGEUR+1);

    /*
            BLEU = 0, JAUNE = 1 ,ROSE = 2 ,ROUGE = 3 ,
            TURQUOISE = 4, VERT = 5 ,BRUN = 6 ,INDESTRUCTIBLE = 9, AUCUNE = -1
    */
    private static final int LEVELS[][] = new int [][]
    {
        {
            -1,-1,-1,0,0,-1,-1,-1,
            -1,-1,0,0,0,0,-1,-1,
            -1,0,0,0,0,0,0,-1,
            -1,0,0,0,0,0,0,-1,
            -1,-1,0,0,0,0,-1,-1,
            -1,-1,-1,0,0,-1,-1,-1
        },
        {
            2,3,4,5,5,4,3,2,
            2,3,4,-1,-1,4,3,2,
            2,3,-1,-1,-1,-1,3,2,
            2,-1,-1,-1,-1,-1,-1,2,
            2,-1,-1,-1,-1,-1,-1,2,
            2,-1,-1,-1,-1,-1,-1,2
        },
        {
             1,0,0,0,0,0,0,1,
             0,1,0,0,0,0,1,0,
             0,0,1,0,0,1,0,0,
             0,0,0,1,1,0,0,0,
             0,0,1,0,0,1,0,0,
             0,1,0,0,0,0,1,0,
             1,0,0,0,0,0,0,1

        },
        {
            6,6,6,6,6,-1,-1,-1,
            6,-1,-1,-1,-1,-1,6,-1,
            6,-1,-1,-1,-1,-1,6,-1,
            6,-1,-1,-1,-1,-1,6,-1,
            6,-1,-1,-1,-1,-1,6,-1,
            6,6,6,6,6,6,6,-1,
            9,9,9,9,9,9,9,-1
        },
        {
            9,9,9,9,9,9,9,9,
            9,1,1,1,1,1,1,9,
            9,2,2,2,2,2,2,9,
            9,3,3,3,3,3,3,9,
            9,4,4,4,4,4,4,9,
            9,5,5,5,5,5,5,9,
            9,6,6,6,6,6,6,9
        }
    };

    public Level(int levelNo)
    {
        int nbBriques = LEVELS[levelNo].length;
        briques = new Brick [nbBriques];
        int [] data = LEVELS[levelNo];
        //génère les briques avec un intervalle regulier entre chaque(elle ne sont pas "collées")
        for(int i = 0 ; i < nbBriques;i++)
        {
            int x = LARGEUR_X + ((i%NB_BRICK_LARGEUR)*24)+(i%NB_BRICK_LARGEUR*LARGEUR_X);
            int y = ESPACE_HAUT + HAUTEUR_Y + ((i / NB_BRICK_LARGEUR) * 12) + HAUTEUR_Y*(i/NB_BRICK_LARGEUR) ;
            briques[i] = new Brick(x, y, data[i]);
        }
    }



    public boolean EstFini()
    {
        boolean done = true;
        for(int i =0;i<briques.length ; i++)
        {
            if (briques[i].getEtat() != Brick.ETAT_DETRUITE && briques[i].getType() != Brick.INDESTRUCTIBLE)
            {
                done &= false;
            }
        }
        return done;
    }

    public void dessiner(Graphics g)
    {
        for(int i = 0 ; i < briques.length; i++)
        {
            briques[i].dessiner(g);
        }
    }
}
