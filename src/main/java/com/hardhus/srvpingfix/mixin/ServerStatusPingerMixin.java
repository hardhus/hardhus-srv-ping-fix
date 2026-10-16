package com.hardhus.srvpingfix.mixin;

import java.net.InetSocketAddress;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.multiplayer.ServerStatusPinger;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * When joining, vanilla puts the SRV-resolved host into the handshake
 * ({@code ConnectScreen}: {@code inetsocketaddress.getHostName()}), but the server list
 * ping uses the host the player typed ({@code ServerStatusPinger}: {@code serveraddress.getHost()}).
 * Tunnels that route by handshake hostname (e.g. playit.gg) therefore reject the ping:
 * the server shows as offline with no MOTD/icon, while joining works.
 * This makes the ping use the resolved host and port, exactly like joining.
 */
@Mixin(ServerStatusPinger.class)
public abstract class ServerStatusPingerMixin {

    @WrapOperation(
            method = "pingServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/Connection;initiateServerboundStatusConnection(Ljava/lang/String;ILnet/minecraft/network/protocol/status/ClientStatusPacketListener;)V"))
    private void hardhus$useResolvedHost(Connection connection, String host, int port, ClientStatusPacketListener listener,
            Operation<Void> original, @Local InetSocketAddress resolved) {
        // getHostString: no reverse DNS lookup; returns the IP literal if the server was added by IP
        original.call(connection, resolved.getHostString(), resolved.getPort(), listener);
    }
}
