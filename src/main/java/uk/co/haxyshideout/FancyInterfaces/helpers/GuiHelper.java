package uk.co.haxyshideout.FancyInterfaces.helpers;

import net.minecraft.client.renderer.Tessellator;

public class GuiHelper {

	public static void drawImageQuad(double x, double y, double w, float h, double us, double vs, double ue, double ve, float zLevel) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x    , y + h, (double) zLevel, us, ve);
		tessellator.addVertexWithUV(x + w, y + h, (double) zLevel, ue, ve);
		tessellator.addVertexWithUV(x + w, y    , (double) zLevel, ue, vs);
		tessellator.addVertexWithUV(x    , y    , (double) zLevel, us, vs);
		tessellator.draw();
	}

}
