 package shadowverseCharbosses.bosses.KMR;

 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.cards.bishop.EnBlackenedScripture;
 import shadowverseCharbosses.cards.bishop.EnMoonAlmiraj;
 import shadowverseCharbosses.cards.nemesis.*;

 import java.util.ArrayList;

 public class ArchetypeKMR2 extends ArchetypeBaseKMR {
   public ArchetypeKMR2() {
     super("KMR_NEMESIS", "KMR_NEMESIS");
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
           addToList(cardsList, (AbstractCard)new AbsoluteOne(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnAncientAmplifier(), extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnAcceleratium(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnMagisteelLion(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnMysticArtifact(),extraUpgrades);
           this.turn++;
           break;
         case 1:
           addToList(cardsList, (AbstractCard)new TheGreatCreation(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnGenesisArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnBlackenedScripture(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnProtectArtifact(), extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnMysticArtifact(),extraUpgrades);
           this.turn++;
           break;
         case 2:
           addToList(cardsList, (AbstractCard)new EnEdgeArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnBlitzArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnMagisteelLion(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new ShadowMode(),extraUpgrades);
           this.turn++;
           break;
         case 3:
           addToList(cardsList, (AbstractCard)new EnBlitzArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnProtectArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnAcceleratium(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnCannonArtifact(),extraUpgrades);
           this.turn++;
           break;
         case 4:
           addToList(cardsList, (AbstractCard)new EnEdgeArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnGenesisArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnRalmia(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnMysticArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnBlackenedScripture(),extraUpgrades);
           this.turn = 0;
           this.looped = true;
           break;
       } 
     } else {
       switch (this.turn) {
         case 0:
           addToList(cardsList, (AbstractCard)new TheGreatCreation(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnSpineLucille(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnMagisteelLion(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnMysticArtifact(),extraUpgrades);
           this.turn++;
           break;
         case 1:
           addToList(cardsList, (AbstractCard)new EnBlackenedScripture(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnCannonArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnProtectArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnBlitzArtifact(),extraUpgrades);
           this.turn++;
           break;
         case 2:
           addToList(cardsList, (AbstractCard)new EnEdgeArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnGenesisArtifact(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnRalmia(),extraUpgrades);
           addToList(cardsList, (AbstractCard)new EnMoonAlmiraj(),extraUpgrades);
           this.turn++;
           this.turn = 0;
           break;
       } 
     } 
     return cardsList;
   }
 
   
   public void initializeBonusRelic() {
   }
 }

