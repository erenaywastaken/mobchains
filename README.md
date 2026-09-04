# Mob Chains

A Minecraft mod that adds a new item, the "Mob Chain".

This new item allows you to attach a sturdy leash made of chain to every mob including hostile mobs.

The Mob Chain can be very useful in several situations:
- Transporting hostile mobs like zombies for zoos or museums
- Transporting guardians to exploit their mining fatigue effect
- A strong (doesn't snap) alternative to the Lead in general

## Installation & Usage
The mod can be downloaded from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/mob-chains) or [Modrinth](https://modrinth.com/mod/mob-chains) (under review at the time of writing this).
You need to have these installed to use the mod:
- [Minecraft 26.2](https://www.minecraft.net/en-us/store/minecraft-deluxe-collection-pc)
- [Fabric Mod Loader](https://fabricmc.net/use/installer/)
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

After downloading and setting up all the necessary components you can drop the Mob Chains .jar file to your [.minecraft directory](https://minecraft.wiki/w/.minecraft)
or use your preferred launcher's mod installing functionality 

## Background
This mod was developed using Fabric loader and it's API. I used their Data Attachment API for attaching the chain data to mobs, the API handles syncing this data with clients and saving it to disk. The rendering code was based off the vanilla leash rendering code and chain model.
