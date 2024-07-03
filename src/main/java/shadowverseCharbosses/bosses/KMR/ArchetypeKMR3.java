 package shadowverseCharbosses.bosses.KMR;

 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.cards.nemesis.*;

 import java.util.ArrayList;

 public class ArchetypeKMR3 extends ArchetypeBaseKMR {
   public ArchetypeKMR3() {
     super("KMR_CALAMITY", "KMR_CALAMITY");
     this.actNum = 4;
   }
   
   public void addedPreBattle() {
     super.addedPreBattle();
     AbstractCharBoss abstractCharBoss = AbstractCharBoss.boss;
   }

   public void initialize() {
     boolean extraUpgrades = (AbstractDungeon.ascensionLevel >= 19);
   }
 
   
   public ArrayList<AbstractCard> getThisTurnCards() {
     ArrayList<AbstractCard> cardsList = new ArrayList<>();
     boolean extraUpgrades = (AbstractDungeon.ascensionLevel >= 19);
     if (!this.looped) {
       switch (this.turn) {
         case 0:
           addToList(cardsList, (AbstractCard)new CalamityMode(),extraUpgrades);
           this.turn++;
           break;
         case 1:
           addToList(cardsList, (AbstractCard)new JusticeTyrant(), extraUpgrades);
           this.turn++;
           break;
         case 2:
           addToList(cardsList, (AbstractCard)new ShangrilaBreaker(),extraUpgrades);
           this.turn++;
           break;
         case 3:
           addToList(cardsList, (AbstractCard)new CalamityEnd(),extraUpgrades);
           this.turn = 0;
           this.looped = true;
       } 
     } else {
       addToList(cardsList, (AbstractCard)new EnDeath(),extraUpgrades);
     } 
     return cardsList;
   }
 
   
   public void initializeBonusRelic() {
   }
 }

