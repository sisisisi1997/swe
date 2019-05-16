package hu.sisisisi.szamforgato;

import com.google.gson.*;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ScoreBoardHandler
{
    public static ArrayList<Pair<String, Integer>> getScores(int tableSize)
    {
        ArrayList<Pair<String, Integer>> result = new ArrayList<>();
        try
        {
            Path p = Paths.get("board" + tableSize + ".json");
            String s = Files.lines(p, Charset.forName("UTF-8")).collect(Collectors.joining());
            JsonArray array = (JsonArray)new JsonParser().parse(s);
            for(int i = 0; i < array.size(); i ++)
            {
                JsonObject e = array.get(i).getAsJsonObject();
                String nev = e.getAsJsonPrimitive("nev").getAsString();
                int lepes = e.getAsJsonPrimitive("lepes").getAsInt();
                result.add(new Pair<>(nev, lepes));
            }
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("file not found");
        }
        catch (IOException ex)
        {
            System.out.println("IO");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getClass());
        }

        return result;
    }

    public static void saveScore(int tableSize, String name, int steps)
    {
        ArrayList<Pair<String, Integer>> scores = getScores(tableSize);
        ArrayList<Pair<String, Integer>> newScores = new ArrayList<>();
        boolean added = false;
        for(int i = 0; i < scores.size(); ++ i)
        {
            if(scores.get(i).getValue() > steps && !added)
            {
                newScores.add(new Pair<>(name, steps));
                added = true;
            }
            newScores.add(scores.get(i));
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
        try {
            Path p = Paths.get("board" + tableSize + ".json");
            //Files.deleteIfExists(p);
            FileWriter f = new FileWriter(p.toFile());
            f.write(result);
            f.flush();
            f.close();
        }
        catch (IOException ex)
        {
            System.out.println("IO in save");
        }
    }
}
