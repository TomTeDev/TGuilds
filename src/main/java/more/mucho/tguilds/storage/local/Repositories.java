package more.mucho.tguilds.storage.local;

import more.mucho.tguilds.storage.GuildsDaoImpl;
import more.mucho.tguilds.storage.MembersDaoImpl;

import javax.sql.DataSource;

public class Repositories {

    private static Repositories instance = null;
    private Repositories(){

    }
    public static Repositories getInstance(){
        if(instance == null){
            instance = new Repositories();
        }
        return instance;
    }
    private GuildsRepository guildsRepository;
    private MembersRepository membersRepository;
    public void init(DataSource dataSource){
        guildsRepository = new GuildsRepositoryImpl(new GuildsDaoImpl(dataSource),new GuildsCache());
        membersRepository = new MembersRepositoryImpl(new MembersDaoImpl(dataSource),new MembersCache());
    }


    public MembersRepository getMembersRepository() {
        return membersRepository;
    }
    public GuildsRepository getGuildsRepository(){
        return guildsRepository;
    }



}
