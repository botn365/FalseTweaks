/*
 * FalseTweaks
 *
 * Copyright (C) 2022 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice, this permission notice and the word "SNEED"
 * shall be included in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.falsepattern.falsetweaks.proxy;

import com.falsepattern.falsetweaks.Compat;
import com.falsepattern.falsetweaks.Share;
import com.falsepattern.falsetweaks.config.ModuleConfig;
import com.falsepattern.falsetweaks.modules.leakfix.LeakFix;
import com.falsepattern.falsetweaks.modules.renderlists.ItemRenderListManager;
import com.falsepattern.falsetweaks.modules.renderlists.VoxelRenderListManager;
import com.falsepattern.falsetweaks.modules.triangulator.calibration.Calibration;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.client.ClientCommandHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        if (LeakFix.ENABLED)
            LeakFix.registerBus();
        Share.LEAKFIX_CLASS_INITIALIZED = true;
        if (ModuleConfig.TRIANGULATOR)
            Calibration.registerBus();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        if (ModuleConfig.ITEM_RENDER_LISTS) {
            ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(
                    ItemRenderListManager.INSTANCE);
            if (ModuleConfig.VOXELIZER) {
                ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(
                        VoxelRenderListManager.INSTANCE);
            }
        }
        if (LeakFix.ENABLED) {
            LeakFix.gc();
        }
        
        if (ModuleConfig.TRIANGULATOR) {
            ClientCommandHandler.instance.registerCommand(new Calibration.CalibrationCommand());
            Compat.applyCompatibilityTweaks();
        }
    }
}
