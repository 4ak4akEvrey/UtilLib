package ak4ak.utillib.API.ChatActionBuilder;

import ak4ak.utillib.API.RandomTeleport.RandomSpawnBuilder;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatPromptExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        RandomSpawnBuilder.create()
                .world(player.getWorld())
                .center(player.getWorld().getSpawnLocation())
                .radius(100000)
                .biomes(Biome.BEACH)
                .spawn(player);



        if (args.length < 2) return true;

        String promptId = args[0];
        String buttonId = args[1];

        PromptManager.run(player, promptId, buttonId);

        return true;
    }
}
