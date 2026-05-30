package ru.spliterash.musicbox;

import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.spliterash.musicbox.customPlayers.abstracts.AbstractBlockPlayer;
import ru.spliterash.musicbox.customPlayers.interfaces.MusicBoxSongPlayer;
import ru.spliterash.musicbox.customPlayers.objects.jukebox.JukeboxPlayer;
import ru.spliterash.musicbox.players.PlayerWrapper;

import java.util.Set;

public class Handler implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onExit(PlayerQuitEvent e) {
        PlayerWrapper
                .getInstanceOptional(e.getPlayer())
                .ifPresent(PlayerWrapper::destroy);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDie(PlayerDeathEvent e) {
        PlayerWrapper
                .getInstanceOptional(e.getEntity())
                .ifPresent(PlayerWrapper::destroyActivePlayer);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSongEnd(SongEndEvent e) {
        if (e.getSongPlayer() instanceof MusicBoxSongPlayer) {
            ((MusicBoxSongPlayer) e.getSongPlayer()).onSongEnd();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent e) {
        @NotNull Chunk chunk = e.getChunk();
        Set<? extends AbstractBlockPlayer> playersInChunk = AbstractBlockPlayer.findByChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
        for (AbstractBlockPlayer player : playersInChunk) {
            player.destroy();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        if (!MusicBox.getInstance().isLoaded()) {
            return;
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        if (e.getClickedBlock() == null)
            return;
        if (e.getHand() != EquipmentSlot.HAND)
            return;
        Block b = e.getClickedBlock();
        if (b.getState() instanceof Jukebox jukebox) {
            ItemStack item = e.getItem();
            if (e.getPlayer().isSneaking()) {
                if (item == null) {
                    JukeboxPlayer.onSneakingClick(jukebox, e.getPlayer());
                }
            } else {
                jukebox.eject();
                JukeboxPlayer.onJukeboxClick(jukebox, item, e);
            }
        }
    }
}
