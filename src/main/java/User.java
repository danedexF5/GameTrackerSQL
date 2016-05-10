import java.util.ArrayList;

/**
 * Created by danedexheimer on 5/3/16.
 */
public class User {
    String name;
    ArrayList<Game> games = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }
}
