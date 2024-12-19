package more.mucho.tguilds.storage;

import more.mucho.tguilds.guilds.Guild;
import more.mucho.tguilds.guilds.GuildImpl;
import more.mucho.tguilds.storage.local.Repositories;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class GuildsDaoImpl extends AbstractDao<Guild,Integer> implements GuildsDao {

    public GuildsDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected Guild mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new GuildImpl(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("tag"),
                fromBytes(rs.getBytes("guild_uuid")),
                Repositories.getInstance().getMembersRepository()
        );
    }

    @Override
    protected String getTableName() {
        return "guilds";
    }


    @Override
    public CompletableFuture<Integer> addGuild(Guild guild) {
        return CompletableFuture.supplyAsync(() -> {
            String insertQuery = "INSERT INTO guilds (name, tag, guild_uuid) VALUES (?, ?, ?)";
            int id = save(insertQuery, guild.getName(), guild.getTag(), toBytes(guild.getUUID()));
            if (id < 0) return id;
            guild.setID(id);
            return id;
        });
    }

    @Override
    public CompletableFuture<Boolean> removeGuild(int ID) {
        return CompletableFuture.supplyAsync(()-> deleteById(ID,"id"));
    }

    @Override
    public CompletableFuture<Optional<Guild>> getGuild(int ID) {
       return CompletableFuture.supplyAsync(()-> findById(ID, "id"));
    }
}
