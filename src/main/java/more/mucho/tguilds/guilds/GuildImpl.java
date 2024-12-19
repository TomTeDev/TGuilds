package more.mucho.tguilds.guilds;

import more.mucho.tguilds.storage.MembersDao;
import more.mucho.tguilds.storage.local.MembersRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class GuildImpl implements Guild {
    private int ID;
    private UUID guildUUID;
    private String name;
    private String tag;
    private final MembersRepository membersRepository;
    private Set<Member> membersCache;

    public GuildImpl(int ID, String name, String tag, UUID guildUUID, MembersRepository membersRepository) {
        this.ID = ID;
        this.name = name;
        this.tag = tag;
        this.guildUUID = guildUUID;
        this.membersRepository = membersRepository;
        this.membersCache = null; // Load lazily
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public UUID getUUID() {
        return guildUUID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Member getOwner() {
        for (Member member : getMembers()) {
            if (member.getRank() == RANK.OWNER) {
                return member;
            }
        }
        throw new IllegalStateException("Guild has no owner.");
    }

    @Override
    public Set<Member> getMembers() {
        if (membersCache == null) {
            membersCache = membersRepository.getAndLoadAllGuildMembers(ID).join();
        }
        return Collections.unmodifiableSet(membersCache);
    }

    @Override
    public boolean isMember(Member member) {
        return getMembers().contains(member);
    }

    @Override
    public Optional<Member> getMember(String name) {
        return getMembers().stream()
                .filter(member -> member.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public Optional<Member> getMember(UUID memberUUID) {
        return getMembers().stream()
                .filter(member -> member.getUUID().equals(memberUUID))
                .findFirst();
    }

    @Override
    public boolean addMember(Member member) {
        if (member.getRank() == RANK.OWNER) {
            throw new IllegalArgumentException("Cannot add another owner to the guild.");
        }
        return getMembers().add(member);
    }

    @Override
    public boolean removeMember(Member member) {
        if (member.getRank() == RANK.OWNER) {
            throw new IllegalArgumentException("Cannot remove the owner from the guild.");
        }
        return getMembers().remove(member);
    }

    @Override
    public boolean promoteMember(Member member, RANK newRank) {
        if (!getMembers().contains(member)) {
            throw new IllegalArgumentException("Member is not part of the guild.");
        }
        if (newRank.power <= member.getRank().power) {
            throw new IllegalArgumentException("New rank must have higher power than current rank.");
        }
        member.setRank(newRank);
        return true;
    }

    @Override
    public boolean demoteMember(Member member, RANK newRank) {
        if (!getMembers().contains(member)) {
            throw new IllegalArgumentException("Member is not part of the guild.");
        }
        if (newRank.power >= member.getRank().power) {
            throw new IllegalArgumentException("New rank must have lower power than current rank.");
        }
        member.setRank(newRank);
        return true;
    }

    @Override
    public String toString() {
        return "Guild{" +
                "name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", uuid=" + guildUUID.toString() +
                '}';
    }

    public static class MemberImpl implements Member {
        private int ID;
        private final String name;
        private final UUID uuid;
        private RANK rank;
        private int guildID;

        public MemberImpl(int ID, String name, UUID uuid, RANK rank, int guildID) {
            this.ID = ID;
            this.name = name;
            this.uuid = uuid;
            this.rank = rank;
            this.guildID = guildID;
        }

        @Override
        public int getID() {
            return ID;
        }

        @Override
        public void setID(int ID) {
            this.ID = ID;
        }

        @Override
        public int getGuildID() {
            return guildID;
        }

        @Override
        public void setGuildID(int guildID) {
            this.guildID = guildID;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public UUID getUUID() {
            return uuid;
        }

        @Override
        public RANK getRank() {
            return rank;
        }

        public void setRank(RANK rank) {
            this.rank = rank;
        }

        @Override
        public String toString() {
            return "Member{" +
                    "name='" + name + '\'' +
                    ", uuid=" + uuid +
                    ", rank=" + rank +
                    ", guildID=" + getGuildID() +
                    '}';
        }
    }
}
