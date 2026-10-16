# Hardhus's SRV Ping Fix

Client-side NeoForge mod for **Minecraft 1.21.1** that makes the multiplayer server list work for servers behind hostname-routing tunnels, such as **playit.gg with your own domain**.

## The problem
You point `mc.example.com` at a playit.gg tunnel with an `_minecraft._tcp` SRV record. Then:

- **Joining works**, but
- the server list shows the server as **offline**: no MOTD, no icon, no player count.

That happens because vanilla handles the two connections differently:

| | Host sent in the handshake |
|---|---|
| Joining (`ConnectScreen`) | the **SRV target**, e.g. `xyz.tun.ply.gg` |
| Server list ping (`ServerStatusPinger`) | the **typed address**, e.g. `mc.example.com` |

playit.gg's free Minecraft Java tunnels only accept their own hostname, so the ping is rejected.

## The fix
A single mixin wraps `Connection#initiateServerboundStatusConnection` in `ServerStatusPinger#pingServer` and passes the resolved host and port, the same values vanilla uses when joining. Servers without an SRV record behave exactly as before. The mod registers no blocks or items.

## Install
Download the jar from [Releases](https://github.com/hardhus/hardhus-srv-ping-fix/releases) and put it in your `mods/` folder.

- Side: **client only**. Not needed on the server; on a dedicated server it does nothing.
- Requires: Minecraft 1.21.1, NeoForge 21.1+

## Build
```bash
./gradlew build    # -> build/libs/hardhus-srv-ping-fix-<version>.jar
```
Requires JDK 21.

## Releases
Bump `mod_version` in `gradle.properties` and push to `main`. CI builds the jar and publishes release `v<mod_version>`. If that release already exists, nothing is published.

## License
MIT
