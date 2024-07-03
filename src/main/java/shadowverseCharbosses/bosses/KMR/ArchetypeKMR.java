 package shadowverseCharbosses.bosses.KMR;

 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.cards.bishop.*;
 import shadowverseCharbosses.cards.bishop.EnMoonAlmiraj;

 import java.util.ArrayList;
 
 public class ArchetypeKMR extends ArchetypeBaseKMR {
   public ArchetypeKMR() {
     super("KMR_BISHOP", "KMR_BISHOP");
     if (AbstractDungeon.ascensionLevel >= 19){
       this.maxHPModifier += 300;
     }else if (AbstractDungeon.ascensionLevel >= 4){
       this.maxHPModifier += 200;
     }
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
           addToList(cardsList, new EnAcolyteLight(),extraUpgrades);
           addToList(cardsList, new EnHereticalInquiry(),extraUpgrades);
           addToList(cardsList, new EnArdentSister(),extraUpgrades);
           this.turn++;
           break;
         case 1:
           addToList(cardsList, new SacredLion(), extraUpgrades);
           addToList(cardsList, new EnDarkPriest(), extraUpgrades);
           addToList(cardsList, new EnPriestOfTheCudgel(),extraUpgrades);
           this.turn++;
           break;
         case 2:
           addToList(cardsList, new EnRadiantAngel(),extraUpgrades);
           addToList(cardsList, new EnBlackenedScripture(),extraUpgrades);
           addToList(cardsList, new EnAncientLionSpirit(),extraUpgrades);
           this.turn++;
           break;
         case 3:
           addToList(cardsList, new EnThemisDecree(),extraUpgrades);
           addToList(cardsList, new EnPriestOfTheCudgel(),extraUpgrades);
           addToList(cardsList, new EnDarkPriest(),extraUpgrades);
           addToList(cardsList, new EnSacredPlea(),extraUpgrades);
           this.turn++;
           break;
         case 4:
           addToList(cardsList, new EnHereticalInquiry(), extraUpgrades);
           addToList(cardsList, new EnLucifer(), extraUpgrades);
           addToList(cardsList, new EnDarkPriest(),extraUpgrades);
           addToList(cardsList, new EnHeavenlyHound(), extraUpgrades);
           addToList(cardsList, new EnArdentSister(), extraUpgrades);
           this.turn++;
           break;
         case 5:
           addToList(cardsList, new SacredLion(),extraUpgrades);
           addToList(cardsList, new EnAncientLionSpirit(),extraUpgrades);
           addToList(cardsList, new EnHeavenlyHound(),extraUpgrades);
           this.turn = 0;
           this.looped = true;
           break;
       } 
     } else {
       switch (this.turn) {
         case 0:
           addToList(cardsList, new SacredLion(),extraUpgrades);
           addToList(cardsList, new EnDarkPriest(),extraUpgrades);
           addToList(cardsList, new EnArdentSister(),extraUpgrades);
           this.turn++;
           break;
         case 1:
           addToList(cardsList, new EnPriestOfTheCudgel(), extraUpgrades);
           addToList(cardsList, new EnRadiantAngel(),extraUpgrades);
           addToList(cardsList, new EnHeavenlyHound(),extraUpgrades);
           this.turn++;
           break;
         case 2:
           addToList(cardsList, new EnAcolyteLight(),extraUpgrades);
           addToList(cardsList, new EnLucifer(),extraUpgrades);
           addToList(cardsList, new EnDarkPriest(),extraUpgrades);
           this.turn++;
           break;
         case 3:
           addToList(cardsList, new EnMoonAlmiraj(),extraUpgrades);
           addToList(cardsList, new EnBlackenedScripture(),extraUpgrades);
           addToList(cardsList, new EnHeavenlyHound(),extraUpgrades);
           this.turn = 0;
           break;
       } 
     } 
     return cardsList;
   }
 
   
   public void initializeBonusRelic() {
   }
 }

