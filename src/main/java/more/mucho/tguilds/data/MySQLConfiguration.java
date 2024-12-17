package more.mucho.tguilds.data;

import com.zaxxer.hikari.HikariConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Properties;

public class MySQLConfiguration {
    private static final FileConfiguration config =new ConfigHandler("mysql.yml").getConfig();
    public MySQLConfiguration() {
    }

    public static HikariConfig asHikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        String host = config.getString("host");
        int port = config.getInt("port");
        String database = config.getString("database");
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setPoolName("skyblock-pool");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setUsername(config.getString("user"));
        hikariConfig.setPassword(config.getString("password"));
        hikariConfig.setMaximumPoolSize(config.getInt("pool.maximum-pool-size"));
        hikariConfig.setMinimumIdle(config.getInt("pool.minimum-idle"));
        hikariConfig.setMaxLifetime(config.getLong("pool.maximum-lifetime"));
        hikariConfig.setConnectionTimeout(config.getLong("pool.connection-timeout"));
        ConfigurationSection propertiesSection = config.getConfigurationSection("properties");
        if (propertiesSection != null) {
            Properties properties = new Properties();
            for(String key : propertiesSection.getKeys(false)){
                properties.setProperty(key, propertiesSection.getString(key));
            }

            hikariConfig.setDataSourceProperties(properties);
        }

        return hikariConfig;
    }
}
