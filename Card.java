import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.util.ArrayList;

// -------------------------------------- Card Class ------------------------------------------------------
class Card
{
    private int rank, suit;
    private Image image;
    private boolean faceup = false;
    private static Image cardback; // shared image for back of card

    public Card (int cardNum)  // Creates card from 0-51
    {
        rank = cardNum % 13;
        suit = cardNum / 13;
        faceup = true;

        image = null;
        try
        {
            image = ImageIO.read (new File ("cards\\" + (cardNum + 1) + ".gif")); // load file into Image object
            cardback = ImageIO.read (new File ("cards\\b.gif")); // load file into Image object
        }
        catch (IOException e)
        {
            System.out.println ("File not found");
        }
    }
    
     public void faceup()
    {
        faceup = true; //Set Card to face up
    }
    
    public void facedown()
    {
        faceup = false; //Set card to face down
    }
    
    public void face()
    {
        faceup = !faceup; //flip the card upside down
    }
    
    public int getRank()
    {
        return rank; //returns rank of card
    }
    
    public int getSuit()
    {
        return suit; //return suit of card
    }
    
    public int getRaS()
    {
        return rank + suit*13; //returns rank and suit of card
    }
    
    public void show (Graphics g, int x, int y)  // draws card face up or face down
    {
        if (faceup)
            g.drawImage (image, x, y, null);
        else
            g.drawImage (cardback, x, y, null);
    }
    
    public String toString()
    {
        return (rank+2) + " " + suit;
    }
}