import java.util.Random;
import java.util.Scanner;

/**
  The BattleShip class manages the gameplay of the Battleship game between two players.
  It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
      The main method that runs the game loop.
      It initializes the grids for both players, places ships randomly, and manages turns.
      The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }
        if(allShipsSunk(player1TrackingGrid)){
            System.out.println("Player 1 won!");
        }
        else if(allShipsSunk(player2TrackingGrid)){
            System.out.println("Player 2 won!");
        }
        System.out.println("Game Over!");
    }

    /**
      Initializes the grid by filling it with water ('~').

      @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = '~';
            }
        }
    }

    /**
      Places ships randomly on the given grid.
      This method is called for both players to place their ships on their respective grids.

      @param grid The grid where ships need to be placed.
     */
    static void placeShips(char[][] grid) {
        Random rand = new Random();

        boolean horizontal = rand.nextBoolean();
        int row = rand.nextInt(GRID_SIZE);
        int col = rand.nextInt(GRID_SIZE);
        for (int i = 2; i <=5; i++) {
            while (!(canPlaceShip(grid,row,col,i,horizontal))){
                horizontal = rand.nextBoolean();
                col = rand.nextInt(GRID_SIZE);
                row = rand.nextInt(GRID_SIZE);
            }
            if(horizontal){
                for(int j = 0; j < i; j++){
                    grid[row+j][col] = '*';
                }
            }
            else {
                for(int j = 0; j < i; j++){
                    grid[row][col+j] = '*';
                }
            }
        }

    }

    /**
      Checks if a ship can be placed at the specified location on the grid.
      This includes checking the size of the ship, its direction (horizontal or vertical),
      and if there's enough space to place it.

      @param grid The grid where the ship is to be placed.
      @param row The starting row for the ship.
      @param col The starting column for the ship.
      @param size The size of the ship.
      @param horizontal The direction of the ship (horizontal or vertical).
      @return true if the ship can be placed at the specified location, false otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        int counter = 0;
        if (horizontal) {
            for (int i = 0; i < size; i++) {
                if((row+i)< GRID_SIZE && grid[row+i][col] == '~') {
                    counter++;
                }
            }
            if (counter == size) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                if((col+i)< GRID_SIZE && grid[row][col+i] == '~') {
                    counter++;
                }
            }
            if (counter == size) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
      Manages a player's turn, allowing them to attack the opponent's grid
      and updates their tracking grid with hits or misses.

      @param opponentGrid The opponent's grid to attack.
      @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        String attack = scanner.nextLine();
        if(!isValidInput(attack)){
            System.out.println("Invalid input!");
        }
        else {
            int row =((int) attack.charAt(1)-48);
            int col= ((int) attack.charAt(0)-65);
            if(opponentGrid[row][col] == '*'){
                trackingGrid[row][col] = 'X';
                System.out.println("Hit!");
            }
            else {
                trackingGrid[row][col] = 'O';
                System.out.println("Miss!");
            }
        }
    }

    /**
      Checks if the game is over by verifying if all ships are sunk.

      @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        if(allShipsSunk(player1TrackingGrid) || allShipsSunk(player2TrackingGrid)){
            return true;
        }
        return false;
    }

    /**
      Checks if all ships have been destroyed on a given grid.

      @param grid The grid to check for destroyed ships.
      @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        int counter = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == 'X') {
                    counter++;
                }
            }
        }
        if(counter == 14){
            return true;
        }
        else {
            return false;
        }
    }

    /**
      Validates if the user input is in the correct format (e.g., A5).

      @param input The input string to validate.
      @return true if the input is in the correct format, false otherwise.
     */
    static boolean isValidInput(String input) {
        if (input.length() ==2 ) {
            if(65<=(int)input.charAt(0) && (int)input.charAt(0)<75){
                if(48<=(int)input.charAt(1) && (int)input.charAt(1)<58){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    /**
      Prints the current state of the player's tracking grid.
      This method displays the grid, showing hits, misses, and untried locations.

      @param grid The tracking grid to print.
     */
    static void printGrid(char[][] grid) {
        System.out.print(" ");
        for(int i=65 ; i<=74; i++){
            System.out.print(" "+ (char)i);
        }
        System.out.println();
        for(int i=0; i<10; i++){
            System.out.print(i);
            for(int j=0; j<10; j++){
                System.out.print(" "+grid[i][j]);
            }

            System.out.println();
        }
    }
}
