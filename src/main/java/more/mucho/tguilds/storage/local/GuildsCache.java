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
    public Optional<Guild> getByName(String guildName){
       return cachedMap.values().stream().filter(guild -> guild.getName().equalsIgnoreCase(guildName)).findFirst();
    }
    public Optional<Guild> getByTag(String tag){
       return cachedMap.values().stream().filter(guild -> guild.getTag().equalsIgnoreCase(tag)).findFirst();
    }
    public Optional<Guild> getPlayerGuild(String name) {
        for (Guild guild : cachedMap.values()) {
            boolean isMember = guild.getMembers().stream()
                    .anyMatch(member -> member.getName().equalsIgnoreCase(name));
            if (isMember) {
                return Optional.of(guild);
            }
        }
        return Optional.empty();
    }

}