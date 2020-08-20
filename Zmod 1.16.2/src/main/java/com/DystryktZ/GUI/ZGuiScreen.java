package com.DystryktZ.GUI;

import com.DystryktZ.ZEventHandlerClient;
import com.DystryktZ.ZmodJson;
import com.DystryktZ.Capability.IZStat;
import com.DystryktZ.Capability.ZStatController;
import com.DystryktZ.utils.Vector2D;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

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
	
	 protected void func_231160_c_() {
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
		 
		   switch((int)this.field_230706_i_.getMainWindow().getGuiScaleFactor())
		   {
		   case 1:
			   scale = 1F;
			   break;
			   
		   case 2: 
			   if(!this.field_230706_i_.getMainWindow().isFullscreen())
			   {  
				   if(this.field_230706_i_.getMainWindow().getWidth() > 1400 && this.field_230706_i_.getMainWindow().getHeight() > 800)
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
		   
		   	  int resolution_width = this.field_230706_i_.getMainWindow().getFramebufferWidth();
			  if((int)this.field_230706_i_.getMainWindow().getGuiScaleFactor() > 1 && resolution_width <= 1440)
			  {
			  if(this.field_230706_i_.getMainWindow().isFullscreen())
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
				   //TODO i doesnt know if this is working
			   w[i] = (int) (Math.round((this.field_230708_k_/2)/scale -370 + (140*i))); 
			   }
			   h[i] = (int) (Math.round((this.field_230709_l_/2)/scale) -110 + (20*i));		   
		   }
		   
		   button1_position = new Vector2D<Integer, Integer>(190,-160);
		   
		   
		   //add buttons
		   
		   button1 = this.func_230480_a_(new Button((int) (Math.round((this.field_230708_k_/2)+(button1_position.getX()*scale))), (int) (Math.round((this.field_230709_l_/2)+(button1_position.getY()*scale))), 80, 16, new StringTextComponent("Ranking"), (p_213055_1_) -> {
		         this.field_230706_i_.displayGuiScreen(new ZRanking(this));
		      }));
		   
		   if(this.field_230706_i_.isSingleplayer())
		   {
			   button1.field_230693_o_ = false;
		   }
	 }
	 
	 private final float RWidth = 50F;
	 private final float RHeight = 5F;
	 private void renderExpBar(float x, float y , int index)
	 {
		    RenderSystem.beginInitialization();
		    Tessellator tessellator = Tessellator.getInstance();
		    BufferBuilder buf = tessellator.getBuffer();
		    //rentacle
		    RenderSystem.lineWidth(2F);
		    buf.begin(1, DefaultVertexFormats.POSITION_COLOR);
		    //up
		    buf.addVertex(x, y, 0, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0, 0, 0, 0, 0, 0);
		    buf.addVertex(x+RWidth, y, 0, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0, 0, 0, 0, 0, 0);
		    
		    //down
		    buf.addVertex(x, y+RHeight, 0, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0, 0, 0, 0, 0, 0);
		    buf.addVertex(x+RWidth, y+RHeight, 0, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0, 0, 0, 0, 0, 0);
		    
		    //left
		    buf.addVertex(x, y, 0, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0, 0, 0, 0, 0, 0);
		    buf.addVertex(x, y+RHeight, 0, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0, 0, 0, 0, 0, 0);
		    
		    //right
		    buf.addVertex(x+RWidth, y, 0, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0, 0, 0, 0, 0, 0);
		    buf.addVertex(x+RWidth, y+RHeight, 0, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0, 0, 0, 0, 0, 0);
		    
		    tessellator.draw();
		    
		    RenderSystem.lineWidth(RHeight - 1F);
		    buf.begin(1, DefaultVertexFormats.POSITION_COLOR);
		    buf.addVertex(x+2F, y+2F, 0, 1.0F, 0, 0, 0.47F, 0, 0, 0, 0, 0, 0, 0);
		    buf.addVertex(x+RWidth-2F, y+2F, 0, 1.0F, 0, 0, 0.47F, 0, 0, 0, 0, 0, 0, 0);
		    tessellator.draw();
		    
		    //oblicz

		    RenderSystem.lineWidth(RHeight - 1F);
		    buf.begin(1, DefaultVertexFormats.POSITION_COLOR);
		    buf.addVertex(x+2F, y+2F, 0, 0, 0, 1.0F, 1.0F, 0, 0, 0, 0, 0, 0, 0);
		    buf.addVertex(x+(RWidth-2F)*player_exp_ratio[index], y+2F, 0, 0, 0, 1.0F, 1.0F, 0, 0, 0, 0, 0, 0, 0);
		    tessellator.draw();
		    
		    RenderSystem.finishInitialization();
	 }
	 
	   public void func_230430_a_(MatrixStack matrixStack,int p_render_1_, int p_render_2_, float p_render_3_) {
		     this.func_230446_a_(matrixStack);     
		      if(iz!=null)
		      {	  	    	  
		    	  RenderSystem.beginInitialization();
		    	  RenderSystem.scalef(scale, scale, scale); 
		    	  this.func_238476_c_(matrixStack,this.field_230712_o_, this.field_230704_d_.getString(), w[2], (int) (Math.round(this.field_230709_l_/2/scale)-160), 16777215);
		    	  //mining
		    	  	  this.func_238476_c_(matrixStack, this.field_230712_o_, "Mining", w[0], h[0], 8684676);
		    		  this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[0], w[0], h[1], 8684676);
		    		  this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[1], w[0], h[2], 8684676);
		    		  this.renderExpBar(w[0], h[3], 0);
		    		  //combat
		    		  this.func_238476_c_(matrixStack,this.field_230712_o_, "Combat", w[1], h[0], 14614785);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[2], w[1], h[1], 14614785);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[3], w[1], h[2], 14614785);
				      this.renderExpBar(w[1], h[3],1);
				      //digging
				      this.func_238476_c_(matrixStack,this.field_230712_o_, "Digging", (int) w[2], h[0], 14644225);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[4], w[2], h[1], 14644225);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[5], w[2], h[2], 14644225);
				      this.renderExpBar(w[2], h[3],2);
				      //woodcutting
				      this.func_238476_c_(matrixStack,this.field_230712_o_, "Woodcutting", w[3], h[0], 6371339);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[6], w[3], h[1], 6371339);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[7], w[3],  h[2], 6371339);
				      this.renderExpBar(w[3], h[3],3);
				      //farming
				      this.func_238476_c_(matrixStack,this.field_230712_o_, "Farming", w[4], h[0],308228);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[8], w[4], h[1],308228);	
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[9], w[4], h[2],308228);
				      this.renderExpBar(w[4], h[3],4);
				      
				      this.func_238476_c_(matrixStack,this.field_230712_o_, "Buliding", w[5], h[0],11208901);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[15], w[5], h[1],11208901);	
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[16], w[5], h[2],11208901);
				      this.renderExpBar(w[5], h[3],5);
				      //bonuses
				      this.func_238476_c_(matrixStack,this.field_230712_o_, "Bonuses", w[0], h[3]+50, 10526880);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[10], w[0], h[4]+50, 10526880);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[11], w[0], h[5]+50, 10526880);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[12], w[0], h[6]+50, 10526880);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[13], w[0], h[7]+50, 10526880);
				      this.func_238476_c_(matrixStack,this.field_230712_o_, string_template[14], w[0], h[8]+50, 10526880);
		      }
		      else
		      {
		    	  this.func_238471_a_(matrixStack,this.field_230712_o_, "Ups something wrong..", (int)(this.RWidth / 2), (int)(this.field_230709_l_ / 5), 16777215);
		      }
		      if(scale != 1F)
		      { 
		    	  RenderSystem.translatef((this.field_230708_k_/2)/scale+button1_position.getX() - (Math.round((this.field_230708_k_/2)+(button1_position.getX()*scale))), (this.field_230709_l_/2)/scale+button1_position.getY() - (Math.round((this.field_230709_l_/2)+(button1_position.getY()*scale))), 1F);
		      }
		      button1.func_230430_a_(matrixStack,p_render_1_, p_render_2_, p_render_3_);	  
		      RenderSystem.finishInitialization();
			 
		   }

	
	
}
