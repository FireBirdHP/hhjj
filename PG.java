package eva.ware.modules.impl.misc;

import com.google.common.eventbus.Subscribe;
import eva.ware.events.EventEntityLeave;
import eva.ware.events.EventPacket;
import eva.ware.events.EventUpdate;
import eva.ware.modules.api.Category;
import eva.ware.modules.api.Module;
import eva.ware.modules.api.ModuleRegister;
import eva.ware.modules.settings.impl.BooleanSetting;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CConfirmTeleportPacket;
import net.minecraft.network.play.client.CResourcePackStatusPacket;
import net.minecraft.network.play.server.SSendResourcePackPacket;

@ModuleRegister(name = "PG", category = Category.Misc)
public class PG extends Module {
        private Timer confirmTimer = new Timer();
        private boolean teleported;

            super("PortalGodMode", Category.PLAYER);
            confirmTimer.setMs(99999);
        }

        @EventHandler
        public void onPacketSend(PacketEvent.Send e) {
            if (e.getPacket() instanceof TeleportConfirmC2SPacket && confirmTimer.getPassedTimeMs() < 5000) {
                teleported = true;
                e.cancel();
            }
        }

        @Override
        public void onDisable(){
            teleported = false;
        }

        @Override
        public void onUpdate() {
            for(int x = (int) (mc.player.getX() - 2); x < mc.player.getX() + 2; x++)
                for(int z = (int) (mc.player.getZ() - 2); z < mc.player.getZ() + 2; z++)
                    for(int y = (int) (mc.player.getY() - 2); y < mc.player.getY() + 2; y++)
                        if(mc.world.getBlockState(BlockPos.ofFloored(x,y,z)).getBlock() == Blocks.NETHER_PORTAL)
                            confirmTimer.reset();
        }

        @Override
        public String getDisplayInfo() {
            return teleported ? "God" : confirmTimer.getPassedTimeMs() < 5000 ? "Ready" : "Waiting";
        }
}