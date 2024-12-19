package more.mucho.tguilds.storage.local;

import more.mucho.tguilds.guilds.Member;

import java.util.Optional;

public class MembersCache extends AbstractCache<Integer, Member> {

    public Optional<Member> get(String playerName) {
        return cachedMap.values()
                .stream()
                .filter(member -> member.getName().equalsIgnoreCase(playerName))
                .findFirst();
    }
}
