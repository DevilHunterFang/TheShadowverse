 package shadowverseCharbosses.powers.bossmechanicpowers;

 import com.badlogic.gdx.graphics.Texture;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;


 public class EnemyNightmareTimePower
   extends AbstractPower
 {
   private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:EnemyNightmareTimePower");
   public static final String POWER_ID = "shadowverse:EnemyNightmareTimePower";
   public static final String NAME = powerStrings.NAME;
   public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

   public EnemyNightmareTimePower(AbstractCreature owner, int drawAmount) {
     this.name = NAME;
     this.ID = POWER_ID;
     this.owner = owner;
     this.amount = drawAmount;
     updateDescription();
     this.img = new Texture("img/powers/NightmareTimePower.png");
     this.priority = 20;
   }
 
   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
     this.type = PowerType.BUFF;
   }
   
   public void atStartOfTurnPostDraw() {
     flash();
   }

 }

