package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import shadowverse.action.ChangeSpriteAction;
import shadowverse.powers.*;

import java.util.Objects;

public class VincentBOSS extends CustomMonster implements SpriteCreature {
    public static final String ID = "shadowverse:VincentBOSS";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:VincentBOSS");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int aDmg;

    private int bDmg;

    private int strAmt;

    private int debuffAmt;

    private int blockAmt;

    private int rapidFireAmt;

    private int fireDmg;

    private int fireAmt;

    private boolean firstTurn = true;

    public SpriterAnimation extra;

    public VincentBOSS() {
        super(NAME, ID, 350, 0.0F, -30F, 230.0F, 520.0F, "img/monsters/VincentBOSS/class_2513_i_60_000.png", 80.0F, -10.0F);
        if (Settings.MAX_FPS > 30){
            this.animation = new SpriterAnimation("img/monsters/VincentBOSS/VincentBOSS.scml");
            extra = new SpriterAnimation("img/monsters/VincentBOSS/extra/extra.scml");
        }
        this.dialogX = -200.0F * Settings.scale;
        this.dialogY = 20.0F * Settings.scale;
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(400, 400);
        } else {
            setHp(350, 350);
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.debuffAmt = 3;
            this.blockAmt = 24;
            this.aDmg = 18;
            this.bDmg = 10;
            this.strAmt = 2;
            this.fireDmg = 3;
            this.fireAmt = 4;
            this.rapidFireAmt = 5;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.debuffAmt = 2;
            this.blockAmt = 22;
            this.aDmg = 17;
            this.bDmg = 9;
            this.fireDmg = 2;
            this.fireAmt = 4;
            this.strAmt = 1;
            this.rapidFireAmt = 4;
        } else {
            this.debuffAmt = 2;
            this.blockAmt = 20;
            this.aDmg = 16;
            this.bDmg = 8;
            this.fireDmg = 2;
            this.fireAmt = 3;
            this.strAmt = 1;
            this.rapidFireAmt = 3;
        }
        this.damage.add(new DamageInfo(this, aDmg));
        this.damage.add(new DamageInfo(this, bDmg));
        this.damage.add(new DamageInfo(this,fireDmg));
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("StormOverRivayle");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new TimeWarpPower(this)));
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
        addToBot(new SFXAction("Vincent_Start"));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new JudgmentWordPower(AbstractDungeon.player)));
        addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new NoDamage(AbstractDungeon.player)));
        addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new ManaPower(AbstractDungeon.player,0)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[4], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom( new SFXAction("Vincent_A4"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FreezePower(AbstractDungeon.player)));
                setMove((byte)2, Intent.ATTACK,(this.damage.get(0)).base);
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2]));
                addToBot(new SFXAction("Vincent_A2"));
                if (Settings.MAX_FPS > 30){
                    AbstractDungeon.actionManager.addToBottom(new ChangeSpriteAction(extra, this, 2.0F));
                }
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ViceCrushEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                setMove((byte)3, Intent.DEFEND_BUFF);
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.blockAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strAmt), this.strAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new RapidFirePower(AbstractDungeon.player,this.rapidFireAmt,this)));
                setMove( (byte)4, Intent.STRONG_DEBUFF);
                break;
            case 4:
                addToBot(new SFXAction("JudgmentWord"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3]));
                AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    if (p instanceof ManaPower){
                        if (p.amount>0){
                            addToBot(new ReducePowerAction(AbstractDungeon.player,AbstractDungeon.player,p,1));
                            break;
                        }
                    }
                    if (p.type == AbstractPower.PowerType.BUFF&& !Objects.equals(p.ID, StrengthPower.POWER_ID) && !Objects.equals(p.ID, DexterityPower.POWER_ID) && !Objects.equals(p.ID, JudgmentWordPower.POWER_ID) && !Objects.equals(p.ID, Cemetery.POWER_ID) &&p.ID!=NaterranTree.POWER_ID)
                        addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, p.ID));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new JudgmentWordPower(AbstractDungeon.player)));
                setMove((byte)5, Intent.ATTACK_DEBUFF,(this.damage.get(2)).base,this.fireAmt,true);
                break;
            case 5:
                addToBot(new SFXAction("Vincent_A3"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[6]));
                for (int i = 0;i<this.fireAmt;i++) {
                    AbstractDungeon.actionManager.addToBottom( new DamageAction( AbstractDungeon.player, this.damage
                            .get(2), AbstractGameAction.AttackEffect.FIRE, true));
                    addToBot(new VFXAction(new ExplosionSmallEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F));
                }
                setMove((byte)6, Intent.DEFEND_BUFF);
                break;
            case 6:
                addToBot(new SFXAction("Vincent_E3"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[5]));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.blockAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strAmt), this.strAmt));
                if (!this.hasPower(TimeWarpPower.POWER_ID)){
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new TimeWarpPower(this)));
                }else {
                    for (AbstractPower power:this.powers){
                        if (power instanceof TimeWarpPower){
                            power.amount = 11;
                            power.flash();
                            power.updateDescription();
                        }
                    }
                }
                setMove((byte)7, Intent.STRONG_DEBUFF);
                break;
            case 7:
                addToBot(new SFXAction("Vincent_E1"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[7]));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FreezePower(AbstractDungeon.player)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player,this.debuffAmt,true),this.debuffAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new RapidFirePower(AbstractDungeon.player,this.rapidFireAmt,this)));
                setMove((byte)8, Intent.DEFEND_BUFF);
                break;
            case 8:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.blockAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strAmt), this.strAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new RapidFirePower(AbstractDungeon.player,this.rapidFireAmt,this)));
                setMove((byte)1, Intent.ATTACK_DEBUFF,(this.damage.get(1)).base);
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            setMove((byte)1, Intent.ATTACK_DEBUFF,(this.damage.get(1)).base);
            return;
        }
    }


    @Override
    public void die() {
        super.die();
        useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        onBossVictoryLogic();
    }

    @Override
    public void setAnimation(SpriterAnimation animation) {
        this.animation = animation;
    }

    @Override
    public SpriterAnimation getAnimation() {
        return (SpriterAnimation) this.animation;
    }
}
