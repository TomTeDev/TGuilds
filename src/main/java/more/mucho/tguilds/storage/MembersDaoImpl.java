package more.mucho.tguilds.storage;

import more.mucho.tguilds.guilds.GuildImpl;
import more.mucho.tguilds.guilds.Member;
import more.mucho.tguilds.guilds.RANK;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MembersDaoImpl extends AbstractDao<Member, Integer> implements MembersDao {
    public MembersDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected Member mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new GuildImpl.MemberImpl(
                rs.getInt("id"),
                rs.getString("name"),
                fromBytes(rs.getBytes("player_uuid")),
                RANK.valueOf(rs.getString("rank")),
                rs.getInt("guild_id")
        );
    }

    @Override
    protected String getTableName() {
        return "guild_members";
    }

    public CompletableFuture<Integer> save(Member member) {
        return CompletableFuture.supplyAsync(() -> {
            String insertQuery = "INSERT INTO " + getTableName() + " (name, player_uuid, rank, guild_id) VALUES (?, ?, ?, ?)";
            return save(insertQuery, member.getName(), toBytes(member.getUUID()), member.getRank().name(), member.getGuildID());
        });

    }

    @Override
    public CompletableFuture<Boolean> removeMember(int ID) {
        return CompletableFuture.supplyAsync(() -> {
            return deleteById(ID, "id");
        });
    }

    @Override
    public CompletableFuture<Optional<Member>> getMember(int ID) {
        return CompletableFuture.supplyAsync(() -> {
            return findById(ID, "id");
        });
    }

    @Override
    public CompletableFuture<Optional<Member>> getMember(String memberName) {
        return CompletableFuture.supplyAsync(() -> {
            return findByName(memberName, "name");
        });
    }

    @Override
    public CompletableFuture<Optional<Member>> getMember(UUID memberUUID) {
        return CompletableFuture.supplyAsync(() -> {
            return findByUUID(memberUUID, "player_uuid");
        });
    }

    @Override
    public CompletableFuture<Set<Member>> getAllGuildMembers(int guildID) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT * FROM " + getTableName() + " WHERE guild_id = ?";
            return findAll(query, guildID);
        });
    }


}
