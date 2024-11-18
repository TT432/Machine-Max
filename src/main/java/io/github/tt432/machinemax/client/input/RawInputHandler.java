package io.github.tt432.machinemax.client.input;

import io.github.tt432.machinemax.MachineMax;
import io.github.tt432.machinemax.common.entity.entity.BasicEntity;
import io.github.tt432.machinemax.network.KeyInputMapping;
import io.github.tt432.machinemax.network.payload.MoveInputPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * 按键逻辑与发包
 *
 * @author 甜粽子
 */
@EventBusSubscriber(modid = MachineMax.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
@OnlyIn(Dist.CLIENT)
public class RawInputHandler {

    static byte[] moveInputs = new byte[6];//x,y,z方向的平移和绕x,y,z轴的旋转输入
    static byte[] moveInputConflicts = new byte[6];//相应轴向上的输入冲突
    static int id = 0;

    static int trans_x_input = 0;
    static int trans_y_input = 0;
    static int trans_z_input = 0;
    static int rot_x_input = 0;
    static int rot_y_input = 0;
    static int rot_z_input = 0;

    /**
     * 在每个客户端tick事件后调用，处理按键逻辑。
     *
     * @param event 客户端tick事件对象
     */
    @SubscribeEvent
    public static void handleMoveInputs(ClientTickEvent.Post event) {
        var client = Minecraft.getInstance();

        if (client.player != null && client.player.getVehicle() instanceof BasicEntity e) {
            id = e.getId();
            int trans_x_conflict = 0;
            int trans_y_conflict = 0;
            int trans_z_conflict = 0;
            int rot_x_conflict = 0;
            int rot_y_conflict = 0;
            int rot_z_conflict = 0;
            switch (e.getMode()) {
                case GROUND:
                    //移动
                    if (KeyBinding.groundForwardKey.isDown() && KeyBinding.groundBackWardKey.isDown()) {
                        trans_z_conflict = 1;
                    }
                    else if (KeyBinding.groundForwardKey.isDown()) trans_z_input = 100;
                    else if (KeyBinding.groundBackWardKey.isDown()) trans_z_input = -100;
                    else trans_z_input = 0;
                    //转向
                    if (KeyBinding.groundLeftwardKey.isDown() && KeyBinding.groundRightwardKey.isDown()) {
                        rot_y_conflict = 1;
                    }
                    else if (KeyBinding.groundLeftwardKey.isDown()) rot_y_input = 100;
                    else if (KeyBinding.groundRightwardKey.isDown()) rot_y_input = -100;
                    else rot_y_input = 0;
                    break;
                case SHIP:
                    break;
                case PLANE:
                    //TODO:键盘输入的优先级应当高于视角朝向
                    break;
                case MECH:
                    break;
                default:
                    break;
            }

            moveInputs = new byte[]{
                    (byte) (trans_x_input),
                    (byte) (trans_y_input),
                    (byte) (trans_z_input),
                    (byte) (rot_x_input),
                    (byte) (rot_y_input),
                    (byte) (rot_z_input)};
            moveInputConflicts = new byte[]{
                    (byte) (trans_x_conflict),
                    (byte) (trans_y_conflict),
                    (byte) (trans_z_conflict),
                    (byte) (rot_x_conflict),
                    (byte) (rot_y_conflict),
                    (byte) (rot_z_conflict)};
            PacketDistributor.sendToServer(new MoveInputPayload(id, moveInputs, moveInputConflicts));
        }
    }

    @SubscribeEvent
    public static void handleAimInputs(ClientTickEvent.Post event) {
        if (KeyBinding.generalFreeCamKey.isDown()) {
            //TODO:自由视角键未被按下时，根据相机镜头角度发包视线输入
        }
    }
}


