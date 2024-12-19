package more.mucho.tguilds;

import com.zaxxer.hikari.HikariDataSource;
import more.mucho.tguilds.commands.AbstractCommand;
import more.mucho.tguilds.commands.GuildCommand;
import more.mucho.tguilds.data.MySQLConfiguration;
import more.mucho.tguilds.listeners.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public final class TGuilds extends JavaPlugin {

    private static TGuilds INSTANCE = null;
    private HikariDataSource hikariDataSource = null;

    @Override
    public void onEnable() {
        INSTANCE = this;
        initDataSource();
        registerCommands(
                new GuildCommand()
        );
        registerListeners(
                new ChatListener()
        );
    }

    @Override
    public void onDisable() {
        shutdownDataSource();
        INSTANCE = null;
    }

    private void registerCommands(AbstractCommand... commands) {
        for (AbstractCommand command : commands) {
            for (String commandName : command.getCommandNames()) {
                PluginCommand pluginCommand = getCommand(commandName);
                if (pluginCommand == null) continue;
                pluginCommand.setExecutor(command);
                pluginCommand.setTabCompleter(command);
            }
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    public HikariDataSource getDataSource() {
        return this.hikariDataSource;
    }

    public static TGuilds i() {
        return INSTANCE;
    }

    private void initDataSource() {
        hikariDataSource = new HikariDataSource(MySQLConfiguration.asHikariConfig());

        String path = "schema.sql";

        try (InputStream inputStream = getResource(path)) {
            if (inputStream == null) {
                throw new IllegalStateException("Can not find " + path + "!");
            }
            try (Connection connection = hikariDataSource.getConnection(); Statement statement = connection.createStatement()) {

                List<String> queries = getStatements(inputStream);
                for (String query : queries) {
                    statement.execute(query);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();  // Handle resource-related exceptions (InputStream)
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void shutdownDataSource() {
        if (this.hikariDataSource != null && !this.hikariDataSource.isClosed()) {
            this.hikariDataSource.close();
        }

    }

    private List<String> getStatements(InputStream is) throws IOException {
        List<String> queries = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                if (line.trim().endsWith(";")) {
                    queries.add(sb.toString().trim());
                    sb.setLength(0);
                }
            }
        }

        return queries;
    }
}
