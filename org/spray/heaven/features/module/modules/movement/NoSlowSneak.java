package org.spray.heaven.features.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "NoSlowSneak", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class NoSlowSneak extends Module {

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (mc.player.isSneaking()) {
            mc.player.jumpMovementFactor = 0.080F;
            if (mc.player.onGround && mc.gameSettings.keyBindJump.isKeyDown())
                mc.player.jump();
        }
    }
}
