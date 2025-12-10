package nicotine.mod.mods.misc;

import ca.weblite.objc.NSObject;
import ca.weblite.objc.Proxy;
import ca.weblite.objc.RuntimeUtils;
import ca.weblite.objc.annotations.Msg;
import com.sun.jna.Pointer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.command.commands.TouchBarCustom;
import nicotine.events.ClientTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import org.apache.commons.io.FileUtils;
import org.joml.Vector2d;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import static nicotine.util.Common.*;

public class TouchBar {

    public static class TouchBarButton {
        public String identifier;
        public Pointer item;
        public boolean visible;

        public TouchBarButton(String identifier, Pointer item, boolean visible) {
            this.identifier = identifier;
            this.item = item;
            this.visible = visible;
        }
    }

    public static final List<TouchBarButton> buttons = new ArrayList<>();

    private static final HashMap<String, byte[]> touchBarImages = new HashMap<>();
    private static final List<byte[]> customImageFrames = new ArrayList<>();
    private static int customImageCurFrame = 0;
    private static long customSize = 0;

    private static long elapsedTime = System.currentTimeMillis();

    private static class ButtonClickHandler extends NSObject {
        String executeString;

        public ButtonClickHandler(String executeString) {
            super();
            this.init("NSObject");
            this.executeString = executeString;
        }

        @Msg(selector = "clicked", signature = "v@:@")
        public void buttonClicked(Proxy sender) {
            Mod mod = ModManager.getMod(executeString);

            if (mod != null) {
                mod.toggle();
            } else {
                String[] command = Arrays.stream(executeString.split(" ")).toArray(String[]::new);

                for (Command cmd : CommandManager.commands) {
                    if (cmd.name.equals(command[0])) {
                        cmd.trigger(command);
                        break;
                    }
                }
            }
        }
    }

    private static void getCustomButtons(boolean visible) {
        for (String btnName : TouchBarCustom.customTouchBarItems.keySet()) {
            createButton(btnName, btnName, touchBarImages.get("item-redstone.png"), true, false, TouchBarCustom.customTouchBarItems.get(btnName));
            setButtonVisibility(btnName, visible);
        }
    }

    private static String getCoordsButton() {
        String coordsText = "0 0 0 [0 0]";

        if (mc.player != null) {
            Vec3 pos = mc.player.position();

            Vector2d otherWorld = new Vector2d(pos.x, pos.z);
            if (!mc.level.dimension().equals(Level.END)) {

                if (mc.level.dimension().equals(Level.NETHER)) {
                    otherWorld.mul(8);
                } else
                    otherWorld.div(8);
            }

            coordsText = String.format("%.1f %.1f %.1f [%.1f %.1f]", pos.x, pos.y, pos.z, otherWorld.x, otherWorld.y);
        }

        return createButton("pos", coordsText, touchBarImages.get("item-compass_01.png"), true, false, null);
    }

    private static String getTotemButton() {
        int totemCount = 0;

        if (mc.player != null) {
            for (int i = 0; i <= 45; i++) {
                ItemStack itemStack = mc.player.getInventory().getItem(i);

                if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
                    totemCount++;
                }
            }
        }

