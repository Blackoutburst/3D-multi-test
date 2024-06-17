package dev.blackoutburst.game.core;

import dev.blackoutburst.game.maths.Vector3f;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.system.MemoryStack.stackPush;

public class UI {
    public static void playerPosition(NkContext ctx, Vector3f position, int fps) {
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.malloc(stack);

            if (nk_begin(
                    ctx,
                    "Position",
                    nk_rect(0, 0, 120, 160, rect),
                    NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_SCALABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE
            )) {
                nk_layout_row_static(ctx, 30, 80, 1);
                nk_label(ctx, "X: " + position.getX(), NK_TEXT_LEFT);
                nk_label(ctx, "Y: " + position.getY(), NK_TEXT_LEFT);
                nk_label(ctx, "Z: " + position.getZ(), NK_TEXT_LEFT);
            }
            nk_end(ctx);
        }
    }
}
