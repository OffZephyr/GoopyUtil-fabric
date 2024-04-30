package net.zephyr.goopyutil.blocks.computer;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.GoopyBlockEntity;
import net.zephyr.goopyutil.init.BlockEntityInit;
import net.zephyr.goopyutil.util.Computer.ComputerApp;

import java.util.Objects;

public class ComputerBlockEntity extends GoopyBlockEntity {
    String currentApp = "";
    public ComputerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COMPUTER, pos, state);
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState state, GoopyBlockEntity entity) {
        for(ComputerApp app : ComputerData.getApps()){
            if(!Objects.equals(currentApp, this.getCustomData().getString("Window"))) {
                if(Objects.equals(app.getName(), this.getCustomData().getString("Window"))){
                    app.init();
                    this.getCustomData().putString("Window", app.getName());
                    currentApp = app.getName();
                }
            }
            if(Objects.equals(this.getCustomData().getString("Window"), app.getName())){
                app.tickWhenOpen();
            }

            app.tickAlways();
        }
        super.tick(world, blockPos, state, entity);
    }
}
