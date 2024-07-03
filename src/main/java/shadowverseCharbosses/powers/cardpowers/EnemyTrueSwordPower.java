 package shadowverseCharbosses.powers.cardpowers;

 import com.badlogic.gdx.graphics.Texture;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;

 public class EnemyTrueSwordPower extends AbstractPower {
     public static final String POWER_ID = "shadowverse:EnTrueSwordPower";
     private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:TrueSwordPower");
     public static final String NAME = powerStrings.NAME;
     public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
     public boolean upgraded;
 
   
   public EnemyTrueSwordPower(AbstractCreature owner,boolean upgraded) {
       this.name = NAME;
       this.ID = POWER_ID;
       this.owner = owner;
       this.amount = -1;
       this.upgraded = upgraded;
       this.type = PowerType.BUFF;
       updateDescription();
       this.img = new Texture("img/powers/TrueSwordPower.png");
   }

   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0];
   }

 }
