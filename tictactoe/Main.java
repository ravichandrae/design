import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Player p1 = new Player("O");
        Player p2 = new Player("X");
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        Game game = new Game(3, players);
        game.play();
    }
}
