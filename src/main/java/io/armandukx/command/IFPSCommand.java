/*
 * IdleTweaks - Enhances performance while Minecraft runs in the background
 * Copyright (c) 2023 Armandukx
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.armandukx.command;

import io.armandukx.IdleTweaks;
import io.armandukx.gui.SettingsGui;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.List;

import static io.armandukx.utils.Utils.executor;

@SuppressWarnings("ALL")
public class IFPSCommand extends CommandBase {
    @Override
    public String getCommandName() {return "ifps";}

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("ifps:settings", "ifps:config");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {return "/ifps";}

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        final EntityPlayer player = (EntityPlayer) sender;
        if (args.length == 0){
            executor.submit(() -> IdleTweaks.instance.getEventListener().setGuiToOpen(new SettingsGui()));
        }
    }   public boolean canCommandSenderUseCommand(final ICommandSender sender) {return true;}
}