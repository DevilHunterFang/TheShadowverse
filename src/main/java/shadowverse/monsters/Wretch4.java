package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class Wretch4 extends CustomMonster {
    public static final String ID = "shadowverse:Wretch4";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Wretch");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 48;

    public static final int HP_MAX = 54;

    public static final int A_2_HP_MIN = 51;

    public static final int A_2_HP_MAX = 57;

    private int fellDmg;
    private int debuffAmt;
    private float posX;
    private float posY;
    private boolean firstMove = true;

    public Wretch4(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(40, 46), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Wretch/Wretch4.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.debuffAmt = 3;
        }else {
            this.debuffAmt = 2;
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.fellDmg = 7;
        } else {
            this.fellDmg = 5;
        }
        posX = x;
        posY = y;
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this, this.fellDmg));
    }

    public Wretch4() {
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
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), 1));
                break;
            case 2:
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new StrengthPower(AbstractDungeon.player,-1),-1));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), this.debuffAmt));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
                AbstractDungeon.actionManager.addToTop(new VFXAction(this, new InflameEffect(this), 0.2F));
                AbstractDungeon.actionManager.addToBottom(new HideHealthBarAction(this));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0F));
                AbstractMonster turned = new Wretch5(this.posX,this.posY);
                addToBot(new ApplyPowerAction(turned,null,new MalleablePower(turned)));
                addToBot(new SpawnMonsterAction(turned,false));
                addToBot(new CanLoseAction());
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
                setMove((byte)1, Intent.ATTACK, (this.damage.get(0)).base);
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
                setMove((byte)2, Intent.DEBUFF);
            }
        } else if (!lastTwoMoves((byte)3)) {
            setMove(MOVES[0], (byte)3, Intent.UNKNOWN);
        } else {
            setMove((byte)1, Intent.ATTACK, (this.damage.get(0)).base);
        }
    }

}
