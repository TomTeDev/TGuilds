package more.mucho.tguilds.storage.local;

import more.mucho.tguilds.guilds.Guild;
import more.mucho.tguilds.guilds.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class GuildsCache extends AbstractCache<Integer,Guild>{

    public Optional<Guild> getByMember(Member member){
        return Optional.ofNullable(cachedMap.get(member.getGuildID()));
    }

    public List<Guild> getAllCachedGuilds() {
        return new ArrayList<>(cachedMap.values());
    }
}