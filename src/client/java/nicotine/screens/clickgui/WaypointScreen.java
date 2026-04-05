package nicotine.screens.clickgui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.FaviconTexture;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.resources.Identifier;
import nicotine.mod.mods.general.GUI;
import nicotine.mod.option.DropDownOption;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.clickgui.element.button.DropDownButton;
import nicotine.screens.clickgui.element.button.GUIButton;
import nicotine.screens.clickgui.element.button.InputText;
import nicotine.screens.clickgui.element.button.ToggleButton;
import nicotine.screens.clickgui.element.misc.HLine;
import nicotine.screens.clickgui.element.misc.Text;
import nicotine.screens.clickgui.element.misc.Texture;
import nicotine.screens.clickgui.element.window.DecoratedWindow;
import nicotine.util.ColorUtil;
import nicotine.util.WaypointInstance;
import nicotine.util.render.Render2D;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.*;

public class WaypointScreen extends BaseScreen {
    private final InputText nameInput, cordsXInput, cordsYInput, cordsZInput, serverInput, searchInput;
    private final DropDownOption dimensionsOption;
    private final ToggleOption filterCurrentServerOption;

    private WaypointInstance instanceToEdit = null;

    private boolean list = true;

    private final ServerList serverList;

    private final HashMap<String, FaviconTexture> serverIcons = new HashMap<>();

    public WaypointScreen() {
        super("nicotine waypoints", new DecoratedWindow(null, "Waypoints", 0, 0, 280, 230) {
            protected void close() {
                mc.setScreen(GUI.screen);
            }
        });

        serverList = new ServerList(mc);
        serverList.load();

        searchInput = new InputText(0, 0, this.window.width - 4, mc.font.lineHeight + 2, "Search");

        int inputWidth = this.window.width - 10;

        nameInput = new InputText(0, 0, inputWidth, mc.font.lineHeight + 2, "Name", ColorUtil.BORDER_COLOR);

        cordsXInput = new InputText(0, 0, inputWidth, mc.font.lineHeight + 2, "X cord", ColorUtil.BORDER_COLOR);
        cordsYInput = new InputText(0, 0, inputWidth, mc.font.lineHeight + 2, "Y cord", ColorUtil.BORDER_COLOR);
        cordsZInput = new InputText(0, 0, inputWidth, mc.font.lineHeight + 2, "Z cord", ColorUtil.BORDER_COLOR);

        cordsXInput.regexString = cordsYInput.regexString = cordsZInput.regexString = "^(-|(-?\\d+))$";

        serverInput = new InputText(0, 0, inputWidth, mc.font.lineHeight + 2, "Server", ColorUtil.BORDER_COLOR);

        filterCurrentServerOption = new ToggleOption("Current server");
        dimensionsOption = new DropDownOption("Dimension", new String[]{"Overworld", "The Nether", "The End"});
    }

    public void changeMode() {
        this.list = !this.list;

        if (list) {
            this.window.width = 280;
            this.window.height = 230;
        } else {
            this.window.width = 250;
            this.window.height = 140;
        }

        this.window.centerPosition();
    }

    public void addNewInstance() {
        for (InputText inputText : List.of(nameInput, cordsXInput, cordsYInput, cordsZInput, serverInput)) {
            if (inputText.text.isEmpty() || (!inputText.regexString.isEmpty() && !NumberUtils.isParsable(inputText.text)))
                return;
        }

        WaypointInstance waypointInstance = new WaypointInstance(
                nameInput.text,
                String.format("minecraft:%s", dimensionsOption.value.toLowerCase().replace(" ", "_")),
                serverInput.text,
                Integer.parseInt(cordsXInput.text),
                Integer.parseInt(cordsYInput.text),
                Integer.parseInt(cordsZInput.text)
        );

        if (instanceToEdit != null) {
            instanceToEdit.name = waypointInstance.name;
            instanceToEdit.dimension = waypointInstance.dimension;
            instanceToEdit.server = waypointInstance.server;
            instanceToEdit.x = waypointInstance.x;
            instanceToEdit.y = waypointInstance.y;
            instanceToEdit.z = waypointInstance.z;
        } else {
            waypointInstances.add(waypointInstance);
        }

        changeMode();
    }

