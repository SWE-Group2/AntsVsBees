package ants;

public class QueenAnt extends ScubaThrowerAnt {
  protected boolean trueQueen = false;

  /** Creates a new QueenAnt. Post-condition: food cost is 6, armor is 1, and isQueen is true */
  public QueenAnt(boolean isTrueQueen) {
    super();
    this.foodCost = 6;
    this.armor = 1;
    this.watersafe = true;
    this.trueQueen = isTrueQueen;
  }

  public QueenAnt() {
    super();
    this.foodCost = 6;
    this.armor = 1;
    this.watersafe = true;
    this.trueQueen = false;
  }

  /** returns true if this is the real queen, false otherwise. Post-condition: returns a boolean */
  public boolean isRealQueen() {
    return trueQueen;
  }

  public void setRealQueen(boolean isTrueQueen) {
    this.trueQueen = isTrueQueen;
  }
}
