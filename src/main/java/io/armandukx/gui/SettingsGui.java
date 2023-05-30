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

package io.armandukx.gui;

import io.armandukx.IdleFPS;
import io.armandukx.config.ConfigHandler;
import io.armandukx.gui.component.ToggleButton;
import io.armandukx.utils.GuiUtils;
import io.armandukx.utils.structs.GuiPage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsGui extends GuiScreen {
	private final Map<Integer, GuiPage> pageMap = new HashMap<>();
	private final List<GuiTextField> textFields = new ArrayList<>();
	private final int scrollbarWidth = 10;
	private final int scrollbarHeight = 30;
	private int scrollbarClickOffset = 0;
	private int scrollbarY = 36;
	private boolean isDragging = false;
	private int newScrollIfInside;
	private int guiWidth;
	private int guiHeight;
	private int guiX;
	private int guiY;
	private int pageNum = 0;

	public SettingsGui() {
		this.pageMap.put(
				0,
				new GuiPage()
						.addButtons(
								() -> addButton(0,0, IdleFPS.config.bFpsToggle),
								() -> addButton(1,2, IdleFPS.config.bDistToggle)
						)
						.addTextFields(
								() -> addTextField(0, 1, IdleFPS.config.backgroundFps)
								//() -> addTextField(1, 3, IdleFPS.config.backgroundRenderDist)
						)
						.addSettings(
								() -> addSetting("Background FPS Toggle","To enable or disable the 'Background FPS' feature", 0),
								() -> addSetting("Background FPS","The limit for FPS when minecraft is in background", 1),

								() -> addSetting("Background Render Distance Toggle","Changes the render distance to 2 when MC in background", 2)
								//() -> addSetting("Background Render Distance","The limit for Render Distance when minecraft is in background", 3) Later Update (I cant figure out the bug)
						)
		);
	}

	@Override
	public void initGui() {
		initGui(true);
	}

	public void initGui(boolean save) {
		updateGuiSize();
		scrollbarY = 36;
		isDragging = false;

		initializeComponents(save);

		addCategory();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (newScrollIfInside >= 36 && GuiUtils.isPointInRegion(guiX, guiY, guiWidth, guiHeight, mouseX, mouseY)) {
			scrollbarY = newScrollIfInside;
			newScrollIfInside = 35;
			initializeComponents(true);
		}
		drawRect(0, 0, width, height, 0x7F000000);

		drawRect(guiX, guiY, guiX + guiWidth, guiY + guiHeight, 0xF2181c25);
		GuiUtils.applyGl(() -> {
			GlStateManager.scale(2, 2, 1);
			fontRendererObj.drawStringWithShadow("ArmandukxSB", guiX / 2F + 5, guiY / 2F + 5, 0xFF28709e);
			GuiUtils.drawHorizontalLine(guiX / 2 + 5, (guiX + guiWidth) / 2 - 5, guiY / 2 + 16, 0xFF28709e);
			GuiUtils.drawVerticalLine(guiX / 2 + 45, guiY / 2 + 16, (guiY + guiHeight) / 2 - 5, 0xFF28709e);
		});

		GuiUtils.enableGlScissors();
		GuiUtils.applyGLScissors(guiX, guiY + 36, guiWidth, guiHeight - 47);

		pageMap.get(pageNum).getSettings().forEach(Runnable::run);

		textFields.forEach(GuiTextField::drawTextBox);

		buttonList.forEach(guiButton -> guiButton.drawButton(mc, mouseX, mouseY));

		GuiUtils.disableGlScissors();

		if (needsScrollbar()) {
			int scrollbarX = guiX + (guiWidth - scrollbarWidth);
			GuiUtils.drawScrollbar(scrollbarX, guiY + scrollbarY, scrollbarX + scrollbarWidth, guiY + scrollbarY + scrollbarHeight);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {

		if (pageNum == 0) {
			switch (button.id) {
				case 0:
					IdleFPS.config.bFpsToggle = !IdleFPS.config.bFpsToggle;
					ConfigHandler.writeBooleanConfig("general", "bFpsToggle", IdleFPS.config.bFpsToggle);
					button.displayString = IdleFPS.config.bFpsToggle ? "On" : "Off";
					break;
				case 1:
					IdleFPS.config.bDistToggle = !IdleFPS.config.bDistToggle;
					ConfigHandler.writeBooleanConfig("general", "bDistToggle", IdleFPS.config.bDistToggle);
					button.displayString = IdleFPS.config.bDistToggle ? "On" : "Off";
					break;
			}
		}

		if (button.id == 100) {
			if (pageNum != 0) {
				onGuiClosed();
				pageNum = 0;
				initGui(false);
			}
		}
	}

	@Override
	public void onGuiClosed() {
		for (GuiTextField textField : textFields) {
			if (pageNum == 0) {
				if (textField.getId() == 0) {
					String backgroundFps = textField.getText();
					int fps = Integer.parseInt(backgroundFps);
					if (fps >= 1 && fps <= 100) {
						IdleFPS.config.backgroundFps = backgroundFps;
						ConfigHandler.writeStringConfig("general", "backgroundFps", IdleFPS.config.backgroundFps);
					}
						/*case 1:
						String backgroundDist = textField.getText();
						int distance = Integer.parseInt(backgroundDist);
						if (distance >= 2 && distance <= 32) {
							IdleFPS.config.backgroundRenderDist = backgroundDist;
							ConfigHandler.writeStringConfig("general", "backgroundDist", IdleFPS.config.backgroundRenderDist);
							break;
						}break;*/
				}
				break;
			}break;
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		textFields.forEach(textBox -> textBox.mouseClicked(mouseX, mouseY, mouseButton));

		if (mouseButton == 0 && needsScrollbar()) {
			if (
					GuiUtils.isPointInRegion(
							guiX + (guiWidth - scrollbarWidth),
							guiY + scrollbarY,
							scrollbarWidth,
							scrollbarHeight,
							mouseX,
							mouseY
					)
			) {
				isDragging = true;
				scrollbarClickOffset = mouseY - (guiY + scrollbarY);
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		textFields.forEach(GuiTextField::updateCursorCounter);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		textFields.forEach(textBox -> textBox.textboxKeyTyped(typedChar, keyCode));
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (state == 0) {
			isDragging = false;
		}
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (isDragging) {
			scrollbarY = MathHelper.clamp_int(mouseY - guiY - scrollbarClickOffset, 36, guiHeight - scrollbarHeight - 11);
			initializeComponents(true);
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();

		int dWheel = Mouse.getEventDWheel();
		if (needsScrollbar() && dWheel != 0) {
			dWheel = -Integer.signum(dWheel) * 10;
			newScrollIfInside = MathHelper.clamp_int(scrollbarY + dWheel, 36, guiHeight - scrollbarHeight - 11);
		}
	}
	private void addButton(int id, int count, boolean curVal) {
		int translateValue = count * 45 + calculateScrollTranslate();

		this.buttonList.add(new ToggleButton(id, guiX + guiWidth - 35, guiY + 37 + translateValue, curVal ? "On" : "Off"));
	}
	private void addTextField(int id, int count, String curVal) {
		int translateValue = count * 45 + calculateScrollTranslate();

		GuiTextField textBox = new GuiTextField(id, this.fontRendererObj, guiX + guiWidth - 101, guiY + 38 + translateValue, 90, 16);
		textBox.setText(curVal);
		this.textFields.add(textBox);
	}

	private void addSetting(String title, String description, int count) {
		int translateValue = count * 45 + calculateScrollTranslate();

		drawRect(guiX + 95, guiY + 36 + translateValue, (guiX + guiWidth) - 10, guiY + 76 + translateValue, 0xFF303b52);
		drawRect(guiX + 95, guiY + 36 + translateValue, (guiX + guiWidth) - 10, guiY + 56 + translateValue, 0xFF212939);

		fontRendererObj.drawString(title, guiX + 100, guiY + 41 + translateValue, 0xFFffffff);
		fontRendererObj.drawString(description, guiX + 100, guiY + 62 + translateValue, 0xFF808080);
	}

	private void addCategory() {
		this.buttonList.add(new ToggleButton(100, guiX + 15, guiY + 40, "General"));
	}

	private void updateGuiSize() {
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		guiWidth = Math.min(scaledResolution.getScaledWidth() - 100 / scaledResolution.getScaleFactor(), 600);
		guiHeight = Math.min(scaledResolution.getScaledHeight() - 100 / scaledResolution.getScaleFactor(), 400);
		guiX = (scaledResolution.getScaledWidth() - guiWidth) / 2;
		guiY = (scaledResolution.getScaledHeight() - guiHeight) / 2;
	}

	private boolean needsScrollbar() {
		double totalContent = (pageMap.get(pageNum).getSettings().size() - 1) * 90 + 80;
		double visibleHeight = guiHeight - 47;
		return totalContent > visibleHeight * 2;
	}

	private int calculateScrollTranslate() {
		if (!needsScrollbar()) {
			return 0;
		}

		double percentScroll = (scrollbarY - 36.0) / (guiHeight - scrollbarHeight - 47);
		double totalContent = (pageMap.get(pageNum).getSettings().size() - 1) * 90 + 80;
		double visibleHeight = guiHeight - 47;
		return (int) ((totalContent - 2 * visibleHeight) * percentScroll / -2);
	}

	private void initializeComponents(boolean save) {
		if (save) {
			onGuiClosed();
		}

		GuiPage curPage = pageMap.get(pageNum);

		this.buttonList.removeIf(button -> button.id < 100);
		for (Runnable buttonRunnable : curPage.getButtons()) {
			buttonRunnable.run();
		}

		this.textFields.clear();
		for (Runnable textFieldRunnable : curPage.getTextFields()) {
			textFieldRunnable.run();
		}
	}
}