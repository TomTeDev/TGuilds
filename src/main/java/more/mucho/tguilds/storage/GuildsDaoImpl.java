package more.mucho.tguilds.storage;

import more.mucho.tguilds.guilds.Guild;

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
        return null;
    }

    @Override
    protected String getTableName() {
        return "guilds";
    }


    @Override
    public CompletableFuture<Boolean> addGuild(Guild guild) {
        return CompletableFuture.supplyAsync(()->{

          return true;
        });
    }

    @Override
    public CompletableFuture<Boolean> removeGuild(int ID) {
        return CompletableFuture.supplyAsync(()->{
            return deleteById(ID,"id");
        });
    }

    @Override
    public CompletableFuture<Optional<Guild>> getGuild(int ID) {
        return CompletableFuture.supplyAsync(()->{

            return true;
        });
    }
}
