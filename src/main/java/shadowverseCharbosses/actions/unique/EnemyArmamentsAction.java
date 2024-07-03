 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.localization.UIStrings;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.ArrayList;
 
 
 
 public class EnemyArmamentsAction
   extends AbstractGameAction
 {
   public static final String[] TEXT;
   private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ArmamentsAction"); static {
     TEXT = uiStrings.TEXT;
   }
   
   private AbstractCharBoss p;
   private ArrayList<AbstractCard> cannotUpgrade;
   private boolean upgraded;
   
   public EnemyArmamentsAction(boolean armamentsPlus) {
     this.cannotUpgrade = new ArrayList<>();
     this.upgraded = false;
     this.actionType = ActionType.CARD_MANIPULATION;
     this.p = AbstractCharBoss.boss;
     this.duration = Settings.ACTION_DUR_FAST;
     this.upgraded = armamentsPlus;
   }
 
   
   public void update() {
     if (this.duration == Settings.ACTION_DUR_FAST) {
       if (this.upgraded) {
         for (AbstractCard c : this.p.hand.group) {
           if (c.canUpgrade()) {
             c.upgrade();
             c.superFlash();
             c.applyPowers();
           } 
         } 
         this.isDone = true;
         return;
       } 
       for (AbstractCard c : this.p.hand.group) {
         if (!c.canUpgrade()) {
           this.cannotUpgrade.add(c);
         }
       } 
       if (this.cannotUpgrade.size() == this.p.hand.group.size()) {
         this.isDone = true;
         return;
       } 
       for (AbstractCard c : this.p.hand.group) {
         if (c.canUpgrade()) {
           c.upgrade();
           c.superFlash();
           c.applyPowers();
           this.isDone = true;
           return;
         } 
       } 
     } 
     tickDuration();
   }
 }
