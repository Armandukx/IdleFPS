package io.armandukx.idletweaks.event;

import io.armandukx.idletweaks.IdleTweaks;
import io.armandukx.idletweaks.command.IDTCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = IdleTweaks.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new IDTCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
