package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import shadowverse.powers.RapidFirePower;

public class Underling extends CustomMonster {
    public static final String ID = "shadowverse:Underling";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Underling");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 60;

    public static final int HP_MAX = 64;

    public static final int A_2_HP_MIN = 64;

    public static final int A_2_HP_MAX = 68;

    private int shootDmg;
    private int shotAmt;
    private int aimAmt;

    public Underling(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(60, 64), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Underling/Underling.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.shootDmg = 5;
            this.shotAmt = 4;
            this.aimAmt = 3;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.shootDmg = 5;
            this.shotAmt = 3;
            this.aimAmt = 2;
        } else {
            this.shootDmg = 4;
            this.shotAmt = 3;
            this.aimAmt = 2;
        }
        this.damage.add(new DamageInfo(this, this.shootDmg));
    }

    public Underling() {
        this(-130.0F, -30.0F);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -(1 + AbstractDungeon.player.gold / 100)), -(1 + AbstractDungeon.player.gold / 100)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                for (int i = 0; i < shotAmt; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                setMove((byte) 2, Intent.BUFF);
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new RapidFirePower(AbstractDungeon.player, this.aimAmt, this), this.aimAmt));
                setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base, this.shotAmt, true);
                break;
        }
    }

    @Override
    protected void getMove(int num) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base, this.shotAmt, true);
    }

}