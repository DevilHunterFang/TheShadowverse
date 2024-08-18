package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import shadowverse.powers.*;

public class Lelouch extends CustomMonster {
    public static final String ID = "shadowverse:Lelouch";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Lelouch");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int aDmg;

    private int bDmg;

    private int strAmt;

    private int guardAmt;

    private int checkmateAmt;

    private int blockAmt;

    private static final String GEASS = MOVES[0];

    private static final String PREP = MOVES[1];

    private static final String SHOOT = MOVES[2];

    private boolean firstTurn = true;

    private boolean shootCheck = false;



    public Lelouch() {
        super(NAME, ID, 270, 0.0F, -30F, 230.0F, 450.0F, null, 80.0F, -30.0F);
        this.animation = new SpriterAnimation("img/monsters/Lelouch/Lelouch.scml");
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(300, 300);
        } else {
            setHp(270, 270);
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.guardAmt = 20;
            this.blockAmt = 20;
            this.aDmg = 7;
            this.bDmg = 25;
            this.strAmt = 4;
            this.checkmateAmt = 30;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.guardAmt = 18;
            this.blockAmt = 18;
            this.aDmg = 7;
            this.bDmg = 22;
            this.strAmt = 3;
            this.checkmateAmt = 28;
        } else {
            this.guardAmt = 16;
            this.blockAmt = 16;
            this.aDmg = 6;
            this.bDmg = 20;
            this.strAmt = 3;
            this.checkmateAmt = 25;
        }
        this.damage.add(new DamageInfo(this, aDmg));
        this.damage.add(new DamageInfo(this, bDmg));
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new KMFPower(this,this.guardAmt)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CommanderPower(this)));
        AbstractMonster m = new Lancelot(-130 + -185.0F * 1, MathUtils.random(-10.0F, 20.0F));
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m, false));
        if (AbstractDungeon.ascensionLevel >= 19) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new ArtifactPower(m, 3)));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new ArtifactPower(m, 2)));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new BetterFlightPower(m, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new CurseOfGeass(m)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom( new SFXAction("Lelouch_GEASS"));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new GeassPower(AbstractDungeon.player)));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
                AbstractDungeon.actionManager.addToBottom(new ShakeScreenAction(0.3F, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.LOW));
                setMove(SHOOT,(byte)3, Intent.ATTACK,(this.damage.get(0)).base,5,true);
                shootCheck = true;
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[2], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LaserBeamEffect(this.hb.cX, this.hb.cY + 60.0F * Settings.scale), 0.5F));
                for (int i=0;i<5;i++){
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                shootCheck = false;
                break;
            case 4:
                addToBot(new VFXAction(new ExplosionSmallEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.FIRE));
                break;
            case 5:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3]));
                addToBot(new SFXAction("FatalOrder"));
                for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                    if (!m.isDead && !m.isDying && !m.isEscaping){
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, this.blockAmt));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.strAmt), this.strAmt));
                    }
                }
                break;
            case 6:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[4]));
                addToBot(new SFXAction("Lelouch_Checkmate"));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new CheckmatePower(AbstractDungeon.player,this.checkmateAmt,this)));
                break;
            default:
                System.out.println("ERROR: Default Take Turn was called on " + this.name);
                break;
        }
        if (!shootCheck){
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            setMove(GEASS,(byte)1, Intent.STRONG_DEBUFF);
            return;
        }
        if (i<30 && !lastMove((byte)3)) {
            setMove(PREP,(byte)2, Intent.UNKNOWN);
            return;
        }
        if (i<50&& !lastTwoMoves((byte)6) && !AbstractDungeon.player.hasPower(CheckmatePower.POWER_ID)){
            setMove((byte)6, Intent.STRONG_DEBUFF);
            return;
        }
        if (!lastMove((byte)5)){
            setMove((byte)5, Intent.DEFEND_BUFF);
        }else {
            setMove((byte)4, Intent.ATTACK, (this.damage.get(1)).base);
        }
    }


    @Override
    public void die() {
        super.die();
        useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
    }
}
