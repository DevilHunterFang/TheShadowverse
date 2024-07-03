 package shadowverseCharbosses.powers.cardpowers;

 import com.badlogic.gdx.graphics.Texture;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.DamageAction;
 import com.megacrit.cardcrawl.actions.common.HealAction;
 import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;

 public class EnemyAmaterasuPower extends AbstractPower {
     public static final String POWER_ID = "shadowverse:EnAmaterasuPower";
     private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:EnAmaterasuPower");
     public static final String NAME = powerStrings.NAME;
     public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
 
   
   public EnemyAmaterasuPower(AbstractCreature owner, int amount) {
       this.name = NAME;
       this.ID = POWER_ID;
       this.owner = owner;
       this.amount = amount;
       this.type = PowerType.BUFF;
       updateDescription();
       this.img = new Texture("img/powers/AmaterasuPower.png");
   }
   
   public void stackPower(int stackAmount) {
       this.fontScale = 8.0F;
       this.amount += stackAmount;
       updateDescription();
   }
   
   public int onAttacked(DamageInfo info, int damageAmount) {
     if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner) {
       flash();
       addToTop(new HealAction(this.owner,this.owner,this.amount));
     } 
     
     return damageAmount;
   }

   public void atStartOfTurn() {
     addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
   }
   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
   }
   
 }
