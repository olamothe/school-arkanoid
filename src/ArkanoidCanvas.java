import java.io.IOException;
import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;

/**
 * @author Olivier Lamothe
 */

public class ArkanoidCanvas extends GameCanvas implements Runnable
{
    //données membre
    private ArkanoidMidlet midlet;
    private Thread t;
    private Graphics g;
    private Level level;
    private Palette palette;
    private Balle balle;
    private int key , oldkey , keypress;
    private long tempsPrecedent = System.currentTimeMillis();
    private long tempsCourant, deltaTime;
    private int iLevel, vie , points ;
    private boolean ne, nouvelEtat;
    private Menu choixMenu;

    //gestion machine d'état
    private int etat;
    private final int ETAT_MENU=0, ETAT_NEW_LEVEL=1, ETAT_PLAY=2,ETAT_PAUSE=3,
            ETAT_GAME_OVER=4,ETAT_WIN=5,ETAT_SERVICE=6,ETAT_QUIT=7;

    //Menu et sous menus
    public Menu optNouvellePartie ;
    public Menu optOptions ;
    public Menu optQuitter;
    public Menu optSfx ;
    public Menu optBGM;
    public Menu optLevel;
    public Menu optPrecedent;
    public Menu menuPrincipal;

    //Constructeur
    public ArkanoidCanvas(ArkanoidMidlet m)
    {
        super(true);
        setFullScreenMode(true);
        initialiserMenu();

        g = this.getGraphics();
        midlet = m;
        
        etat = ETAT_MENU;
        choixMenu = menuPrincipal;
        ne = false;
        vie = 3;
        points = iLevel = 0;
        
        t = new Thread(this);
        t.start();
    }

    //Initialise les menus, avec leurs parents/enfants
    private void initialiserMenu()
    {
        optNouvellePartie = new Menu("Nouvelle Partie");
        optOptions = new Menu("Options");
        optQuitter = new Menu("Quitter");
        optSfx = new Menu("SFX");
        optBGM = new Menu("BGM");
        optLevel = new Menu ("Choix Level");
        optPrecedent = new Menu("Precedent");
        menuPrincipal = new Menu("Principal");

        menuPrincipal.setOption(this);
        optOptions.setOption(this);
        optBGM.setOption(this);
        optSfx.setOption(this);
        optLevel.setOption(this);
        optOptions.setPrecedent(this);
        optBGM.setPrecedent(this);
        optSfx.setPrecedent(this);
        optLevel.setPrecedent(this);
    }

    //Boucle principale du jeu
    public void run()
    {
        while(true)
        {
            //Polling des touches
            oldkey = key;
            key = getKeyStates();
            if(key !=0)
                keypress = key ^ oldkey;
            else
                keypress = 0;

            nouvelEtat=ne;
            ne = false;
            switch(etat)
            {
                case ETAT_MENU:
                    etatMenu();
                    break;
                case ETAT_NEW_LEVEL:
                    etatNewLevel();
                    break;
                case ETAT_PLAY:
                    etatPlay();
                    break;
                case ETAT_PAUSE:
                    etatPause();
                    break;
                case ETAT_GAME_OVER:
                    etatGameOver();
                    break;
                case ETAT_WIN:
                    etatWinner();
                    break;
                case ETAT_SERVICE:
                    etatService();
                    break;
                case ETAT_QUIT:
                    etatQuit();
                    break;
            }
             dessiner();
             flushGraphics();
        }
    }
    //joueur est dans le menu
    private void etatMenu()
    {
        if(nouvelEtat)
            choixMenu=menuPrincipal;
        if(choixMenu == optNouvellePartie)
            changerEtat(ETAT_NEW_LEVEL);
        if(choixMenu==optQuitter)
            changerEtat(ETAT_QUIT);
    }
    
    //joueur commence un level
    private void etatNewLevel()
    {
        if(nouvelEtat)
        {
            if(iLevel < 5)
            {
                palette= new Palette();
                balle = new Balle();
                level = new Level(iLevel);
            }
            else
            {
                changerEtat(ETAT_WIN);
            }
        }
        else
            changerEtat(ETAT_SERVICE);
    }

