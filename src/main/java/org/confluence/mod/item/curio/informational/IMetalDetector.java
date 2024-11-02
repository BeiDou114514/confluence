package org.confluence.mod.item.curio.informational;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.confluence.mod.misc.ModConfigs;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

public interface IMetalDetector {
    static Component getInfo(Player localPlayer) {
        AtomicReference<Component> atomic = new AtomicReference<>(Component.translatable("info.confluence.metal_detector.none"));
        Object2IntMap<BlockState> cached = new Object2IntOpenHashMap<>();
        localPlayer.level().getBlockStates(new AABB(localPlayer.getOnPos()).inflate(15.5)).forEach(blockState -> {
            if (cached.containsKey(blockState)) return;
            for (int i = 0; i < ModConfigs.rareBlocks.size(); i++) {
                if (ModConfigs.rareBlocks.get(i).test(blockState)) {
                    cached.put(blockState, i);
                }
            }
        });
        cached.object2IntEntrySet().stream().min(Comparator.comparingInt(Object2IntMap.Entry::getIntValue))
                .ifPresent(entry -> atomic.set(Component.translatable("info.confluence.metal_detector", entry.getKey().getBlock().getName())));
        return atomic.get();
    }

    Component TOOLTIP = Component.translatable("curios.tooltip.metal_detector");
    byte INDEX = 4;
}
