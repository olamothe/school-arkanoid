
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

/**
 * @author Olivier Lamothe
 */
public class Menu implements IUpdatable,IDessinable
{
    public String titre;

    private Menu[] choix;
    private Menu precedent , next;
    private int sfx,bgm,choixLevel,choixCourant;

    public Menu(String pTitre)
    {
        titre = pTitre;
        precedent = null;
        sfx = 50;
        bgm = 50;
        choixLevel = 1 ;
    }
    public Menu nextMenu()
    {
        if(next.titre.equals("Precedent"))
            return precedent;
        return next;
    }

    public void update(int keys, long deltaTime)
    {
        next = this;
        if((keys & GameCanvas.UP_PRESSED) !=0)
        {
            choixCourant--;
            if(choixCourant<0)
            {
                choixCourant=0;
            }
        }
        if((keys & GameCanvas.DOWN_PRESSED) !=0)
        {
            choixCourant++;
            if(choixCourant>choix.length-1)
                choixCourant=choix.length-1;
        }
        if((keys & GameCanvas.FIRE_PRESSED) !=0)
        {
            next = choix[choixCourant];
        }
    }

    public void dessiner(Graphics g)
    {
        g.setColor(0);
        g.fillRect(0, 0, 240, 320);
        //titre menu
        g.setColor(255,255,255);
        g.drawString(titre, 15, 10, 0);
        //options menu
        if (choix != null)
        {
            for (int i =0 ; i <= choix.length-1;i++)
            {
                if(i==choixCourant)
                {
                   g.setColor(179,210,241);
                }
                else
                {
                    g.setColor(255,255,255);
                }
                int y = 50 *(i+1);
                g.drawString(choix[i].titre, 35, y, 0);
            }
        }
    }
    //Set les options dépendamment des menus
    public void setOption(ArkanoidCanvas canvas)
    {
        if(titre.equals(canvas.menuPrincipal.titre))
            choix = new Menu[]{canvas.optNouvellePartie,canvas.optOptions,canvas.optQuitter};
        if(titre.equals(canvas.optOptions.titre))
            choix = new Menu[]{canvas.optSfx,canvas.optBGM,canvas.optLevel,canvas.optPrecedent};
        if(titre.equals(canvas.optSfx.titre)|| titre.equals(canvas.optBGM.titre)||titre.equals(canvas.optLevel.titre))
            choix = new Menu[]{canvas.optPrecedent};
    }
    //Set le niveau de menu précédent
    public void setPrecedent(ArkanoidCanvas canvas)
    {
        if(titre.equals(canvas.optSfx.titre)|| titre.equals(canvas.optBGM.titre)||titre.equals(canvas.optLevel.titre))
            precedent = canvas.optOptions;
        if(titre.equals(canvas.optOptions.titre))
            precedent = canvas.menuPrincipal;
    }
    //Dessine le rectangle de niveau de son SFX
    public void dessinerSFX(int key , Graphics g)
    {
        if (sfx<0)
            sfx =1;
        if(sfx>200)
            sfx = 200;
        g.setColor(0,153,0);
        g.drawRect(20, 150, 200, 10);
        g.fillRect(20, 150, sfx, 10);
        if((key & GameCanvas.LEFT_PRESSED) != 0)
            sfx--;
        if((key & GameCanvas.RIGHT_PRESSED) != 0)
            sfx++;
    }
     //Dessine le rectangle de niveau de son BGM
    public void dessinerBGM(int key , Graphics g)
    {
        if (bgm<0)
            bgm =1;
        if(bgm>200)
            bgm = 200;
        g.setColor(0,153,0);
        g.drawRect(20, 150, 200, 10);
        g.fillRect(20, 150, bgm, 10);
        if((key & GameCanvas.LEFT_PRESSED) != 0)
            bgm--;
        if((key & GameCanvas.RIGHT_PRESSED) != 0)
            bgm++;
    }
    //Dessine le no de level choisi par le joueur
    public int dessinerChoixLevel(int keypress, Graphics g)
    {
         g.setColor(0,153,0);
         g.drawString(String.valueOf(choixLevel), 40 , 100, 0);
         if((keypress & GameCanvas.UP_PRESSED )!= 0)
             choixLevel++;
         if((keypress & GameCanvas.DOWN_PRESSED )!= 0)
             choixLevel--;
         if(choixLevel<1)
             choixLevel=1;
         if(choixLevel>5)
             choixLevel=5;
         return choixLevel;

     }
}
