import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import de.umass.lastfm.Playlist;
import de.umass.lastfm.Tag;

public class visualisation extends JPanel {


    int squareX, squareY;
    int w = 500, h = 500;
    int count = 0;
    double rho = 0, theta = 0;
    int fontSize = 14; //think is int
    int i;
    int maxTags  = 60;
    ArrayList<tag> tags = new ArrayList<tag>();
    ArrayList<shape> shapes = new ArrayList<shape>();
    Collection<Tag> lastfmtags;
    double mouseX = 0, mouseY = 0;
    int clickX, clickY;
    String key = "c16451e142b6902ae41ae727b50f6bf3"; 
    String secret = "fc8ebb1b3d9c16be60a488cc322ff048";
    double xDiff, xChange;
    radioHandler rh = new radioHandler();
    MediaPlayer mp;
    Color grey = new Color(100,100,100);

    public visualisation(MediaPlayer mp) {
            //System.setProperty("proxyHost", "wwwcache-20.cs.nott.ac.uk");
            //System.setProperty("proxyPort", "3128");
            this.mp = mp;
            Color c = new Color(0,0,0);
            this.setBackground(c);



            //setupElems();
            w = 500;
            h = 500;
            //set the span of all objects to null? for the if in draw()?
            //run draw
            //run roate every 50ms.


            addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                            clickX = e.getX();
                            clickY = e.getY();
                            for(shape theShape : shapes) {
                                    if(theShape.contains(clickX, clickY)) {
                                            String tempTag = theShape.getName();
                                            updateTags(tempTag);
                                            Playlist testPlaylist = rh.getRadio(tempTag);
                                            System.out.println("testPlaylist = null");
                                            if(testPlaylist != null) { //check if lastfm have actually returned any songs
                                                    System.out.println("testPlaylist != null");
                                                    Playlist tempPlaylist = testPlaylist;
                                                    addTracks(tempPlaylist);
                                            }


                                    }
                            }
                    }
            });



            addMouseListener(new MouseAdapter() {
                    public void mouseExited(MouseEvent e) {

                    mouseX = 230;
                    mouseY = 230;

            }
            });

            addMouseMotionListener(new MouseAdapter() {
                    public void mouseMoved(MouseEvent e) {
                                    mouseX = e.getX();
                                    mouseY = e.getY();
                    }
            });


                    Timer timer = new Timer(25, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                    setPos();
                                    repaint();

                            }
                    });
                    timer.setRepeats(true);
                    timer.start();



    }

    void addTracks(Playlist playlist) {
            mp.addPlaylist(playlist);

    }

    void updateTags(String newTag) {
            if(lastfmtags != null) {
                    lastfmtags.clear();
            }
            lastfmtags = Tag.search(newTag, key);
            setupElems();


    }

    void setTags(Collection<Tag> tags) {
            this.lastfmtags = tags;
    }

    void setupElems() {
            tag theTag;
            tags.clear();
            i = 1;
            for (Tag musictag : lastfmtags) {
                    if(i > maxTags) {
                            break;
                    }
                    theTag = new tag();
                    theTag.setID(i);
                    i++;
                    theTag.setName(musictag.getName());
                    theTag.setX(1);
                    theTag.setY(0);
                    theTag.setZ(0);
                    theTag = spin(theTag, (Math.random() * 2 - 1) * Math.PI);
                    theTag = step(theTag, (Math.random() * 2 - 1) * Math.PI);
                    theTag = spin(theTag, (Math.random() * 2 - 1) * Math.PI);
                    tags.add(theTag);
            }
    }


    void adjustElems() {

            for (tag theTag : tags) {
                    double dx = 0, dy = 0, dz = 0;
                    for (tag theTag2 : tags) {
                            if (theTag.getID() == theTag2.getID()) {
                                    continue;
                            }
                            double diffX = theTag.getX() - theTag2.getX();
                            double diffY = theTag.getY() - theTag2.getY();
                            double diffZ = theTag.getZ() - theTag2.getZ();
                            double r = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
                            dx += 0.05 / (r * r) * diffX / r;
                            dy += 0.05 / (r * r) * diffY / r;
                            dz += 0.05 / (r * r) * diffZ / r;
                    }
                    theTag.x += dx; //might need to be done with getters and setters
                    theTag.y += dy;
                    theTag.z += dz;
                    double dist = Math.sqrt(theTag.x * theTag.x + theTag.y * theTag.y + theTag.z * theTag.z);
                    theTag.x /= dist;
                    theTag.y /= dist;
                    theTag.z /= dist;

            } 

    } 




    void setPos() {
            double x = mouseX;
            double y = mouseY;
            x = (x - 250) / w;
            y = (y - 250) / h;
            theta = Math.atan2(y, x);
            rho = Math.sqrt(x * x + y * y);
            rotate();
    } 



    void rotate() {
            for(tag theTag : tags) {
                    theTag = spin(theTag, -theta);
                    theTag = step(theTag, rho / 7);
                    theTag = spin(theTag, theta);
            }; 
            adjustElems();
    }


    tag spin(tag theTag, double angle) {
            double x = theTag.getX();
            double y = theTag.getY();
            theTag.setX(x * Math.cos(angle) - y * Math.sin(angle));
            theTag.setY(x * Math.sin(angle) + y * Math.cos(angle));;
            return theTag;
    }


    tag step(tag theTag, double angle) {
            double x = theTag.getX();
            double z = theTag.getZ();
            theTag.setX(x * Math.cos(angle) - z * Math.sin(angle));
            theTag.setZ(x * Math.sin(angle) + z * Math.cos(angle));
            return theTag;
    } 


    public Dimension getPreferredSize() {
            return new Dimension(650,500);
    }

    public void paintComponent(Graphics g) {
            super.paintComponent(g);
            shapes.clear();
            for(tag theTag : tags) {
                    double size = fontSize + theTag.z * 10;
                    double left = w * (theTag.x * 0.9 + 1) / 2 - theTag.width * size / fontSize / 2;
                    double top = h * (theTag.y * 0.9 + 1) / 2 - theTag.height *size / fontSize / 2;

                    Font font = new Font("Candara", Font.PLAIN, (int)size);
                    g.setFont(font);
                    g.setColor(grey);
                    g.drawString(theTag.getName(),(int)left,(int)top);
                    Rectangle2D bounds = g.getFontMetrics().getStringBounds(theTag.getName(), g);
                    shape current = new shape((int)left, (int)(top+bounds.getY()), (int)bounds.getWidth(), 
                            (int)bounds.getHeight(), theTag.getName());
                    shapes.add(current);	
            }


    }  
}