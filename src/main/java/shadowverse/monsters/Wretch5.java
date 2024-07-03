package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
public class Wretch5 extends CustomMonster {
    public static final String ID = "shadowverse:Wretch5";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Wretch5");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 74;

    public static final int HP_MAX = 80;

    public static final int A_2_HP_MIN = 80;

    public static final int A_2_HP_MAX = 86;

    private int fellDmg;
    private int debuffAmt;

    private int suckDmg;
    private int multDmg;

    public Wretch5(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(74, 80), -5.0F, -20.0F, 145.0F, 450.0F, "img/monsters/Wretch/Wretch5.png", x, y);
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
            this.fellDmg = 18;
            this.suckDmg = 10;
            this.multDmg = 7;
        } else {
            this.fellDmg = 15;
            this.suckDmg = 8;
            this.multDmg = 6;
        }
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this, this.fellDmg));
        this.damage.add(new DamageInfo(this, this.suckDmg));
        this.damage.add(new DamageInfo(this,this.multDmg));
    }

    public Wretch5() {
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
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX +

                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, AbstractDungeon.player.hb.cY +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.DARK_GRAY
                        .cpy()), 0.0F));
                AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.NONE));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new FrailPower(AbstractDungeon.player,this.debuffAmt,true),this.debuffAmt));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (AbstractDungeon.ascensionLevel >= 17) {
            if (num < 65) {
                if (!lastTwoMoves((byte)1)) {
                    setMove((byte)1, AbstractMonster.Intent.ATTACK, (this.damage.get(0)).base);
                }else {
                    setMove((byte)2, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
                }
            } else if (lastMove((byte)2) || lastMoveBefore((byte)2)) {
                setMove((byte)3, AbstractMonster.Intent.ATTACK, (this.damage.get(2)).base, 3, true);
            } else {
                setMove((byte)2, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
            }
        } else if (num < 65) {
            if (lastTwoMoves((byte)1)) {
                setMove((byte)1, AbstractMonster.Intent.ATTACK, (this.damage.get(0)).base);
            } else {
                setMove((byte)3, AbstractMonster.Intent.ATTACK, (this.damage.get(2)).base, 3, true);
            }
        } else if (lastMove((byte)2)) {
            setMove((byte)3, AbstractMonster.Intent.ATTACK, (this.damage.get(2)).base, 3, true);
        } else {
            setMove((byte)2, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
        }
    }

}
