package e1;
import org.junit.jupiter.api.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
public class LogicTest {


  private static int BOARD_SIZE = 10;

  private enum ElementOnBoard{
    KNIGHT,
    PAWN,
    EMPTY
  }

  private List<Pair<Integer, Integer>> possibleMove = new ArrayList<>(
    List.of(
    new Pair<Integer, Integer>(-2, -1),
    new Pair<Integer, Integer>(-2, 1),
    new Pair<Integer, Integer>(-1, -2),
    new Pair<Integer, Integer>(-1, 2),
    new Pair<Integer, Integer>(1, -2),
    new Pair<Integer, Integer>(1, 2),
    new Pair<Integer, Integer>(2, -1),
    new Pair<Integer, Integer>(2, 1)
  ));




  private Logics logic;
  private Map<Pair<Integer, Integer>,ElementOnBoard> grid ;


  @BeforeEach
  public void createLogic(){
    grid = new HashMap<>();
    logic = new LogicsImpl(BOARD_SIZE);

    IntStream.iterate(0, i -> i + 1).limit(BOARD_SIZE).forEach(x->{
      IntStream.iterate(0, i -> i + 1).limit(BOARD_SIZE).forEach(y->{

        ElementOnBoard objectOnBoard;

        if(logic.hasPawn(x, y)){
          objectOnBoard = ElementOnBoard.PAWN;
        }else if (logic.hasKnight(x, y)){
          objectOnBoard = ElementOnBoard.KNIGHT;
        }
        else{
          objectOnBoard = ElementOnBoard.EMPTY;
        }
        grid.put(new Pair<Integer,Integer>(x, y), objectOnBoard);
      });
    });

    
  }


  private Pair<Integer, Integer> getElemPositionInLocalGrid(ElementOnBoard elem){
    return grid.entrySet().stream()
    .filter(e->e.getValue()
    .equals(elem))
    .findFirst().get().getKey();
  }


  @Test
  public void horseIsSpawned(){

    Pair<Integer, Integer> knightPosition = getElemPositionInLocalGrid(ElementOnBoard.KNIGHT);
    Pair<Integer, Integer> pawnPosition = getElemPositionInLocalGrid(ElementOnBoard.PAWN);

    assertAll(
      ()->assertTrue(knightPosition.getX()>=0 && knightPosition.getX()<BOARD_SIZE),
      ()->assertTrue(knightPosition.getY()>=0 && knightPosition.getY()<BOARD_SIZE),
      ()->assertTrue(pawnPosition.getX()>=0 && pawnPosition.getX()<BOARD_SIZE),
      ()->assertTrue(pawnPosition.getY()>=0 && pawnPosition.getY()<BOARD_SIZE)
    );

  
   
  }

  @Test
  public void movesTest() {

    Pair<Integer, Integer> knightPosition = getElemPositionInLocalGrid(ElementOnBoard.KNIGHT);
    Pair<Integer, Integer> pawnPosition = getElemPositionInLocalGrid(ElementOnBoard.PAWN);

    for (Pair<Integer, Integer> move : possibleMove) {
      int x = knightPosition.getX() + move.getX();
      int y = knightPosition.getY() + move.getY();
      if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
        if (x == pawnPosition.getX() && y == pawnPosition.getY()) {
          Assertions.assertTrue(logic.hit(x, y));
        } else {
          Assertions.assertFalse(logic.hit(x, y));
        }
      }
    }

  }

  @Test
  public void outOfBoundsTest() {
    Pair<Integer, Integer> knightPosition = getElemPositionInLocalGrid(ElementOnBoard.KNIGHT);

    for (Pair<Integer, Integer> move : possibleMove) {
      int x = knightPosition.getX() + move.getX();
      int y = knightPosition.getY() + move.getY();
      if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> logic.hit(x, y));
      }
    }
  }



}
