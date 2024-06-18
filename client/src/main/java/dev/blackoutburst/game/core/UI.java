package dev.blackoutburst.game.core;

import dev.blackoutburst.game.maths.Vector2i;
import dev.blackoutburst.game.maths.Vector3f;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;

import static dev.blackoutburst.game.utils.NumberUtilsKt.FBN;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class UI {

    public static int renderOption = GL_FILL;

    public static void renderInformation(NkContext ctx, Vector2i wp, Vector2i ws, int fps, int blocks, int chunks, int vertex) {
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.malloc(stack);

            if (nk_begin(
                    ctx,
                    "Render",
                    nk_rect(wp.getX(), wp.getY(), ws.getX(), ws.getY(), rect),
                    NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE | NK_WINDOW_NO_SCROLLBAR
            )) {
                nk_layout_row_static(ctx, 30, ws.getX(), 1);
                nk_label(ctx, "Render mode: ", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 30, 2);
                if (nk_option_label(ctx, "Fill", renderOption == GL_FILL)) {
                    renderOption = GL_FILL;
                }
                if (nk_option_label(ctx, "Line", renderOption == GL_LINE)) {
                    renderOption = GL_LINE;

                }
                nk_layout_row_static(ctx, 30, ws.getX(), 1);
                nk_label(ctx, "FPS: " + FBN(fps), NK_TEXT_LEFT);
                nk_label(ctx, "Blocks: " + FBN(blocks), NK_TEXT_LEFT);
                nk_label(ctx, "Chunks: " + FBN(chunks), NK_TEXT_LEFT);
                nk_label(ctx, "Vertex: " + FBN(vertex), NK_TEXT_LEFT);
            }
            nk_end(ctx);
        }
    }

    public static void playerPosition(NkContext ctx, Vector2i wp, Vector2i ws, Vector3f position) {
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.malloc(stack);

            if (nk_begin(
                    ctx,
                    "Camera position",
                    nk_rect(wp.getX(), wp.getY(), ws.getX(), ws.getY(), rect),
                    NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE | NK_WINDOW_NO_SCROLLBAR
            )) {
                nk_layout_row_static(ctx, 30, ws.getX(), 1);
                nk_label(ctx, "X: " + FBN(position.getX()), NK_TEXT_LEFT);
                nk_label(ctx, "Y: " + FBN(position.getY()), NK_TEXT_LEFT);
                nk_label(ctx, "Z: " + FBN(position.getZ()), NK_TEXT_LEFT);
            }
            nk_end(ctx);
        }
    }
}
