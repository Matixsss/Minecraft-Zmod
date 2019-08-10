package com.DystryktZ.GUI;




import org.lwjgl.opengl.GL11;

import com.DystryktZ.ZEventHandlerClient;
import com.DystryktZ.ZmodJson;
import com.DystryktZ.Capability.IZStat;
import com.DystryktZ.Capability.ZStatController;
import com.DystryktZ.utils.Vector2D;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;

public class ZGuiScreen extends Screen {
	
	private IZStat iz;
	private String[] string_template;
	float scale=1F;
	int[] w;
	int[] h;
	
	private Vector2D<Integer, Integer> button1_position;
	
	private Button button1;

	public ZGuiScreen() {
		super(new StringTextComponent("Character statistics"));
		// TODO Auto-generated constructor stub
	}
	
	
	private int[] player_exp = new int[12];
	private float[] player_exp_ratio = new float[6];
	
	 protected void init() {
			LazyOptional<IZStat> lo = ZEventHandlerClient.mc_instance.player.getCapability(ZStatController.ZStat_CAP);
			iz = lo.orElse(null);
			
			//mining
			player_exp[0] = iz.get_mining_xp();
			player_exp[1] = ZmodJson.levelToExp(ZmodJson.expToLevel(iz.get_mining_xp())+1);
			player_exp_ratio[0] = ((float)player_exp[0] - ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[0]))) /((float)player_exp[1]-ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[0])));
			//combat
			player_exp[2] = iz.get_combat_xp();
			player_exp[3] = ZmodJson.levelToExp(ZmodJson.expToLevel(iz.get_combat_xp())+1);
			player_exp_ratio[1] = ((float)player_exp[2] - ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[2])))/((float)player_exp[3]- ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[2])));
			//digging
			player_exp[4] = iz.get_digging_xp();
			player_exp[5] = ZmodJson.levelToExp(ZmodJson.expToLevel(iz.get_digging_xp())+1);
			player_exp_ratio[2] = ((float)player_exp[4] - ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[4])))/((float)player_exp[5]- ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[4])));
			//cutting
			player_exp[6] = iz.get_cutting_xp();
			player_exp[7] = ZmodJson.levelToExp(ZmodJson.expToLevel(iz.get_cutting_xp())+1);
			player_exp_ratio[3] = ((float)player_exp[6]- ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[6])))/((float)player_exp[7]- ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[6])));
			//farming
			player_exp[8] = iz.get_farm_xp();
			player_exp[9] = ZmodJson.levelToExp(ZmodJson.expToLevel(iz.get_farm_xp())+1);
			player_exp_ratio[4] = ((float)player_exp[8]- ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[8])))/((float)player_exp[9]- ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[8])));
			//building
			player_exp[10] = iz.get_building_xp();
			player_exp[11] = ZmodJson.levelToExp(ZmodJson.expToLevel(iz.get_building_xp())+1);
			player_exp_ratio[5] = ((float)player_exp[10]- ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[10])))/((float)player_exp[11]- ZmodJson.levelToExp(ZmodJson.expToLevel(player_exp[10])));
			
			string_template = new String[17];
			string_template[0] = "Level: "+ZmodJson.expToLevel(player_exp[0]);
			string_template[1] = "Exp: "+player_exp[0] + "/" + player_exp[1];
			
			string_template[2] = "Level: "+ZmodJson.expToLevel(player_exp[2]);
			string_template[3] = "Exp: "+player_exp[2] + "/" + player_exp[3];
			
			string_template[4] = "Level: "+ZmodJson.expToLevel(player_exp[4]);
		    string_template[5] = "Exp: "+player_exp[4]+ "/" + player_exp[5];
		    
		    string_template[6] = "Level: "+ZmodJson.expToLevel(player_exp[6]);
		    string_template[7] = "Exp: "+player_exp[6]+ "/" + player_exp[7];
		    
		    string_template[8] = "Level: "+ZmodJson.expToLevel(player_exp[8]);
		    string_template[9] = "Exp: "+player_exp[8]+ "/" + player_exp[9];
		   
		    string_template[10] = "Mining: "+"Breaking speed bonus: "+String.format("%.2f",((double)(ZmodJson.expToLevel(iz.get_mining_xp())*ZmodJson.server_configs.getDouble("break_bonus"))*100.0))+"% Luck: "+String.format("%.2f", ((double)ZmodJson.expToLevel(iz.get_mining_xp())*ZmodJson.server_configs.getDouble("mining_bonus")))+"%";
		    string_template[11] = "Combat: " +"Luck: "+String.format("%.2f", ((double)ZmodJson.expToLevel(iz.get_combat_xp())*ZmodJson.server_configs.getDouble("combat_bonus")))+"%";
		    string_template[12] = "Digging: " +"Luck: "+String.format("%.2f", ((double)ZmodJson.expToLevel(iz.get_digging_xp())*ZmodJson.server_configs.getDouble("digging_bonus")))+"%";
		    string_template[13] = "Woodcutting: " + "Breaking speed bonus: "+String.format("%.2f", (double)((ZmodJson.expToLevel(iz.get_cutting_xp())*ZmodJson.server_configs.getDouble("break_bonus"))*100.0))+"% Luck: "+String.format("%.2f", ((double)ZmodJson.expToLevel(iz.get_cutting_xp())*ZmodJson.server_configs.getDouble("woodcutting_bonus")))+"%";
		    string_template[14] = "Farming: " +"Luck: "+String.format("%.2f", ((double)ZmodJson.expToLevel(iz.get_farm_xp())*ZmodJson.server_configs.getDouble("farming_bonus")))+"%";
		    string_template[15] = "Level: "+ZmodJson.expToLevel(player_exp[10]);
		    string_template[16] = "Exp: "+player_exp[10]+ "/" + player_exp[11];
		  
		  
		   
		   switch((int)this.minecraft.mainWindow.getGuiScaleFactor())
		   {
		   case 1:
			   scale = 1F;
			   break;
			   
		   case 2: 
			   if(!this.minecraft.mainWindow.isFullscreen())
			   {  
				   if(this.minecraft.mainWindow.getWidth() > 1400 && this.minecraft.mainWindow.getHeight() > 800)
				   {
					   scale = 0.8F;
				   }
				   else
				   {
					   scale = 0.5F;
				   }
			   }
			   else
			   {
			   scale = 1F;
			   }
			   break;
			   
		   case 3:
			   scale = 0.66F;
			   break;
			   
		   case 4:
			   scale = 0.5F;
			   break;
		   }
		   
		   	  int resolution_width = this.minecraft.mainWindow.getFramebufferWidth();
			  if((int)this.minecraft.mainWindow.getGuiScaleFactor() > 1 && resolution_width <= 1440)
			  {
			  if(this.minecraft.mainWindow.isFullscreen())
			  {
				  scale = scale * 0.75F;
			  }
			  }
		   
		   //setup
		   w = new int[6];
		   h = new int[9];
		   for(int i=0;i<=8;i++)
		   {
			   if(i<6)
			   {
			   w[i] = (int) (Math.round((this.width/2)/scale -370 + (140*i)));
			   }
			   h[i] = (int) (Math.round((this.height/2)/scale) -110 + (20*i));		   
		   }
		   
		   button1_position = new Vector2D<Integer, Integer>(190,-160);
		   
		   
		   //add buttons
		   button1 = this.addButton(new Button((int) (Math.round((this.width/2)+(button1_position.getX()*scale))), (int) (Math.round((this.height/2)+(button1_position.getY()*scale))), 80, 16, "Ranking", (p_213055_1_) -> {
		         this.minecraft.displayGuiScreen(new ZRanking(this));
		      }));
		   
		   if(this.minecraft.isSingleplayer())
		   {
			   button1.active = false;
		   }
	 }
	 
	 private final float RWidth = 50F;
	 private final float RHeight = 5F;
	 private void renderExpBar(float x, float y , int index)
	 {
		 	GlStateManager.pushMatrix();
		 	GlStateManager.disableTexture();
		    Tessellator tessellator = Tessellator.getInstance();
		    BufferBuilder buf = tessellator.getBuffer();
		    //rentacle
		    GlStateManager.lineWidth(2F);
		    buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		    //up
		    buf.pos(x, y, 0).color(1F, 1F, 1F, 1F).endVertex();
		    buf.pos(x+RWidth, y, 0).color(1F, 1F, 1F, 1F).endVertex();
		    
		    //down
		    buf.pos(x, y+RHeight, 0).color(1F, 1F, 1F, 1F).endVertex();
		    buf.pos(x+RWidth, y+RHeight, 0).color(1F, 1F, 1F, 1F).endVertex();
		    
		    //left
		    buf.pos(x, y, 0).color(1F, 1F, 1F, 1F).endVertex();
		    buf.pos(x, y+RHeight, 0).color(1F, 1F, 1F, 1F).endVertex();
		    
		    //right
		    buf.pos(x+RWidth, y, 0).color(1F, 1F, 1F, 1F).endVertex();
		    buf.pos(x+RWidth, y+RHeight, 0).color(1F, 1F, 1F, 1F).endVertex();
		    
		    tessellator.draw();
		    
		    GlStateManager.lineWidth(RHeight - 1F);
		    buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		    buf.pos(x+2F, y+2F, 0).color(1F, 0F, 0F, 0.5F).endVertex();
		    buf.pos(x+RWidth-2F, y+2F, 0).color(1F, 0F, 0F, 0.5F).endVertex();
		    tessellator.draw();
		    
		    //oblicz

		    GlStateManager.lineWidth(RHeight - 1F);
		    buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		    buf.pos(x+2F, y+2F, 0).color(0F, 0F, 1F, 1F).endVertex();
		    buf.pos(x+(RWidth-2F)*player_exp_ratio[index], y+2F, 0).color(0F, 0F, 1F, 1F).endVertex();
		    tessellator.draw();
		    
		    
		    GlStateManager.enableTexture();
		    GlStateManager.popMatrix();
	 }
	 
	   public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		      this.renderBackground();      
		      if(iz!=null)
		      {	  	    	  
		    	  GlStateManager.pushMatrix();   
		    	  GlStateManager.scalef(scale, scale, scale); 	    
		    	  this.drawString(this.font, this.title.getFormattedText(), w[2], (int) (Math.round(this.height/2/scale)-160), 16777215);
		    	  //mining
		    	  	  this.drawString(this.font, "Mining", w[0], h[0], 8684676);
		    		  this.drawString(this.font, string_template[0], w[0], h[1], 8684676);
		    		  this.drawString(this.font, string_template[1], w[0], h[2], 8684676);
		    		  this.renderExpBar(w[0], h[3], 0);
		    		  //combat
		    		  this.drawString(this.font, "Combat", w[1], h[0], 14614785);
				      this.drawString(this.font, string_template[2], w[1], h[1], 14614785);
				      this.drawString(this.font, string_template[3], w[1], h[2], 14614785);
				      this.renderExpBar(w[1], h[3],1);
				      //digging
				      this.drawString(this.font, "Digging", (int) w[2], h[0], 14644225);
				      this.drawString(this.font, string_template[4], w[2], h[1], 14644225);
				      this.drawString(this.font, string_template[5], w[2], h[2], 14644225);
				      this.renderExpBar(w[2], h[3],2);
				      //woodcutting
				      this.drawString(this.font, "Woodcutting", w[3], h[0], 6371339);
				      this.drawString(this.font, string_template[6], w[3], h[1], 6371339);
				      this.drawString(this.font, string_template[7], w[3],  h[2], 6371339);
				      this.renderExpBar(w[3], h[3],3);
				      //farming
				      this.drawString(this.font, "Farming", w[4], h[0],308228);
				      this.drawString(this.font, string_template[8], w[4], h[1],308228);	
				      this.drawString(this.font, string_template[9], w[4], h[2],308228);
				      this.renderExpBar(w[4], h[3],4);
				      
				      this.drawString(this.font, "Buliding", w[5], h[0],11208901);
				      this.drawString(this.font, string_template[15], w[5], h[1],11208901);	
				      this.drawString(this.font, string_template[16], w[5], h[2],11208901);
				      this.renderExpBar(w[5], h[3],5);
				      //bonuses
				      this.drawString(this.font, "Bonuses", w[0], h[3]+50, 10526880);
				      this.drawString(this.font, string_template[10], w[0], h[4]+50, 10526880);
				      this.drawString(this.font, string_template[11], w[0], h[5]+50, 10526880);
				      this.drawString(this.font, string_template[12], w[0], h[6]+50, 10526880);
				      this.drawString(this.font, string_template[13], w[0], h[7]+50, 10526880);
				      this.drawString(this.font, string_template[14], w[0], h[8]+50, 10526880);
		      }
		      else
		      {
		    	  this.drawCenteredString(this.font, "Ups something wrong..", this.width / 2, this.height / 5, 16777215);
		      }
		      if(scale != 1F)
		      {
		      GlStateManager.translatef((this.width/2)/scale+button1_position.getX() - (Math.round((this.width/2)+(button1_position.getX()*scale))), (this.height/2)/scale+button1_position.getY() - (Math.round((this.height/2)+(button1_position.getY()*scale))), 1F);
		      }
		      button1.render(p_render_1_, p_render_2_, p_render_3_);	  
	    	  GlStateManager.popMatrix();
			 
		   }

	
	
}
