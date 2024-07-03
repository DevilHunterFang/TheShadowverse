package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class Wretch3 extends CustomMonster {
    public static final String ID = "shadowverse:Wretch3";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Wretch2");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 44;

    public static final int HP_MAX = 50;

    public static final int A_2_HP_MIN = 50;

    public static final int A_2_HP_MAX = 56;

    private int fellDmg;

    private int suckDmg;
    private int debuffAmt;

    private boolean firstMove = true;

    public Wretch3(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(40, 46), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Wretch/Wretch2.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.debuffAmt = 2;
        }else {
            this.debuffAmt = 1;
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.fellDmg = 14;
            this.suckDmg = 9;
        } else {
            this.fellDmg = 13;
            this.suckDmg = 8;
        }
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this, this.fellDmg));
        this.damage.add(new DamageInfo(this, this.suckDmg));
    }

    public Wretch3() {
        this(-20.0F, 0.0F);
    }

    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MalleablePower(this)));
    }

    public void takeTurn() {
        int i;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 2:
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new VulnerablePower(AbstractDungeon.player,this.debuffAmt,true),this.debuffAmt));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), 1));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX +

                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, AbstractDungeon.player.hb.cY +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.DARK_GRAY
                        .cpy()), 0.0F));
                AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), 1));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (this.firstMove) {
            this.firstMove = false;
            if (AbstractDungeon.ascensionLevel >= 17) {
                setMove((byte)1, Intent.ATTACK, (this.damage.get(0)).base);
                return;
            }
            if (AbstractDungeon.aiRng.randomBoolean()) {
                setMove((byte)2, Intent.DEBUFF);
            } else {
                setMove((byte)3, Intent.ATTACK_BUFF, (this.damage.get(1)).base);
            }
            return;
        }
        if (num < 20) {
            if (!lastMove((byte)2)) {
                setMove((byte)2, Intent.DEBUFF);
            } else {
                getMove(AbstractDungeon.aiRng.random(20, 99));
            }
        } else if (num < 60) {
            if (!lastTwoMoves((byte)1)) {
                setMove((byte)1, Intent.ATTACK, (this.damage.get(0)).base);
            } else {
                setMove((byte)3, Intent.ATTACK_BUFF, (this.damage.get(1)).base);
            }
        } else if (!lastTwoMoves((byte)3)) {
            setMove((byte)3, Intent.ATTACK_BUFF, (this.damage.get(1)).base);
        } else {
            setMove((byte)1, Intent.ATTACK, (this.damage.get(0)).base);
        }
    }
}
