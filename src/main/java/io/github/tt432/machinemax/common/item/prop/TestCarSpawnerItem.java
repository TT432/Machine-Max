package io.github.tt432.machinemax.common.item.prop;

import io.github.tt432.machinemax.common.entity.MMEntities;
import io.github.tt432.machinemax.common.entity.entity.TestCarEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TestCarSpawnerItem extends Item {
    public TestCarSpawnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(!level.isClientSide()){
            TestCarEntity car = new TestCarEntity(MMEntities.TEST_CAR_ENTITY.get(),level);
            car.setPos(player.getPosition(0));
            level.addFreshEntity(car);
        }
        return super.use(level, player, usedHand);
    }
}
