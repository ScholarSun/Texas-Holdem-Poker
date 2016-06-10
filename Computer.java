
public class Computer extends Hand
{
    Poker game;
    boolean raised = false;
    public Computer (Poker g, int c)
    {
        //initialize computer
        super(c);
        game = g;
    }

    public int getRaise()
    {
        //return amount the computer will raise
        return this.getChips()/10;
    }

    public void choose()
    {
        //set choice by evaluating the hand
        choice = evaluate();
    }

    private int evaluate()
    {
        //bluff
        int r = (int) (Math.random() * 10 + 1);
        if(r > 7)
            return 3;

        Deck temp = new Deck();
        for ( int x = 0; x < 2; x++)    
        {
            temp.add(deck.get(x));
        }   
        for(int y =0; y < game.getTable().getSize(); y++)
        {
            temp.add(game.getTable().getCard(y));
        }
        //if last player always check
        if(game.getPlayers() == 1)
            return 1;
        //fold = 0, check = 1, call = 2, raise = 3
        if(game.getTable().getSize() == 0)
        {
            //if it has a pair it raises once
            if(pair() && !raised)
            {
                raised = true;
                return 3;
            }
            else if ( deck.get(1).getRank() > 8)
                return 2;
            return 1;
        }
        else
        {
            //raise if hand has triple, straight or flush
            if(three(temp) || straight(temp) || flush(temp))
            {
                raised = true;
                return 3;
            }
            else if ( pair(temp) || deck.get(1).getRank() > 10) //call if pair or king/ace
                return 2;
            //check - fold
            return 1;
        }
    }

    private boolean pair()
    {
        //check for a pair in the hand
        if(deck.get(0).getRank() == deck.get(1).getRank())
            return true;
        //if one is not found return -1
        return false;
    }

    private static boolean flush (Deck cards)
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
        if(spades >= 4)
            return true;  
        else if (hearts >= 4)
            return true;
        else if (clubs >= 4)
            return true;
        else if (diamonds >= 4)
            return true;

        //return -1 if there is no flush
        return false;
    }

    private static boolean straight (Deck cards)
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
            for(int x = y; x < y+3; x++)
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
                return true;
            }
        }
        //if it is not a straight return -1
        return false;
    }

    private static boolean three (Deck cards)
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
                return true;
            }
        }

        //if one is not found return -1
        return false;
    }

    private static boolean pair (Deck cards)
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
                return true;
            }
        }
        //if one is not found return -1
        return false;
    }
}
