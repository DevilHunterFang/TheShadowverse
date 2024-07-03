package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;

import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import shadowverse.powers.BetterFlightPower;

public class Tisiphone extends CustomMonster {
    public static final String ID = "shadowverse:Tisiphone";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Tisiphone");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int slashDmg;

    private int debuffAmt;

    private int cleaveDmg;

    private int finishDmg;

    private boolean firstTurn = true;

    private static final String SLASH = MOVES[0];

    private static final String RECHARGE = MOVES[1];

    public Tisiphone() {
        super(NAME, ID, 200, 0.0F, -40.0F, 350.0F, 470.0F, null, -70.0F, 25.0F);
        this.animation = new SpriterAnimation("img/monsters/Tisiphone/Tisiphone.scml");
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(220, 220);
        } else {
            setHp(200, 200);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.debuffAmt = 3;
        } else {
            this.debuffAmt = 2;
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.slashDmg = 10;
            this.cleaveDmg = 21;
            this.finishDmg = 60;
        } else {
            this.slashDmg = 8;
            this.cleaveDmg = 18;
            this.finishDmg = 54;
        }
        this.damage.add(new DamageInfo(this, cleaveDmg));
        this.damage.add(new DamageInfo(this, slashDmg));
        this.damage.add(new DamageInfo(this, finishDmg));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PainfulStabsPower(this)));
        if (AbstractDungeon.ascensionLevel >= 18) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 3), 3));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BetterFlightPower(this, 3), 3));
        }else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 2), 2));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BetterFlightPower(this, 2), 2));
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BufferPower(this, 1), 1));
                AbstractDungeon.actionManager.addToBottom( new SFXAction("Tisiphone_SLASH"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_HEAVY"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new CleaveEffect(), 0.1F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, debuffAmt, true), debuffAmt));
                setMove(SLASH, (byte)2, Intent.ATTACK_DEBUFF,(this.damage.get(1)).base,2,true);
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[1], 1.0F, 2.0F));
                for (int i=0;i<2;i++){
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(1), AbstractGameAction.AttackEffect.NONE));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BufferPower(this, 1), 1));
                addToBot(new VFXAction(new ClashEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -debuffAmt), -debuffAmt));
                setMove(SLASH, (byte)3, Intent.ATTACK_DEBUFF,(this.damage.get(1)).base,3,true);
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[2], 1.0F, 2.0F));
                for (int i=0;i<3;i++){
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BufferPower(this, 1), 1));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -debuffAmt), -debuffAmt));
                setMove(SLASH, (byte)4, Intent.ATTACK_DEBUFF,(this.damage.get(1)).base,4,true);
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[3], 1.0F, 2.0F));
                for (int i=0;i<4;i++){
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BufferPower(this, 1), 1));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction((AbstractCard)new Dazed(), this.debuffAmt));
                setMove(SLASH, (byte)5, Intent.ATTACK_DEBUFF,(this.damage.get(1)).base,5,true);
                break;
            case 5:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[4], 1.0F, 2.0F));
                for (int i=0;i<5;i++){
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BufferPower(this, 1), 1));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction((AbstractCard)new Pain()));
                setMove(SLASH, (byte)6, Intent.ATTACK,(this.damage.get(2)).base);
                break;
            case 6:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[5], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ViceCrushEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BufferPower(this, 1), 1));
                setMove(RECHARGE, (byte)7, Intent.UNKNOWN);
                break;
            case 7:
                AbstractDungeon.actionManager.addToBottom(new ShakeScreenAction(0.3F, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.LOW));
                setMove(SLASH, (byte)1, Intent.ATTACK_DEBUFF,(this.damage.get(0)).base);
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            setMove(SLASH, (byte)1, Intent.ATTACK_DEBUFF,(this.damage.get(0)).base);
            return;
        }
    }

    private void playDeathSfx() {
        int roll = MathUtils.random(1);
        if (roll == 0) {
            CardCrawlGame.sound.play("Tisiphone_D1");
        } else {
            CardCrawlGame.sound.play("Tisiphone_D2");
        }
    }

    @Override
    public void die() {
        super.die();
        playDeathSfx();
    }
}
