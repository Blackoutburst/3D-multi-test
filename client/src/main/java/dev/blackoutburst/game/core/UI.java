package dev.blackoutburst.game.core;

import dev.blackoutburst.game.maths.Vector2i;
import dev.blackoutburst.game.maths.Vector3f;
import dev.blackoutburst.game.maths.Vector3i;
import dev.blackoutburst.game.world.BlockType;
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

    public static void renderInformation(NkContext ctx, Vector2i wp, Vector2i ws, int fps, int chunkUpdates, int blocks, int chunks, int vertex) {
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.malloc(stack);

            if (nk_begin(
                    ctx,
                    "Render",
                    nk_rect(wp.getX(), wp.getY(), ws.getX(), ws.getY(), rect),
                    NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE | NK_WINDOW_NO_SCROLLBAR
            )) {
                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "RENDER MODE", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 20, 2);
                if (nk_option_label(ctx, "Fill", renderOption == GL_FILL)) {
                    renderOption = GL_FILL;
                }
                if (nk_option_label(ctx, "Line", renderOption == GL_LINE)) {
                    renderOption = GL_LINE;
                }

                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "FPS: " + FBN(fps), NK_TEXT_LEFT);
                nk_label(ctx, "Chunk updates: " + chunkUpdates, NK_TEXT_LEFT);
                nk_label(ctx, "Blocks: " + FBN(blocks), NK_TEXT_LEFT);
                nk_label(ctx, "Chunks: " + FBN(chunks), NK_TEXT_LEFT);
                nk_label(ctx, "Vertex: " + FBN(vertex), NK_TEXT_LEFT);
            }
            nk_end(ctx);
        }
    }

    public static void renderCoroutines(NkContext ctx, Vector2i wp, Vector2i ws, int coroutines, int coroutinesIO, int coroutinesDefault, int coroutinesUnconfined) {
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.malloc(stack);

            if (nk_begin(
                    ctx,
                    "Coroutines",
                    nk_rect(wp.getX(), wp.getY(), ws.getX(), ws.getY(), rect),
                    NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE | NK_WINDOW_NO_SCROLLBAR
            )) {
                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "Total: " + FBN(coroutines), NK_TEXT_LEFT);
                nk_label(ctx, "IO: " + FBN(coroutinesIO), NK_TEXT_LEFT);
                nk_label(ctx, "Default: " + FBN(coroutinesDefault), NK_TEXT_LEFT);
                nk_label(ctx, "Unconfined: " + FBN(coroutinesUnconfined), NK_TEXT_LEFT);
            }
            nk_end(ctx);
        }
    }

    public static void renderGameInformation(NkContext ctx, Vector2i wp, Vector2i ws, Vector3f position, BlockType blockType, Vector3i blockPosition) {
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.malloc(stack);

            if (nk_begin(
                    ctx,
                    "Game information",
                    nk_rect(wp.getX(), wp.getY(), ws.getX(), ws.getY(), rect),
                    NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE | NK_WINDOW_NO_SCROLLBAR
            )) {
                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "POSITION", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 20, 3);
                nk_label(ctx, "X: " + FBN(position.getX()), NK_TEXT_LEFT);
                nk_label(ctx, "Y: " + FBN(position.getY()), NK_TEXT_LEFT);
                nk_label(ctx, "Z: " + FBN(position.getZ()), NK_TEXT_LEFT);

                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "LOOKING AT", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "Type: " + blockType.toString(), NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 20, 3);
                nk_label(ctx, "X: " + FBN(blockPosition.getX()), NK_TEXT_LEFT);
                nk_label(ctx, "Y: " + FBN(blockPosition.getY()), NK_TEXT_LEFT);
                nk_label(ctx, "Z: " + FBN(blockPosition.getZ()), NK_TEXT_LEFT);
            }
            nk_end(ctx);
        }
    }

    public static void renderSystemUsage(NkContext ctx, Vector2i wp, Vector2i ws, int cpuProcess, int thread, int memUsed, int memFree, int memTotal) {
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.malloc(stack);

            if (nk_begin(
                    ctx,
                    "System",
                    nk_rect(wp.getX(), wp.getY(), ws.getX(), ws.getY(), rect),
                    NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE | NK_WINDOW_NO_SCROLLBAR
            )) {
                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "CPU: " + cpuProcess + "%", NK_TEXT_LEFT);

                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "Threads: " + FBN(thread), NK_TEXT_LEFT);

                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "MEMORY", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "Used: " + FBN(memUsed) + "Mo", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "Free: " + FBN(memFree) + "Mo", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "Total: " + FBN(memTotal) + "Mo", NK_TEXT_LEFT);
            }
            nk_end(ctx);
        }
    }
}
