import java.util.ArrayList;
public abstract class Hand extends Deck 
{
    // instance variables - replace the example below with your own
    private int chips = 0;
    protected int choice, raise = 0, commit = 0;
    public Hand( int c)
    {
        //initialize deck and chips
        super();
        chips = c;
    }
    public void reset()
    {
        //empty the deck
        deck = new ArrayList(0);
    }
    abstract void choose();
    abstract int getRaise();
    public void add(Card c)
    {
        //add a card
        super.add(c);
    }
    //check if the choice is fold, check, call or raise
    public boolean fold()
    {
        return choice == 0;
    }
    public boolean check()
    {
       return choice == 1;
    }
    public boolean call()
    {
        return choice == 2;
    }
    public boolean raise()
    {
        return choice == 3;
    }
    //return amount of chips the player has
    public int getChips()
    {
        return chips;
    }
    //give the player chips
    public void addChips(int c)
    {
        chips += c;
    }
    //take the player's chips
    public void removeChips (int c)
    {
        chips-= c;
    }
    //return the amount raised
    public int r()
    {
        return raise;
    }
    //set the amount raised by the player
    public void setRaise(int x)
    {
        raise = x;
    }
    //get the amount committed to the betting round 
    public int getCommit()
    {
        return commit;
    }
    //set the amount committed to the betting round
    public void setCommit(int x)
    {
        commit = x;
    }
    //set the choice of the player
    public void setChoice(int x)
    {
        choice = x;
    }
}
