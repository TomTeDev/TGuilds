package more.mucho.tguilds.commands;

import more.mucho.tguilds.guilds.Guild;
import more.mucho.tguilds.guilds.Member;
import more.mucho.tguilds.guilds.RANK;
import more.mucho.tguilds.storage.GuildFactory;
import more.mucho.tguilds.storage.MembersFactory;
import more.mucho.tguilds.storage.local.Repositories;
import more.mucho.tguilds.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GuildCommand extends AbstractCommand {
    public GuildCommand() {
        super("guild");
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length == 0) {
            sendMessage(sender, "command_info.try_help");
            return false;
        }
        if (args[0].equalsIgnoreCase("help")) {
            sendMessages(sender, "command_guild_help");
            return true;
        }
        Player player = (Player) sender;
        switch (args[0].toLowerCase()) {
            case "create" -> {
                return createGuild(player, args);
            }
            case "add" -> {
                return addMember(player, args);
            }
            case "join" -> {
                return join(player, args);
            }
            case "kick" -> {
                return kick(player, args);
            }
            case "leave" -> {
                return leave(player, args);
            }
            case "info" -> {
                return sendInfo(player, args);
            }
            case "chat" -> {
                return sendChatMessage(player, args);
            }
            case "sethome" -> {
                return setHome(player, args);
            }
            case "home" -> {
                return teleportHome(player, args);
            }
            case "setowner" -> {
                return setOwner(player, args);
            }
            case "promote" -> {
                return promote(player, args);
            }
            case "demote" -> {
                return demote(player, args);
            }
            case "delete" -> {
                return delete(player, args);
            }
            case "togglechat" -> {
                return toggleChat(player, args);
            }

        }

        return false;
    }

    private boolean createGuild(Player sender, String[] args) {
        // Validate arguments
        if (args.length != 2) {
            // Correct usage: guild create name TAG
            sendCorrectCommand(sender, "create");
            return false;
        }

        String name = args[0];
        String tag = args[1];

        // Validate guild name and tag
        if (!GuilUtils.isValidName(name)) {
            sendMessage(sender, "command_info.invalid_name",
                    new Tuple<>("%name_min_length%", String.valueOf(Config.NAME_MIN_LENGTH)),
                    new Tuple<>("%name_max_length%", String.valueOf(Config.NAME_MAX_LENGTH)));
            return false;
        }

        if (!GuilUtils.isValidTag(tag)) {
            sendMessage(sender, "command_info.invalid_tag",
                    new Tuple<>("%tag_min_length%", String.valueOf(Config.TAG_MIN_LENGTH)),
                    new Tuple<>("%tag_max_length%", String.valueOf(Config.TAG_MAX_LENGTH)));
            return false;
        }

        // Fetch member information
        Repositories.getInstance()
                .getMembersRepository()
                .getOrLoad(sender.getName())
                .thenCompose(member -> {
                    if (member.isPresent()) {
                        sendMessage(sender, "command_info.already_in_guild");
                        return CompletableFuture.completedFuture(false);
                    }

                    // Check if a guild with the given name already exists
                    return Repositories.getInstance()
                            .getGuildsRepository()
                            .getOrLoadByName(name)
                            .thenCompose(guildWithNameExist -> {
                                if (guildWithNameExist.isPresent()) {
                                    sendMessage(sender, "command_info.guild_with_name_exist");
                                    return CompletableFuture.completedFuture(false);
                                }

                                // Check if a guild with the given tag already exists
                                return Repositories.getInstance()
                                        .getGuildsRepository()
                                        .getOrLoadByTag(tag)
                                        .thenCompose(guildWithTagExist -> {
                                            if (guildWithTagExist.isPresent()) {
                                                sendMessage(sender, "command_info.guild_with_tag_exist");
                                                return CompletableFuture.completedFuture(false);
                                            }

                                            // Create and save the guild
                                            Guild guild = GuildFactory.create("default", name, tag, UUID.randomUUID(), Repositories.getInstance().getMembersRepository());
                                            return Repositories.getInstance()
                                                    .getGuildsRepository()
                                                    .saveGuild(guild)
                                                    .thenApply(saved -> {
                                                        if (!saved) {
                                                            sendMessage(sender, "command_info.guild_not_saved");
                                                            return false;
                                                        }
                                                        Member ownerMember = MembersFactory.createMember("default", sender.getName(), UUID.randomUUID(), RANK.OWNER, guild.getID());
                                                        Repositories.getInstance().getMembersRepository().save(ownerMember);
                                                        guild.addMember(ownerMember);
                                                        sendMessage(sender,
                                                                "command_info.guild_created",
                                                                new Tuple<>("%guild_name%", guild.getName()),
                                                                new Tuple<>("%guild_tag%", guild.getTag())
                                                        );
                                                        return true;
                                                    });
                                        });
                            });
                })
                .exceptionally(ex -> {
                    // Handle unexpected exceptions
                    sendMessage(sender, "command_info.unexpected_error");
                    ex.printStackTrace(); // Log the error for debugging
                    return false;
                });

        return true; // Indicate the process has started
    }

    private boolean addMember(Player player, String[] args) {
        if (args.length < 2) {
            sendCorrectCommand(player, "add");
            return false;
        }
        String targetName = args[1];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            sendMessage(player, "command_info.player_not_found");
            return false;
        }
        Optional<Member> senderMember = Repositories.getInstance().getMembersRepository().cache().get(player.getName());
        if (senderMember.isEmpty()) {
            sendMessage(player, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Guild> senderGuild = Repositories.getInstance().getGuildsRepository().cache().get(senderMember.get().getGuildID());
        if (senderGuild.isEmpty()) {
            sendMessage(player, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Member> targetMember = Repositories.getInstance().getMembersRepository().cache().get(targetName);
        if (targetMember.isPresent()) {
            sendMessage(player, "command_info.player_already_in_guild");
            return false;
        }
        Guild guild = senderGuild.get();
        if (guild.getInvitesHandler().isInvited(targetName)) {
            sendMessage(player, "command_info.player_already_invited");
            return false;
        }
        if (!guild.getPermissionsHandler().canInvite(senderMember.get())) {
            sendMessage(player, "command_info.insufficient_permissions");
            return false;
        }
        if (guild.getMembers().size() > Config.MAX_MEMBERS) {
            sendMessage(player, "command_info.guild_full");
            return false;
        }
        guild.getInvitesHandler().addInvited(targetName);
        sendMessage(player, "command_info.player_invited", new Tuple<>("%player_name%", target.getName()));
        sendMessage(target, "command_info.you_were_invited", new Tuple<>("%guild_name%", player.getName()));

        return true;
    }

    private boolean promote(Player sender, String[] args) {
        if (args.length < 2) {
            sendCorrectCommand(sender, "promote");
            return false;
        }
        Optional<Member> senderMember = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (senderMember.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Guild> senderGuild = Repositories.getInstance().getGuildsRepository().cache().get(senderMember.get().getGuildID());
        if (senderGuild.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        String targetName = args[1];
        Repositories.getInstance().getMembersRepository().getOrLoad(targetName).thenAccept(targetMember -> {
            if (targetMember.isEmpty()) {
                sendMessage(sender, "command_info.player_not_found");
                return;
            }
            if (targetMember.get().equals(senderMember.get())) {
                sendMessage(sender, "command_info.cant_use_it_on_yourself");
                return;
            }
            if (targetMember.get().getGuildID() != senderMember.get().getGuildID()) {
                sendMessage(sender, "command_info.not_member_of_your_guild");
                return;
            }
            if (targetMember.get().getRank() == RANK.VETERAN) {
                sendMessage(sender, "command_info.veteran_cant_be_promoted");
                return;
            }
            if (senderMember.get().getRank().power <= (targetMember.get().getRank().power + RANK.POWER_DIF)) {
                sendMessage(sender, "command_info.insufficient_permissions");
                return;
            }
            RANK nextRank = RANK.getNextPromoteRank(targetMember.get().getRank());
            if (nextRank == null) {
                sendMessage(sender, "command_info.unexpected_error");
                return;
            }

            targetMember.get().setRank(nextRank);

            Player targetPlayer = Bukkit.getPlayer(targetName);
            if (targetPlayer != null && targetPlayer.isOnline()) {
                sendMessage(targetPlayer, "command_info.you_were_promoted", new Tuple<>("%rank_name%", nextRank.name()));
            }
            sendMessage(sender, "command_info.you_have_promoted", new Tuple<>("%player_name%", targetName), new Tuple<>("%rank_name%", nextRank.name()));
        });
        return true;
    }

    private boolean demote(Player sender, String[] args) {
        if (args.length < 2) {
            sendCorrectCommand(sender, "demote");
            return false;
        }
        Optional<Member> senderMember = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (senderMember.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Guild> senderGuild = Repositories.getInstance().getGuildsRepository().cache().get(senderMember.get().getGuildID());
        if (senderGuild.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        String targetName = args[1];
        Repositories.getInstance().getMembersRepository().getOrLoad(targetName).thenAccept(targetMember -> {
            if (targetMember.isEmpty()) {
                sendMessage(sender, "command_info.player_not_found");
                return;
            }
            if (targetMember.get().equals(senderMember.get())) {
                sendMessage(sender, "command_info.cant_use_it_on_yourself");
                return;
            }
            if (targetMember.get().getGuildID() != senderMember.get().getGuildID()) {
                sendMessage(sender, "command_info.not_member_of_your_guild");
                return;
            }
            if (targetMember.get().getRank() == RANK.MEMBER) {
                sendMessage(sender, "command_info.member_cant_be_demoted");
                return;
            }
            if (senderMember.get().getRank().power <= (targetMember.get().getRank().power)) {
                sendMessage(sender, "command_info.insufficient_permissions");
                return;
            }
            RANK nextRank = RANK.getNextDemoteRank(targetMember.get().getRank());
            if (nextRank == null) {
                sendMessage(sender, "command_info.unexpected_error");
                return;
            }
            targetMember.get().setRank(nextRank);

            Player targetPlayer = Bukkit.getPlayer(targetName);
            if (targetPlayer != null && targetPlayer.isOnline()) {
                sendMessage(targetPlayer, "command_info.you_were_demoted", new Tuple<>("%rank_name%", nextRank.name()));
            }
            sendMessage(sender, "command_info.you_have_demoted", new Tuple<>("%player_name%", targetName), new Tuple<>("%rank_name%", nextRank.name()));
        });
        return true;
    }

    private boolean setOwner(Player sender, String[] args) {
        if (args.length < 2) {
            sendCorrectCommand(sender, "setowner");
            return false;
        }
        Optional<Member> senderMember = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (senderMember.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        if (senderMember.get().getRank() != RANK.OWNER) {
            sendMessage(sender, "command_info.insufficient_permissions");
            return false;
        }
        Optional<Guild> senderGuild = Repositories.getInstance().getGuildsRepository().cache().get(senderMember.get().getGuildID());
        if (senderGuild.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        String targetName = args[1];
        Repositories.getInstance().getMembersRepository().getOrLoad(targetName).thenAccept(targetMember -> {
            if (targetMember.isEmpty()) {
                sendMessage(sender, "command_info.player_not_found");
                return;
            }
            if (targetMember.get().equals(senderMember.get())) {
                sendMessage(sender, "command_info.cant_use_it_on_yourself");
                return;
            }
            if (targetMember.get().getGuildID() != senderMember.get().getGuildID()) {
                sendMessage(sender, "command_info.not_member_of_your_guild");
                return;
            }
            senderMember.get().setRank(RANK.OFFICER);
            targetMember.get().setRank(RANK.OWNER);

            Player targetPlayer = Bukkit.getPlayer(targetName);
            if (targetPlayer != null && targetPlayer.isOnline()) {
                sendMessage(targetPlayer, "command_info.you_are_new_owner");
            }
            sendMessage(sender, "command_info.owner_passed", new Tuple<>("%player_name%", targetName));
        });

        return true;
    }

    private boolean join(Player player, String[] args) {
        if (args.length < 2) {
            sendCorrectCommand(player, "join");
            return false;
        }
        String targetGuildName = args[1];

        Optional<Member> senderMember = Repositories.getInstance().getMembersRepository().cache().get(player.getName());
        if (senderMember.isPresent()) {
            sendMessage(player, "command_info.you_are_already_in_a_guild");
            return false;
        }
        Optional<Guild> targetGuild = Repositories.getInstance().getGuildsRepository().cache().getByName(targetGuildName);
        if (targetGuild.isEmpty()) {
            sendMessage(player, "command_info.unable_to_find_guild");
            return false;
        }

        Guild guild = targetGuild.get();
        if (!guild.getInvitesHandler().isInvited(player.getName())) {
            sendMessage(player, "command_info.you_are_not_invited");
            return false;
        }
        if (guild.getMembers().size() > Config.MAX_MEMBERS) {
            sendMessage(player, "command_info.guild_full", new Tuple<>("%max_members%", String.valueOf(Config.MAX_MEMBERS)));
            return false;
        }
        Member member = MembersFactory.createMember("default", player.getName(), player.getUniqueId(), RANK.MEMBER, guild.getID());
        Repositories.getInstance().getMembersRepository().save(member).thenAccept(saved -> {
            if (!saved) {
                sendMessage(player, "command_info.unable_to_save_member");
                return;
            }
            guild.getInvitesHandler().removeInvited(player.getName());
            guild.addMember(member);
            sendMessage(player, "command_info.you_joined_guild");
        });

        return true;
    }

    private boolean kick(Player sender, String[] args) {
        if (args.length < 2) {
            sendCorrectCommand(sender, "kick");
            return false;
        }
        Optional<Member> senderMember = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (senderMember.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Guild> senderGuild = Repositories.getInstance().getGuildsRepository().cache().get(senderMember.get().getGuildID());
        if (senderGuild.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }

        Optional<Member> targetMember = Repositories.getInstance().getMembersRepository().cache().get(args[1]);
        if (targetMember.isEmpty()) {
            sendMessage(sender, "command_info.not_member_of_your_guild");
            return false;
        }
        if (!senderGuild.get().isMember(targetMember.get())) {
            sendMessage(sender, "command_info.not_member_of_your_guild");
            return false;
        }
        if (!senderGuild.get().getPermissionsHandler().canKick(senderMember.get())) {
            sendMessage(sender, "command_info.insufficient_permissions");
            return false;
        }
        if (targetMember.get().getRank().power >= senderMember.get().getRank().power) {
            sendMessage(sender, "command_info.insufficient_permissions");
            return false;
        }
        senderGuild.get().removeMember(targetMember.get());
        Repositories.getInstance().getMembersRepository().delete(targetMember.get().getID());
        sendMessage(sender, "command_info.player_kicked", new Tuple<>("%player_name%", targetMember.get().getName()));
        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer != null && targetPlayer.isOnline()) {
            sendMessage(targetPlayer, "command_info.you_were_kicked", new Tuple<>("%player_name%", sender.getName()));
        }
        return true;
    }

    private boolean leave(Player sender, String[] args) {
        Optional<Member> senderMember = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (senderMember.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Guild> senderGuild = Repositories.getInstance().getGuildsRepository().cache().get(senderMember.get().getGuildID());
        if (senderGuild.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        if (senderMember.get().getRank() == RANK.OWNER) {
            sendMessage(sender, "command_info.owner_cannot_leave_guild");
            return false;
        }

        senderGuild.get().removeMember(senderMember.get());
        Repositories.getInstance().getMembersRepository().delete(senderMember.get().getID());
        sendMessage(sender, "command_info.you_left_guild");
        return true;
    }

    private boolean sendInfo(Player sender, String[] args) {
        if (args.length > 1 && !Config.allowViewOthers) return false;
        Optional<Member> member = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (member.isEmpty() && args.length < 2) {
            sendCorrectCommand(sender, "sendinfo");
            return false;
        }
        if (member.isPresent() && args.length < 2) {
            Optional<Guild> guild = Repositories.getInstance().getGuildsRepository().cache().get(member.get().getGuildID());
            if (guild.isPresent()) {
                sendGuildInfo(sender, guild.get());
                return true;
            }
            return false;
        }
        Repositories.getInstance().getGuildsRepository().getOrLoadByName(args[1]).thenAccept(guild -> {
            if (guild.isPresent()) {
                sendGuildInfo(sender, guild.get());
                return;
            }
            sendMessage(sender, "command_info.unable_to_find_guild");
        });
        return true;
    }

    private void sendGuildInfo(Player sender, Guild guild) {
        sender.sendMessage(ChatColor.RED + "Feature is not ready");
        //TODO fetch parse and send
    }

    private boolean sendChatMessage(Player sender, String[] args) {
        Optional<Member> member = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (member.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Guild> guild = Repositories.getInstance().getGuildsRepository().cache().get(member.get().getGuildID());
        if (guild.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        guild.get().sendMessage(member.get(), TextUtils.subString(args, 1, args.length - 1));
        return true;
    }

    private boolean setHome(Player sender, String[] args) {
        if (!Config.homesEnabled) return false;
        Optional<Member> member = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (member.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Guild> guild = Repositories.getInstance().getGuildsRepository().cache().get(member.get().getGuildID());
        if (guild.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        if (!guild.get().getPermissionsHandler().canSethome(member.get())) {
            sendMessage(sender, "command_info.insufficient_permissions");
            return false;
        }
        guild.get().setHome(sender.getLocation().clone());
        sendMessage(sender, "command_info.home_set");
        return true;
    }

    private boolean teleportHome(Player sender, String[] args) {
        if (!Config.homesEnabled) return false;
        Optional<Member> member = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (member.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Guild> guild = Repositories.getInstance().getGuildsRepository().cache().get(member.get().getGuildID());
        if (guild.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Location> homeLocation = guild.get().getHome();
        if (homeLocation.isEmpty()) {
            sendMessage(sender, "command_info.no_home_set");
            return false;
        }
        sender.teleport(homeLocation.get());
        sendMessage(sender, "command_info.home_teleported");
        return true;
    }

    private boolean delete(Player sender, String[] args) {
        Optional<Member> member = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (member.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        if (member.get().getRank() != RANK.OWNER) {
            sendMessage(sender, "command_info.insufficient_permissions");
            return false;
        }
        Optional<Guild> guild = Repositories.getInstance().getGuildsRepository().cache().get(member.get().getGuildID());
        if (guild.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Repositories.getInstance().getGuildsRepository().deleteGuild(guild.get().getID());
        sendMessage(sender, "command_info.guild_deleted");
        return true;
    }

    private boolean toggleChat(Player sender, String[] args) {
        Optional<Member> member = Repositories.getInstance().getMembersRepository().cache().get(sender.getName());
        if (member.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        Optional<Guild> guild = Repositories.getInstance().getGuildsRepository().cache().get(member.get().getGuildID());
        if (guild.isEmpty()) {
            sendMessage(sender, "command_info.you_are_not_in_a_guild");
            return false;
        }
        if (PlayerUtils.isChatToggled(sender)) {
            sendMessage(sender, "command_info.chat_disabled");
        } else {
            sendMessage(sender, "command_info.chat_enabled");
        }
        PlayerUtils.toggleChat(sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        return null;
    }
}
