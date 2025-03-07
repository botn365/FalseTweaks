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

package com.falsepattern.falsetweaks.mixin.mixins.client.triangulator.chromaticraft;

import Reika.ChromatiCraft.Render.ISBRH.RuneRenderer;
import com.falsepattern.falsetweaks.modules.triangulator.interfaces.IRenderBlocksMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

@Mixin(value = RuneRenderer.class,
       remap = false)
public abstract class RuneRendererMixin {
    @Inject(method = "renderInventoryBlock",
            at = @At(value = "INVOKE",
                     target = "LReika/ChromatiCraft/Registry/CrystalElement;getFaceRune()Lnet/minecraft/util/IIcon;"),
            require = 1)
    private void enableReuse(Block b, int metadata, int modelId, RenderBlocks rb, CallbackInfo ci) {
        ((IRenderBlocksMixin) rb).reusePreviousStates(true);
    }

    @Inject(method = "renderWorldBlock",
            at = @At(value = "INVOKE",
                     target = "LReika/ChromatiCraft/Registry/CrystalElement;getFaceRune()Lnet/minecraft/util/IIcon;"),
            require = 1)
    private void enableReuse(IBlockAccess world, int x, int y, int z, Block b, int modelId, RenderBlocks rb, CallbackInfoReturnable<Boolean> cir) {
        ((IRenderBlocksMixin) rb).reusePreviousStates(true);
    }

    @Inject(method = "renderInventoryBlock",
            at = @At(value = "RETURN"),
            require = 1)
    private void disableReuse(Block b, int metadata, int modelId, RenderBlocks rb, CallbackInfo ci) {
        ((IRenderBlocksMixin) rb).reusePreviousStates(false);
    }

    @Inject(method = "renderWorldBlock",
            at = @At(value = "RETURN"),
            require = 1)
    private void disableReuse(IBlockAccess world, int x, int y, int z, Block b, int modelId, RenderBlocks rb, CallbackInfoReturnable<Boolean> cir) {
        ((IRenderBlocksMixin) rb).reusePreviousStates(false);
    }
}
