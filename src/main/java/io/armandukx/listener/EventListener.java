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

package io.armandukx.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
/**
 * Code was taken from ArmandukxSB under GNU Affero General Public License v3.0
 *
 * @author Armandukx
 * @link https://github.com/Armandukx/ArmandukxSB/blob/main/LICENSE
 */
public class EventListener {
	private GuiScreen guiToOpen;

	@SubscribeEvent
	public void onTick(TickEvent.RenderTickEvent event) {
		if (guiToOpen != null) {
			Minecraft.getMinecraft().displayGuiScreen(guiToOpen);
			guiToOpen = null;
		}
	}
	public void setGuiToOpen(GuiScreen guiToOpen) {
		this.guiToOpen = guiToOpen;
	}
}
