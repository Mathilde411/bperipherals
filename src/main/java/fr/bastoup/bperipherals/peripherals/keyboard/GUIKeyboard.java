package fr.bastoup.bperipherals.peripherals.keyboard;


import com.mojang.blaze3d.vertex.PoseStack;
import dan200.computercraft.client.gui.widgets.WidgetTerminal;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.shared.computer.core.ClientComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import java.util.BitSet;

public class GUIKeyboard extends AbstractContainerScreen<KeyboardContainer> {

    private static final float TERMINATE_TIME = 0.5f;

    protected final ClientComputer computer;
    protected final ComputerFamily family;

    private final BitSet keysDown = new BitSet( 256 );

    private float terminateTimer = -1;
    private float rebootTimer = -1;
    private float shutdownTimer = -1;
    private int lastMouseButton;

    public GUIKeyboard( KeyboardContainer container, Inventory player, Component title)
    {
        super( container, player, title );
        computer = (ClientComputer) container.getComputer();
        family = container.getFamily();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if( terminateTimer >= 0 && terminateTimer < TERMINATE_TIME && (terminateTimer += 0.05f) > TERMINATE_TIME )
        {
            computer.queueEvent( "terminate" );
        }

        if( shutdownTimer >= 0 && shutdownTimer < TERMINATE_TIME && (shutdownTimer += 0.05f) > TERMINATE_TIME )
        {
            computer.shutdown();
        }

        if( rebootTimer >= 0 && rebootTimer < TERMINATE_TIME && (rebootTimer += 0.05f) > TERMINATE_TIME )
        {
            computer.reboot();
        }
    }

    @Override
    protected final void init()
    {
        super.init();
        minecraft.keyboardHandler.setSendRepeatsToGui( true );
    }



    @Override
    public final void removed()
    {
        super.removed();
        minecraft.keyboardHandler.setSendRepeatsToGui( false );
    }

    @Override
    public final boolean keyPressed( int key, int scancode, int modifiers )
    {
        if( key == GLFW.GLFW_KEY_ESCAPE )
            return super.keyPressed(key, scancode, modifiers);
        if( (modifiers & GLFW.GLFW_MOD_CONTROL) != 0 )
        {
            switch( key )
            {
                case GLFW.GLFW_KEY_T:
                    if( terminateTimer < 0 ) terminateTimer = 0;
                    return true;
                case GLFW.GLFW_KEY_S:
                    if( shutdownTimer < 0 ) shutdownTimer = 0;
                    return true;
                case GLFW.GLFW_KEY_R:
                    if( rebootTimer < 0 ) rebootTimer = 0;
                    return true;

                case GLFW.GLFW_KEY_V:
                    // Ctrl+V for paste
                    String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard();
                    if( clipboard != null )
                    {
                        // Clip to the first occurrence of \r or \n
                        int newLineIndex1 = clipboard.indexOf( "\r" );
                        int newLineIndex2 = clipboard.indexOf( "\n" );
                        if( newLineIndex1 >= 0 && newLineIndex2 >= 0 )
                        {
                            clipboard = clipboard.substring( 0, Math.min( newLineIndex1, newLineIndex2 ) );
                        }
                        else if( newLineIndex1 >= 0 )
                        {
                            clipboard = clipboard.substring( 0, newLineIndex1 );
                        }
                        else if( newLineIndex2 >= 0 )
                        {
                            clipboard = clipboard.substring( 0, newLineIndex2 );
                        }

                        // Filter the string
                        clipboard = SharedConstants.filterText( clipboard );
                        if( !clipboard.isEmpty() )
                        {
                            // Clip to 512 characters and queue the event
                            if( clipboard.length() > 512 ) clipboard = clipboard.substring( 0, 512 );
                            computer.queueEvent( "paste", new Object[] { clipboard } );
                        }

                        return true;
                    }
            }
        }



        if( key >= 0 && terminateTimer < 0 && rebootTimer < 0 && shutdownTimer < 0 )
        {
            // Queue the "key" event and add to the down set
            boolean repeat = keysDown.get( key );
            keysDown.set( key );
            computer.keyDown( key, repeat );
        }

        return true;
    }

    @Override
    public boolean keyReleased( int key, int scancode, int modifiers )
    {
        // Queue the "key_up" event and remove from the down set
        if( key >= 0 && keysDown.get( key ) )
        {
            keysDown.set( key, false );
            computer.keyUp( key );
        }

        switch( key )
        {
            case GLFW.GLFW_KEY_T:
                terminateTimer = -1;
                break;
            case GLFW.GLFW_KEY_R:
                rebootTimer = -1;
                break;
            case GLFW.GLFW_KEY_S:
                shutdownTimer = -1;
                break;
            case GLFW.GLFW_KEY_LEFT_CONTROL:
            case GLFW.GLFW_KEY_RIGHT_CONTROL:
                terminateTimer = rebootTimer = shutdownTimer = -1;
                break;
        }

        return true;
    }

    @Override
    public boolean charTyped( char ch, int modifiers )
    {
        if( ch >= 32 && ch <= 126 || ch >= 160 && ch <= 255 ) // printable chars in byte range
        {
            // Queue the "char" event
            computer.queueEvent( "char", new Object[] { Character.toString( ch ) } );
        }

        return true;
    }

    @Override
    public boolean mouseClicked( double mouseX, double mouseY, int button )
    {
        if( !computer.isColour() || button < 0 || button > 2 ) return false;

        Terminal term = computer.getTerminal();
        if( term != null )
        {
            computer.mouseClick( button + 1, 1, 1);
            lastMouseButton = button;
        }

        return true;
    }

    @Override
    public boolean mouseReleased( double mouseX, double mouseY, int button )
    {
        if( !computer.isColour() || button < 0 || button > 2 ) return false;

        Terminal term = computer.getTerminal();
        if( term != null )
        {

            if( lastMouseButton == button )
            {
                computer.mouseUp( lastMouseButton + 1, 1, 1 );
                lastMouseButton = -1;
            }
        }

        return false;
    }

    @Override
    public boolean mouseScrolled( double mouseX, double mouseY, double delta )
    {
        if( !computer.isColour() || delta == 0 ) return false;

        Terminal term = computer.getTerminal();
        if( term != null )
        {
            computer.mouseScroll( delta < 0 ? 1 : -1, 0, 0);
        }

        return true;
    }



    @Override
    protected void renderBg(PoseStack p_97787_, float p_97788_, int p_97789_, int p_97790_) {

    }

    @Override
    public void render(PoseStack p_97795_, int p_97796_, int p_97797_, float p_97798_) {

    }
}
