import java.util.List;

public class Game {
    private final Board board;
    private List<Player> players;
    private GameStatus status;

    public Game(int boardSize, List<Player> players) {
        board = new Board(boardSize);
        this.setPlayers(players);
        this.setStatus(GameStatus.NOT_STARTED);
    }

    public void play() {
        setStatus(GameStatus.IN_PROGRESS);
        int playerIndex = 0;
        while (getStatus() == GameStatus.IN_PROGRESS) {
            board.draw();
            Player player = players.get(playerIndex);
            int[] coordinates = player.play();
            board.place(player, coordinates);
            setStatus(checkStatus());
            if(getStatus() == GameStatus.WON) {
                System.out.println("Player " + player.getName() + " won!");
            } else if(getStatus() == GameStatus.DRAW) {
                System.out.println("Game Drawn!");
            }
            playerIndex = (playerIndex + 1) % players.size();
        }
    }

    public GameStatus checkStatus() {
        for (int i = 0; i < board.getSize(); i++) {
            boolean rowSame = isRowSame(i);
            if (rowSame) {
                return GameStatus.WON;
            }
            boolean columnSame = isColumnSame(i);
            if (columnSame) {
                return GameStatus.WON;
            }
        }
        boolean diagonalSame = isDiagonalSame();
        if (diagonalSame) {
            return GameStatus.WON;
        }
        if (isAnyCellEmpty()) {
            return GameStatus.IN_PROGRESS;
        }
        return GameStatus.DRAW;
    }

    private boolean isRowSame(int row) {
        Cell[][] cells = board.getCells();
        boolean hasEmptyCell = false;
        for (int i = 0; i < board.getSize() - 1; i++) {
            String currentValue = cells[row][i].getValue();
            String nextValue = cells[row][i + 1].getValue();
            if (!currentValue.equals(nextValue)) {
                return false;
            }
            if (currentValue.isEmpty())
                hasEmptyCell = true;
        }
        return !hasEmptyCell;
    }

    private boolean isColumnSame(int col) {
        Cell[][] cells = board.getCells();
        boolean hasEmptyCell = false;
        for (int i = 0; i < board.getSize() - 1; i++) {
            String currentValue = cells[i][col].getValue();
            String nextValue = cells[i + 1][col].getValue();
            if (!currentValue.equals(nextValue)) {
                return false;
            }
            if (currentValue.isEmpty())
                hasEmptyCell = true;
        }
        return !hasEmptyCell;
    }

    private boolean isDiagonalSame() {
        Cell[][] cells = board.getCells();
        boolean hasEmptyCell = false;
        for (int i = 0; i < board.getSize() - 1; i++) {
            String currentValue = cells[i][i].getValue();
            String nextValue = cells[i + 1][i + 1].getValue();
            if (!currentValue.equals(nextValue)) {
                return false;
            }
            if (currentValue.isEmpty())
                hasEmptyCell = true;
        }
        int r = 0, c = board.getSize() - 1;
        for (int i = 0; i < board.getSize() - 1; i++) {
            String currentValue = cells[r][c].getValue();
            String nextValue = cells[r + 1][c - 1].getValue();
            if (!currentValue.equals(nextValue)) {
                return false;
            }
            if (currentValue.isEmpty())
                hasEmptyCell = true;
            r++;
            c--;
        }
        return !hasEmptyCell;
    }

    private boolean isAnyCellEmpty() {
        Cell[][] cells = board.getCells();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (cells[i][j].getValue().isEmpty())
                    return true;
            }
        }
        return false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    enum GameStatus {
        NOT_STARTED, IN_PROGRESS, WON, DRAW
    }
}
