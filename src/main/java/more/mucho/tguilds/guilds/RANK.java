package more.mucho.tguilds.guilds;

public enum RANK {

    OWNER(40),
    OFFICER(30),
    VETERAN(20),
    MEMBER(10);
    public final int power;
    public static final int POWER_DIF = 10;
    RANK(int power){
        this.power = power;
    }
    public static RANK getNextDemoteRank(RANK rank){
        if(rank == RANK.OFFICER){
            return RANK.VETERAN;
        }
        if(rank == RANK.VETERAN){
            return RANK.MEMBER;
        }
        return null;
    }
    public static RANK getNextPromoteRank(RANK rank){
        if(rank == RANK.MEMBER){
            return RANK.VETERAN;
        }
        if(rank == RANK.VETERAN){
            return RANK.OFFICER;
        }
        return null;
    }
}
