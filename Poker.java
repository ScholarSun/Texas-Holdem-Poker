import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.*;

public class Poker extends JFrame implements MouseListener
{
    //declare variables
    private Deck deck, table;
    //static small and big blind
    private int  Bblind = 20, Sblind = 10, dealer = 0, sb, bb;
    //array list of players ( hands)
    private ArrayList<Hand> hands = new ArrayList<Hand>();
    private boolean endGame;
    protected int pot = 0, roundpot = 0, highest = 0, current = 0 ;
    private int cur = 0;
    private int computers, players;
    private boolean[] inplay;

    public Poker ( int comps)
    {
        //set amount of computers and players
        computers = comps;
        players = computers + 1;
        //initialize player
        hands.add(new Human(this,200));
        //initialize computers
        for ( int x=0; x < computers; x++)
        {
            hands.add(new Computer(this, 200));
        }
        //initialize table
        table = new Deck();
        //Make all the hands in play
        inplay = new boolean[hands.size()]; 
        for(int k =0; k<hands.size(); k++) 
        {
            if ( hands.get(k).getChips() > 0 )
                inplay[k] = true;
        } 
        endGame = false;
        //initialize gui
        gui(this);
        callbtnlabel.addMouseListener(this);
        raisebtnlabel.addMouseListener(this);
        foldbtnlabel.addMouseListener(this);
        checkbtnlabel.addMouseListener(this);
    }

    public void play ()
    {
        //while the player hasnt lost, keep playing the rounds
        while(!endGame)
        {
            //determine if the player has lost or won
            endGame = round();
            //brief intermission between rounds
            try
            {
                Thread.sleep(10000);
            }
            catch (Exception e){}
        }
        //close window when lost/won
        dispose();
    }

    public boolean round ()
    {
        int temp;
        //check if player has lost
        if ( hands.get(0).getChips() <= 0 || hands.get(0).getChips() == 1000)
            return true;

        //reset decks, pot, and hands
        deck = new Deck(52);
        deck.shuffle();
        pot = Sblind + Bblind;
        table = new Deck();
        for(int j =0; j<hands.size();j++)
        {
            hands.get(j).reset();
        }

        //reset choice
        for(int p = 0; p < hands.size(); p++)
        {
            hands.get(p).setChoice(1);
        }
        
        //make all the players with chips in play again
        inplay = new boolean[hands.size()]; 
        for(int k =0; k<hands.size(); k++) 
        {
            if ( hands.get(k).getChips() > 0 )
                inplay[k] = true;
        } 
        //Dealer 
        dealer ++;
        if(dealer == hands.size())
            dealer = 0;

        //Small and Big Blind  
        if(dealer>1)
        {
            sb = dealer-2;
        }
        else
            sb = dealer+computers-1;

        while(hands.get(sb).getChips() == 0)
        {
            sb = (sb -1 + players)%players;
        }

        if(dealer>0)
        {
            bb = dealer-1;
        }
        else 
            bb = dealer + computers; 

        while(hands.get(bb).getChips() == 0)
        {
            bb = (bb -1 + players)%players;
        }  

        //reset the amount commit by each player
        resetcommit();
        //take small blind and big blind from players
        hands.get(sb).removeChips(Sblind);
        hands.get(sb).setCommit(Sblind);
        hands.get(bb).removeChips(Bblind);
        hands.get(bb).setCommit(Bblind);

        //deal cards to players
        for(int i = 0; i<2; i++)
        {
            for(int j =0; j < hands.size(); j++)
            {
                Card x = deck.deal(0);
                //flip the cards
                x.facedown();
                if(j == 0)
                    x.faceup();
                hands.get(j).add(x);
                hands.get(j).quickSort(0, hands.get(j).getSize()-1);
            }
        }
        //display
        repaint();
        //get choice from player and computers
        //reset highest bet
        highest = Bblind;
        getChoice();
        if (players>0)
        {
            //flop
            //reset highest bet
            highest = 0;
            for(int f =0; f<3; f++)
            {
                //deal cards
                table.add(deck.deal(0));
                repaint();
                try
                {
                    Thread.sleep(250);
                }
                catch (Exception e){}
            }
            //reset betting round
            resetcommit();
            getChoice();
            if(players>0)
            {
                //turn
                //reset betting round
                highest = 0;
                table.add(deck.deal(0));
                repaint();
                resetcommit();
                getChoice();
                if(players > 0)
                {
                    //river
                    //reset betting round
                    highest = 0;
                    table.add(deck.deal(0));
                    repaint();
                    resetcommit();
                    getChoice();
                }
            }
        }
        repaint();
        //determine winner, distribute chips
        evaluate();
        repaint();
        return false;
    }

