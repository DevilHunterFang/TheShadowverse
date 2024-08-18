package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.actions.unique.ApplyStasisAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StasisPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.BloodShotEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import shadowverse.action.SummonShadowAction;
import shadowverse.cards.temp.MaiserTitan;
import shadowverse.cards.temp.SpectorTitan;
import shadowverse.characters.Bishop;
import shadowverse.characters.Royal;
import shadowverse.characters.Witchcraft;
import shadowverse.powers.MaiserTitanPower;
import shadowverse.powers.NexusPower;
import shadowverse.powers.SpectorTitanPower;

import java.util.ArrayList;

public class Nexus extends CustomMonster {
    public static final String ID = "shadowverse:Nexus";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Nexus");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;
    public AbstractMonster[] shades = new AbstractMonster[2];

    public float[] POSX = new float[2];

    private int strAmt;

    private int blockAmt;

    private int STAB_DMG = 5;

    private int heavyDmg;

    private boolean usedHaste = false;

    private int resetAmt;
    public Nexus(float x, float y) {
        super(NAME, ID, 400, 0.0F, -40.0F, 350.0F, 550.0F, "img/monsters/Shadows/Nexus.png", x, y);
        this.type = EnemyType.BOSS;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = 20.0F * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(500);
            resetAmt = 4;
        } else {
            setHp(400);
            resetAmt = 3;
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.strAmt = 4;
            this.blockAmt = 8;
            this.heavyDmg = 10;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.strAmt = 3;
            this.blockAmt = 6;
            this.heavyDmg = 8;
        } else {
            this.strAmt = 2;
            this.blockAmt = 6;
            this.heavyDmg = 7;
        }
        this.damage.add(new DamageInfo(this, this.STAB_DMG));
        this.damage.add(new DamageInfo(this, this.heavyDmg));
        POSX[0] = x - 331.0F;
        POSX[1] = x - 566.0F;
    }

    public Nexus(){
        this(65.0F,-40.0F);
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("YuwanTheme");
        (AbstractDungeon.getCurrRoom()).cannotLose = true;
        addToBot(new ApplyPowerAction(this,this,new NexusPower(this,POSX)));
        if (Loader.isModLoaded("shadowverse")){
            if (AbstractDungeon.player instanceof Royal){
                addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new MaiserTitanPower(AbstractDungeon.player,1),1));
            }else if (AbstractDungeon.player instanceof Witchcraft || AbstractDungeon.player instanceof Bishop){
                addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new SpectorTitanPower(AbstractDungeon.player)));
            }else {
                ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
                stanceChoices.add(new MaiserTitan());
                stanceChoices.add(new SpectorTitan());
                addToBot(new ChooseOneAction(stanceChoices));
            }
        }else {
            ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
            stanceChoices.add(new MaiserTitan());
            stanceChoices.add(new SpectorTitan());
            addToBot(new ChooseOneAction(stanceChoices));
        }
    }

    @Override
    public void takeTurn() {
        int powAmt = 0;
        if (this.hasPower(NexusPower.POWER_ID)){
            powAmt = this.getPower(NexusPower.POWER_ID).amount;
        }
        switch (this.nextMove) {
            case 2:
                if (numAliveShades() == 0) {
                    if (powAmt > 20) {
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new General(POSX[0], -40.0F), new Commander(POSX[1], -40.0F)));
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new General(POSX[0], -40.0F), new Commander(POSX[1], -40.0F)));
                    } else if (powAmt > 16) {
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new General(POSX[0], -40.0F), new Assault(POSX[1], -40.0F)));
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new General(POSX[0], -40.0F), new Assault(POSX[1], -40.0F)));
                    } else if (powAmt > 12) {
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Assault(POSX[0], -40.0F), new Assault(POSX[1], -40.0F)));
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Assault(POSX[0], -40.0F), new Assault(POSX[1], -40.0F)));
                    } else if (powAmt > 8) {
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Commander(POSX[0], -40.0F), new Shade(POSX[1], -40.0F)));
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Commander(POSX[0], -40.0F), new Shade(POSX[1], -40.0F)));
                    } else if (powAmt > 4) {
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Assault(POSX[0], -40.0F), new Shade(POSX[1], -40.0F)));
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Assault(POSX[0], -40.0F), new Shade(POSX[1], -40.0F)));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Shade(POSX[0], -40.0F), new Shade(POSX[1], -40.0F)));
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Shade(POSX[0], -40.0F), new Shade(POSX[1], -40.0F)));
                    }
                } else {
                    if (powAmt > 16) {
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new General(POSX[0], -40.0F), new General(POSX[1], -40.0F)));
                    } else if (powAmt > 8) {
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Commander(POSX[0], -40.0F), new Commander(POSX[1], -40.0F)));
                    } else if (powAmt > 4) {
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Assault(POSX[0], -40.0F), new Assault(POSX[1], -40.0F)));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new SummonShadowAction(shades, new Shade(POSX[0], -40.0F), new Shade(POSX[1], -40.0F)));
                    }
                }
                break;
            case 3:
                addToBot(new SFXAction("Nexus_E1"));
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[4]));
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                    if (m == this) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.strAmt), this.strAmt));
                        continue;
                    }
                    if (!m.isDeadOrEscaped()) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.strAmt), this.strAmt));
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, this.blockAmt));
                    }
                }
                break;
            case 4:
                addToBot(new SFXAction("Nexus_A1"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2]));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, 3), 0.25F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, 3), 0.6F));
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 5:
                addToBot(new SFXAction("Nexus_A2"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3]));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
                addToBot(new MakeTempCardInDiscardAction(new Dazed(), 2));
                break;
            case 6:
                addToBot(new SFXAction("Nexus_E2"));
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[5]));
                AbstractDungeon.actionManager.addToBottom(new ApplyStasisAction(this));
                addToBot(new HealAction(this, this, this.maxHealth));
                AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth / 2 - this.currentHealth));
                break;
            case 7:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new IntenseZoomEffect(this.hb.cX, this.hb.cY, true), 0.05F, true));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "RESTART"));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (this.currentHealth < this.maxHealth / 2 && !this.usedHaste && !lastMove((byte)7)) {
            this.usedHaste = true;
            setMove((byte)6, Intent.BUFF);
            return;
        }
        if (numAliveShades() == 0) {
            setMove((byte)2, AbstractMonster.Intent.UNKNOWN);
        } else if (numAliveShades() < 2) {
            if (num < 50) {
                if (!lastMove((byte)2)) {
                    setMove((byte)2, Intent.UNKNOWN);
                } else {
                    getMove(AbstractDungeon.aiRng.random(50, 99));
                }
            } else if (num < 80) {
                if (!lastMove((byte)3)) {
                    setMove((byte)3, Intent.DEFEND_BUFF);
                } else {
                    setMove((byte)4, Intent.ATTACK, this.STAB_DMG, 3, true);
                }
            } else if (!lastMove((byte)4)) {
                setMove((byte)4, Intent.ATTACK, this.STAB_DMG, 3, true);
            } else {
                getMove(AbstractDungeon.aiRng.random(0, 80));
            }
        } else if (numAliveShades() > 1) {
            if (num < 66) {
                if (!lastMove((byte)3)) {
                    setMove((byte)3, Intent.DEFEND_BUFF);
                } else {
                    setMove((byte)4, Intent.ATTACK, this.STAB_DMG, 3, true);
                }
            } else if (!lastMove((byte)4)) {
                setMove((byte)4, Intent.ATTACK, this.STAB_DMG, 3, true);
            } else {
                setMove((byte)3, Intent.DEFEND_BUFF);
            }
        }
    }

    private int numAliveShades() {
        int count = 0;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m != null && m != this && !m.isDeadOrEscaped())
                count++;
        }
        return count;
    }

    @Override
    public void changeState(String key) {
        super.changeState(key);
        if (key.equals("RESTART")) {
            this.maxHealth = this.resetAmt * 100;
            this.resetAmt--;
            this.halfDead = false;
            this.usedHaste = false;
            if (resetAmt == 0) {
                AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
            }
            if (this.hasPower(StasisPower.POWER_ID)){
                addToBot(new RemoveSpecificPowerAction(this,this,StasisPower.POWER_ID));
            }
            AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[1]));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("Restart"));
            AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
        }
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
                this.powers.removeIf(p -> p.type == AbstractPower.PowerType.DEBUFF);
                this.getPower(NexusPower.POWER_ID).amount = 0;
                this.getPower(NexusPower.POWER_ID).updateDescription();
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0]));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("Reset"));
                for (AbstractCard c : AbstractDungeon.player.discardPile.group){
                    if (c.type == AbstractCard.CardType.ATTACK || (CardLibrary.getCard(c.cardID)!=null && CardLibrary.getCard(c.cardID).type == AbstractCard.CardType.ATTACK)){
                        addToBot(new ExhaustSpecificCardAction(c,AbstractDungeon.player.discardPile));
                    }
                }
                addToBot(new ShuffleAllAction());
                addToBot(new ShuffleAction(AbstractDungeon.player.drawPile, false));
                addToBot(new HealAction(AbstractDungeon.player,this,5));
                setMove((byte) 7, AbstractMonster.Intent.UNKNOWN);
                createIntent();
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte) 7, AbstractMonster.Intent.UNKNOWN));
                applyPowers();
            }
        }
    }

    public void die() {
        if (!(AbstractDungeon.getCurrRoom()).cannotLose) {
            super.die();
            useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                if (!m.isDying){
                    AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(m));
                }
            }
            onBossVictoryLogic();
            onFinalBossVictoryLogic();
        }
    }
}
