package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.GainBlockRandomMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Underling2 extends CustomMonster {
    public static final String ID = "shadowverse:Underling2";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Underling");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 48;

    public static final int HP_MAX = 52;

    public static final int A_2_HP_MIN = 50;

    public static final int A_2_HP_MAX = 54;

    private int shootDmg;
    private int shotAmt;
    private int blockAmt;
    private int heavyDmg;

    public Underling2(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(48, 52), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Underling/Underling2.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.shootDmg = 3;
            this.shotAmt = 6;
            this.blockAmt = 20;
            this.heavyDmg = 14;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.shootDmg = 3;
            this.shotAmt = 5;
            this.blockAmt = 15;
            this.heavyDmg = 12;
        } else {
            this.shootDmg = 2;
            this.shotAmt = 5;
            this.blockAmt = 15;
            this.heavyDmg = 12;
        }
        this.damage.add(new DamageInfo(this, this.shootDmg));
        this.damage.add(new DamageInfo(this,this.heavyDmg));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new AddTemporaryHPAction(this,this,7 * (1 + AbstractDungeon.player.gold/100)));
    }

    @Override
    public void takeTurn() {
        int i;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new GainBlockRandomMonsterAction(this, this.blockAmt));
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,1),1));
                break;
            case 3:
                for (i = 0; i < this.shotAmt; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (num >= 65 && !lastTwoMoves((byte)2) && !lastTwoMoves((byte)3)) {
            int i = 0;
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                if (!m.isDying && !m.isEscaping)
                    i++;
            }
            if (i > 1) {
                setMove((byte)2, Intent.DEFEND_BUFF);
                return;
            }
            setMove((byte)3, AbstractMonster.Intent.ATTACK, (this.damage.get(0)).base, this.shotAmt, true);
            return;
        }
        if (!lastTwoMoves((byte)1)) {
            setMove((byte)1, AbstractMonster.Intent.ATTACK, (this.damage.get(1)).base);
            return;
        }
        int aliveCount = 0;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (!m.isDying && !m.isEscaping)
                aliveCount++;
        }
        if (aliveCount > 1) {
            setMove((byte)2, AbstractMonster.Intent.DEFEND);
            return;
        }
        setMove((byte)3, AbstractMonster.Intent.ATTACK, (this.damage.get(0)).base, this.shotAmt, true);
    }

}
