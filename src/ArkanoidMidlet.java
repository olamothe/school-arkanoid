

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author JFBoily
 */
public class ArkanoidMidlet extends MIDlet
{
    public void startApp()
    {
        ArkanoidCanvas canvas = new ArkanoidCanvas(this);

        Display d = Display.getDisplay(this);
        d.setCurrent(canvas);
    }

    public void pauseApp()
    {
    }

    public void destroyApp(boolean unconditional)
    {
        notifyDestroyed();
    }

}
