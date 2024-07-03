 package shadowverseCharbosses.cards;

 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.cards.CardGroup;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.monsters.AbstractMonster;
 import com.megacrit.cardcrawl.orbs.AbstractOrb;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.ArrayList;
 
 public class EnemyCardGroup extends CardGroup {
   public static final int HAND_ROW_LENGTH = 10;
   public static final float HAND_HEIGHT_OFFSET = 0.56F;
   public AbstractCharBoss owner;
   public static AbstractBossCard hov2holder = null;
   
   public EnemyCardGroup(CardGroupType type) {
     super(type);
     this.owner = AbstractCharBoss.boss;
   }
   
   public EnemyCardGroup(CardGroupType type, AbstractCharBoss owner) {
     super(type);
     this.owner = owner;
   }
   
   public EnemyCardGroup(CardGroup group, CardGroupType type) {
     super(group, type);
     this.owner = AbstractCharBoss.boss;
   }
   
   public EnemyCardGroup(CardGroup group, CardGroupType type, AbstractCharBoss owner) {
     super(group, type);
     this.owner = owner;
   }
   
   public void moveToDiscardPile(AbstractCard c) {
     resetCardBeforeMoving(c);
     
     System.out.printf(c.name + " DARKEN-CANCELED", new Object[0]);
     
     this.owner.onCardDrawOrDiscard();
   }
   
   public void moveToExhaustPile(AbstractCard c) {
     for (AbstractPower p : this.owner.powers) {
       p.onExhaust(c);
     }
     c.triggerOnExhaust();
     resetCardBeforeMoving(c);
     AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
     
     this.owner.onCardDrawOrDiscard();
   }
   
   public void moveToHand(AbstractCard c, CardGroup group) {
     c.unhover();
     c.lighten(true);
     c.setAngle(0.0F);
     c.drawScale = 0.12F;
     c.targetDrawScale = 0.35F;
     c.current_x = CardGroup.DRAW_PILE_X;
     c.current_y = CardGroup.DRAW_PILE_Y;
     group.removeCard(c);
     this.owner.hand.addToTop(c);
     this.owner.hand.refreshHandLayout();
     this.owner.hand.applyPowers();
   }
 
   
   public void moveToHand(AbstractCard c) {
     resetCardBeforeMoving(c);
     c.unhover();
     c.lighten(true);
     c.setAngle(0.0F);
     c.drawScale = 0.12F;
     c.targetDrawScale = 0.35F;
     c.current_x = CardGroup.DRAW_PILE_X;
     c.current_y = CardGroup.DRAW_PILE_Y;
     this.owner.hand.addToTop(c);
     this.owner.hand.refreshHandLayout();
     this.owner.hand.applyPowers();
   }
   
   public void moveToDeck(AbstractCard c, boolean randomSpot) {
     resetCardBeforeMoving(c);
   }
 
 
 
 
 
 
 
 
   
   public void moveToBottomOfDeck(AbstractCard c) {
     resetCardBeforeMoving(c);
   }
 
   
   private void resetCardBeforeMoving(AbstractCard c) {
     if (AbstractDungeon.player.hoveredCard == c) {
       AbstractDungeon.player.releaseCard();
     }
     AbstractDungeon.actionManager.removeFromQueue(c);
     c.unhover();
     c.untip();
     c.stopGlowing();
     this.group.remove(c);
   }
   
   public void initializeDeck(CardGroup masterDeck) {
     clear();
     CardGroup copy = new CardGroup(masterDeck, CardGroupType.DRAW_PILE);
     copy.shuffle(AbstractDungeon.shuffleRng);
     ArrayList<AbstractCard> placeOnTop = new ArrayList<>();
     for (AbstractCard c : copy.group) {
       if (c.isInnate) {
         placeOnTop.add(c); continue;
       }  if (c.inBottleFlame || c.inBottleLightning || c.inBottleTornado) {
         placeOnTop.add(c); continue;
       } 
       c.target_x = CardGroup.DRAW_PILE_X;
       c.target_y = CardGroup.DRAW_PILE_Y;
       c.current_x = CardGroup.DRAW_PILE_X;
       c.current_y = CardGroup.DRAW_PILE_Y;
       addToTop(c);
     } 
     
     for (AbstractCard c : placeOnTop) {
       addToTop(c);
     }
     if (placeOnTop.size() > AbstractDungeon.player.masterHandSize);
 
     
     placeOnTop.clear();
   }
   
   public void triggerOnOtherCardPlayed(AbstractCard usedCard) {
     for (AbstractCard c : this.group) {
       if (c != usedCard) {
         c.triggerOnOtherCardPlayed(usedCard);
       }
     } 
     for (AbstractPower p : AbstractCharBoss.boss.powers) {
       p.onAfterCardPlayed(usedCard);
     }
   }
   
   public void removeCard(AbstractCard c) {
     this.group.remove(c);
     if (this.type == CardGroupType.MASTER_DECK) {
       c.onRemoveFromMasterDeck();
     }
   }
   
   public void refreshHandLayout() {
     if ((AbstractDungeon.getCurrRoom()).monsters != null && (AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead()) {
       return;
     }
     for (AbstractOrb o : AbstractCharBoss.boss.orbs) {
       o.hideEvokeValues();
     }
     AbstractCard hoveredcard = null;
     for (int i = 0; i < this.group.size(); i++) {
       AbstractCard c = this.group.get(i);
       if (hov2holder != c) {
         c.targetDrawScale = 0.35F;
       }
       int cardsinrow = Math.min(this.group.size() - 10 * (int)Math.floor((i / 10.0F)), 10);
       float widthspacing = AbstractCard.IMG_WIDTH_S + 100.0F * Settings.scale;
       c.target_x = Settings.WIDTH * 0.9F - (cardsinrow + 0.5F) * widthspacing * 0.35F + widthspacing * 0.35F * (i % 10);
       c.target_y = Settings.HEIGHT * 0.66F + AbstractCard.IMG_HEIGHT_S * 0.35F * ((float)Math.floor((i / 10.0F)) + ((this.group.size() > 10) ? 0.0F : 1.0F));
       if (((AbstractBossCard)c).hov2 && c.hb.hovered && (
         hoveredcard == null || hov2holder == c)) {
         hoveredcard = c;
       }
     } 
     
     if (hoveredcard != null) {
       hoverCardPush(hoveredcard);
     }
   }
   
   public void glowCheck() {
     for (AbstractCard c : this.group) {
       if (c.canUse(AbstractDungeon.player, (AbstractMonster)AbstractCharBoss.boss) && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.HAND_SELECT) {
         c.beginGlowing();
       } else {
         c.stopGlowing();
       } 
       c.triggerOnGlowCheck();
     } 
   }
   
   public int getCardNumber(AbstractCard c) {
     for (int i = 0; i < this.group.size(); i++) {
       if (c.equals(this.group.get(i))) {
         return i;
       }
     } 
     return -1;
   }
   
   public ArrayList<AbstractCard> getCardRow(AbstractCard c) {
     int cardNum = getCardNumber(c);
     ArrayList<AbstractCard> cardrow = new ArrayList<>();
     for (int i = 10 * (int)Math.floor((cardNum / 10.0F)); i < this.group.size() && i < 10 * (1 + (int)Math.floor((i / 10.0F))); i++) {
       cardrow.add(this.group.get(i));
     }
     return cardrow;
   }
   
   public void hoverCardPush(AbstractCard c) {
     int cardNum = getCardNumber(c) % 10;
     ArrayList<AbstractCard> cardrow = getCardRow(c);
     if (cardrow.size() > 1) {
       float pushAmt = 0.4F;
       if (cardrow.size() == 2) {
         pushAmt = 0.2F;
       } else if (cardrow.size() == 3 || cardrow.size() == 4) {
         pushAmt = 0.27F;
       } 
       pushAmt *= 0.46666667F; int currentSlot;
       for (currentSlot = cardNum + 1; currentSlot < cardrow.size(); currentSlot++) {
         AbstractCard abstractCard = cardrow.get(currentSlot);
         abstractCard.target_x += AbstractCard.IMG_WIDTH_S * pushAmt;
         pushAmt *= 0.25F;
       } 
       pushAmt = 0.4F;
       if (cardrow.size() == 2) {
         pushAmt = 0.2F;
       } else if (cardrow.size() == 3 || cardrow.size() == 4) {
         pushAmt = 0.27F;
       } 
       pushAmt *= 0.46666667F;
       for (currentSlot = cardNum - 1; currentSlot > -1 && currentSlot < cardrow.size(); currentSlot--) {
         AbstractCard abstractCard2 = cardrow.get(currentSlot);
         abstractCard2.target_x -= AbstractCard.IMG_WIDTH_S * pushAmt;
         pushAmt *= 0.25F;
       } 
     } 
   }
   
   public AbstractBossCard getHighestValueCard() {
     AbstractBossCard r = null;
     int record = -99;
     for (AbstractCard c : this.group) {
       AbstractBossCard cc = (AbstractBossCard)c;
       if (cc.getValue() > record) {
         r = cc;
         record = cc.getValue();
       } 
     } 
     return r;
   }
   
   public AbstractBossCard getHighestValueCard(AbstractCard.CardType type) {
     AbstractBossCard r = null;
     int record = -99;
     for (AbstractCard c : this.group) {
       if (c.type == type) {
         AbstractBossCard cc = (AbstractBossCard)c;
         if (cc.getValue() > record) {
           r = cc;
           record = cc.getValue();
         } 
       } 
     } 
     return r;
   }
   
   public AbstractBossCard getHighestUpgradeValueCard() {
     AbstractBossCard r = null;
     int record = -99;
     for (AbstractCard c : this.group) {
       AbstractBossCard cc = (AbstractBossCard)c;
       if (cc.getUpgradeValue() > record) {
         r = cc;
         record = cc.getValue();
       } 
     } 
     return r;
   }
 }

