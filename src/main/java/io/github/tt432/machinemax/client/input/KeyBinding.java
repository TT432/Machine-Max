package io.github.tt432.machinemax.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.tt432.machinemax.MachineMax;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = MachineMax.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class KeyBinding {

    //本地化用的按键资源路径
    public static final String FREE_CAM_KEY = "key.machine_max.ground.free_cam";
    public static final String ABANDON_KEY = "key.machine_max.ground.abandon";

    public static final String GROUND_FORWARD_KEY = "key.machine_max.ground.forward";
    public static final String GROUND_BACKWARD_KEY = "key.machine_max.ground.backward";
    public static final String GROUND_LEFTWARD_KEY = "key.machine_max.ground.leftward";
    public static final String GROUND_RIGHTWARD_KEY = "key.machine_max.ground.rightward";

    /**
     * 在此注册所有按键
     */
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        //通用按键
        event.register(KeyBinding.generalFreeCamKey);//自由视角
        event.register(KeyBinding.generalAbandonKey);//放弃载具
        //地面载具
        event.register(KeyBinding.groundForwardKey);//地面前进
        event.register(KeyBinding.groundBackWardKey);//地面后退
        event.register(KeyBinding.groundLeftwardKey);//地面载具左转
        event.register(KeyBinding.groundRightwardKey);//地面载具右转
        //船只

        //飞行器

        //机甲

    }

    public static KeyMapping generalFreeCamKey = new KeyMapping(FREE_CAM_KEY,//键位名称
            KeyCategory.GENERAL,//键位冲突类型
            InputConstants.Type.KEYSYM,//默认为键盘
            GLFW.GLFW_KEY_C,//默认按键
            KeyCategory.GENERAL.getCategory()//键位类型
    );
    public static KeyMapping generalAbandonKey = new KeyMapping(ABANDON_KEY,//键位名称
            KeyCategory.GENERAL,//键位冲突类型
            InputConstants.Type.KEYSYM,//默认为键盘
            GLFW.GLFW_KEY_J,//默认按键
            KeyCategory.GENERAL.getCategory()//键位类型
    );

    public static KeyMapping groundForwardKey = new KeyMapping(GROUND_FORWARD_KEY,//键位名称
            KeyCategory.GROUND,//键位冲突类型
            InputConstants.Type.KEYSYM,//默认为键盘
            GLFW.GLFW_KEY_W,//默认按键
            KeyCategory.GROUND.getCategory()//键位类型
    );
    public static KeyMapping groundBackWardKey = new KeyMapping(GROUND_BACKWARD_KEY,//键位名称
            KeyCategory.GROUND,//键位冲突类型
            InputConstants.Type.KEYSYM,//默认为键盘
            GLFW.GLFW_KEY_S,//默认按键
            KeyCategory.GROUND.getCategory()//键位类型
    );
    public static KeyMapping groundLeftwardKey = new KeyMapping(GROUND_LEFTWARD_KEY,//键位名称
            KeyCategory.GROUND,//键位冲突类型
            InputConstants.Type.KEYSYM,//默认为键盘
            GLFW.GLFW_KEY_A,//默认按键
            KeyCategory.GROUND.getCategory()//键位类型
    );
    public static KeyMapping groundRightwardKey = new KeyMapping(GROUND_RIGHTWARD_KEY,//键位名称
            KeyCategory.GROUND,//键位冲突类型
            InputConstants.Type.KEYSYM,//默认为键盘
            GLFW.GLFW_KEY_D,//默认按键
            KeyCategory.GROUND.getCategory()//键位类型
    );

}