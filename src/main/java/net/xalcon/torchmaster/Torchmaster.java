package net.xalcon.torchmaster;

import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xalcon.torchmaster.common.EntityFilterRegistry;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.commands.CommandTorchmaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Torchmaster.MODID)
public class Torchmaster
{
    public static final String MODID = "torchmaster";
    public static final Logger Log = LogManager.getLogger();

    public static MinecraftServer server;

    public static final EntityFilterRegistry MegaTorchFilterRegistry = new EntityFilterRegistry();
    public static final EntityFilterRegistry DreadLampFilterRegistry = new EntityFilterRegistry();

    public Torchmaster() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOWEST, this::postInit);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TorchmasterConfig.spec, "torchmaster.toml");
        //Log.info("Applying dread lamp entity block list overrides...");
        //DreadLampFilterRegistry.applyListOverrides(TorchmasterConfig.DreadLampEntityBlockListOverrides);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ModCaps.registerModCaps();
    }

    private void postInit(final FMLLoadCompleteEvent event)
    {
        Log.info("Applying mega torch entity block list overrides...");
        MegaTorchFilterRegistry.applyListOverrides(TorchmasterConfig.GENERAL.MegaTorchEntityBlockListOverrides.get().toArray(new String[0]));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        Log.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event)
    {
        server = event.getServer();
        CommandTorchmaster.register(server.getCommandManager().getDispatcher());
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        server = null;
    }
}