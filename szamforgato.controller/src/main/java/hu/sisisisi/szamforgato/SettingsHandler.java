package hu.sisisisi.szamforgato;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Ez az osztály kezeli a memóriában és a lemezen a játék beállításait.
 */
public class SettingsHandler
{
    private static Logger logger = LoggerFactory.getLogger(SettingsHandler.class);
    private static SettingsHandler settings;
    private static final String SETTINGS_FILE = "settings.json",
                                USERNAME_PROPERTY = "username",
                                TABLESIZE_PROPERTY = "tablesize";

    /**
     * Szükség esetén elkészíti, és visszaadja a singleton {@link SettingsHandler} objektumot.
     * @return A singleton {@link SettingsHandler} objektum.
     */
    public static SettingsHandler getSettings()
    {
        if(settings == null)
            settings = new SettingsHandler();
        return settings;
    }

    private String username = "John Doe";
    private int tableSize = 5;

    private SettingsHandler()
    {
        if(!Files.exists(Paths.get(SETTINGS_FILE)))
            SettingsHandler.createDefaults();

        try
        {
            JsonObject settingsObj = new JsonParser().parse(Files.lines(Paths.get(SETTINGS_FILE), Charset.forName("UTF-8")).collect(Collectors.joining())).getAsJsonObject();

            this.username =  settingsObj.getAsJsonPrimitive(USERNAME_PROPERTY).getAsString();
            this.tableSize = settingsObj.getAsJsonPrimitive(TABLESIZE_PROPERTY).getAsInt();
        }
        catch(IOException e)
        {
            logger.error("Beállítások betöltése sikertelen. Alapértelmezések használata.");
        }

    }

    private static void createDefaults()
    {
        JsonObject settingsObj = new JsonObject();
        settingsObj.addProperty(USERNAME_PROPERTY, "John Doe");
        settingsObj.addProperty(TABLESIZE_PROPERTY, 5);

        try
        {
            FileWriter out = new FileWriter(Paths.get(SETTINGS_FILE).toFile());
            out.write(new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(settingsObj));
            out.close();
        }
        catch(IOException e)
        {
            logger.error("Sikertelen az alapértelmezett beállítások fájlba írása.");
        }
    }

    /**
     * Visszaadja az alapértelmezett felhasználói nevet.
     * @return A felhasználói név. Kezdeti értéke 'John Doe'.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Visszaadja az alapértelmezett táblaméretet.
     * @return A táblaméret. Kezdeti értéke 5.
     */
    public int getTableSize()
    {
        return tableSize;
    }

    /**
     * Beállítja az innentől alapértelmezett felhasználói nevet.
     * @param username Az új alapértelmezett felhasználói név.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Beállítja az innentől alapértelmezett táblaméretet.
     * @param tableSize Az új alapértelmezett táblaméret.
     */
    public void setTableSize(int tableSize)
    {
        this.tableSize = tableSize;
    }

    public void saveToDisk() throws IOException
    {
        JsonObject settingsObj = new JsonObject();
        settingsObj.addProperty("username", this.username);
        settingsObj.addProperty("tablesize", this.tableSize);

        FileWriter out = new FileWriter(Paths.get(SETTINGS_FILE).toFile());
        out.write(new GsonBuilder().setPrettyPrinting().create().toJson(settingsObj));
        out.close();
    }
}
