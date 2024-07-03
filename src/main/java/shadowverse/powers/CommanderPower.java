 package shadowverse.powers;

 import com.badlogic.gdx.graphics.Texture;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.monsters.AbstractMonster;
 import com.megacrit.cardcrawl.powers.*;

 public class CommanderPower extends AbstractPower {
   private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:CommanderPower");
   public static final String POWER_ID = "shadowverse:CommanderPower";
   public static final String NAME = powerStrings.NAME;
   public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
   public int turnCount = 0;

   public CommanderPower(AbstractCreature owner) {
     this.name = NAME;
     this.ID = POWER_ID;
     this.owner = owner;
     this.amount = -1;
     this.type = PowerType.BUFF;
     updateDescription();
       this.img = new Texture("img/powers/CommanderPower.png");
   }
 
   
   public void stackPower(int stackAmount) {
     this.amount += stackAmount;
     if (this.amount >= 999)
       this.amount = 999; 
   }
   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0];
   }
 

   
   public void atEndOfRound() {
       flash();
       turnCount++;
       switch (turnCount){
           case 2:
               for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                   if (!m.isDead && !m.isDying && !m.isEscaping){
                       AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m,(AbstractCreature)this.owner,(AbstractPower)new StrengthPower((AbstractCreature)m,1),1));
                   }
               }
               break;
           case 1:
               for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                   if (!m.isDead && !m.isDying && !m.isEscaping){
                       AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m,(AbstractCreature)this.owner,(AbstractPower)new ArtifactPower((AbstractCreature)m,1),1));
                   }
               }
               break;
           case 3:
               for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                   if (!m.isDead && !m.isDying && !m.isEscaping){
                       AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m,(AbstractCreature)this.owner,(AbstractPower)new PlatedArmorPower((AbstractCreature)m,3),3));
                   }
               }
               break;
           case 4:
               AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player,(AbstractCreature)this.owner,(AbstractPower)new WeakPower((AbstractCreature)AbstractDungeon.player,2,false),2));
               this.turnCount=0;
               break;
       }
   }
 }


