package hu.sisisisi.szamforgato;

import com.google.gson.*;
import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Ez az osztály statikus metódusokat szolgáltat a ranglétra lekérésére és manipulálására különböző táblaméretek esetén.
 */
public class ScoreBoardHandler
{
    /**
     * Visszaadja az adott táblamérethez tartozó top 10 pontot (vagyis legkisebb lépésszámot).
     * @param tableSize A megadott táblaméret.
     * @return A táblamérethez tartozó top 10 játékos neve és lépésszáma. Amennyiben hiba történik a fájlhoz való hozzáférésben, {@code null}-t ad vissza.
     */
    public static ArrayList<Pair<String, Integer>> getScores(int tableSize)
    {
        ArrayList<Pair<String, Integer>> result = new ArrayList<>();
        try
        {
            Path p = Paths.get("board" + tableSize + ".json");
            if(Files.exists(p))
            {
                String s = Files.lines(p, Charset.forName("UTF-8")).collect(Collectors.joining());
                JsonArray array = (JsonArray)new JsonParser().parse(s);
                for(int i = 0; i < array.size(); i ++)
                {
                    JsonObject e = array.get(i).getAsJsonObject();
                    String name = e.getAsJsonPrimitive("nev").getAsString();
                    int steps = e.getAsJsonPrimitive("lepes").getAsInt();
                    result.add(new Pair<>(name, steps));
                }
            }
        }
        catch(IOException ex)
        {
            return null;
        }

        return result;
    }

    /**
     * A megadott méretű táblához hozzáfűzi a megadott nevet és pontszámot, ha az a top 10-be tartozik.
     * @param tableSize A megadott méret.
     * @param name A megadott név.
     * @param steps A megadott pontszám.
     */
    public static void saveScore(int tableSize, String name, int steps)
    {
        ArrayList<Pair<String, Integer>> scores = getScores(tableSize);
        ArrayList<Pair<String, Integer>> newScores = new ArrayList<>();
        boolean added = false;
        if(scores != null)
        {
            for(Pair<String, Integer> score : scores)
            {
                if(score.getValue() > steps && !added)
                {
                    newScores.add(new Pair<>(name, steps));
                    added = true;
                }
                newScores.add(score);
            }
        }
        if(!added) // ha a megadott pontszám az utolsó, akkor nem fog hozzáadódni a ciklusban
        {
            newScores.add(new Pair<>(name, steps));
        }

        while(newScores.size() > 10)
            newScores.remove(10);

        JsonArray array = new JsonArray();
        for(Pair<String, Integer> p : newScores)
        {
            JsonObject obj = new JsonObject();
            obj.addProperty("nev", p.getKey());
            obj.addProperty("lepes", p.getValue());
            array.add(obj);
        }

        String result = new Gson().toJson(array);
        try
        {
            Path p = Paths.get("board" + tableSize + ".json");
            FileWriter f = new FileWriter(p.toFile());
            f.write(result);
            f.close();
        }
        catch (IOException ex)
        {
            // TODO
        }
    }
}