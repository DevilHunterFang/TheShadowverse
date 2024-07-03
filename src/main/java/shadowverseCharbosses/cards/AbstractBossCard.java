package shadowverseCharbosses.cards;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import com.megacrit.cardcrawl.vfx.ShieldParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.StunStarEffect;
import com.megacrit.cardcrawl.vfx.combat.UnknownParticleEffect;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.orbs.AbstractEnemyOrb;
import shadowverseCharbosses.ui.EnemyEnergyPanel;

import java.util.ArrayList;
import java.util.Iterator;


public abstract class AbstractBossCard
        extends CustomCard {
    public static final float HAND_SCALE = 0.35F;
    public static final float HOVER_SCALE = 0.8F;
    public static boolean recursionCheck = false;
    public AbstractCharBoss owner;
    public boolean hov2 = false;
    public int limit;
    public int magicValue;
    public boolean forceDraw = false;
    public boolean bossDarkened = false;
    public boolean tempLighten = false;
    public boolean intentActive = false;
    public boolean showIntent = false;
    public int energyGeneratedIfPlayed = 0;
    public int strengthGeneratedIfPlayed = 0;
    public int damageMultGeneratedIfPlayed = 1;
    public int damageMultIfPlayed = 1;
    public int focusGeneratedIfPlayed = 0;
    public int vulnGeneratedIfPlayed = 0;
    public int artifactConsumedIfPlayed = 0;
    public boolean vulnCalculated = false;
    public int blockGeneratedIfPlayed = 0;
    public int clawDamageGeneratedIfPlayed = 0;

    public static final String[] TEXT;

    public int newPrio = 0;

    public int manualCustomDamageModifier = 0;
    public float manualCustomDamageModifierMult = 1.0F;

    public boolean manualCustomVulnModifier = false;

    public static boolean fakeStormPower = false;

    private static final float INTENT_HB_W = 64.0F * Settings.scale;
    public Hitbox intentHb = new Hitbox(INTENT_HB_W, INTENT_HB_W);
    public AbstractMonster.Intent intent;
    public AbstractMonster.Intent tipIntent;
    public float intentAlpha;
    public float intentAlphaTarget;
    public float intentOffsetX;
    public float intentOffsetY = -90.0F * Settings.scale;
    private BobEffect bobEffect = new BobEffect();
    private Texture intentImg;
    private Texture intentBg;
    private PowerTip intentTip = new PowerTip();
    public int intentDmg;
    protected int intentMultiAmt;
    private Color intentColor = Color.WHITE.cpy();
    private float intentParticleTimer;
    private float intentAngle;
    public boolean lockIntentValues;
    public ArrayList<AbstractGameEffect> intentFlash = new ArrayList<>();
    private ArrayList<AbstractGameEffect> intentVfx = new ArrayList<>();

    public boolean alwaysDisplayText;

    public AbstractBossCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.alwaysDisplayText = false;
        this.owner = AbstractCharBoss.boss;
        this.limit = 99;
    }

    public AbstractBossCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractMonster.Intent intent) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.alwaysDisplayText = false;
        this.owner = AbstractCharBoss.boss;
        this.limit = 99;
        this.intent = intent;
    }

    public AbstractBossCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractMonster.Intent intent, boolean isCustomCard) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.alwaysDisplayText = false;
        this.owner = AbstractCharBoss.boss;
        this.limit = 99;
        this.intent = intent;
    }

    public static int statusValue() {
        int value = -5;
        if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && AbstractCharBoss.boss != null)
            if (AbstractCharBoss.boss.hasPower("Evolve")) {
                value = 0;
            }
        return value;
    }

    public int getPriority(ArrayList<AbstractCard> hand) {
        if (this.type == CardType.STATUS) return statusValue();
        return autoPriority();
    }

    public int getValue() {
        return 0;
    }

    public int getUpgradeValue() {
        return 0;
    }

    public int autoPriority() {
        AbstractCharBoss ownerBoss = this.owner;
        boolean setupPhase = ownerBoss.onSetupTurn;
        float blockModifier = 1.0F;
        if (setupPhase) blockModifier = 1.5F;
        int value = 0;
        if (this.type == CardType.STATUS) {
            value -= 10;
        } else if (this.type == CardType.CURSE && this.costForTurn < -1) {
            value -= 100;
        }
        if (this.type == CardType.ATTACK) {
            if (ownerBoss.currentHealth > ownerBoss.maxHealth / 2) {
                value += Math.max(this.damage, 0);
            } else {
                value = (int) (value + Math.max(this.damage * 2.0F, 0.0F));
            }
        }
        value = (int) (value + Math.max(this.block * 1.3F * blockModifier, 0.0F));
        if (this.type == CardType.POWER || this.color == CardColor.COLORLESS)
            if (ownerBoss.currentHealth > ownerBoss.maxHealth / 2) {
                value *= 5;
            } else {
                value /= 2;
            }
        if (this.isEthereal) value *= 2;
        return value;
    }

    public void applyPowers() {
        applyPowersToBlock();
        AbstractPlayer player = AbstractDungeon.player;
        this.isDamageModified = false;
        float tmp = this.baseDamage;
        if (this.owner == null) this.owner = AbstractCharBoss.boss;
        for (AbstractPower p : this.owner.powers) tmp = p.atDamageGive(tmp, this.damageTypeForTurn, this);
        tmp = this.owner.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
        if (this.baseDamage != (int) tmp) this.isDamageModified = true;
        for (AbstractPower p : player.powers) tmp = p.atDamageReceive(tmp, this.damageTypeForTurn, this);
        tmp = this.owner.stance.atDamageReceive(tmp, this.damageTypeForTurn);
        for (AbstractPower p : this.owner.powers) tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn, this);
        for (AbstractPower p : player.powers) tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn, this);
        if (tmp < 0.0F) tmp = 0.0F;
        if (this.baseDamage != MathUtils.floor(tmp)) this.isDamageModified = true;
        this.damage = MathUtils.floor(tmp);
        initializeDescription();
        if (this.intent != null && !this.bossDarkened) createIntent();
        if (AbstractCharBoss.boss != null && AbstractCharBoss.boss.hasPower("stslib:Stunned")) {
            bossDarken();
            destroyIntent();
        }
    }

    protected void applyPowersToBlock() {
        this.isBlockModified = false;
        float tmp = this.baseBlock;
        for (AbstractPower p : this.owner.powers) tmp = p.modifyBlock(tmp, this);
        if (this.baseBlock != MathUtils.floor(tmp)) this.isBlockModified = true;
        if (tmp < 0.0F) tmp = 0.0F;
        this.block = MathUtils.floor(tmp);
    }

    public void atb(AbstractGameAction action) {
        addToBot(action);
    }

    public void calculateCardDamage(AbstractMonster mo) {
        AbstractCharBoss abstractCharBoss = null;
        applyPowersToBlock();
        AbstractPlayer player = AbstractDungeon.player;
        this.isDamageModified = false;
        if (mo == null) {
            for (AbstractMonster monster : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                if (monster.type== AbstractMonster.EnemyType.BOSS&&monster instanceof AbstractCharBoss)
                    abstractCharBoss = (AbstractCharBoss) monster;
            }
        }
        if (abstractCharBoss != null) {
            this.damage = MathUtils.floor(calculateDamage((AbstractMonster) abstractCharBoss, player, this.baseDamage));
            this.intentDmg = MathUtils.floor(this.manualCustomDamageModifierMult * calculateDamage((AbstractMonster) abstractCharBoss, player, (this.baseDamage + customIntentModifiedDamage() + this.manualCustomDamageModifier)));
        }
        initializeDescription();
    }

    private float calculateDamage(AbstractMonster mo, AbstractPlayer player, float tmp) {
        for (AbstractPower p : this.owner.powers) tmp = p.atDamageGive(tmp, this.damageTypeForTurn, this);
        tmp = this.owner.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
        if (this.baseDamage != (int) tmp) this.isDamageModified = true;
        if (mo == this.owner) {
            for (AbstractPower p : player.powers) tmp = p.atDamageReceive(tmp, this.damageTypeForTurn, this);
            tmp = player.stance.atDamageReceive(tmp, this.damageTypeForTurn);
        } else {
            for (AbstractPower p : mo.powers) tmp = p.atDamageReceive(tmp, this.damageTypeForTurn, this);
        }
        for (AbstractPower p : this.owner.powers) tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn, this);
        if (mo == this.owner) {
            for (AbstractPower p : player.powers) tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn, this);
        } else {
            for (AbstractPower p : mo.powers) tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn, this);
        }
        if (tmp < 0.0F) tmp = 0.0F;
        if (this.baseDamage != MathUtils.floor(tmp)) this.isDamageModified = true;
        return tmp;
    }

    public AbstractBossCard(AbstractCard baseCard) {
        super("shadowverse:" + baseCard.cardID, baseCard.name, baseCard.assetUrl, baseCard.cost, baseCard.rawDescription, baseCard.type, baseCard.color, baseCard.rarity, baseCard.target);
        this.alwaysDisplayText = false;
        this.owner = AbstractCharBoss.boss;
        this.limit = 99;
        if (this.type == CardType.CURSE || this.type == CardType.STATUS) this.forceDraw = true;
    }

    public void triggerOnEndOfPlayerTurn() {
        if (this.isEthereal)
            addToTop((AbstractGameAction) new ExhaustSpecificCardAction(this, AbstractCharBoss.boss.hand));
    }

    public boolean hasEnoughEnergy() {
        for (AbstractPower p : AbstractCharBoss.boss.powers) {
            if (!p.canPlayCard(this)) {
                this.cantUseMessage = AbstractCard.TEXT[13];
                return false;
            }
        }
        if (AbstractCharBoss.boss.hasPower("Entangled") && this.type == CardType.ATTACK) {
            this.cantUseMessage = AbstractCard.TEXT[10];
            return false;
        }

        for (AbstractCard c : AbstractCharBoss.boss.hand.group) {
            if (!c.canPlay(this)) return false;
        }
        if (EnemyEnergyPanel.totalCount >= this.costForTurn || freeToPlay() || this.isInAutoplay) return true;
        this.cantUseMessage = AbstractCard.TEXT[11];
        return false;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (m instanceof AbstractCharBoss) {
            AbstractCharBoss cB = (AbstractCharBoss) m;
            if (this.type == CardType.CURSE && this.costForTurn < -1) return false;
            if (this.type == CardType.STATUS) return false;
            if (cardPlayable(m) && hasEnoughEnergy()) return true;
            return false;
        }
        return false;
    }

    public boolean cardPlayable(AbstractMonster m) {
        if (m != null) {
            if (((this.target == CardTarget.ENEMY || this.target == CardTarget.SELF_AND_ENEMY) && AbstractDungeon.player != null && AbstractDungeon.player.isDying) || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.cantUseMessage = null;
                return false;
            }
            return true;
        }
        return false;
    }

    public void hover() {
        super.hover();
        if (!this.hov2) {
            this.hov2 = true;
            EnemyCardGroup.hov2holder = this;
            AbstractCharBoss.boss.hand.refreshHandLayout();
            this.targetDrawScale = 0.8F;
            this.drawScale = 0.8F;
        }
    }

    public void unhover() {
        super.unhover();
        if (this.hov2) {
            this.hov2 = false;
            if (EnemyCardGroup.hov2holder == this) EnemyCardGroup.hov2holder = null;
            AbstractCharBoss.boss.hand.refreshHandLayout();
            this.targetDrawScale = 0.35F;
        }
    }

    public void bossDarken() {
        if (!this.bossDarkened) this.bossDarkened = true;
    }

    public void bossLighten() {
        if (this.bossDarkened) this.bossDarkened = false;
    }

    public void renderHelperB(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY) {
        sb.setColor(color);
        sb.draw((TextureRegion) img, drawX + img.offsetX - img.originalWidth / 2.0F, drawY + img.offsetY - img.originalHeight / 2.0F, img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY, img.packedWidth, img.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
    }

    public String overrideIntentText() {
        if (this.type == CardType.POWER && (this.owner.hasPower("Storm") || fakeStormPower)) {
            return "(" + (3 + AbstractEnemyOrb.masterPretendFocus) + ")";
        }
        if (this.isMultiDamage) {
            return this.intentDmg + "x" + this.intentMultiAmt;
        }
        return Integer.toString(this.intentDmg);
    }

    public void renderHelperB(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY, float scale) {
        sb.setColor(color);
        sb.draw((TextureRegion) img, drawX + img.offsetX - img.originalWidth / 2.0F, drawY + img.offsetY - img.originalHeight / 2.0F, img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY, img.packedWidth, img.packedHeight, this.drawScale * Settings.scale * scale, this.drawScale * Settings.scale * scale, this.angle);
    }

    public void renderHelperB(SpriteBatch sb, Color color, Texture img, float drawX, float drawY) {
        sb.setColor(color);
        sb.draw(img, drawX + 256.0F, drawY + 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 512, 512, false, false);
    }

    public void renderHelperB(SpriteBatch sb, Color color, Texture img, float drawX, float drawY, float scale) {
        sb.setColor(color);
        sb.draw(img, drawX, drawY, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale * scale, this.drawScale * Settings.scale * scale, this.angle, 0, 0, 512, 512, false, false);
    }

    public void beginGlowing() {
    }

    public void stopGlowing() {
    }

    public void update() {
        if (AbstractCharBoss.boss != null) {
            super.update();
            if (this.intent != null) updateIntent();
        }
        if (this.bossDarkened)
            ReflectionHacks.setPrivate(this, AbstractCard.class, "tintColor", new Color(109.65F, 94.35F, 165.75F, 0.0F));
    }

    public void refreshIntentHbLocation() {
        this.intentHb.move(this.target_x + this.intentOffsetX, this.target_y + this.intentOffsetY);
    }

    private void updateIntent() {
        this.bobEffect.update();
        if (this.intentAlpha != this.intentAlphaTarget && this.intentAlphaTarget == 1.0F) {
            this.intentAlpha += Gdx.graphics.getDeltaTime();
            if (this.intentAlpha > this.intentAlphaTarget) this.intentAlpha = this.intentAlphaTarget;
        } else if (this.intentAlphaTarget == 0.0F) {
            this.intentAlpha -= Gdx.graphics.getDeltaTime() / 1.5F;
            if (this.intentAlpha < 0.0F) this.intentAlpha = 0.0F;
        }
        if (!this.owner.isDying && !this.owner.isEscaping) updateIntentVFX();
        Iterator<AbstractGameEffect> i = this.intentVfx.iterator();
        while (i.hasNext()) {
            AbstractGameEffect e = i.next();
            e.update();
            if (e.isDone) i.remove();
        }
        i = this.intentFlash.iterator();
        while (i.hasNext()) {
            AbstractGameEffect e = i.next();
            e.update();
            if (e.isDone) i.remove();
        }
    }

    private void updateIntentVFX() {
        if (this.intentAlpha > 0.0F)
            if (this.intent != AbstractMonster.Intent.ATTACK_DEBUFF && this.intent != AbstractMonster.Intent.DEBUFF && this.intent != AbstractMonster.Intent.STRONG_DEBUFF && this.intent != AbstractMonster.Intent.DEFEND_DEBUFF) {
                if (this.intent != AbstractMonster.Intent.ATTACK_BUFF && this.intent != AbstractMonster.Intent.BUFF && this.intent != AbstractMonster.Intent.DEFEND_BUFF) {
                    if (this.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
                        this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                        if (this.intentParticleTimer < 0.0F) {
                            this.intentParticleTimer = 0.5F;
                            this.intentVfx.add(new ShieldParticleEffect(this.intentHb.cX, this.intentHb.cY));
                        }
                    } else if (this.intent == AbstractMonster.Intent.UNKNOWN) {
                        this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                        if (this.intentParticleTimer < 0.0F) {
                            this.intentParticleTimer = 0.5F;
                            this.intentVfx.add(new UnknownParticleEffect(this.intentHb.cX, this.intentHb.cY));
                        }
                    } else if (this.intent == AbstractMonster.Intent.STUN) {
                        this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                        if (this.intentParticleTimer < 0.0F) {
                            this.intentParticleTimer = 0.67F;
                            this.intentVfx.add(new StunStarEffect(this.intentHb.cX, this.intentHb.cY));
                        }
                    }
                } else {
                    this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                    if (this.intentParticleTimer < 0.0F) {
                        this.intentParticleTimer = 0.1F;
                        this.intentVfx.add(new BuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
                    }
                }
            } else {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 1.0F;
                    this.intentVfx.add(new DebuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
                }
            }
    }

    private void renderIntentVfxBehind(SpriteBatch sb) {
        Iterator<AbstractGameEffect> var2 = this.intentVfx.iterator();

        while (var2.hasNext()) {
            AbstractGameEffect e = var2.next();
            if (e.renderBehind)
                e.render(sb);
        }
    }

    private void updateIntentTip() {
        switch (this.intent) {
            case ATTACK:
                this.intentTip.header = TEXT[0];
                if (this.isMultiDamage) {
                    this.intentTip.body = TEXT[1] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[3];
                } else {
                    this.intentTip.body = TEXT[4] + this.intentDmg + TEXT[5];
                }
                this.intentTip.img = getAttackIntentTip();
                return;
            case ATTACK_BUFF:
                this.intentTip.header = TEXT[6];
                if (this.isMultiDamage) {
                    this.intentTip.body = TEXT[7] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[8];
                } else {
                    this.intentTip.body = TEXT[9] + this.intentDmg + TEXT[5];
                }
                this.intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
                return;
            case ATTACK_DEBUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[11] + this.intentDmg + TEXT[5];
                this.intentTip.img = ImageMaster.INTENT_ATTACK_DEBUFF;
                return;
            case ATTACK_DEFEND:
                this.intentTip.header = TEXT[0];
                if (this.isMultiDamage) {
                    this.intentTip.body = TEXT[12] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[3];
                } else {
                    this.intentTip.body = TEXT[12] + this.intentDmg + TEXT[5];
                }
                this.intentTip.img = ImageMaster.INTENT_ATTACK_DEFEND;
                return;
            case BUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[19];
                this.intentTip.img = ImageMaster.INTENT_BUFF;
                return;
            case DEBUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[20];
                this.intentTip.img = ImageMaster.INTENT_DEBUFF;
                return;
            case STRONG_DEBUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[21];
                this.intentTip.img = ImageMaster.INTENT_DEBUFF2;
                return;
            case DEFEND:
                this.intentTip.header = TEXT[13];
                this.intentTip.body = TEXT[22];
                this.intentTip.img = ImageMaster.INTENT_DEFEND;
                return;
            case DEFEND_DEBUFF:
                this.intentTip.header = TEXT[13];
                this.intentTip.body = TEXT[23];
                this.intentTip.img = ImageMaster.INTENT_DEFEND;
                return;
            case DEFEND_BUFF:
                this.intentTip.header = TEXT[13];
                this.intentTip.body = TEXT[24];
                this.intentTip.img = ImageMaster.INTENT_DEFEND_BUFF;
                return;
            case ESCAPE:
                this.intentTip.header = TEXT[14];
                this.intentTip.body = TEXT[25];
                this.intentTip.img = ImageMaster.INTENT_ESCAPE;
                return;
            case MAGIC:
                this.intentTip.header = TEXT[15];
                this.intentTip.body = TEXT[26];
                this.intentTip.img = ImageMaster.INTENT_MAGIC;
                return;
            case SLEEP:
                this.intentTip.header = TEXT[16];
                this.intentTip.body = TEXT[27];
                this.intentTip.img = ImageMaster.INTENT_SLEEP;
                return;
            case STUN:
                this.intentTip.header = TEXT[17];
                this.intentTip.body = TEXT[28];
                this.intentTip.img = ImageMaster.INTENT_STUN;
                return;
            case UNKNOWN:
                this.intentTip.header = TEXT[18];
                this.intentTip.body = TEXT[29];
                this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
                return;
            case NONE:
                this.intentTip.header = "";
                this.intentTip.body = "";
                this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
                return;
        }
        this.intentTip.header = "NOT SET";
        this.intentTip.body = "NOT SET";
        this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
    }

    private Texture getAttackIntentTip() {
        int tmp;
        if (this.isMultiDamage || this.intentMultiAmt > 0) {
            tmp = this.intentDmg * this.intentMultiAmt;
        } else {
            tmp = this.intentDmg;
        }
        if (tmp < 5) return ImageMaster.INTENT_ATK_TIP_1;
        if (tmp < 10) return ImageMaster.INTENT_ATK_TIP_2;
        if (tmp < 15) return ImageMaster.INTENT_ATK_TIP_3;
        if (tmp < 20) return ImageMaster.INTENT_ATK_TIP_4;
        if (tmp < 25) return ImageMaster.INTENT_ATK_TIP_5;
        return (tmp < 30) ? ImageMaster.INTENT_ATK_TIP_6 : ImageMaster.INTENT_ATK_TIP_7;
    }

    public void createIntent() {
        if (this.intent == null) return;
        refreshIntentHbLocation();
        if (!this.intentActive) this.intentParticleTimer = 0.5F;
        if (!this.lockIntentValues) calculateCardDamage(null);
        if (!this.lockIntentValues && this.damage > -1) if (this.isMultiDamage) {
            this.intentMultiAmt = this.magicNumber;
        } else {
            this.intentMultiAmt = -1;
        }
        if (!this.intentActive) this.intentImg = getIntentImg();
        if (!this.intentActive) this.intentBg = getIntentBg();
        this.tipIntent = this.intent;
        if (!this.intentActive) this.intentAlpha = 0.0F;
        if (!this.intentActive) this.intentAlphaTarget = 1.0F;
        updateIntentTip();
        this.showIntent = true;
        this.intentActive = true;
    }

    public int customIntentModifiedDamage() {
        return 0;
    }

    public void destroyIntent() {
        if (this.intent != null) {
            this.intentImg = null;
            this.intentBg = null;
            this.intentAlpha = 0.0F;
            this.intentAlphaTarget = 0.0F;
            this.showIntent = false;
            this.intentParticleTimer = 0.0F;
            this.tipIntent = null;
        }
    }

    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.intent != null && this.showIntent && !this.hov2) {
            if (!this.owner.isDying && !this.owner.isEscaping && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic("Runic Dome") && this.intent != AbstractMonster.Intent.NONE && !Settings.hideCombatElements) {
                renderIntentVfxBehind(sb);
                renderIntent(sb);
                renderIntentVfxAfter(sb);
                renderDamageRange(sb);
            }
            this.hb.render(sb);
            this.intentHb.render(sb);
        }
    }

    private Texture getIntentImg() {
        switch (this.intent) {
            case ATTACK:
                return getAttackIntent();
            case ATTACK_BUFF:
                return getAttackIntent();
            case ATTACK_DEBUFF:
                return getAttackIntent();
            case ATTACK_DEFEND:
                return getAttackIntent();
            case BUFF:
                return ImageMaster.INTENT_BUFF_L;
            case DEBUFF:
                return ImageMaster.INTENT_DEBUFF_L;
            case STRONG_DEBUFF:
                return ImageMaster.INTENT_DEBUFF2_L;
            case DEFEND:
            case DEFEND_DEBUFF:
                return ImageMaster.INTENT_DEFEND_L;
            case DEFEND_BUFF:
                return ImageMaster.INTENT_DEFEND_BUFF_L;
            case ESCAPE:
                return ImageMaster.INTENT_ESCAPE_L;
            case MAGIC:
                return ImageMaster.INTENT_MAGIC_L;
            case SLEEP:
                return ImageMaster.INTENT_SLEEP_L;
            case STUN:
                return null;
            case UNKNOWN:
                return ImageMaster.INTENT_UNKNOWN_L;
        }
        return ImageMaster.INTENT_UNKNOWN_L;
    }

    private Texture getIntentBg() {
        switch (this.intent) {
            case ATTACK_DEFEND:
                return null;
        }
        return null;
    }

    protected Texture getAttackIntent() {
        int tmp;
        if (this.isMultiDamage || this.intentMultiAmt > 0) {
            tmp = this.intentDmg * this.intentMultiAmt;
        } else {
            tmp = this.intentDmg;
        }
        if (tmp < 5) return ImageMaster.INTENT_ATK_1;
        if (tmp < 10) return ImageMaster.INTENT_ATK_2;
        if (tmp < 15) return ImageMaster.INTENT_ATK_3;
        if (tmp < 20) return ImageMaster.INTENT_ATK_4;
        if (tmp < 25) return ImageMaster.INTENT_ATK_5;
        return (tmp < 30) ? ImageMaster.INTENT_ATK_6 : ImageMaster.INTENT_ATK_7;
    }

    private void renderDamageRange(SpriteBatch sb) {
        if (this.intent.name().contains("ATTACK") || this.alwaysDisplayText)
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, overrideIntentText(), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
    }

    private void renderIntentVfxAfter(SpriteBatch sb) {
        Iterator<AbstractGameEffect> var2 = this.intentVfx.iterator();

        while (var2.hasNext()) {
            AbstractGameEffect e = var2.next();
            if (!e.renderBehind) {
                e.render(sb);
            }
        }
    }


    private void renderIntent(SpriteBatch sb) {
        this.intentColor.a = this.intentAlpha;
        sb.setColor(this.intentColor);
        if (this.intentBg != null) {
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.intentAlpha / 2.0F));
            sb.draw(this.intentBg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
        }

        if (this.intentImg != null && this.intent != AbstractMonster.Intent.UNKNOWN && this.intent != AbstractMonster.Intent.STUN) {
            if (this.intent != AbstractMonster.Intent.DEBUFF && this.intent != AbstractMonster.Intent.STRONG_DEBUFF) {
                this.intentAngle = 0.0F;
            } else {
                this.intentAngle += Gdx.graphics.getDeltaTime() * 150.0F;
            }

            sb.setColor(this.intentColor);
            sb.draw(this.intentImg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, this.intentAngle, 0, 0, 128, 128, false, false);
        }

        Iterator<AbstractGameEffect> var2 = this.intentFlash.iterator();

        while (var2.hasNext()) {
            AbstractGameEffect e = var2.next();
            e.render(sb, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F);
        }
    }


    static {
        TEXT = (CardCrawlGame.languagePack.getUIString("AbstractMonster")).TEXT;
    }
}

