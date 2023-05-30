/*
 * IdleFPS - Limit FPS & Render Distance when Minecraft is in the background
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

package io.armandukx.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
/**
 * Code was taken from ArmandukxSB under GNU Affero General Public License v3.0
 *
 * @author Armandukx
 * @link https://github.com/Armandukx/ArmandukxSB/blob/main/LICENSE
 */
public class GuiUtils {

	public static void applyGLScissors(double x, double y, double width, double height) {
		Minecraft mc = Minecraft.getMinecraft();
		double scaleFactor = new ScaledResolution(mc).getScaleFactor();
		GL11.glScissor(
			(int) (x * scaleFactor),
			(int) (mc.displayHeight - (y + height) * scaleFactor),
			(int) (width * scaleFactor),
			(int) (height * scaleFactor)
		);
	}

	public static void drawScrollbar(int x, int y, int x2, int y2) {
		Gui.drawRect(x, y, x2, y2, 0xff000000);
		Gui.drawRect(x, y, x + 1, y2, 0xff444444);
		Gui.drawRect(x2 - 1, y, x2, y2, 0xff444444);
		Gui.drawRect(x, y, x2, y + 1, 0xff444444);
		Gui.drawRect(x, y2 - 1, x2, y2, 0xff444444);
	}

	public static void enableGlScissors() {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}

	public static void disableGlScissors() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	public static void applyGl(Runnable insideGl) {
		GlStateManager.pushMatrix();
		insideGl.run();
		GlStateManager.popMatrix();
	}

	public static boolean isPointInRegion(int x, int y, int width, int height, int pointX, int pointY) {
		return pointX >= x && pointX < x + width && pointY >= y && pointY < y + height;
	}

	// GuiScreen#drawHorizontalLine is weird and increases endX by 1
	public static void drawHorizontalLine(int startX, int endX, int y, int color) {
		if (endX < startX) {
			int i = startX;
			startX = endX;
			endX = i;
		}

		GuiScreen.drawRect(startX, y, endX, y + 1, color);
	}

	// GuiScreen#drawVerticalLine is weird and increases startY by 1
	public static void drawVerticalLine(int x, int startY, int endY, int color) {
		if (endY < startY) {
			int i = startY;
			startY = endY;
			endY = i;
		}

		GuiScreen.drawRect(x, startY, x + 1, endY, color);
	}
}
