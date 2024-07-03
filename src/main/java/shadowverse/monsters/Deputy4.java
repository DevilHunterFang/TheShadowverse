package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import shadowverse.powers.RapidFirePower;
import shadowverse.powers.RapidPower;

public class Deputy4 extends CustomMonster {
    public static final String ID = "shadowverse:Deputy4";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Deputy");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 48;

    public static final int HP_MAX = 56;

    public static final int A_2_HP_MIN = 50;

    public static final int A_2_HP_MAX = 58;

    private int shootDmg;
    private int dazeAmt;
    private int debuffAmt;

    public Deputy4(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(70, 76), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Deputy/Deputy4.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.shootDmg = 10;
            this.dazeAmt = 3;
            this.debuffAmt = 4;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.shootDmg = 8;
            this.dazeAmt = 2;
            this.debuffAmt = 3;
        } else {
            this.shootDmg = 7;
            this.dazeAmt = 2;
            this.debuffAmt = 3;
        }
        this.damage.add(new DamageInfo(this, this.shootDmg));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this,this,new RapidPower(this,6)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new RapidFirePower(AbstractDungeon.player,this.debuffAmt,this)));
                addToBot(new MakeTempCardInDiscardAction(new Dazed(),this.dazeAmt));
                break;
            case 3:
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,2),2));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                    addToBot(new GainBlockAction(mo,8));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (num < 35) {
            if (!lastMove((byte)1)) {
                setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
            }else {
                getMove(AbstractDungeon.aiRng.random(35, 99));
            }
        } else if (num < 70) {
            if (!lastMove((byte)2)) {
                setMove((byte) 2, Intent.STRONG_DEBUFF);
            }else {
                setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
            }
        } else if (!lastMove((byte)3)) {
            setMove((byte)3, Intent.DEFEND_BUFF);
        } else {
            setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        }
        createIntent();
    }
}
