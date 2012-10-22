
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 * @author Olivier Lamothe
 */
public class Balle implements IDessinable,IUpdatable
{
    public Sprite sprite;
    public int x,y;
    public int  vx,vy;

    private Image img;
    private long delais;
    private int DELAIS = 5 ;
    public Balle()
    {
        x = 116;
        y = 304;
        vx = 0;
        vy = 0;
        delais = DELAIS;
        try 
        {
            img = Image.createImage("/images/balleFeu.png");
        } 
        catch (IOException ex) 
        {
            System.err.println(ex);
        }
        sprite = new Sprite(img,8,8);
        sprite.setPosition(x, y);
        sprite.setFrame(0);
    }
    public void dessiner(Graphics g)
    {
        sprite.paint(g);
    }

    public void update(int key,long delta)
    {
        //Balle updater apres un delai, afin de la ralentir 
        delais -= delta;
        if(delais<0)
        {
            x += vx;
            y += vy;
            if(x<=0)
            {
                x = 0;
                vx = -vx;
            }
            if(x>=232)
            {
                x= 232;
                vx = -vx;
            }
            if(y<=30)
            {
                vy=-vy;
            }
            sprite.setPosition(x, y);
            delais = DELAIS;
        }
    }
    public void colPalette(Palette palette)
    {
        //les bouts de la palette renvoie la balle en vx opposé
        int boutPalette= (palette.largeur)/4;
        if(
                (x+4<=palette.x+boutPalette && vx>0) ||
                (x+4>=palette.x+palette.largeur-boutPalette && vx <0 ))
        {
            vx=-vx;
        }
        vy=-vy;
    }

    public void colBrique(int typeCollision)
    {
        //type 0 = horizontal, type 1 = vertical
        if(typeCollision == 0)
            vy = -vy;
        if(typeCollision == 1)
            vx = -vx;
    }
}
