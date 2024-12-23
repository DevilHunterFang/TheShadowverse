package shadowverse.monsters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.BloodShotEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import shadowverse.action.ChangeSpriteAction;
import shadowverse.action.eventAction;
import shadowverse.powers.ICPower;
import shadowverse.powers.ICPower2;
import shadowverse.relics.Bullet;

import java.util.HashMap;

public class Iceschillendrig extends CustomMonster implements SpriteCreature {
    public static final String ID = "shadowverse:Iceschillendrig";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Iceschillendrig");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int Dmg;

    private int bloodHitCount;

    private int debuffAmount;

    private int turnsTaken = 0;

    private boolean usedHaste = false;

    private boolean usedPenalty = false;

    private float spawnX = -100.0F;

    private static final String DEATH_PENALTY = MOVES[0];

    private HashMap<Integer, AbstractMonster> enemySlots = new HashMap<>();

    public GenericEventDialog imageEventText;

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("shadowverse:IceschillendrigEvent");

    private int eventNext;

    private int selection;

    public SpriterAnimation stage2 = new SpriterAnimation("img/monsters/Iceschillendrig/stage2/stage2.scml");

    public SpriterAnimation extra = new SpriterAnimation("img/monsters/Iceschillendrig/stage2/extra/extra.scml");

    private boolean secondStage;
    public Iceschillendrig() {
        super(NAME, ID, 450, 0.0F, -30F, 230.0F, 560.0F, null, 80.0F, -50.0F);
        this.animation = new SpriterAnimation("img/monsters/Iceschillendrig/Iceschillendrig.scml");
        this.dialogX = -100.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        this.type = EnemyType.BOSS;
        AbstractEvent.type = AbstractEvent.EventType.IMAGE;
        this.imageEventText = new GenericEventDialog();
        GenericEventDialog.hide();
        this.imageEventText.clear();
        eventAction.iceschillendrig = this;
        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(500);
        } else {
            setHp(450);
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.debuffAmount = 3;
            this.bloodHitCount = 7;
            this.Dmg = 25;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.bloodHitCount = 6;
            this.debuffAmount = 2;
            this.Dmg = 23;
        } else {
            this.bloodHitCount = 5;
            this.debuffAmount = 2;
            this.Dmg = 21;
        }
        this.damage.add(new DamageInfo(this, this.Dmg));
        this.damage.add(new DamageInfo(this, 2));
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("IceschillendrigBgm");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ICPower(AbstractDungeon.player, this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WeakPower(this, 99, true)));
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
        addToBot(new SFXAction("IC1"));
        AbstractMonster m = new MagiTrain(this.spawnX + -185.0F * 1, MathUtils.random(-10.0F, -20.0F));
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m, true));
        this.enemySlots.put(1, m);
        if (AbstractDungeon.ascensionLevel >= 19) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new ArtifactPower(m, 2)));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new ArtifactPower(m, 1)));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new InvinciblePower(m, 80), 80));

    }


    @Override
    public void takeTurn() {
        int i;
        switch (this.nextMove) {
            case 2:
                if (!this.secondStage) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
                    addToBot(new SFXAction("IC2"));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[7]));
                    addToBot(new SFXAction("IC_Stage2_A1"));
                }
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.bloodHitCount), 0.25F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.bloodHitCount), 0.6F));
                }
                for (i = 0; i < this.bloodHitCount; i++)
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                break;
            case 3:
                if (this.secondStage) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[9]));
                    addToBot(new SFXAction("IC_Stage2_A3"));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2]));
                    addToBot(new SFXAction("IC3"));
                }
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ViceCrushEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 4:
                int cardCount = 1;
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3]));
                addToBot(new SFXAction("IC4"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 1.5F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -4), -4));
                for (AbstractCard ca : AbstractDungeon.player.drawPile.group) {
                    if (ca.rarity == AbstractCard.CardRarity.RARE) {
                        cardCount--;
                        AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(ca, AbstractDungeon.player.drawPile));
                        break;
                    }
                }
                if (cardCount > 0) {
                    for (AbstractCard ca : AbstractDungeon.player.discardPile.group) {
                        if (ca.rarity == AbstractCard.CardRarity.RARE) {
                            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(ca, AbstractDungeon.player.discardPile));
                            break;
                        }
                    }
                }
                break;
            case 5:
                int str = 1;
                if (this.secondStage) {
                    str = 2;
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[8]));
                    addToBot(new SFXAction("IC_Stage2_A2"));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true)));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[4]));
                    addToBot(new SFXAction("IC5"));
                }
                for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                    if (!mo.isDead && !mo.isDying && !mo.isEscaping) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(mo, this, 20));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this, new StrengthPower(mo, str), str));
                    }
                }
                break;
            case 6:
                eventNext = 0;
                ReflectionHacks.setPrivate(this.imageEventText, GenericEventDialog.class, "title", eventStrings.NAME);
                this.imageEventText.loadImage("img/event/Furozes.png");
                this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[0]);
                this.imageEventText.setDialogOption(eventStrings.OPTIONS[0]);
                AbstractDungeon.actionManager.addToBottom(new eventAction());
                break;
            default:
                System.out.println("ERROR: Default Take Turn was called on " + this.name);
                break;
        }
        if (this.secondStage){
            if (this.hasPower(ICPower2.POWER_ID)){
                addToBot((AbstractGameAction)new DamageAction(AbstractDungeon.player,new DamageInfo(this,((ICPower2)this.getPower(ICPower2.POWER_ID)).amount2, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
            }
        }
        this.turnsTaken++;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (this.currentHealth < this.maxHealth / 2 && !this.usedHaste) {
            this.usedHaste = true;
            setMove((byte) 6, Intent.BUFF);
            return;
        }
        if (!usedPenalty && this.turnsTaken >= 2 && !lastMove((byte) 4)) {
            setMove(DEATH_PENALTY, (byte) 4, Intent.STRONG_DEBUFF);
            usedPenalty = true;
            return;
        }
        if (i <= 70) {
            if (!lastMove((byte) 2)) {
                setMove((byte) 2, Intent.ATTACK, ((DamageInfo) this.damage.get(1)).base, this.bloodHitCount, true);
                return;
            }
            setMove((byte) 5, Intent.DEFEND_BUFF);
            return;
        }
        if (!lastMove((byte) 3)) {
            setMove((byte) 3, Intent.ATTACK, ((DamageInfo) this.damage.get(0)).base);
        }
    }

    public void die() {
        super.die();
        (AbstractDungeon.getCurrRoom()).cannotLose = false;
        useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m.isDying || m.isDead)
                continue;
            AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
            AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
            AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
        }
        onBossVictoryLogic();
    }

    public void buttonEffect(int p) {
        switch (eventNext) {
            case 0:
                eventNext = 1;
                this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[1]);
                this.imageEventText.updateDialogOption(0, eventStrings.OPTIONS[1]);
                this.imageEventText.setDialogOption(eventStrings.OPTIONS[2]);
                this.imageEventText.setDialogOption(eventStrings.OPTIONS[3]);
                AbstractDungeon.actionManager.addToBottom(new eventAction());
                break;
            case 1:
                switch (p) {
                    case 0:
                        selection = 0;
                        eventNext = 2;
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, eventStrings.OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        eventNext = 0;
                        break;
                    case 2:
                        if (AbstractDungeon.player.hasRelic(Bullet.ID)) {
                            selection = 1;
                            this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[4]);
                            this.imageEventText.updateDialogOption(0, eventStrings.OPTIONS[5]);
                            this.imageEventText.clearRemainingOptions();
                            AbstractDungeon.player.getRelic(Bullet.ID).counter = 0;
                            ((Bullet) AbstractDungeon.player.getRelic(Bullet.ID)).changeDescription();
                            eventNext = 2;
                        } else {
                            this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[2]);
                            this.imageEventText.updateDialogOption(0, eventStrings.OPTIONS[0]);
                            this.imageEventText.clearRemainingOptions();
                            eventNext = 0;
                        }
                        break;
                }
                AbstractDungeon.actionManager.addToBottom(new eventAction());
                break;
            case 2:
                this.imageEventText.clearAllDialogs();
                this.imageEventText.clear();
                switch (selection) {
                    case 0:
                        AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[5], 0.5F, 2.0F));
                        AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
                        this.increaseMaxHp(AbstractDungeon.player.maxHealth * 10 - this.maxHealth, true);
                        AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, AbstractDungeon.player.maxHealth * 10));
                        for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                            if (!mo.isDead && !mo.isDying && !mo.isEscaping) {
                                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(mo, this, 20));
                                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this, new StrengthPower(mo, 1), 1));
                            }
                        }
                        break;
                    case 1:
                        this.setAnimation(stage2);
                        addToBot(new ChangeStateAction(this, "STAGE2"));
                        break;
                }
                break;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (AbstractDungeon.id != null)
            this.imageEventText.render(sb);
    }

    public void setAnimation(SpriterAnimation animation) {
        this.animation = animation;
    }

    public SpriterAnimation getAnimation() {
        return (SpriterAnimation) this.animation;
    }

    public void changeState(String key) {
        if ("STAGE2".equals(key)) {
            this.secondStage = true;
            if (AbstractDungeon.ascensionLevel >= 9) {
                this.maxHealth = 500;
            } else {
                this.maxHealth = 400;
            }
            AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[6], 0.5F, 2.0F));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("IC_Stage2"));
            AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
            AbstractDungeon.actionManager.addToBottom(new ChangeSpriteAction(this.extra, this, 1.6F));
            AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
            if (AbstractDungeon.player.hasPower(ICPower.POWER_ID)) {
                addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, this, ICPower.POWER_ID));
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ICPower2(this)));
            for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                if (!mo.isDead && !mo.isDying && !mo.isEscaping) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(mo, this, 20));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this, new StrengthPower(mo, 1), 1));
                }
            }
            for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                if (!mo.isDead && mo != this) {
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
                }
            }
            AbstractMonster zecilwenshe = new Zecilwenshe(this.spawnX + -185.0F * 1, -70.0F);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(zecilwenshe, true, 0));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(zecilwenshe, this, new StrengthPower(zecilwenshe,2),2));
            (AbstractDungeon.getCurrRoom()).cannotLose = true;
        }
    }


}
