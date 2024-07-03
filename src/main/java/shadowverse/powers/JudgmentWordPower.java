 package shadowverse.powers;


 import com.badlogic.gdx.graphics.Texture;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
 import com.megacrit.cardcrawl.characters.AbstractPlayer;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.powers.ArtifactPower;
 import com.megacrit.cardcrawl.powers.HexPower;
 import com.megacrit.cardcrawl.powers.NoBlockPower;
 import com.megacrit.cardcrawl.ui.panels.EnergyPanel;


 public class JudgmentWordPower
   extends AbstractPower
 {
   public static final String POWER_ID = "shadowverse:JudgmentWordPower";
   private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:JudgmentWordPower");
   public static final String NAME = powerStrings.NAME;
   public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
   public int turnCount = 0;
   private AbstractPlayer p = AbstractDungeon.player;

   public JudgmentWordPower(AbstractCreature owner) {
     this.name = NAME;
     this.ID = POWER_ID;
     this.owner = owner;
     this.amount = -1;
     this.type = PowerType.BUFF;
     updateDescription();
     this.img = new Texture("img/powers/VincentPower.png");
   }

   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0];
   }

     @Override
     public void atEndOfTurn(boolean isPlayer) {
         if (EnergyPanel.getCurrentEnergy()>0)
             addToBot((AbstractGameAction)new ApplyPowerAction(this.owner,this.owner,(AbstractPower)new ArtifactPower(this.owner,1),1));
     }

     @Override
   public void atStartOfTurnPostDraw() {
       turnCount++;
       flash();
       switch (turnCount){
           case 1:
               addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p,(AbstractCreature)p,(AbstractPower)new NoPower((AbstractCreature)p)));
               break;
           case 2:
               addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new HexPower((AbstractCreature)p, 1), 1));
               break;
           case 3:
               addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new NoBlockPower((AbstractCreature)p, 1, false), 1));
               if (p.hasPower(HexPower.POWER_ID))
                   addToBot((AbstractGameAction)new RemoveSpecificPowerAction((AbstractCreature)p,(AbstractCreature)p,HexPower.POWER_ID));
               break;
           case 4:
               addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p,(AbstractCreature)p,(AbstractPower)new NoDamage((AbstractCreature)p)));
               turnCount=0;
               break;
           default:break;
       }
   }
 }
