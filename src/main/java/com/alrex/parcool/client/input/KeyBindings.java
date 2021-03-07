package com.alrex.parcool.client.input;

import net.java.games.input.Controller;
import net.java.games.input.Keyboard;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyBindings {
    private static final GameSettings settings= Minecraft.getInstance().gameSettings;
    private static final KeyBinding keyBindCrawl=new KeyBinding("key.crawl.description", GLFW.GLFW_KEY_C ,"key.categories.movement");
    public static KeyBinding getKeySprint(){ return settings.keyBindSprint; }
    public static KeyBinding getKeyCrawl(){return keyBindCrawl;}

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event){
        ClientRegistry.registerKeyBinding(keyBindCrawl);
    }
}
