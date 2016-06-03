/*
 ** 2012 March 31
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.minema.client.modules;

import info.ata4.minecraft.minema.client.config.MinemaConfig;
import info.ata4.minecraft.minema.client.event.FrameImportEvent;
import static info.ata4.minecraft.minema.client.modules.CaptureSession.L;
import info.ata4.minecraft.minema.client.util.CaptureTime;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Minema information screen overlay.
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class CaptureOverlay extends CaptureModule {

    private static final Minecraft MC = Minecraft.getMinecraft();

    private CaptureTime time;

    public CaptureOverlay(MinemaConfig cfg) {
        super(cfg);
    }
    
    @SubscribeEvent
    public void onFrameImport(FrameImportEvent evt) {
        time = evt.time;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text evt) {
        if (time == null) {
            return;
        }
        
        ArrayList<String> left = evt.getLeft();

        if (MC.gameSettings.showDebugInfo) {
            // F3 menu is open -> add spacer
            left.add("");
        }

        String frame = String.valueOf(time.getNumFrames());
        left.add("Frame: " + frame);

        String fps = Minecraft.getDebugFPS() + " fps";
        left.add("Rate: " + fps);

        String avg = (int) time.getAverageFPS() + " fps";
        left.add("Avg.: " + avg);

        String delay = CaptureTime.getTimeUnit(time.getPreviousCaptureTime());
        left.add("Delay: " + delay);

        left.add("Time R: " + time.getRealTimeString());
        left.add("Time V: " + time.getVideoTimeString());
    }

    @Override
    protected void doEnable() throws Exception {
        MinecraftForge.EVENT_BUS.register(this);
        
        playChickenPlop();
    }

    @Override
    protected void doDisable() throws Exception {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
    
    private void playChickenPlop() {
        try {
            MC.theWorld.playSound(MC.thePlayer, MC.thePlayer.playerLocation,
                    SoundEvents.entity_chicken_egg, SoundCategory.NEUTRAL, 1, 1);
        } catch (Exception e) {
            L.error("cannot play chicken plop", e);
        }
    }
}
