package shadowverseCharbosses.bosses.KMR;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import shadowverse.action.ChangeSpriteAction;
import shadowverse.cards.temp.KMRsPresent;
import shadowverse.characters.Nemesis;
import shadowverse.effect.ShadowverseEnergyOrb;
import shadowverse.monsters.SpriteCreature;
import shadowverse.powers.AbsoluteOnePower;
import shadowverse.powers.LionSanctuaryPower;
import shadowverseCharbosses.actions.util.CharbossTurnstartDrawAction;
import shadowverseCharbosses.actions.util.DelayedActionAction;
import shadowverseCharbosses.bosses.AbstractBossDeckArchetype;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.core.EnemyEnergyManager;

public class KMR
        extends AbstractCharBoss implements SpriteCreature {
    public static final String ID = "shadowverse:KMR";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:KMR");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean secondPhase = true;
    private static Texture BASE_LAYER = new Texture("img/ui/layer_nemesis.png");
    private SpriterAnimation extra;

    public KMR() {
        super(NAME, ID, 500, -4.0F, -16.0F, 220.0F, 400.0F, null, 0.0F, -20.0F, AbstractPlayer.PlayerClass.IRONCLAD);
        if (Loader.isModLoaded("shadowverse")) {
            this.chosenClass = Nemesis.Enums.Nemesis;
        }
        this.energyOrb = new ShadowverseEnergyOrb(null, null,null,BASE_LAYER);
        this.energy = new EnemyEnergyManager(5);
        this.animation = new SpriterAnimation("img/monsters/KMR/KMR.scml");
        if (Settings.MAX_FPS > 30){
            extra = new SpriterAnimation("img/monsters/KMR/extra/KMR.scml");
        }
        this.type = EnemyType.BOSS;
        this.dialogX = -100.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
    }


    public void generateDeck() {
        AbstractBossDeckArchetype archetype = new ArchetypeKMR();
        archetype.initialize();
        this.chosenArchetype = archetype;
    }

    public void loseBlock(int amount) {
        super.loseBlock(amount);
    }

    @Override
    public void usePreBattleAction() {
        this.energy.recharge();
        addToBot(new DelayedActionAction(new CharbossTurnstartDrawAction()));
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("GrandBattle");
        (AbstractDungeon.getCurrRoom()).cannotLose = true;
        AbstractDungeon.actionManager.addToBottom(new SFXAction("KMR1"));
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
        if (AbstractDungeon.ascensionLevel >= 19) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, (AbstractPower) new InvinciblePower(this, 300), 300));
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, (AbstractPower) new InvinciblePower(this, 350), 350));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, (AbstractPower) new InvinciblePower(this, 400), 400));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, (AbstractPower) new LionSanctuaryPower(this, 3), 3));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction((AbstractCard)new KMRsPresent()));
        this.chosenArchetype.addedPreBattle();
    }

    public void takeTurn() {
        if (this.nextMove == 20) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(this, (AbstractGameEffect) new IntenseZoomEffect(this.hb.cX, this.hb.cY, true), 0.05F, true));
            if (this.secondPhase){
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "REBIRTH"));
            }else {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CALAMITY"));
            }
            setMove((byte) 0, Intent.NONE);
        } else {
            super.takeTurn();
        }
    }

    @Override
    public void changeState(String key) {
        super.changeState(key);
        switch (key) {
            case "REBIRTH":
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.maxHealth = 1000;
                } else if (AbstractDungeon.ascensionLevel >= 4) {
                    this.maxHealth = 900;
                } else {
                    this.maxHealth = 800;
                }
                this.halfDead = false;
                this.secondPhase = false;
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[1]));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("KMR2"));
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
                AbstractBossDeckArchetype archetype = new ArchetypeKMR2();
                archetype.initialize();
                this.chosenArchetype = archetype;
                this.chosenArchetype.addedPreBattle();
                if (Settings.MAX_FPS > 30){
                    AbstractDungeon.actionManager.addToBottom(new ChangeSpriteAction(extra, this, 2.1F));
                }
                break;
            case "CALAMITY":
                this.powers.removeIf(p -> p.type == AbstractPower.PowerType.DEBUFF || p.ID.equals(AbsoluteOnePower.POWER_ID) || p.ID.equals(InvinciblePower.POWER_ID));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.maxHealth = 1000;
                } else if (AbstractDungeon.ascensionLevel >= 4) {
                    this.maxHealth = 900;
                } else {
                    this.maxHealth = 800;
                }
                this.halfDead = false;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, (AbstractPower) new InvinciblePower(this, 500), 500));
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[2]));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("KMR3"));
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
                AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
                AbstractBossDeckArchetype archetype3 = new ArchetypeKMR3();
                archetype3.initialize();
                this.chosenArchetype = archetype3;
                this.chosenArchetype.addedPreBattle();
                if (Settings.MAX_FPS > 30){
                    AbstractDungeon.actionManager.addToBottom(new ChangeSpriteAction(extra, this, 2.1F));
                }
                break;
            default:
                break;
        }
    }

    public void onPlayAttackCardSound() {
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            if ((AbstractDungeon.getCurrRoom()).cannotLose) {
                this.halfDead = true;
                for (AbstractPower p : this.powers)
                    p.onDeath();
                for (AbstractRelic r : AbstractDungeon.player.relics)
                    r.onMonsterDeath(this);
                addToTop(new ClearCardQueueAction());
                this.powers.removeIf(p -> p.type == AbstractPower.PowerType.DEBUFF || p.ID.equals(LionSanctuaryPower.POWER_ID));
                setMove((byte) 20, AbstractMonster.Intent.UNKNOWN);
                createIntent();
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte) 20, AbstractMonster.Intent.UNKNOWN));
                applyPowers();
            }
        }
    }

    public SpriterAnimation getAnimation() {
        return (SpriterAnimation) this.animation;
    }
    public void setAnimation(SpriterAnimation animation) {
        this.animation = animation;
    }

    public void die() {
        if (!(AbstractDungeon.getCurrRoom()).cannotLose) {
            super.die();
            onBossVictoryLogic();
            onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        }
    }


    public void render(SpriteBatch sb) {
        super.render(sb);
    }
}


