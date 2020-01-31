package com.DystryktZ.GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;


import com.DystryktZ.ZEventHandlerClient;
import com.DystryktZ.ZmodJson;
import com.DystryktZ.ranking.RankPacker;
import com.DystryktZ.utils.Vector2D;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ZRanking extends Screen {
	
	private ZGuiScreen back_parent;
	boolean received_packet = false;
	private List<RankPacker> MiningScore;
	private List<RankPacker> DiggingScore;
	private List<RankPacker> CombatScore;
	private List<RankPacker> CuttersScore;
	private List<RankPacker> FarmersScore;
	private List<RankPacker> BuildersScore;
	float scale=1F;
	private Vector2D<Integer, Integer> button1_position;
	private Button button1;
	public double[] Bonus = new double[5];
	
	protected ZRanking(ZGuiScreen zGuiScreen) {
		super(new TranslationTextComponent("Ranking"));
		back_parent = zGuiScreen;
	}
	
	 protected void init() {
		 if(!received_packet)
		 {
		 ZEventHandlerClient.getRanking(this);
		 }	 
		 
		  switch((int)this.minecraft.func_228018_at_().getGuiScaleFactor())
		   {
		   case 1:
			   scale = 1F;
			   break;
			   
		   case 2:
			   if(!this.minecraft.func_228018_at_().isFullscreen())
			   {  
				   if(this.minecraft.func_228018_at_().getWidth() > 1400 && this.minecraft.func_228018_at_().getHeight() > 800)
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
		  
		  int resolution_width = this.minecraft.func_228018_at_().getFramebufferWidth();
		  if((int)this.minecraft.func_228018_at_().getGuiScaleFactor() > 1 && resolution_width <= 1440)
		  {
		  if(this.minecraft.func_228018_at_().isFullscreen())
		  {
			  scale = scale * 0.75F;
		  }
		  }
		  
		  button1_position = new Vector2D<Integer, Integer>(-100,200);
		 
		button1 = this.addButton(new Button((int) (Math.round((this.width/2)+(button1_position.getX()*scale))), (int) (Math.round((this.height/2)+(button1_position.getY()*scale))), 200, 20, "Return", (p_213079_1_) -> {
	         this.minecraft.displayGuiScreen(this.back_parent);
	      }));
				
	 }
	 
	 public void PacketRespond(CompoundNBT nbt)
	 {
		 if(received_packet) { return; }
		 MiningScore = new ArrayList<RankPacker>();
		 DiggingScore = new ArrayList<RankPacker>();
		 CombatScore = new ArrayList<RankPacker>();
		 CuttersScore = new ArrayList<RankPacker>();
		 FarmersScore = new ArrayList<RankPacker>();
		 BuildersScore = new ArrayList<RankPacker>();
		 int rank;
		 int category;
		 String real_name;		  
		  Set<String> names = nbt.keySet();
		  for(String name : names)
		  {
			  rank = Integer.parseInt(name.substring(0,1));
			  category = Integer.parseInt(name.substring(2,3));
			  real_name = name.substring(4,name.length());
			  switch(category)
			  {
			  case 0: //mining
				  MiningScore.add(new RankPacker(real_name,rank,nbt.getInt(name)));
				  break;
				  
			  case 1: //digging
				  DiggingScore.add(new RankPacker(real_name,rank,nbt.getInt(name)));
				  break;
				  
			  case 2: //combat
				  CombatScore.add(new RankPacker(real_name,rank,nbt.getInt(name)));
				  break;
				  
			  case 3: //woodcutting
				  CuttersScore.add(new RankPacker(real_name,rank,nbt.getInt(name)));
				  break;
				  
			  case 4: //farming
				  FarmersScore.add(new RankPacker(real_name,rank,nbt.getInt(name)));
				  break;
				  
			  case 5: //building
				  BuildersScore.add(new RankPacker(real_name,rank,nbt.getInt(name)));
				  break;
			  }
		  }
		  
		  Collections.sort(MiningScore);
		  Collections.sort(DiggingScore);
		  Collections.sort(CombatScore);
		  Collections.sort(CuttersScore);
		  Collections.sort(FarmersScore);
		  Collections.sort(BuildersScore);
		  
		  getPlayerBonuses();
		  
	      createEntityToShow();
		  received_packet = true;
	 }
	 
	 private void getPlayerBonuses()
	 {
		 String name = this.minecraft.player.getName().getUnformattedComponentText();
		 //default values
		 for(int i=0;i<5;i++)
		 {
			 Bonus[i] = 1.0;
		 }
		 
		 for(int i = 0;i<3;i++)
		 {
			 if(i>=MiningScore.size()) { break;}
			 if(MiningScore.get(i).getName().equals(name))
			 {
				 Bonus[0] = getBonusFromIndex(i);
				 break;
			 }
		 }
		 
		 for(int i = 0;i<3;i++)
		 {
			 if(i>=DiggingScore.size()) { break;}
			 if(DiggingScore.get(i).getName().equals(name))
			 {
				 Bonus[1] = getBonusFromIndex(i);
				 break;
			 } 
		 }
		 
		 for(int i = 0;i<3;i++)
		 {
			 if(i>=CombatScore.size()) { break;}
			 if(CombatScore.get(i).getName().equals(name))
			 {
				 Bonus[2] = getBonusFromIndex(i);
				 break;
			 }
		 }
		 
		 for(int i = 0;i<3;i++)
		 {
			 if(i>=CuttersScore.size()) { break;}
			 if(CuttersScore.get(i).getName().equals(name))
			 {
				 Bonus[3] = getBonusFromIndex(i);
				 break;
			 }
		 }
			 
		 for(int i = 0;i<3;i++)
		 {
			 if(i>=FarmersScore.size()) { break;}
			 if(FarmersScore.get(i).getName().equals(name))
			 {
				 Bonus[4] = getBonusFromIndex(i);
				 break;
			 } 
		 }		 
	 }
	 
	 private double getBonusFromIndex(int index)
	 {
		 switch(index)
		 {
		 case 0:
			 return ZmodJson.RankingBonusSettings[0];
		 case 1:
			 return ZmodJson.RankingBonusSettings[1];
		 case 2:
			 return ZmodJson.RankingBonusSettings[2];
		 }
		 return 1;
	 }
	 
	 Entity[] entList;

	private void createEntityToShow()
	 {
		 World w = this.minecraft.player.world;
		 List<AbstractClientPlayerEntity> listaGraczy = this.minecraft.world.getPlayers();
		 Entity[] e = new Entity[6];
		 
		 for(AbstractClientPlayerEntity gracz : listaGraczy)
		 {
			 if(MiningScore.size() > 0)
			 {
			 if(gracz.getName().getUnformattedComponentText().equals(MiningScore.get(0).getName()))
			 {
				 e[0] = getPlayerEntity(gracz);
			 }}
			 
			 if(DiggingScore.size() > 0)
			 {
			 if(gracz.getName().getUnformattedComponentText().equals(DiggingScore.get(0).getName()))
			 {
				 e[1] = getPlayerEntity(gracz);
			 }}
			 
			 if(CombatScore.size() > 0)
			 {
			 if(gracz.getName().getUnformattedComponentText().equals(CombatScore.get(0).getName()))
			 {
				e[2] = getPlayerEntity(gracz);
			 }}
			 
			 if(CuttersScore.size() > 0)
			 {
			 if(gracz.getName().getUnformattedComponentText().equals(CuttersScore.get(0).getName()))
			 {
				 e[3] = getPlayerEntity(gracz);
			 }}
			 
			 if(FarmersScore.size() > 0)
			 {
			 if(gracz.getName().getUnformattedComponentText().equals( FarmersScore.get(0).getName()))
			 {
				 e[4] = getPlayerEntity(gracz);
			 }}
			 
			 if(BuildersScore.size() > 0)
			 {
			 if(gracz.getName().getUnformattedComponentText().equals( BuildersScore.get(0).getName()))
			 {
				 e[5] = getPlayerEntity(gracz);
			 }}
		 }	
		 
		 boolean nobody;
		 for(int i = 0 ;i< 6;i++)
		 {
			 if(e[i] == null)
			 {	
				 nobody = false;
				 switch(i)
				 {
				 case 0:
					 if(MiningScore.size() == 0)
					 {
						nobody = true;
					 }
			     break;
			     
				 case 1:
					 if(DiggingScore.size() == 0)
					 {
						 nobody = true;
					 }
			     break;
			     
				 case 2:
					 if(CombatScore.size() == 0)
					 {
						 nobody = true;
					 }
			     break;
			     
				 case 3:
					 if(CuttersScore.size() == 0)
					 {
						 nobody = true;
					 }
			     break;
			     
				 case 4:
					 if(FarmersScore.size() == 0)
					 {
						 nobody = true;
					 }
			     break;
			     
				 case 5:
					 if(BuildersScore.size() == 0)
					 {
						 nobody = true;
					 }
			     break;
				 }
				 
				 if(nobody) {  e[i] = new RemoteClientPlayerEntity((ClientWorld) w, new GameProfile(new UUID(1,1),"???"));}
				 else {
				 e[i] = new RemoteClientPlayerEntity((ClientWorld) w, new GameProfile(new UUID(1,1),"")); }
				 
			 }
		 }
			
			entList = e;
	 }
	 
	 private Entity getPlayerEntity(AbstractClientPlayerEntity gracz)
	 {
		 World w = this.minecraft.player.world;
		 switch(gracz.getName().getUnformattedComponentText())
		 {
		 case "Xeriss":
			 return  EntityType.PIG.create(w);
			 
		 case "Swoew":
			 return EntityType.WITCH.create(w);
			 
		 case "Yorgen":
			 return EntityType.TRADER_LLAMA.create(w);
		 }
		 return new RemoteClientPlayerEntity((ClientWorld) w, new GameProfile(gracz.getUniqueID(),gracz.getName().getUnformattedComponentText()));
	 }
	 
	 private float model_rotate = 0F;
	 public void drawEntityOnScreen(int x_pos, int y_pos, LivingEntity p_228187_5_) {
	      RenderSystem.pushMatrix();
	      RenderSystem.translatef((float)x_pos, (float)y_pos, 1050.0F);
    	  model_rotate += 0.1F;
    	  if(model_rotate >= 360F)
    	  {
    		  model_rotate = 0F;
    	  }
	      RenderSystem.scalef(0.35f, 0.35F, -0.35F);
	      MatrixStack matrixstack = new MatrixStack();
	      matrixstack.func_227861_a_(0.0D, 60.0D, 1000.0D);
	      matrixstack.func_227862_a_(150F, 150F, 50F);
	      Quaternion quaternion = Vector3f.field_229183_f_.func_229187_a_(180.0F);
	      matrixstack.func_227863_a_(quaternion);
	      p_228187_5_.renderYawOffset = 180.0F + model_rotate;
	      p_228187_5_.rotationYaw = 180.0F + model_rotate;
	      p_228187_5_.rotationYawHead = p_228187_5_.rotationYaw;
	      p_228187_5_.rotationPitch = -2F; //glowa
	      p_228187_5_.prevRotationYawHead = p_228187_5_.rotationYaw;
	      EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
	      entityrenderermanager.setRenderShadow(false);
	      IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().func_228019_au_().func_228487_b_();
	      entityrenderermanager.func_229084_a_(p_228187_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
	      irendertypebuffer$impl.func_228461_a_();
	      entityrenderermanager.setRenderShadow(true);
	      RenderSystem.popMatrix();
	   }
	 
	 
	
	 
	
	
	 public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
	      this.renderBackground();
	      RenderSystem.pushMatrix();
    	  RenderSystem.scalef(scale, scale, scale); 
	      this.drawCenteredString(this.font, this.title.getFormattedText(), (int)((this.width / 2)/scale), (int) (Math.round(this.height/2/scale)-220), 16777215);
	      if(received_packet == false)
	      {
	    	  this.drawCenteredString(this.font, "Loading..", (int) ((this.width/2)/(scale)), (int)(this.height/2/scale) - 150, 16777215);
	      }
	      else
	      {	    	 
	    	  this.drawCenteredString(this.font, "Best MINER", (int) (Math.round(this.width/2)/(scale)-375), (int) (Math.round(this.height/2/scale)-160), 8684676);
	    	  this.drawCenteredString(this.font, "Best DIGGER", (int) (Math.round(this.width/2)/(scale)-225), (int) (Math.round(this.height/2/scale)-160), 14644225);
	    	  this.drawCenteredString(this.font, "Best FIGHTER", (int) (Math.round(this.width/2)/(scale)-75), (int) (Math.round(this.height/2/scale)-160), 14614785);
	    	  this.drawCenteredString(this.font, "Best WOODCUTTER", (int) (Math.round(this.width/2)/(scale)+75), (int) (Math.round(this.height/2/scale)-160), 6371339);
	    	  this.drawCenteredString(this.font, "Best FARMER", (int) (Math.round(this.width/2)/(scale)+225), (int) (Math.round(this.height/2/scale)-160), 308228);
	    	  this.drawCenteredString(this.font, "Best BOB", (int) (Math.round(this.width/2)/(scale)+375), (int) (Math.round(this.height/2/scale)-160), 11208901);
	    	  int i=0;
	    	  for(Entity e : entList)
	    	  {
	    		  if(e.getName().getUnformattedComponentText().equals("???")) {RenderSystem.color3f(0F, 0F, 0F);}	    		
	    		 // drawEntityOnScreen((int)(this.width/2/scale)-375+(i*150),(int)(this.height/2/scale),60,e);
	    		  int i_temp = (int)(this.width/2/scale)-375+(i*150);
	    		  int j_temp = (int)(this.height/2/scale-40);
	    		  drawEntityOnScreen(i_temp, j_temp, (LivingEntity)e);
	    		  switch(i)
	    		  {	    	
	    		  case 0: //mining
	    			  if(MiningScore.size() > 0)
	    			  {
	    				  this.drawCenteredString(this.font, "1: "+MiningScore.get(0).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+20), 13452343);
	    				  this.drawCenteredString(this.font, "Exp: "+MiningScore.get(0).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40), 13452343);
	    			  }
	    			  
	    			  //draw next scores
	    			  for(int v = 1;v<5;v++)
	    			  {
	    				  if(v < MiningScore.size())
	    				  {
	    					 this.drawCenteredString(this.font, (v+1)+": "+MiningScore.get(v).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40+(v*20)), 13421258);
	    					 this.drawCenteredString(this.font, "Exp: "+MiningScore.get(v).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+50+(v*20)), 13421258);
	    				  }
	    			  }
	    			  
	    		  break;
	    		  
	    		  case 1: //digging
	    			  if(DiggingScore.size() > 0)
		    		  {
		    		  this.drawCenteredString(this.font, "1: "+ DiggingScore.get(0).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+20), 13452343);
		    		  this.drawCenteredString(this.font, "Exp: "+DiggingScore.get(0).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40), 13452343);
		    		  }
	    			  
	    			  for(int v = 1;v<5;v++)
	    			  {
	    				  if(v < DiggingScore.size())
	    				  {
	    					 this.drawCenteredString(this.font, (v+1)+": "+DiggingScore.get(v).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40+(v*20)), 13421258);
	    					 this.drawCenteredString(this.font, "Exp: "+DiggingScore.get(v).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+50+(v*20)), 13421258);
	    				  }
	    			  }
	    		  break;
	    		  
	    		  case 2: //combat
	    			  if(CombatScore.size() > 0)
		    		  {
		    		  this.drawCenteredString(this.font, "1: "+ CombatScore.get(0).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+20), 13452343);
		    		  this.drawCenteredString(this.font, "Exp: "+CombatScore.get(0).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40), 13452343);
		    		  }
	    			  
	    			  for(int v = 1;v<5;v++)
	    			  {
	    				  if(v < CombatScore.size())
	    				  {
	    					 this.drawCenteredString(this.font, (v+1)+": "+CombatScore.get(v).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40+(v*20)), 13421258);
	    					 this.drawCenteredString(this.font, "Exp: "+CombatScore.get(v).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+50+(v*20)), 13421258);
	    				  }
	    			  }
	    		  break;
	    		  
	    		  case 3: //woodcutters
	    			  if(CuttersScore.size() > 0)
		    		  {
		    		  this.drawCenteredString(this.font, "1: "+ CuttersScore.get(0).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+20), 13452343);
		    		  this.drawCenteredString(this.font, "Exp: "+CuttersScore.get(0).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40), 13452343);
		    		  }
	    			  
	    			  for(int v = 1;v<5;v++)
	    			  {
	    				  if(v < CuttersScore.size())
	    				  {
	    					 this.drawCenteredString(this.font, (v+1)+": "+CuttersScore.get(v).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40+(v*20)), 13421258);
	    					 this.drawCenteredString(this.font, "Exp: "+CuttersScore.get(v).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+50+(v*20)), 13421258);
	    				  }
	    			  }
	    			  break;
	    			  
	    			  
	    		  case 4: //farmers
	    			  if(FarmersScore.size() > 0)
		    		  {
		    		  this.drawCenteredString(this.font, "1: "+ FarmersScore.get(0).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+20), 13452343);
		    		  this.drawCenteredString(this.font, "Exp: "+FarmersScore.get(0).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40), 13452343);
		    		  }
	    			  
	    			  for(int v = 1;v<5;v++)
	    			  {
	    				  if(v < FarmersScore.size())
	    				  {
	    					 this.drawCenteredString(this.font, (v+1)+": "+FarmersScore.get(v).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40+(v*20)), 13421258);
	    					 this.drawCenteredString(this.font,"Exp: "+FarmersScore.get(v).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+50+(v*20)), 13421258);
	    				  }
	    			  }
	    			  break;
	    			  
	    			  
	    		  case 5: //builders
	    			  if(BuildersScore.size() > 0)
		    		  {
		    		  this.drawCenteredString(this.font, "1: "+  BuildersScore.get(0).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+20), 13452343);
		    		  this.drawCenteredString(this.font, "Exp: "+BuildersScore.get(0).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40), 13452343);
		    		  }
	    			  
	    			  for(int v = 1;v<5;v++)
	    			  {
	    				  if(v < BuildersScore.size())
	    				  {
	    					 this.drawCenteredString(this.font, (v+1)+": "+BuildersScore.get(v).getName(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+40+(v*20)), 13421258);
	    					 this.drawCenteredString(this.font, "Exp: "+BuildersScore.get(v).getScore(), (int) (Math.round(this.width/2)/(scale)-375+(i*150)), (int) (Math.round(this.height/2/scale)+50+(v*20)), 13421258);
	    				  }
	    			  }
	    			  break;
	    		  
	    		  }
	    		  i++;
	    	  }
	    	  
	    	  this.drawString(this.font, "Ranking bonuses", (int)((this.width / 2)/scale + 60), (int) (Math.round(this.height/2/scale)-220), 16579836);
		      this.drawString(this.font, "Mining luck:", (int)((this.width / 2)/scale + 60), (int) (Math.round(this.height/2/scale)-210), 16579836);
		      this.drawString(this.font, "x"+String.format("%2.2f", Bonus[0]), (int)((this.width / 2)/scale + 140), (int) (Math.round(this.height/2/scale)-210), 21730944);
		      this.drawString(this.font, "Digging luck:", (int)((this.width / 2)/scale + 60), (int) (Math.round(this.height/2/scale)-200), 16579836);
		      this.drawString(this.font, "x"+String.format("%2.2f", Bonus[1]), (int)((this.width / 2)/scale + 140),(int) (Math.round(this.height/2/scale)-200) , 21730944);
		      this.drawString(this.font, "Combat luck:", (int)((this.width / 2)/scale + 60), (int) (Math.round(this.height/2/scale)-190), 16579836);
		      this.drawString(this.font, "x"+String.format("%2.2f", Bonus[2]), (int)((this.width / 2)/scale + 140), (int) (Math.round(this.height/2/scale)-190), 21730944);
		      this.drawString(this.font, "Woodcutting luck:", (int)((this.width / 2)/scale + 190), (int) (Math.round(this.height/2/scale)-210), 16579836);
		      this.drawString(this.font, "x"+String.format("%2.2f", Bonus[3]), (int)((this.width / 2)/scale + 290), (int) (Math.round(this.height/2/scale)-210), 21730944);
		      this.drawString(this.font, "Farming luck:", (int)((this.width / 2)/scale + 190), (int) (Math.round(this.height/2/scale)-200), 16579836);
		      this.drawString(this.font, "x"+String.format("%2.2f", Bonus[4]), (int)((this.width / 2)/scale + 290), (int) (Math.round(this.height/2/scale)-200), 21730944); 	  
	      }
	      
	      
	      //RenderSystem.
	      if(scale != 1F)
	      {
	      RenderSystem.translatef((this.width/2)/scale+button1_position.getX() - (Math.round((this.width/2)+(button1_position.getX()*scale))), (this.height/2)/scale+button1_position.getY() - (Math.round((this.height/2)+(button1_position.getY()*scale))), 1F);
	      }
	      button1.render(p_render_1_, p_render_2_, p_render_3_);
	      //super.render(p_render_1_, p_render_2_, p_render_3_);
	      RenderSystem.popMatrix();
	   }
	      

}
