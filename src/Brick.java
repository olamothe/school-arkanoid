import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 * @author Olivier Lamothe
 */

public class Brick implements IDessinable
{
    public static final int
            BLEU = 0, JAUNE = 1 ,ROSE = 2 ,ROUGE = 3 ,
            TURQUOISE = 4, VERT = 5 ,BRUN = 6 ,INDESTRUCTIBLE = 9, AUCUNE = -1,
            
            ETAT_VIE = 0, ETAT_EXPLOSE = 1, ETAT_DETRUITE = 2;

    public int x,y;
    public Sprite sprite;

    private int etat;
    private static Image imgBricks;
    private int type, vie, points;
    
    public Brick(int px, int py, int pType)
    {
        //Load l'image une seule fois
        if(imgBricks==null)
        {
            try
            {
                imgBricks = Image.createImage("/images/bricks.png");
            }
            catch (Exception ex)
            {
                System.err.println(ex);
            }
        }
        type = pType;
        
        x = px;
        y = py;

        if(type == BRUN)
        {
            vie = 3;
            points=50;
        }
        else if(type == INDESTRUCTIBLE)
            vie = -1;
        else
        {
            points=10;
            vie = 1;
        }

        sprite = new Sprite(imgBricks,24,12);
        sprite.setPosition(x, y);
        etat = ETAT_VIE;
        if (type == AUCUNE)
        {
            sprite = null;
            etat=ETAT_DETRUITE;
        }
        else
            sprite.setFrame(type);

        
    }

    public void dessiner(Graphics g)
    {
        if(sprite!= null)
        {
            sprite.paint(g);
        }
    }

    public int collision()
    {
        vie--;
        if(type==BRUN)
        {
            sprite.nextFrame();
        }
        if(vie <= 0 && type !=INDESTRUCTIBLE)
        {
            etat=ETAT_DETRUITE;
            sprite.setVisible(false);
            return points;
        }
        return 0;
    }

    public int getEtat()
    {
        return etat;
    }
    public int getType()
    {
        return type;
    }

}
