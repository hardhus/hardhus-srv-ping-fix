package com.hardhus.srvpingfix;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

/** All the work is in {@code mixin/ServerStatusPingerMixin}; registers no blocks/items. */
@Mod(value = SrvPingFix.MOD_ID, dist = Dist.CLIENT)
public final class SrvPingFix {
    public static final String MOD_ID = "hardhus_srvpingfix";
}
