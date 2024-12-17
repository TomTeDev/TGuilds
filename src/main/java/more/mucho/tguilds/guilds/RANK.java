package more.mucho.tguilds.guilds;

public enum RANK {
    OWNER(40),
    OFFICER(30),
    VETERAN(20),
    MEMBER(10);
    public final int power;
    RANK(int power){
        this.power = power;
    }
}
