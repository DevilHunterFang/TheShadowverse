 package shadowverseCharbosses.bosses;

 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
 import shadowverse.Shadowverse;
 import shadowverseCharbosses.BossMechanicDisplayPanel;

 import java.util.ArrayList;

 public abstract class AbstractBossDeckArchetype
 {
   public static final Logger logger = LogManager.getLogger(Shadowverse.class.getName());

   public String bossMechanicName;
   public String bossMechanicDesc;
   public int maxHPModifier;
   public int actNum;
   private AbstractCharBoss currentBoss;

   public void addedPreBattle() {
     initializeBossPanel();
   }
   public boolean looped = false;
   
   public int turn = 0; private String ID;
   
   public ArrayList<AbstractCard> getThisTurnCards() {
     return new ArrayList<>();
   }
   public abstract void initializeBonusRelic();
   public void addToList(ArrayList<AbstractCard> c, AbstractCard q, boolean upgraded) {
     if (upgraded) q.upgrade(); 
     c.add(q);
   }
   
   public void initializeBossPanel() {
     if (this.bossMechanicDesc != null) {
       BossMechanicDisplayPanel.mechanicName = this.bossMechanicName;
       BossMechanicDisplayPanel.mechanicDesc = this.bossMechanicDesc;
     } 
   }
   
   public void addToList(ArrayList<AbstractCard> c, AbstractCard q) {
     addToList(c, q, false);
   }

   public AbstractBossDeckArchetype(String id, String loggerClassName, String loggerArchetypeName) {
     this.ID = id;
     if (AbstractDungeon.actNum != 4) {
       AbstractDungeon.lastCombatMetricKey = this.ID;
     }
   }
   public void initialize() {}
 }
