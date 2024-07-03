 package shadowverseCharbosses.stances;

 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.localization.StanceStrings;
 
 public class EnNeutralStance
   extends AbstractEnemyStance
 {
   public static final String STANCE_ID = "Neutral";
   
   public EnNeutralStance() {
     this.ID = "Neutral";
     this.img = null;
     this.name = null;
     updateDescription();
   }
   
   public void updateDescription() {
     this.description = stanceString.DESCRIPTION[0];
   }
 
   
   public void onEnterStance() {}
 
   
   public void render(SpriteBatch sb) {}
 
   
   private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString("Neutral");
 }
