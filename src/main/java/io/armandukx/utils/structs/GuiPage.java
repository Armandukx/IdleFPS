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

package io.armandukx.utils.structs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiPage {

	private final List<Runnable> buttons = new ArrayList<>();
	private final List<Runnable> textFields = new ArrayList<>();
	private final List<Runnable> settings = new ArrayList<>();

	public GuiPage addButtons(Runnable... buttons) {
		this.buttons.addAll(Arrays.asList(buttons));
		return this;
	}

	public List<Runnable> getButtons() {
		return buttons;
	}

	public GuiPage addTextFields(Runnable... textFields) {
		this.textFields.addAll(Arrays.asList(textFields));
		return this;
	}

	public List<Runnable> getTextFields() {
		return textFields;
	}

	public GuiPage addSettings(Runnable... settings) {
		this.settings.addAll(Arrays.asList(settings));
		return this;
	}

	public List<Runnable> getSettings() {
		return settings;
	}
}