    public void resetcommit()
    {
        //set all player's commits to zero
        for(int x =0; x < hands.size(); x++)
        {
            hands.get(x).setCommit(0);
        }
    }

    public void mouseClicked(MouseEvent e) // #nick this is the method i was talking about
    {
        // 0 = fold, 1 = check, 2 = call, 3 = raise
        if (e.getSource() == callbtnlabel)
        {
            hands.get(0).setChoice(2);
            current++;
        } else if (e.getSource() == raisebtnlabel) 
        {
            hands.get(0).setRaise(amount.getValue());
            hands.get(0).setChoice(3);
            current++;
        } else if (e.getSource() == foldbtnlabel) 
        {
            hands.get(0).setChoice(0);
            current++;
        }else if (e.getSource() == checkbtnlabel) 
        {
            hands.get(0).setChoice(1);
            current++;
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void getChoice()
    {
        //initialize variables
        boolean exit = false;
        int cur = (dealer+2)%players,end = cur, current = cur;
        while(!exit)
        {
            //if the player is in play
            if(inplay[cur])
            {
                try
                {
                    Thread.sleep(500);
                }
                catch ( Exception e){}
                //get choice
                hands.get(cur).choose();
                //if they have no chips, do nothing
                if(hands.get(cur).getChips() == 0)
                {}
                else if(hands.get(cur).fold()) //if they fold reset their cards
                {
                    hands.get(cur).reset();
                    inplay[cur] = false;
                }
                else if (hands.get(cur).check()) //check fold
                {
                    if(hands.get(cur).getCommit() != highest)
                    {
                        hands.get(cur).reset();
                        inplay[cur] = false;
                    }
                }
                else if (hands.get(cur).raise()) //raise by the amount specified
                {
                    //get raise amount and subtract from player total
                    highest += hands.get(cur).getRaise();
                    hands.get(cur).removeChips(highest-hands.get(cur).getCommit());
                    //add to pot
                    pot += highest - hands.get(cur).getCommit();
                    //go through all the other players
                    end = cur;
                    //change commit and choice
                    hands.get(cur).setCommit(highest);
                    hands.get(cur).setChoice(1);
                }
                else //call
                {
                    //go all in
                    if(highest - hands.get(cur).getCommit() > hands.get(cur).getChips())
                    {
                        pot += hands.get(cur).getChips();
                        hands.get(cur).setCommit(hands.get(cur).getChips());
                        hands.get(cur).removeChips(hands.get(cur).getChips());
                    }
                    else
                    {
                        //if they have enough to move on
                        pot += highest - hands.get(cur).getCommit();
                        hands.get(cur).removeChips(highest-hands.get(cur).getCommit());
                        hands.get(cur).setCommit(highest);
                    }
                }
            }
            //short pause
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {}
            repaint();
            //cycle through players
            if((cur+1) % players == end)
            {
                exit = true;
            }
            cur = (cur+1) % players;
            current = cur;
        }
    }

    public void playerchoice()
    {
        //wait until the player has made a decision
        repaint();
        current=0;
        while(current == 0)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (Exception e)
            {}
        }
    }

    public int getPlayers()
    {
        //return number of players
        return players;
    }

    /**************************************************************************************
     * -------   |     |   -------        -------   -------   -------   -------   ------- *
     * |         |     |      |           |     |   |     |   |     |      |      |       *
     * |         |     |      |           |     |   |     |   |     |      |      |       *
     * |   ---   |     |      |           |------   |-----|   |\-----      |      ------- *
     * |     |   |     |      |           |         |     |   | \          |            | *
     * |     |   |     |      |           |         |     |   |  \         |            | *
     * -------   -------   -------        |         |     |   |   \__      |      ------- *
     **************************************************************************************/

    ImageIcon ttop = new ImageIcon("TableTop.PNG");
    ImageIcon tbot = new ImageIcon("TableBot.PNG");
    ImageIcon ttable = new ImageIcon("table.PNG");
    ArrayList<String> money = new ArrayList<String>();
    // Call Button
    Icon callbtn = new ImageIcon("CallBtn.PNG");
    ImageIcon darkcall = new ImageIcon( "CallBtnDark.PNG");
    ImageIcon lightcall = new ImageIcon("CallBtnLight.PNG");
    JLabel callbtnlabel = new JLabel(callbtn);
    // Raise Button
    Icon raisebtn = new ImageIcon("RaiseBtn.PNG");
    ImageIcon darkraise = new ImageIcon("RaiseBtnDark.PNG");
    ImageIcon lightraise = new ImageIcon("RaiseBtnLight.PNG");
    JLabel raisebtnlabel = new JLabel(raisebtn);
    // Fold Button
    Icon foldbtn = new ImageIcon("FoldBtn.PNG");
    ImageIcon darkfold = new ImageIcon("FoldBtnDark.PNG");
    ImageIcon lightfold = new ImageIcon("FoldBtnLight.PNG");
    JLabel foldbtnlabel = new JLabel(foldbtn);

    // Check Button
    Icon checkbtn = new ImageIcon("CheckBtn.PNG");
    ImageIcon darkcheck = new ImageIcon("CheckBtnDark.PNG");
    ImageIcon lightcheck = new ImageIcon("CheckBtnLight.PNG");
    JLabel checkbtnlabel = new JLabel(checkbtn);

    JSlider amount = new JSlider(JSlider.HORIZONTAL,0,200-highest,0);

    private void gui ( Poker game)
    {
        refresh();

        //Slider
        amount.setMajorTickSpacing((hands.get(0).getChips()-highest)/4);
        amount.setMinorTickSpacing(0);
        amount.setPaintTicks(true);
        amount.setPaintLabels(true);

        //call button
        callbtnlabel.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    callbtnlabel.setIcon(darkcall);
                }

                public void mouseReleased(MouseEvent e) {
                    callbtnlabel.setIcon(callbtn);
                }

                public void mouseEntered(MouseEvent e) {
                    callbtnlabel.setIcon(lightcall);
                }

                public void mouseExited(MouseEvent e) {
                    callbtnlabel.setIcon(callbtn);
                }
            });

        //raise button
        raisebtnlabel.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    raisebtnlabel.setIcon(darkraise);
                }

                public void mouseReleased(MouseEvent e) {
                    raisebtnlabel.setIcon(raisebtn);
                }

                public void mouseEntered(MouseEvent e) {
                    raisebtnlabel.setIcon(lightraise);
                }

                public void mouseExited(MouseEvent e) {
                    raisebtnlabel.setIcon(raisebtn);
                }
            });

        //fold button
        foldbtnlabel.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    foldbtnlabel.setIcon(darkfold);
                }

                public void mouseReleased(MouseEvent e) {
                    foldbtnlabel.setIcon(foldbtn);
                }

                public void mouseEntered(MouseEvent e) {
                    foldbtnlabel.setIcon(lightfold);
                }

                public void mouseExited(MouseEvent e) {
                    foldbtnlabel.setIcon(foldbtn);
                }
            });

        //check button
        checkbtnlabel.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    checkbtnlabel.setIcon(darkcheck);
                }

                public void mouseReleased(MouseEvent e) {
                    checkbtnlabel.setIcon(checkbtn);
                }

                public void mouseEntered(MouseEvent e) {
                    checkbtnlabel.setIcon(lightcheck);
                }

                public void mouseExited(MouseEvent e) {
                    checkbtnlabel.setIcon(checkbtn);
                }
            });

        //--------CONTENT PANE AND LAYOUTS--------

        // Create content pane, set layout
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());

        //Panel of buttons
        DrawUpper upperboard = new DrawUpper(1204, 745);
        DrawLower ButtonPanel = new DrawLower(1204, 85);
        ButtonPanel.setLayout(new FlowLayout());

        //Adds the buttons to the ButtonPanel
        ButtonPanel.add(foldbtnlabel);
        ButtonPanel.add(checkbtnlabel);
        ButtonPanel.add(callbtnlabel);
        ButtonPanel.add(raisebtnlabel);
        ButtonPanel.add(amount,FlowLayout.LEFT);

        //Adds components to panel
        content.add(ButtonPanel, "South");
        content.add(upperboard, "North");

        // Set this window's attributes.
        setContentPane(content);
        pack();
        setTitle("Texas Hold em'");
        setSize(1204, 745);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window.
        setVisible(true);

    }
    // Draw Area Object
    class DrawUpper extends JPanel {
        // Sets the size
        public DrawUpper(int width, int height) {
            this.setPreferredSize(new Dimension(width, height));
        }
        // Draws the board
        public void paintComponent(Graphics g) {
            //Background
            ttable.paintIcon(rootPane, g, 0, 0);

            //update slider
            if(hands.get(0).getChips()> highest) {
                amount.setVisible(true);
                amount.setMaximum(hands.get(0).getChips()-highest);
            } else amount.setVisible(false);
            amount.setLabelTable(null);
            amount.setMajorTickSpacing((hands.get(0).getChips())/4);
            amount.setMinorTickSpacing(0);

            //Cards on table
            hands.get(0).human(g);
            if(table.getSize() != 0)
                table.tableshow(g);
            if(inplay[1])
                hands.get(1).comp1(g);
            if (inplay[2])
                hands.get(2).comp2(g);
            if(inplay[3])
                hands.get(3).comp3(g);
            if(inplay[4])
                hands.get(4).comp4(g);

            //Font and money amount
            g.setColor(Color.ORANGE);
            g.setFont(new Font("Arial", Font.PLAIN, 20)); 
            g.drawString("Player:", 25, 630);
            g.drawString("" + hands.get(1).getChips(), 25, 345);
            g.drawString("CPU 1: ", 25, 315);
            g.drawString("" + hands.get(2).getChips(), 370, 90);
            g.drawString("CPU 2: ", 370, 60);
            g.drawString("" + hands.get(3).getChips(), 670, 90);
            g.drawString("CPU 3: ", 670, 60);
            g.drawString("" + hands.get(4).getChips(), 1080, 345);
            g.drawString("CPU 4:", 1080, 315);
            g.drawString("Pot: "+pot, 560, 410);
            g.setFont(new Font("Arial", Font.PLAIN, 40));
            g.drawString("" + hands.get(0).getChips(), 25, 670);

            //turn chip
            if ( current  == 0)
            {
                g.fillOval(500, 430, 25, 25);
            }
        }   
    }

    class DrawLower extends JPanel {
        // Sets size
        public DrawLower(int width, int height) {
            this.setPreferredSize(new Dimension(width, height));
        }

        public void paintComponent(Graphics g) {
        }
    }

    private void refresh()
    {
        for(int x =0; x < hands.size(); x++)
        {
            String mon = "" + hands.get(x).getChips();
            money.add(mon);
        }
    }

    public void evaluate()
    {
        //flip all the cards up so that the player can see who won
        for(int h =0; h < hands.size(); h++)
        {
            if(inplay[h])
            {
                hands.get(h).getCard(0).faceup();
                hands.get(h).getCard(1).faceup();
            }
        }

        //determine the values of each hand
        int[] hevals = new int[hands.size()];
        //determine the winner
        int pos = 0, counter =0;
        //find the values of the hands in play
        for(int x =0; x < hands.size(); x++)
        {
            if ( inplay[x] )
            {
                hevals[x] = eval(x);
            }
            else
                hevals[x] = -1;
        }
        //find the hand with the highest value
        for (int y = 1; y < hevals.length; y++)
        {
            if(hevals[y] > hevals[pos])
            {
                pos = y;
            }
        }

        //check for ties
        for(int z = 0; z<hevals.length; z++)
        {
            if(hevals[z] == hevals[pos])
                counter++;
        }

        if(counter == 1)
        {
            //give player those chips
            hands.get(pos).addChips(pot);  
        }
        else
        {
            //split pot between the winners
            int sum = pot / counter;
            for(int z = 0; z<hevals.length; z++)
            {
                if(hevals[z] == hevals[pos])
                {
                    hands.get(z).addChips(sum);
                }
            }
        }                                                                        
    }

    private int eval(int p)
    {
        //create a 7 card deck of the player's hand and the table cards
        Deck temp = new Deck();
        int card1 = hands.get(p).getCard(0).getRank(), card2 = hands.get(p).getCard(1).getRank();
        for(int x =0; x<hands.get(p).getSize(); x++)
        {
            temp.add(hands.get(p).getCard(x));
        }
        for(int y =0; y< table.getSize(); y++)
        {
            temp.add(table.getCard(y));
        }
        temp.quickSort(0,6);
        //find the best possible hand from the 7 cards, return value that corresponds with the hand
        int r= royal(temp),sf = straightflush(temp), four = four(temp), f = flush(temp), fullh = fullhouse(temp);
        int s = straight(temp), three = three(temp), twopair = twopair(temp), pair = pair(temp);
        if(r != -1) //Royal Flush 9
        {
            return 9000000 + (3 - r) * 100 + card2;
        }
        else if(sf != -1) //Straight Flush 8
        {
            return 8000000 + sf % 13 * 10000 + (3 - sf/13)*100 + card2; 
        }
        else if(four != -1) //Four of a kind 7
        {
            return 7000000 + four*100 + card2;
        }
        else if(fullh != -1) // Full House 6
        {
            return 6000000 + fullh;
        }
        else if(f != -1) // Flush 5 
        {
            return 5000000 + 3 - f;
        }
        else if(s != -1) //Straight 4
        {
            return 4000000 + s * 100 + card2;
        }
        else if(three != -1) //three of a kind
        {
            if ( three == card1)
                return 3000000 + three * 100 + card2;
            else if ( three == card2)
                return 3000000 + three * 100 + card1;
            else
                return card2;
        }
        else if(twopair != -1) //two pair
        {
            return 2000000 + twopair*100 + card2;
        }
        else if(pair != -1) //one pair
        {
            if(pair == card1)
                return 1000000 + pair * 100 + card2;
            else if ( pair == card2)
                return 1000000 + pair * 100 + card1;
            else
                return card2;
        }
        //high card  
        return hands.get(p).getCard(1).getRank();
    }

    private static int royal(Deck cards)
    {
        if(flush(cards) != -1 && straight(cards) == 12)
        {
            //check if there is 10 to Ace from Spades, Hearts, Clubs and Diamonds and return suit if there is one
            if(cards.searchCard(8) && cards.searchCard(9) && cards.searchCard(10) && cards.searchCard(11) && cards.searchCard(12))
            {
                return 0;
            }
            else if(cards.searchCard(21) && cards.searchCard(22) && cards.searchCard(23) && cards.searchCard(24) && cards.searchCard(25))
            { 
                return 1;
            }
            else if(cards.searchCard(34) && cards.searchCard(35) && cards.searchCard(36) && cards.searchCard(37) && cards.searchCard(38))
            {
                return 2;
            }
            else if(cards.searchCard(47) && cards.searchCard(48) && cards.searchCard(49) && cards.searchCard(50) && cards.searchCard(51))
            {
                return 3;
            }
        }
        //if there isnt, return -1
        return -1;
    }

    private static int straightflush (Deck cards)
    {
        if (flush(cards) != -1 && straight(cards)!= -1)
        {
            int suit = flush (cards);
            int highcard = straight(cards);
            int card = highcard + suit*13;
            //check if all the cards in the straight are the same suit
            for(int x = card; x < card-5; x--)
            {
                if(!cards.searchCard(x))
                {
                    return -1;
                }
            }
            return card;
        }
        //return -1 if there is not a straightflush
        return -1;
    }

    private static int four (Deck cards)
    {
        if(three(cards) != -1)
        {
            int counter = 1;
            for(int x=0; x<cards.getSize()-2; x++)
            {
                //if there is another card of same rank increment counter, else make it 1
                if ( cards.getCard(x).getRank() == cards.getCard(x+1).getRank())
                {
                    counter++;
                }
                else 
                {
                    counter = 1;
                }
                //once you find the four of a kind return it
                if(counter == 4)
                {
                    return cards.getCard(x).getRank();
                }
            }
        }
        //if one is not found return -1
        return -1;
    }

    private static int fullhouse (Deck cards)
    {
        //check if there are 2 pairs and a triplet
        if(three(cards) != -1 && twopair(cards) != -1)
        {
            //check to see what the house is full of, which one is the triplet
            if ( three(cards) == twopair(cards)/100)
            {
                return three(cards) * 100 + twopair(cards) % 100;
            }
            return three(cards)*100 + twopair(cards)/100;
        }
        //if full house not found return -1
        return -1;
    }

    private static int flush (Deck cards)
    {
        //counters of each suit
        int spades=0, hearts=0, clubs=0, diamonds=0;
        //go through all the cards and record their suit
        for(int x =0; x<cards.getSize(); x++)
        {
            int suit = cards.getCard(x).getSuit();
            if (suit == 0)
                spades++;
            else if (suit == 1)
                hearts++;
            else if (suit == 2)
                clubs++;
            else if (suit == 3)
                diamonds++;
        }

        //if there is a flush return the suit
        if(spades >= 5)
            return 0;  
        else if (hearts >= 5)
            return 1;
        else if (clubs >= 5)
            return 2;
        else if (diamonds >= 5)
            return 3;

        //return -1 if there is no flush
        return -1;
    }

    private static int straight (Deck cards)
    {
        //get rid of duplicates
        Deck temp = new Deck();
        temp.add(cards.getCard(0));
        for(int k = 1; k < cards.getSize(); k++)
        {
            if(cards.getCard(k).getRank() != cards.getCard(k-1).getRank())
            {
                temp.add(cards.getCard(k));
            }
        }

        //assume that there is a straight
        boolean straight;
        for(int y = temp.getSize()-5; y >= 0; y--)
        {
            straight = true;
            //check through biggest cards to find if there is a straight
            for(int x = y; x < y+4; x++)
            {
                //if any card in the 5 card hand does not increment by 1 from the previous card then set straight to false
                if(temp.getCard(x+1).getRank() != temp.getCard(x).getRank() + 1)
                {
                    straight = false;
                }
            }
            //if it is a straight return the highest card of the straight
            if( straight == true)
            {
                return temp.getCard(y+4).getRank();
            }
        }
        //if it is not a straight return -1
        return -1;
    }

    private static int three (Deck cards)
    {
        int counter = 1;

        for(int x=cards.getSize()-1; x > 0; x--)
        {
            //if there is another card of same rank increment counter, else make it 1
            if ( cards.getCard(x).getRank() == cards.getCard(x-1).getRank())
            {
                counter++;
            }
            else 
            {
                counter = 1;
            }
            //once you find the biggest triplet return it
            if(counter == 3)
            {
                return cards.getCard(x).getRank();
            }
        }

        //if one is not found return -1
        return -1;
    }

    private static int twopair (Deck cards)
    {
        if(pair(cards) != -1)
        {
            int highpair = pair(cards), lowpair = -1, counter = 1;
            for(int x=0; x<cards.getSize()-2; x++)
            {
                //if there is another card of same rank increment counter, else make it 1
                if ( cards.getCard(x).getRank() == cards.getCard(x+1).getRank())
                {
                    counter++;
                }
                else 
                {
                    counter = 1;
                }
                //once you find two different pairs return it, if not return -1
                if(counter == 2)
                {
                    //check to see if you found the same pair
                    lowpair = cards.getCard(x).getRank();
                    if(pair(cards) != lowpair)
                    {
                        return pair(cards) * 100 + lowpair;
                    }
                    return -1;
                }
            }
        }
        return -1;
    }

    private static int pair (Deck cards)
    {
        int counter = 1;
        for(int x = cards.getSize()-1; x > 0; x--)
        {
            //if there is another card of same rank increment counter, else make it 1
            if ( cards.getCard(x).getRank() == cards.getCard(x-1).getRank())
            {
                counter++;
            }
            else 
            {
                counter = 1;
            }
            //once you find the biggest pair return it
            if(counter == 2)
            {
                return cards.getCard(x).getRank();
            }
        }
        //if one is not found return -1
        return -1;
    }

    public Deck getTable()
    {
        return table;
    }

    public static void main(String[] args)
    {
        Poker game= new Poker(4);
        Instructions ins = new Instructions();
        game.play();
        End e = new End();
    }
}
