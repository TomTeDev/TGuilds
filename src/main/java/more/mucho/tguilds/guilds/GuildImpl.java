package more.mucho.tguilds.guilds;

import java.util.*;

public class GuildImpl implements Guild {
    private int ID;
    private String name;
    private String tag;
    private Member owner;
    private final Set<Member> members;

    public GuildImpl(String name, String tag, Member owner) {
        if (owner == null || owner.getRank() != RANK.OWNER) {
            throw new IllegalArgumentException("Owner must not be null and must have rank OWNER.");
        }
        this.name = name;
        this.tag = tag;
        this.owner = owner;
        this.members = new HashSet<>();
        this.members.add(owner);
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
        return owner;
    }

    @Override
    public Set<Member> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    @Override
    public Optional<Member> getMember(String name) {
        return members.stream()
                .filter(member -> member.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public Optional<Member> getMember(UUID memberUUID) {
        return members.stream()
                .filter(member -> member.getUUID().equals(memberUUID))
                .findFirst();
    }

    @Override
    public boolean addMember(Member member) {
        if (member.getRank() == RANK.OWNER) {
            throw new IllegalArgumentException("Cannot add another owner to the guild.");
        }
        return members.add(member);
    }

    @Override
    public boolean removeMember(Member member) {
        if (member.getRank() == RANK.OWNER) {
            throw new IllegalArgumentException("Cannot remove the owner from the guild.");
        }
        return members.remove(member);
    }

    @Override
    public boolean promoteMember(Member member, RANK newRank) {
        if (!members.contains(member)) {
            throw new IllegalArgumentException("Member is not part of the guild.");
        }
        if (newRank.power <= member.getRank().power) {
            throw new IllegalArgumentException("New rank must have higher power than current rank.");
        }
        member.setRank(newRank); // Casting to a concrete implementation of Member
        return true;
    }

    @Override
    public boolean demoteMember(Member member, RANK newRank) {
        if (!members.contains(member)) {
            throw new IllegalArgumentException("Member is not part of the guild.");
        }
        if (newRank.power >= member.getRank().power) {
            throw new IllegalArgumentException("New rank must have lower power than current rank.");
        }
        member.setRank(newRank); // Casting to a concrete implementation of Member
        return true;
    }

    @Override
    public String toString() {
        return "Guild{" +
                "name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", owner=" + owner.getName() +
                ", members=" + members.size() +
                '}';
    }

    // Example Member Implementation
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