    public void addListDrawables() {
        int posX = this.window.x + 5;
        int posY = this.window.y + 5;

        searchInput.x = posX - 2;
        searchInput.y = posY - 2;
        this.window.add(searchInput);

        posY += searchInput.height + 5;

        DropDownButton dimensionsButton = new DropDownButton(dimensionsOption, posX, posY);
        this.window.add(dimensionsButton);

        ToggleButton filterCurrentServerButton = new ToggleButton(filterCurrentServerOption, this.window.x + this.window.width, posY);
        filterCurrentServerButton.x -= filterCurrentServerButton.getFullWidth() + 5;
        this.window.add(filterCurrentServerButton);

        posY += dimensionsButton.height + 3;

        this.window.add(new HLine(this.window.x, posY, this.window.width, ColorUtil.getPulsatingColor()));

        posY += 5;

        posX = this.window.x + 5;

        List<WaypointInstance> filteredWaypointInstances = new ArrayList<>();
        for (WaypointInstance waypointInstance : waypointInstances) {
            if (currentServer != null && filterCurrentServerButton.toggleOption.enabled && !waypointInstance.server.equals(currentServer.ip))
                continue;

            if (!searchInput.text.isEmpty() && !waypointInstance.name.contains(searchInput.text))
                continue;

            if (!waypointInstance.dimension.contains(dimensionsOption.value.toLowerCase().replace(" ", "_")))
                continue;

            filteredWaypointInstances.add(waypointInstance);
        }

        if (this.window.scrollOffset > filteredWaypointInstances.size() - 1 && !filteredWaypointInstances.isEmpty())
            this.window.scrollOffset = filteredWaypointInstances.size() - 1;

        for (WaypointInstance waypointInstance : filteredWaypointInstances.stream().skip(this.window.scrollOffset).toList()) {
            if (posY + 32 > this.window.y + this.window.height)
                break;


            FaviconTexture faviconTexture = serverIcons.get(waypointInstance.server);

            if (faviconTexture == null) {
                faviconTexture = FaviconTexture.forServer(mc.getTextureManager(), waypointInstance.server);
                try {
                    ServerData serverData = serverList.get(waypointInstance.server);
                    if (serverData != null && serverData.getIconBytes() != null) {
                        faviconTexture.upload(NativeImage.read(serverData.getIconBytes()));
                        serverIcons.put(waypointInstance.server, faviconTexture);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            Texture serverIcon = new Texture(faviconTexture.textureLocation(), posX, posY);
            this.window.add(serverIcon);

            posX += serverIcon.width;

            Text nameText = new Text(waypointInstance.name, posX + 3, posY + 3);
            this.window.add(nameText);

            Text cordsText = new Text(String.format("%d %d %d", waypointInstance.x, waypointInstance.y, waypointInstance.z), nameText.x + nameText.width + 5, posY + 3) {
                @Override
                public void draw(GuiGraphicsExtractor context, double mouseX, double mouseY) {
                    Render2D.drawBorderAroundText(context, this.x, this.y, this.width, this.height, 1, ColorUtil.BORDER_COLOR);
                    context.text(mc.font, this.text, this.x, this.y, this.color, true);
                }
            };
            this.window.add(cordsText);

            GUIButton editButton = new GUIButton("Edit", window.x + window.width - mc.font.width("Edit") - 15, cordsText.y, 1, ColorUtil.BORDER_COLOR) {
                @Override
                public void click(double mouseX, double mouseY, int input) {
                    if (input == InputConstants.MOUSE_BUTTON_LEFT) {
                        instanceToEdit = waypointInstance;

                        nameInput.text = waypointInstance.name;
                        cordsXInput.text = String.valueOf(waypointInstance.x);
                        cordsYInput.text = String.valueOf(waypointInstance.y);
                        cordsZInput.text = String.valueOf(waypointInstance.z);
                        serverInput.text = waypointInstance.server;

                        changeMode();
                        window.centerPosition();
                    }
                }
            };
            this.window.add(editButton);

            GUIButton removeButton = new GUIButton("-", editButton.x + editButton.width + 5, cordsText.y, 1, ColorUtil.BORDER_COLOR) {
                @Override
                public void click(double mouseX, double mouseY, int input) {
                    if (input == InputConstants.MOUSE_BUTTON_LEFT) {
                       waypointInstances.remove(waypointInstance);
                    }
                }
            };
            this.window.add(removeButton);

            posY += serverIcon.height + 3;

            posX = this.window.x + 5;
        }

        GUIButton newInstanceButton = new GUIButton(" + ",
                this.window.x + (this.window.width / 2) - (mc.font.width(" + ") / 2),
                this.window.y + this.window.height - mc.font.lineHeight - 5,
                1
        ) {
            @Override
            public void click(double mouseX, double mouseY, int input) {
                if (input == InputConstants.MOUSE_BUTTON_LEFT) {
                    if (mc.player != null) {
                        nameInput.text = "";

                        cordsXInput.text = String.valueOf((int) mc.player.getX());
                        cordsYInput.text = String.valueOf((int) mc.player.getY());
                        cordsZInput.text = String.valueOf((int) mc.player.getZ());

                        if (currentServer != null) {
                            serverInput.text = currentServer.ip;
                        }
                    }

                    changeMode();
                }
            }
        };

        this.window.add(newInstanceButton);
    }

    public void addNewInstanceDrawables() {
        int posX = this.window.x + 8;
        int posY = this.window.y + 8;

        for (InputText inputText : List.of(nameInput, cordsXInput, cordsYInput, cordsZInput, serverInput)) {
            Text text = new Text(inputText.tempText, posX, posY);
            this.window.add(text);

            posX += text.width + 5;

            inputText.x = posX;
            inputText.y = posY;
            inputText.width = window.width - text.width - 18;
            this.window.add(inputText);

            posX = this.window.x + 8;
            posY += mc.font.lineHeight + 10;
        }

        DropDownButton dimensionsButton = new DropDownButton(dimensionsOption, posX, posY);
        this.window.add(dimensionsButton);

        GUIButton newInstanceButton = new GUIButton("Add",
               0,
                this.window.y + this.window.height - mc.font.lineHeight - 5,
                1
        ) {
            @Override
            public void click(double mouseX, double mouseY, int input) {
                if (input == InputConstants.MOUSE_BUTTON_LEFT) {
                    addNewInstance();
                }
            }
        };

        GUIButton listButton = new GUIButton("List",
                0,
                this.window.y + this.window.height - mc.font.lineHeight - 5,
                1
        ) {
            @Override
            public void click(double mouseX, double mouseY, int input) {
                if (input == InputConstants.MOUSE_BUTTON_LEFT) {
                    changeMode();
                }
            }
        };

        int buttonsX = this.window.x + (this.window.width / 2) - (newInstanceButton.width / 2) - (listButton.width / 2);
        newInstanceButton.x = buttonsX;
        listButton.x = buttonsX + listButton.width + 5;

        this.window.add(newInstanceButton);
        this.window.add(listButton);
    }

    @Override
    public void closeScreen() {
        if (list)
            mc.setScreen(GUI.screen);
        else
           changeMode();
    }

    @Override
    public void addDrawables() {
        if (list) {
            instanceToEdit = null;
            addListDrawables();
        } else {
            addNewInstanceDrawables();
        }

        super.addDrawables();
    }
}
