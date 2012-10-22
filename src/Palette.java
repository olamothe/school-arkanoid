
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 * @author Olivier Lamothe
 */
public class Palette implements IDessinable,IUpdatable
{
    public Sprite sprite;
    public int x , largeur ;

    private Image imgPetite;
    private final int Y = 310 , LARG_PETITE=48 ;

    public Palette ()
    {
        try
        {
            imgPetite = Image.createImage("/images/barre.png");
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
        x = 96;
        largeur = LARG_PETITE;
        sprite=new Sprite(imgPetite,48,8);
        sprite.setFrame(0);
        sprite.setPosition(96,Y);
    }

    public void dessiner(Graphics g)
    {
        sprite.paint(g);
    }

    public void update(int key, long temps)
    {
        if ((key &ArkanoidCanvas.LEFT_PRESSED) !=0)
        {
            x --;
            if(x<0)
                x = 0;
        }
        if((key & ArkanoidCanvas.RIGHT_PRESSED)!= 0)
        {
            x ++;
            if(x>240-largeur)
                x=240-largeur;
        }
        sprite.setPosition(x, Y);
    }
}
