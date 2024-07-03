 package shadowverseCharbosses.bosses;

 import basemod.abstracts.CustomMonster;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.badlogic.gdx.math.MathUtils;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.SuicideAction;
 import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
 import com.megacrit.cardcrawl.actions.utility.WaitAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.cards.CardGroup;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.characters.AbstractPlayer;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.monsters.AbstractMonster;
 import com.megacrit.cardcrawl.orbs.AbstractOrb;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.relics.AbstractRelic;
 import com.megacrit.cardcrawl.rooms.AbstractRoom;
 import com.megacrit.cardcrawl.stances.AbstractStance;
 import com.megacrit.cardcrawl.ui.buttons.PeekButton;
 import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;
 import com.megacrit.cardcrawl.vfx.ThoughtBubble;
 import com.megacrit.cardcrawl.vfx.cardManip.CardDisappearEffect;
 import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
 import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
 import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
 import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
 import shadowverseCharbosses.BossMechanicDisplayPanel;
 import shadowverseCharbosses.actions.common.EnemyDiscardAtEndOfTurnAction;
 import shadowverseCharbosses.actions.common.EnemyUseCardAction;
 import shadowverseCharbosses.actions.orb.EnemyAnimateOrbAction;
 import shadowverseCharbosses.actions.orb.EnemyChannelAction;
 import shadowverseCharbosses.actions.orb.EnemyEvokeOrbAction;
 import shadowverseCharbosses.actions.orb.EnemyTriggerEndOfTurnOrbActions;
 import shadowverseCharbosses.actions.util.CharbossDoNextCardAction;
 import shadowverseCharbosses.actions.util.CharbossMakePlayAction;
 import shadowverseCharbosses.actions.util.CharbossTurnstartDrawAction;
 import shadowverseCharbosses.actions.util.DelayedActionAction;
 import shadowverseCharbosses.cards.AbstractBossCard;
 import shadowverseCharbosses.cards.EnemyCardGroup;
 import shadowverseCharbosses.core.EnemyEnergyManager;
 import shadowverseCharbosses.orbs.AbstractEnemyOrb;
 import shadowverseCharbosses.orbs.EnemyDark;
 import shadowverseCharbosses.orbs.EnemyEmptyOrbSlot;
 import shadowverseCharbosses.stances.AbstractEnemyStance;
 import shadowverseCharbosses.stances.EnNeutralStance;
 import shadowverseCharbosses.ui.EnemyEnergyPanel;

 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.Comparator;

 public abstract class AbstractCharBoss
   extends CustomMonster
 {
   public static AbstractCharBoss boss;
   public static boolean finishedSetup;
   public AbstractStance stance;
   public ArrayList<AbstractOrb> orbs;
   public int maxOrbs;
   public int masterMaxOrbs;
   public int masterHandSize;
   public int gameHandSize;
   public int mantraGained = 0;
   
   public boolean powerhouseTurn;
   
   public EnemyEnergyManager energy;
   
   public EnergyOrbInterface energyOrb;
   
   public EnemyEnergyPanel energyPanel;
   
   public CardGroup hand;
   
   public CardGroup limbo;
   
   public AbstractCard cardInUse;
   
   public int damagedThisCombat;
   
   public int cardsPlayedThisTurn;
   public int attacksPlayedThisTurn;
   public AbstractPlayer.PlayerClass chosenClass;
   public AbstractBossDeckArchetype chosenArchetype = null;
   
   public boolean onSetupTurn = true;
   
   private static boolean debugLog = false;
   
   private static int attacksDrawnForAttackPhase = 0;
   private static int setupsDrawnForSetupPhase = 0;
   
   public String energyString = "[E]";
   
   public AbstractCharBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, AbstractPlayer.PlayerClass playerClass) {
     super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
     finishedSetup = false;
     this.drawX = Settings.WIDTH * 0.75F - 150.0F * Settings.xScale;
     this.type = EnemyType.BOSS;
     this.chosenClass = playerClass;
     this.energyPanel = new EnemyEnergyPanel(this);
     this.hand = (CardGroup)new EnemyCardGroup(CardGroup.CardGroupType.HAND, this);
     this.limbo = (CardGroup)new EnemyCardGroup(CardGroup.CardGroupType.UNSPECIFIED, this);
     this.masterHandSize = 3;
     this.gameHandSize = 3;
     this.masterMaxOrbs = this.maxOrbs = 0;
     this.stance = (AbstractStance)new EnNeutralStance();
     this.orbs = new ArrayList<>();
   }
 
   
   public void init() {
     boss = this;
     setHp(this.maxHealth);
     this.energy.energyMaster = 5;
     generateAll();
     super.init();
     preBattlePrep();
     finishedSetup = true;
   }
 
   
   public void getMove(int num) {
     setMove((byte)0, Intent.NONE);
   }
 
 
   
   public void generateDeck() {}
 
   
   public void generateAll() {
     generateDeck();
     this.maxHealth += this.chosenArchetype.maxHPModifier;
     this.currentHealth = this.maxHealth;
     updateHealthBar();
   }
   
   public void usePreBattleAction() {
     this.energy.recharge();
     addToBot(new DelayedActionAction(new CharbossTurnstartDrawAction()));
     playMusic();
     this.chosenArchetype.addedPreBattle();
   }
   public void playMusic() {
     String musicKey;
     CardCrawlGame.music.unsilenceBGM();
     AbstractDungeon.scene.fadeOutAmbiance();
 
     
     if (AbstractDungeon.actNum == 0) { musicKey = "BOSS_BOTTOM"; }
     else if (AbstractDungeon.actNum == 1) { musicKey = "BOSS_CITY"; }
     else if (AbstractDungeon.actNum == 2) { musicKey = "BOSS_BEYOND"; }
     else { musicKey = "MINDBLOOM"; }
     
     AbstractDungeon.getCurrRoom().playBgmInstantly(musicKey);
   }
 
   
   public void takeTurn() {
     attacksDrawnForAttackPhase = 0;
     setupsDrawnForSetupPhase = 0;
     startTurn();
     
     addToBot(new CharbossMakePlayAction());
     
     this.onSetupTurn = !this.onSetupTurn;
   }
   
   public void makePlay() {
     for (AbstractCard _c : this.hand.group) {
       AbstractBossCard c = (AbstractBossCard)_c;
       if (c.canUse(AbstractDungeon.player, this) && c.getPriority(this.hand.group) > -100) {
         
         useCard(c, this, EnemyEnergyPanel.totalCount);
         addToBot(new DelayedActionAction(new CharbossDoNextCardAction()));
 
         
         return;
       } 
     } 
     
     addToBot(new EnemyTriggerEndOfTurnOrbActions());
   }
 
   
   public void update() {
     super.update();
     for (AbstractOrb o : this.orbs) {
       o.update();
       o.updateAnimation();
     } 
     
     combatUpdate();
   }
 
   
   public void applyEndOfTurnTriggers() {
     if (hasPower("stslib:Stunned")) this.chosenArchetype.turn--;
     
     this.energy.recharge();
     
     for (AbstractPower p : boss.powers) {
       p.onEnergyRecharge();
     }
     
     for (AbstractCard c : this.hand.group) {
       c.triggerOnEndOfTurnForPlayingCard();
     }
     this.stance.onEndOfTurn();
     
     addToBot(new EnemyDiscardAtEndOfTurnAction());

     for (AbstractCard c : this.hand.group) {
       c.resetAttributes();
     }
     addToBot(new DelayedActionAction(new CharbossTurnstartDrawAction()));
   }
 
 
 
   
   public void startTurn() {
     this.cardsPlayedThisTurn = 0;
     this.attacksPlayedThisTurn = 0;
     for (AbstractCard c : this.hand.group) {
       ((AbstractBossCard)c).lockIntentValues = true;
     }
     applyStartOfTurnRelics();
     applyStartOfTurnPreDrawCards();
     applyStartOfTurnCards();
     
     applyStartOfTurnOrbs();
   }
 
 
   
   public ArrayList<AbstractCard> getThisTurnCards() {
     return this.chosenArchetype.getThisTurnCards();
   }
   
   class sortByNewPrio
     implements Comparator<AbstractBossCard>
   {
     public int compare(AbstractBossCard a, AbstractBossCard b) {
       return a.newPrio - b.newPrio;
     }
   }
 
   
   public void endTurnStartTurn() {
     if (!(AbstractDungeon.getCurrRoom()).isBattleOver) {
       
       addToBot(new AbstractGameAction()
           {
             public void update() {
               this.isDone = true;
               for (AbstractCard q : AbstractCharBoss.this.getThisTurnCards()) {
                 AbstractCharBoss.boss.hand.addToTop(q);
                 if (q instanceof AbstractBossCard) ((AbstractBossCard)q).bossDarken(); 
                 q.current_y = Settings.HEIGHT / 2.0F;
                 q.current_x = Settings.WIDTH;
               } 
               
               ArrayList<AbstractBossCard> handAsBoss = new ArrayList<>();
               for (AbstractCard c : AbstractCharBoss.boss.hand.group) {
                 handAsBoss.add((AbstractBossCard)c);
               }
               
               Collections.sort(handAsBoss, new sortByNewPrio());
               
               ArrayList<AbstractCard> newHand = new ArrayList<>();
               for (AbstractCard c : handAsBoss) {
                 newHand.add(c);
                 c.applyPowers();
               } 
               
               AbstractCharBoss.boss.hand.group = newHand;
               
               AbstractCharBoss.boss.hand.refreshHandLayout();
               AbstractCharBoss.this.applyPowers();
             }
           });
       addToBot(new WaitAction(0.2F));
       applyStartOfTurnPostDrawRelics();
       applyStartOfTurnPostDrawPowers();
       if (!AbstractDungeon.player.hasRelic("Runic Dome"))
       {
         addToBot(new AbstractGameAction()
             {
               public void update() {
                 this.isDone = true;
                 int budget = AbstractCharBoss.this.energyPanel.getCurrentEnergy();
                 for (AbstractCard c : AbstractCharBoss.boss.hand.group) {
                   if (c.costForTurn <= budget && c.costForTurn != -2 && c instanceof AbstractBossCard) {
                     ((AbstractBossCard)c).createIntent();
                     ((AbstractBossCard)c).bossLighten();
                     budget -= c.costForTurn;
                     budget += ((AbstractBossCard)c).energyGeneratedIfPlayed;
                     if (budget < 0) budget = 0;  continue;
                   } 
                   if (c.costForTurn == -2 && c.type == AbstractCard.CardType.CURSE && c.color == AbstractCard.CardColor.CURSE) {
                     ((AbstractBossCard)c).bossLighten();
                   }
                 } 
                 for (AbstractCard c : AbstractCharBoss.boss.hand.group) {
                   AbstractBossCard cB = (AbstractBossCard)c;
                   cB.refreshIntentHbLocation();
                 } 
               }
             });
       }
       
       this.cardsPlayedThisTurn = 0;
       this.attacksPlayedThisTurn = 0;
     } 
   }
 
   
   public void preApplyIntentCalculations() {
     int attackCount = 0;
     int artifactCount = 0;
     
     if (AbstractDungeon.player.hasPower("Artifact")) {
       artifactCount = (AbstractDungeon.player.getPower("Artifact")).amount;
     }
 
     
     for (AbstractCard c : this.hand.group) {
       ((AbstractBossCard)c).manualCustomDamageModifier = 0;
       ((AbstractBossCard)c).manualCustomDamageModifierMult = 1.0F;
     } 
     
     for (int i = 0; i < this.hand.size(); i++) {
       AbstractBossCard c = (AbstractBossCard) this.hand.group.get(i);
       if (!c.lockIntentValues) {
         if (c.artifactConsumedIfPlayed > 0) {
           artifactCount -= c.artifactConsumedIfPlayed;
         }
         if (c.vulnGeneratedIfPlayed > 0 && 
           artifactCount <= 0) {
           for (int j = i + 1; j < this.hand.size(); j++) {
             AbstractBossCard c2 = (AbstractBossCard) this.hand.group.get(j);
             c2.manualCustomVulnModifier = true;
           } 
         }

         if (c.strengthGeneratedIfPlayed > 0) {
           for (int j = i + 1; j < this.hand.size(); j++) {
             AbstractBossCard c2 = (AbstractBossCard) this.hand.group.get(j);
             c2.manualCustomDamageModifier += c.strengthGeneratedIfPlayed;
           } 
         }
         
         if (c.block > 0) {
           int tmpBlock = c.block;

           for (int j = i + 1; j < this.hand.size(); j++) {
             AbstractBossCard c2 = (AbstractBossCard) this.hand.group.get(j);

           } 
         }
         
         if (c.damageMultGeneratedIfPlayed > 1) {
           for (int j = i + 1; j < this.hand.size(); j++) {
             AbstractBossCard c2 = (AbstractBossCard) this.hand.group.get(j);
             c2.manualCustomDamageModifierMult = c.damageMultGeneratedIfPlayed;
           } 
         }
         
         c.manualCustomDamageModifierMult *= c.damageMultIfPlayed;
       } 
     }
     
     for (AbstractCard c : this.hand.group) {
       if (!((AbstractBossCard)c).bossDarkened) {
         ((AbstractBossCard)c).createIntent();
       }
     } 
   }

   public void applyPowers() {
     super.applyPowers();
     
     preApplyIntentCalculations();
     
     this.hand.applyPowers();
   }

   public int getIntentDmg() {
     int totalIntentDmg = -1;
     for (AbstractCard c : this.hand.group) {
       AbstractBossCard cB = (AbstractBossCard)c;
       if (cB.intentDmg > 0 && (!cB.bossDarkened || AbstractDungeon.player.hasRelic("Runic Dome"))) {
         if (totalIntentDmg == -1) {
           totalIntentDmg = 0;
         }
         totalIntentDmg += cB.intentDmg;
       } 
     } 
     return totalIntentDmg;
   }
   
   public int getIntentBaseDmg() {
     return getIntentDmg();
   }
   
   public void gainEnergy(int e) {
     EnemyEnergyPanel.addEnergy(e);
     this.hand.glowCheck();
   }
 
   
   public void loseEnergy(int e) {
     EnemyEnergyPanel.useEnergy(e);
   }

   public void onCardDrawOrDiscard() {
     for (AbstractPower p : this.powers) {
       p.onDrawOrDiscard();
     }
     if (hasPower("Corruption")) {
       for (AbstractCard c : this.hand.group) {
         if (c.type == AbstractCard.CardType.SKILL && c.costForTurn != 0) {
           c.modifyCostForCombat(-9);
         }
       } 
     }
     this.hand.applyPowers();
     this.hand.glowCheck();
   }
   
   public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
     if (monster == null) {
       monster = this;
     }
     if (c.type == AbstractCard.CardType.ATTACK) {
       this.attacksPlayedThisTurn++;
       useFastAttackAnimation();
       
       if (c.damage > MathUtils.random(20)) {
         onPlayAttackCardSound();
       }
     } 
     this.cardsPlayedThisTurn++;
     c.calculateCardDamage(monster);
     if (c.cost == -1 && EnemyEnergyPanel.totalCount < energyOnUse && !c.ignoreEnergyOnUse) {
       c.energyOnUse = EnemyEnergyPanel.totalCount;
     }
     if (c.cost == -1 && c.isInAutoplay) {
       c.freeToPlayOnce = true;
     }
     c.use(AbstractDungeon.player, monster);
     addToBot(new EnemyUseCardAction(c, (AbstractCreature)monster));
     if (!c.dontTriggerOnUseCard) {
       this.hand.triggerOnOtherCardPlayed(c);
     }
     this.hand.removeCard(c);
     this.cardInUse = c;
     c.target_x = (Settings.WIDTH / 2);
     c.target_y = (Settings.HEIGHT / 2);
     if (c.costForTurn > 0 && !c.freeToPlay() && !c.isInAutoplay && (!hasPower("Corruption") || c.type != AbstractCard.CardType.SKILL)) {
       this.energy.use(c.costForTurn);
     }
     
     AbstractBossCard cB = (AbstractBossCard)c;
     cB.showIntent = false;
   }
   
   public void combatUpdate() {
     if (this.cardInUse != null) {
       this.cardInUse.update();
     }
     this.energyPanel.update();
     this.limbo.update();
 
 
 
 
     
     this.hand.update();
     this.hand.updateHoverLogic();
     for (AbstractPower p : this.powers) {
       p.updateParticles();
     }
     for (AbstractOrb o : this.orbs) {
       o.update();
     }
     this.stance.update();
   }
 
 
   
   public void onPlayAttackCardSound() {}
 
   
   public void damage(DamageInfo info) {
     int damageAmount = info.output;
     boolean hadBlock = true;
     if (this.currentBlock == 0) {
       hadBlock = false;
     }
     if (damageAmount < 0) {
       damageAmount = 0;
     }
     if (damageAmount > 1 && hasPower("Intangible")) {
       damageAmount = 1;
     }
     boolean weakenedToZero = (damageAmount == 0);
     damageAmount = decrementBlock(info, damageAmount);

     if (info.owner == AbstractDungeon.player) {
       for (AbstractRelic r : AbstractDungeon.player.relics) {
         damageAmount = r.onAttackToChangeDamage(info, damageAmount);
       }
     }
     if (info.owner != null) {
       for (AbstractPower p : info.owner.powers) {
         damageAmount = p.onAttackToChangeDamage(info, damageAmount);
       }
     }
     for (AbstractPower p : this.powers) {
       damageAmount = p.onAttackedToChangeDamage(info, damageAmount);
     }
     if (info.owner == AbstractDungeon.player) {
       for (AbstractRelic r : AbstractDungeon.player.relics) {
         r.onAttack(info, damageAmount, (AbstractCreature)this);
       }
     }
     if (info.owner != null) {
       for (AbstractPower p : info.owner.powers) {
         p.onAttack(info, damageAmount, (AbstractCreature)this);
       }
       for (AbstractPower p : this.powers) {
         damageAmount = p.onAttacked(info, damageAmount);
       }

     }
     this.lastDamageTaken = Math.min(damageAmount, this.currentHealth);
     boolean probablyInstantKill = (this.currentHealth == 0);
     if (damageAmount > 0 || probablyInstantKill) {
       for (AbstractPower p : this.powers) {
         damageAmount = p.onLoseHp(damageAmount);
       }
       for (AbstractPower p : this.powers) {
         p.wasHPLost(info, damageAmount);
       }

       if (info.owner != null) {
         for (AbstractPower p : info.owner.powers) {
           p.onInflictDamage(info, damageAmount, (AbstractCreature)this);
         }
       }
       if (info.owner != this) {
         useStaggerAnimation();
       }
       if (damageAmount > 0) {
         if (info.owner != this) {
           useStaggerAnimation();
         }
         if (damageAmount >= 99 && !CardCrawlGame.overkill) {
           CardCrawlGame.overkill = true;
         }
         this.currentHealth -= damageAmount;
         if (!probablyInstantKill) {
           AbstractDungeon.effectList.add(new StrikeEffect((AbstractCreature)this, this.hb.cX, this.hb.cY, damageAmount));
         }
         if (this.currentHealth < 0) {
           this.currentHealth = 0;
         }
         healthBarUpdatedEvent();
         updateCardsOnDamage();
       } 
       if (this.currentHealth <= this.maxHealth / 2.0F && !this.isBloodied) {
         this.isBloodied = true;
       } 
       if (this.currentHealth < 1) {
         die();
         if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
           AbstractDungeon.actionManager.cleanCardQueue();
           AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
           AbstractDungeon.effectList.add(new DeckPoofEffect(Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
           AbstractDungeon.overlayMenu.hideCombatPanels();
         } 
         if (this.currentBlock > 0) {
           loseBlock();
           AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + AbstractMonster.BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + AbstractMonster.BLOCK_ICON_Y));
         } 
         
         for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
           if (!m.isDead && !m.isDying && m.hasPower("Minion")) {
             AbstractDungeon.actionManager.addToTop(new HideHealthBarAction((AbstractCreature)m));
             AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
           } 
         } 
       } 
     } else if (!probablyInstantKill) {
       if (weakenedToZero && this.currentBlock == 0) {
         if (hadBlock) {
           AbstractDungeon.effectList.add(new BlockedWordEffect((AbstractCreature)this, this.hb.cX, this.hb.cY, AbstractMonster.TEXT[30]));
         } else {
           AbstractDungeon.effectList.add(new StrikeEffect((AbstractCreature)this, this.hb.cX, this.hb.cY, 0));
         } 
       } else if (Settings.SHOW_DMG_BLOCK) {
         AbstractDungeon.effectList.add(new BlockedWordEffect((AbstractCreature)this, this.hb.cX, this.hb.cY, AbstractMonster.TEXT[30]));
       } 
     } 
   }
 
   
   public void die() {
     if (this.currentHealth <= 0) {
       BossMechanicDisplayPanel.resetBossPanel();
       useFastShakeAnimation(5.0F);
       CardCrawlGame.screenShake.rumble(4.0F);
       onBossVictoryLogic();
     } 

     boss = null;
     finishedSetup = false;
     this.hand.clear();
     this.limbo.clear();
     this.orbs.clear();
     this.stance.onExitStance();
     this.stance = (AbstractStance)AbstractEnemyStance.getStanceFromName("Neutral");
     this.stance.onEnterStance();
     
     super.die();
   }
 
 
 
 
   
   protected void onFinalBossVictoryLogic() {
     super.onFinalBossVictoryLogic();
   }
 
 
 
   
   private void updateCardsOnDamage() {
     if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
       for (AbstractCard c : this.hand.group) {
         c.tookDamage();
       }
     }
   }

   public void updateCardsOnDiscard() {
     for (AbstractCard c : this.hand.group) {
       c.didDiscard();
     }
   }

   public void heal(int healAmount) {
     int amt = healAmount;

     super.heal(amt);
     if (this.currentHealth > this.maxHealth / 2.0F && this.isBloodied) {
       this.isBloodied = false;
     } 
   }
   
   public void preBattlePrep() {
     this.damagedThisCombat = 0;
     this.cardsPlayedThisTurn = 0;
     this.attacksPlayedThisTurn = 0;
     this.maxOrbs = 0;
     this.orbs.clear();
     increaseMaxOrbSlots(this.masterMaxOrbs, false);
     this.isBloodied = (this.currentHealth <= this.maxHealth / 2);
     AbstractPlayer.poisonKillCount = 0;
     this.gameHandSize = this.masterHandSize;
     this.cardInUse = null;
     AbstractDungeon.overlayMenu.endTurnButton.enabled = false;
     this.hand.clear();
     this.energy.prep();
     this.powers.clear();
     healthBarUpdatedEvent();
     applyPreCombatLogic();
   }
 
   
   public ArrayList<String> getRelicNames() {
     ArrayList<String> arr = new ArrayList<>();
     return arr;
   }
   
   public void applyPreCombatLogic() {
   }
 
   
   public void applyStartOfCombatLogic() {
   }
   
   public void applyStartOfCombatPreDrawLogic() {
   }
   
   public void applyStartOfTurnRelics() {
     this.stance.atStartOfTurn();
   }
   
   public void applyStartOfTurnPostDrawRelics() {
   }
   
   public void applyStartOfTurnPreDrawCards() {
     for (AbstractCard c : this.hand.group) {
       if (c != null) {
         c.atTurnStartPreDraw();
       }
     } 
   }
 
 
 
 
 
 
 
   
   public void applyStartOfTurnCards() {
     for (AbstractCard c : this.hand.group) {
       if (c != null) {
         c.atTurnStart();
         c.triggerWhenDrawn();
       } 
     } 
   }

   public boolean relicsDoneAnimating() {
     return true;
   }
   
   public void switchedStance() {
     for (AbstractCard c : this.hand.group) {
       c.switchedStance();
     }
   }
   public void onStanceChange(String id) {}

   public void addBlock(int blockAmount) {
     float tmp = blockAmount;
     if (tmp > 0.0F) {
       for (AbstractPower p : this.powers) {
         p.onGainedBlock(tmp);
       }
     }
     super.addBlock((int)Math.floor(tmp));
   }
 
 
 
 
   
   public void triggerEvokeAnimation(int slot) {
     if (this.maxOrbs <= 0) {
       return;
     }
     ((AbstractOrb)this.orbs.get(slot)).triggerEvokeAnimation();
   }
   
   public void evokeOrb() {
     if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EnemyEmptyOrbSlot)) {
       ((AbstractOrb)this.orbs.get(0)).onEvoke();
       EnemyEmptyOrbSlot enemyEmptyOrbSlot = new EnemyEmptyOrbSlot(); int i;
       for (i = 1; i < this.orbs.size(); i++) {
         Collections.swap(this.orbs, i, i - 1);
       }
       this.orbs.set(this.orbs.size() - 1, enemyEmptyOrbSlot);
       for (i = 0; i < this.orbs.size(); i++) {
         ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
       }
     } 
   }
   
   public ArrayList<AbstractEnemyOrb> orbsAsEn() {
     ArrayList<AbstractEnemyOrb> orbies = new ArrayList<>();
     for (AbstractOrb o : this.orbs) {
       if (o instanceof AbstractEnemyOrb) {
         orbies.add((AbstractEnemyOrb)o);
       }
     } 
     return orbies;
   }
   
   public void evokeNewestOrb() {
     if (!this.orbs.isEmpty() && !(this.orbs.get(this.orbs.size() - 1) instanceof EnemyEmptyOrbSlot)) {
       ((AbstractOrb)this.orbs.get(this.orbs.size() - 1)).onEvoke();
     }
   }
   
   public void evokeWithoutLosingOrb() {
     if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EnemyEmptyOrbSlot)) {
       ((AbstractOrb)this.orbs.get(0)).onEvoke();
     }
   }
   
   public void removeNextOrb() {
     if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EnemyEmptyOrbSlot)) {
       EnemyEmptyOrbSlot enemyEmptyOrbSlot = new EnemyEmptyOrbSlot(((AbstractOrb)this.orbs.get(0)).cX, ((AbstractOrb)this.orbs.get(0)).cY); int i;
       for (i = 1; i < this.orbs.size(); i++) {
         Collections.swap(this.orbs, i, i - 1);
       }
       this.orbs.set(this.orbs.size() - 1, enemyEmptyOrbSlot);
       for (i = 0; i < this.orbs.size(); i++) {
         ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
       }
     } 
   }
   
   public boolean hasEmptyOrb() {
     if (this.orbs.isEmpty()) {
       return false;
     }
     for (AbstractOrb o : this.orbs) {
       if (o instanceof EnemyEmptyOrbSlot) {
         return true;
       }
     } 
     return false;
   }
   
   public boolean hasOrb() {
     return (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EnemyEmptyOrbSlot));
   }
   
   public int filledOrbCount() {
     int orbCount = 0;
     for (AbstractOrb o : this.orbs) {
       if (!(o instanceof EnemyEmptyOrbSlot)) {
         orbCount++;
       }
     } 
     return orbCount;
   }


   public void channelOrb(AbstractOrb orbToSet) {
     EnemyDark enemyDark = null;
     if (this.maxOrbs <= 0) {
       AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, AbstractPlayer.MSG[4], true));
       return;
     }
     int index = -1;
     for (int i = 0; i < this.orbs.size(); i++) {
       if (this.orbs.get(i) instanceof EnemyEmptyOrbSlot) {
         index = i;
         break;
       } 
     } 
     if (index != -1 && enemyDark!=null) {
       ((AbstractOrb)enemyDark).cX = ((AbstractOrb)this.orbs.get(index)).cX;
       ((AbstractOrb)enemyDark).cY = ((AbstractOrb)this.orbs.get(index)).cY;
       this.orbs.set(index, enemyDark);
       ((AbstractOrb)this.orbs.get(index)).setSlot(index, this.maxOrbs);
       enemyDark.playChannelSFX();
       for (AbstractPower p : this.powers) {
         p.onChannel((AbstractOrb)enemyDark);
       }
       AbstractDungeon.actionManager.orbsChanneledThisCombat.add(enemyDark);
       AbstractDungeon.actionManager.orbsChanneledThisTurn.add(enemyDark);
       enemyDark.applyFocus();
     } else {
       AbstractDungeon.actionManager.addToTop(new EnemyChannelAction((AbstractOrb)enemyDark));
       AbstractDungeon.actionManager.addToTop(new EnemyEvokeOrbAction(1));
       AbstractDungeon.actionManager.addToTop(new EnemyAnimateOrbAction(1));
     } 
   }
   
   public void increaseMaxOrbSlots(int amount, boolean playSfx) {
     if (this.maxOrbs == 10) {
       AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, AbstractPlayer.MSG[3], true));
       return;
     } 
     if (playSfx) {
       CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1F);
     }
     this.maxOrbs += amount; int i;
     for (i = 0; i < amount; i++) {
       this.orbs.add(new EnemyEmptyOrbSlot());
     }
     for (i = 0; i < this.orbs.size(); i++) {
       ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
     }
   }
   
   public void decreaseMaxOrbSlots(int amount) {
     if (this.maxOrbs <= 0) {
       return;
     }
     this.maxOrbs -= amount;
     if (this.maxOrbs < 0) {
       this.maxOrbs = 0;
     }
     if (!this.orbs.isEmpty()) {
       this.orbs.remove(this.orbs.size() - 1);
     }
     for (int i = 0; i < this.orbs.size(); i++) {
       ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
     }
   }
   
   public void applyStartOfTurnOrbs() {
     if (!this.orbs.isEmpty()) {
       for (AbstractOrb o : this.orbs) {
         o.onStartOfTurn();
       }
     } 
   }
 
 
 
 
 
   
   public void render(SpriteBatch sb) {
     super.render(sb);
     if (!this.isDead) {
       renderHand(sb);
       this.stance.render(sb);
       if (!this.orbs.isEmpty())
       {
         for (AbstractOrb o : this.orbs) {
           o.render(sb);
         }
       }
       this.energyPanel.render(sb);
     } 
   }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   
   public void renderHand(SpriteBatch sb) {
     this.hand.renderHand(sb, this.cardInUse);
     if (this.cardInUse != null && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.HAND_SELECT && !PeekButton.isPeeking) {
       this.cardInUse.render(sb);
       if ((AbstractDungeon.getCurrRoom()).phase != AbstractRoom.RoomPhase.COMBAT) {
         AbstractDungeon.effectList.add(new CardDisappearEffect(this.cardInUse.makeCopy(), this.cardInUse.current_x, this.cardInUse.current_y));
         this.cardInUse = null;
       } 
     } 
     this.limbo.render(sb);
   }
   
   private enum DrawTypes {
     Attack,
     Setup,
     EitherPhase;
   }
 
 
 
 
   
   public void dispose() {
     super.dispose();
     BossMechanicDisplayPanel.resetBossPanel();
   }
 }