       return createButton("totem", String.valueOf(totemCount), touchBarImages.get("item-totem_of_undying.png"),  true, false, null);
    }

    private static String getECrystalButton() {
        int eCrystalCount = 0;

        if (mc.player != null) {
            for (int i = 0; i <= 45; i++) {
                ItemStack itemStack = mc.player.getInventory().getItem(i);

                if (itemStack.getItem() == Items.END_CRYSTAL) {
                    eCrystalCount += itemStack.getCount();
                }
            }
        }

        return createButton("eCrystal", String.valueOf(eCrystalCount), touchBarImages.get("item-end_crystal.png"), true, false, null);
    }

    private static String getServerButton() {

        String address = "singleplayer";
        byte[] favicon = new byte[0];

        if (mc.player == null)
            address = "main menu";
        else if (mc.isSingleplayer())
            address = "single player";
        else if (currentServer != null) {
            address = currentServer.ip;
            favicon = currentServer.getIconBytes();
        }

        return createButton("server", address, favicon, true, true, null);
    }

    private static String getCustomGIFButton() {
        if (customImageFrames.size() == customImageCurFrame + 1) {
            customImageCurFrame = 0;
        } else {
            customImageCurFrame++;
        }

        return createButton("custom", "", customImageFrames.get(customImageCurFrame), false, true, null);
    }

    private static void setButtonTitle(Pointer button, String title) {
        Pointer titleStr = RuntimeUtils.msgPointer("NSString", "stringWithUTF8String:", title);
        RuntimeUtils.msg(button, "setTitle:", titleStr);
    }

    private static void setButtonImage(Pointer button, byte[] img, boolean adjustImgPosition, boolean adjustImageScaling) {
        if (img == null)
            return;

        Pointer NSData = RuntimeUtils.cls("NSData");
        Pointer imageData = RuntimeUtils.msgPointer(NSData, "dataWithBytes:length:", img, img.length);

        Pointer NSImage = RuntimeUtils.cls("NSImage");
        Pointer allocImage = RuntimeUtils.msgPointer(NSImage, "alloc");

        Pointer image = RuntimeUtils.msgPointer(allocImage, "initWithData:", imageData);
        RuntimeUtils.msg(button, "setImage:", image);

        if (adjustImgPosition)
            RuntimeUtils.msg(button, "setImagePosition:", 2);

        if (adjustImageScaling)
            RuntimeUtils.msg(button, "setImageScaling:", 3);
    }

    private static void setButtonVisibility(String identifier, boolean visibility) {
       for (TouchBarButton touchBarButton : buttons) {
           if (identifier.equals(touchBarButton.identifier)) {
               if (touchBarButton.visible == visibility)
                   return;

               touchBarButton.visible = visibility;
               setTouchBar();

               return;
           }
       }
    }

    public static String createButton(String identifier, String title, byte[] img, boolean adjustImgPosition, boolean adjustImageScaling, String executeString) {
        for (TouchBarButton touchBarButton : buttons) {
            if (touchBarButton.identifier.equals(identifier)) {
                Pointer button = RuntimeUtils.msgPointer(touchBarButton.item, "view");
                setButtonTitle(button, title);
                setButtonImage(button, img, adjustImgPosition, adjustImageScaling);
                return identifier;
            }
        }

        Pointer NSCustomTouchBarItem = RuntimeUtils.cls("NSCustomTouchBarItem");
        Pointer alloc = RuntimeUtils.msgPointer(NSCustomTouchBarItem, "alloc");
        Pointer customItem = RuntimeUtils.msgPointer(alloc, "initWithIdentifier:", RuntimeUtils.str(identifier));

        Pointer NSButtonClass = RuntimeUtils.cls("NSButton");
        Pointer allocButton = RuntimeUtils.msgPointer(NSButtonClass, "alloc");
        Pointer button = RuntimeUtils.msgPointer(allocButton, "init");

        setButtonTitle(button, title);
        setButtonImage(button, img, adjustImgPosition, adjustImageScaling);

        if (executeString != null) {
            ButtonClickHandler handler = new ButtonClickHandler(executeString);
            Pointer target = handler.getPeer();

            RuntimeUtils.msg(button, "setTarget:", target);
            RuntimeUtils.msg(button, "setAction:", RuntimeUtils.sel("clicked"));
        }

        RuntimeUtils.msg(customItem, "setView:", button);

        buttons.add(new TouchBarButton(identifier, customItem, true));

        return identifier;
    }

    public static void setTouchBar() {
        Pointer NSTouchBarClass = RuntimeUtils.cls("NSTouchBar");
        Pointer alloc = RuntimeUtils.msgPointer(NSTouchBarClass, "alloc");
        Pointer touchBar = RuntimeUtils.msgPointer(alloc, "init");

        List<Pointer> identifierPtrs = new ArrayList<>();
        List<Pointer> itemPtrs = new ArrayList<>();

        int count = 0;

        for (TouchBarButton touchBarButton : buttons) {
            if (!touchBarButton.visible)
                continue;

            identifierPtrs.add(RuntimeUtils.str(touchBarButton.identifier));
            itemPtrs.add(touchBarButton.item);
            count++;
        }

        Pointer identifiers = RuntimeUtils.msgPointer("NSArray", "arrayWithObjects:count:", identifierPtrs.toArray(Pointer[]::new), count);
        RuntimeUtils.msg(touchBar, "setDefaultItemIdentifiers:", identifiers);

        Pointer itemSet = RuntimeUtils.msgPointer("NSSet", "setWithObjects:count:", itemPtrs.toArray(Pointer[]::new), count);
        RuntimeUtils.msg(touchBar, "setTemplateItems:", itemSet);

        Pointer NSApp = RuntimeUtils.msgPointer(RuntimeUtils.cls("NSApplication"), "sharedApplication");
        Pointer mainWindow = RuntimeUtils.msgPointer(NSApp, "mainWindow");
        RuntimeUtils.msg(mainWindow, "setTouchBar:", touchBar);
    }

    private static void extractGIFFrameBytes(File file) {
        customImageFrames.clear();
        customImageCurFrame = 0;

        try {
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream stream = ImageIO.createImageInputStream(file);
            reader.setInput(stream);

            int count = reader.getNumImages(true);
            for (int i = 0; i < count; i++) {
                BufferedImage frame = reader.read(i);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(frame, "png", byteArrayOutputStream);
                customImageFrames.add(byteArrayOutputStream.toByteArray());
            }
        } catch (IOException e) {
           e.printStackTrace();
        }

    }

    private static void extractTouchBarImagesBytes() {

        List<String> fileNames = Arrays.asList(
                "custom.gif",
                "block-grass_block_side.png",
                "item-compass_01.png",
                "item-redstone.png",
                "item-end_crystal.png",
                "item-totem_of_undying.png"
        );

        File dir = new File("nicotine");

        if (!dir.exists())
            dir.mkdir();

        for (String fileName : fileNames) {
            if (fileName.contains("-")) { // minecraft
                String path = String.format("minecraft:textures/%s", fileName.replace('-', '/'));
                try {
                    InputStream inputStream = mc.getResourceManager().getResource(Identifier.parse(path)).get().open();
                    touchBarImages.put(fileName, inputStream.readAllBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else { // custom
                File outputFile = new File("nicotine/" + fileName);

                if (!outputFile.exists()) {
                    InputStream inputStream = TouchBar.class.getClassLoader().getResourceAsStream("touchbarimages/custom.gif");
                    try {
                        FileUtils.copyInputStreamToFile(inputStream, outputFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                customSize = outputFile.length();
                extractGIFFrameBytes(outputFile);
            }
        }
    }

    public static void init() {
        Mod touchBar = new Mod("TouchBar", "Touch bar for Macbook Pro devices")  {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!this.enabled) {
                    buttons.clear();
                    setTouchBar();
                }
            }
        };
        SliderOption refresh = new SliderOption("Refresh", 5, 1, 20, false);
        ToggleOption coords = new ToggleOption("Coords");
        ToggleOption totem = new ToggleOption("Totem");
        ToggleOption eCrystal = new ToggleOption("ECrystal");
        ToggleOption server = new ToggleOption("Server");
        ToggleOption customGIF = new ToggleOption("CustomGIF");
        ToggleOption customButtons = new ToggleOption("CustomButtons");
        touchBar.modOptions.addAll(Arrays.asList(refresh, coords, totem, eCrystal, server, customGIF, customButtons));
        ModManager.addMod(ModCategory.Misc, touchBar);

        extractTouchBarImagesBytes();

        File customFile = new File("nicotine/custom.gif");

        EventBus.register(ClientTickEvent.class, event -> {
           if (!touchBar.enabled)
               return true;

           long curTime = System.currentTimeMillis();
           if (curTime - elapsedTime < refresh.value * 50)
               return true;
           else
               elapsedTime = curTime;

           if (customFile.length() != customSize) {
               extractGIFFrameBytes(customFile);
               customSize = customFile.length();
           }

           int buttonsSize = buttons.size();

           setButtonVisibility(getCoordsButton(), coords.enabled);
           setButtonVisibility(getTotemButton(), totem.enabled);
           setButtonVisibility(getECrystalButton(), eCrystal.enabled);
           setButtonVisibility(getServerButton(), server.enabled);
           setButtonVisibility(getCustomGIFButton(), customGIF.enabled);

           getCustomButtons(customButtons.enabled);

           if (buttons.size() != buttonsSize) {
               setTouchBar();
           }

           return true;
        });

    }
}