    //joueur en train de jouer
    private void etatPlay()
    {
        //MAJ Temps
        tempsCourant = System.currentTimeMillis();
        deltaTime = tempsCourant - tempsPrecedent;
        tempsPrecedent = tempsCourant;

        // MAJ jeu
        checkCollision();
        palette.update(key,0);
        balle.update(0,deltaTime);

        //appuie sur enter
        if((keypress & FIRE_PRESSED) != 0)
        {
            changerEtat(ETAT_PAUSE);
        }
        //balle tombe en bas
        if(balle.y>304)
        {
            vie--;
            changerEtat(ETAT_SERVICE);
        }
        //plus de briques a casser
        if(level.EstFini())
        {
            iLevel++;
            changerEtat(ETAT_NEW_LEVEL);
        }
    }
    //joueur entre en pause
    private void etatPause()
    {
        if((keypress & FIRE_PRESSED) != 0)
        {
            changerEtat(ETAT_PLAY);
        }
    }
    //joueur perd toute ses vies
    private void etatGameOver()
    {
         if(nouvelEtat)
        {
            iLevel=points=0;
            vie=3;
        }
        if((keypress & FIRE_PRESSED)!= 0)
        {
            choixMenu=menuPrincipal;
            changerEtat(ETAT_MENU);
        }
    }
    //joueur finit level 5
    private void etatWinner()
    {
        if(nouvelEtat)
        {
            iLevel=points=0;
            vie=3;
        }
        if((keypress & FIRE_PRESSED)!= 0)
        {
            choixMenu=menuPrincipal;
            changerEtat(ETAT_MENU);
        }
    }
    //joueur met la balle en jeu
    private void etatService()
    {
         if(vie<0)
        {
            changerEtat(ETAT_GAME_OVER);
        }
        //remet la palette au centre
        if(nouvelEtat)
            palette.x = 96;

        //balle immobile et au centre de la palette
        balle.vx = 0;
        balle.vy = 0;
        balle.x = palette.x+palette.largeur/2-4;
        balle.y = 302;

        //la palette peut se deplacer
        balle.update(0, 1000);
        palette.update(key, 0);

        //la balle décolle de la palette
        if((keypress & FIRE_PRESSED)  != 0)
        {
            balle.vy=-1;
            balle.vx=1;
            changerEtat(ETAT_PLAY);
        }
    }
    //ferme l'application
    private void etatQuit()
    {
        midlet.destroyApp(true);
    }
    private void dessiner()
    {
        switch(etat)
        {
            case ETAT_MENU:
                dessinerMenu();
                break;
            case ETAT_NEW_LEVEL:
                dessinerNewLevel();
                break;
            case ETAT_PLAY:
                dessinerPlay();
                break;
            case ETAT_PAUSE:
                dessinerPause();
                break;
            case ETAT_GAME_OVER:
                dessinerGameOver();
                break;
            case ETAT_WIN:
                dessinerWinner();
                break;
            case ETAT_SERVICE:
                dessinerService();
                break;
        }
    }
    private void dessinerMenu()
    {
        choixMenu.update(keypress, 0);
        choixMenu.dessiner(g);
        if(choixMenu==optSfx)
            choixMenu.dessinerSFX(key,g);
        if(choixMenu==optBGM)
            choixMenu.dessinerBGM(key, g);
        if(choixMenu==optLevel)
            iLevel=choixMenu.dessinerChoixLevel(keypress, g) - 1;

        choixMenu = choixMenu.nextMenu();
    }
    
    private void dessinerNewLevel()
    {
        if(nouvelEtat)
        {
            effacerEcran();
            g.setColor(255,255,255);
            g.drawString("Level " + String.valueOf(iLevel+1), 50, 50, 0);
            flushGraphics();
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException ex)
            {
                System.err.println(ex);
            }
        }
    }
    
    private void dessinerPlay()
    {
        effacerEcran();
        level.dessiner(g);
        balle.dessiner(g);
        palette.dessiner(g);
        g.setColor(255,255,255);
        g.drawString("Vie : " + String.valueOf(vie), 1, 1, 0);
        g.drawString("Points : " + String.valueOf(points), 100, 1, 0);
        g.drawLine(0, 30, 240, 30);
    }
    private void dessinerPause()
    {
        dessinerPlay();
        g.drawString("PAUSE...(FIRE)", 40,250,0);
    }
    private void dessinerGameOver()
    {
        Image imgLost = null;
        try
        {
            imgLost = Image.createImage("/images/lost.png");
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }

        effacerEcran();
        g.drawImage(imgLost, 0, 0, 0);
        g.setColor(255,255,255);
        g.drawString("Enter pour terminer..", 10, 300, 0);
    }
    private void dessinerWinner()
    {
        Image imgWin = null;
        try
        {
            imgWin = Image.createImage("/images/win.png");
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }

        effacerEcran();
        g.drawImage(imgWin, 0, 0, 0);
        g.setColor(255,255,255);
        g.drawString("Enter pour terminer..", 10, 200, 0);
    }
    private void dessinerService()
    {
        effacerEcran();
        level.dessiner(g);
        balle.dessiner(g);
        palette.dessiner(g);
        g.setColor(255,255,255);
        g.drawString("Vie : " + String.valueOf(vie), 1, 1, 0);
        g.drawString("Points : " + String.valueOf(points), 100, 1, 0);
        g.drawLine(0, 30, 240, 30);
    }
    private void effacerEcran()
    {
        g.setColor(0);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    private void changerEtat(int e)
    {
        ne = true;
        etat =e;
    }
    private void checkCollision()
    {
        int i = 0 ,iMax =level.briques.length-1 ,
                xMilieu = balle.x+4, yMilieu = balle.y+4;
        boolean finCheckCollision = false;
        while(! finCheckCollision)
        {
            Brick brick = level.briques[i];
            if(brick.getEtat()== Brick.ETAT_VIE)
            {
                //collision horizontale (type collision 0)
                if(xMilieu>=brick.x && xMilieu <=brick.x+24 &&
                        balle.sprite.collidesWith(brick.sprite, false))
                {
                    //deplace le sprite tant qu'il est en collision
                    while(balle.sprite.collidesWith(brick.sprite, false))
                    {
                        balle.sprite.move(0, -1*balle.vy);
                        balle.y+=-1*balle.vy;
                    }
                    finCheckCollision=true;
                    balle.colBrique(0);
                    points+=brick.collision();
                }
                //collision verticale (type collision 1)
                if(yMilieu>=brick.y && yMilieu <= brick.y+12 &&
                        balle.sprite.collidesWith(brick.sprite, false))
                {
                    //deplace le sprite tant qu'il est en collision
                    while(balle.sprite.collidesWith(brick.sprite, false))
                    {
                        balle.sprite.move(-1*balle.vx,0);
                        balle.x+=-1*balle.vx;
                    }
                    finCheckCollision=true;
                    balle.colBrique(1);
                    points+=brick.collision();
                }
            }
            if(i==iMax)
                finCheckCollision=true;
            i++;
        }
        //collision palette-balle
        if(balle.sprite.collidesWith(palette.sprite, false))
        {
            //deplace le sprite tant qu'il est en collision
            while(balle.sprite.collidesWith(palette.sprite, false))
            {
                balle.sprite.move(0, -1);
                balle.y--;
            }
            balle.colPalette(palette);
        }
    }
}
