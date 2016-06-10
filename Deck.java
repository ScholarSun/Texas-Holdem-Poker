import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.util.ArrayList;

// -------------------------------------- Deck Class ------------------------------------------------------
class Deck
{
    protected ArrayList<Card> deck;
    public Deck ()
    {
        deck = new ArrayList(0);
    }
    
    public Deck( int A)
    {
        deck = new ArrayList (52);
        for (int x = 0 ; x < A ; x++)  // for each card in standard deck
        {
            deck.add(new Card (x)); // create card
        }
    }

    public void show (Graphics g)  // draws card face up or face down
    {
        for (int c = 0 ; c < deck.size() ; c++)
        {
            deck.get(c).show (g, c % 13 * 20 + 150, c / 13 * 50 + 20);
        }
    }

    public void human (Graphics g)
    {
        for(int c =0; c < deck.size(); c++)
        {
            //sends to show(graphics, coordinates
            deck.get(c).show(g, (c * 35) + 525, c + 450);
        }
    }
    
    // Draws cards
	public void comp1(Graphics g) {
		for (int c = 0; c < deck.size(); c++) {
			// Sends to show(graphics,coordinates)
			deck.get(c).show(g, (c * 35) + 165, c + 285);
		}
	}

	// Draws cards
	public void comp2(Graphics g) {
		for (int c = 0; c < deck.size(); c++) {
			// Sends to show(graphics,coordinates)
			deck.get(c).show(g, (c * 35) + 350, c + 125);
		}
	}

	// Draws cards
	public void comp3(Graphics g) {
		for (int c = 0; c < deck.size(); c++) {
			// Sends to show(graphics,coordinates)
			deck.get(c).show(g, (c * 35) + 650, c + 125);
		}
	}

	// Draws cards
	public void comp4(Graphics g) {
		for (int c = 0; c < deck.size(); c++) {
			// Sends to show(graphics,coordinates)
			deck.get(c).show(g, (c * 35) + 900, c + 285);
		}
	}

	public void tableshow(Graphics g) {
		for (int c = 0; c < deck.size(); c++) {
			// Sends to show(graphics,coordinates)
			deck.get(c).show(g, (c * 80) + 385, 285);
		}
	}
    
    public void shuffle ()
    {
        //remove card at random position r and add it to the bottom of the deck
        for( int x = 0; x< 2 * deck.size(); x++)
        {
            int r = (int) (Math.random() * deck.size());
            deck.add(deck.remove(r));
        }        
    }

    private int Partition(int start, int end)
    {
        //Take a random pivot point, in this case the last index
        int pivot = deck.get(end).getRank();
        //set value for next partition point
        int partitionIndex = start;
        for(int i = start; i <end; i++)
        {
            //if there is a smaller value on the left of the pivot and there is a larger value
            // at or to the right of the pivot then swap them
            if(deck.get(i).getRank() <= pivot)
            {
                Card temp = deck.get(i);
                deck.set(i, deck.get(partitionIndex));
                deck.set(partitionIndex, temp);
                //advance partition point position
                partitionIndex++;
            }
        }
        Card temp1 = deck.get(partitionIndex);
        deck.set(partitionIndex, deck.get(end));
        deck.set(end, temp1);
        //return point where all the values to the left are smaller than that element
        //and all the values to the right are larger than that element
        return partitionIndex;
    }
    
    public void quickSort (int start, int end)
    {   
       //base case of sort is when there is an array of 1 element to sort
       if(start < end)
       {
           //find partition index
           int partitionIndex = Partition(start, end);
           //sort elements to the left and right of the partition
           quickSort(start, partitionIndex - 1);
           quickSort(partitionIndex+1, end);
       }
    }

    public int getSize()
    {
        //return size of deck
        return deck.size();
    }
    
    public void selectionSort ()
    {   
            Card temp;
            for (int x = 0 ; x < deck.size() - 1 ; x++) 
            {
                //assume first index is the smallest element
                int lowPos = x; 
                for (int y = x + 1 ; y < deck.size(); y++) 
                {
                    //if there is a smaller element, change lowest position index
                    if (deck.get(y).getRaS()<deck.get(lowPos).getRaS()) 
                        lowPos = y; 
                }
                //swap first index with the smallest element
                temp = deck.get(x); 
                deck.set(x, deck.get(lowPos));
                deck.set(lowPos, temp);
            }
    }
    
    public void add(Card x)
    {
        //add card to deck
        deck.add(x);
    }
    
    public Card deal ( int x)
    {
        //remove card from deck
        return deck.remove(x);
    }
    
    public Card get(int x)
    {
        //return card in the specified index
        return deck.get(x);
    }
    
    public int[] search ( Card x )
    {
        int counter = 0, pos = 0;
        //determine the size of the array
        for(int i = 0; i < deck.size(); i++)
        {
            if(deck.get(i).getRaS() == x.getRaS())
                counter++;
        }
        int[] cards = new int[counter];
        //fill the array with the index of the wanted card
        for(int i = 0; i < deck.size(); i++)
        {
            if(deck.get(i).getRaS() == x.getRaS())
            {
                cards[pos] = i;
                pos++;
            }
        }
        //return array of positions
        return cards;
    }
    
    public Card getCard(int p)
    {
        return deck.get(p);
    }
    
    public boolean searchCard ( int c)
    {
        for(int i = 0; i < deck.size(); i++)
        {
            if(deck.get(i).getRaS() == c)
            {
                return true;
            }
        }
        return false;
    }
}