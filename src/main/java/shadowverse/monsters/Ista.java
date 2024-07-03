package shadowverse.monsters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.ApplyStasisAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.HexPower;
import com.megacrit.cardcrawl.powers.StasisPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

public class Ista extends CustomMonster {
    public static final String ID = "shadowverse:Ista";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Ista");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 50;

    public static final int HP_MAX = 55;

    public static final int A_2_HP_MIN = 55;

    public static final int A_2_HP_MAX = 60;

    private int shootDmg;
    private int slashCount = 0;
    private int dazeAmt;
    private static final String START = MOVES[0];

    private boolean firstMove = true;

    public Ista(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(50, 55), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Underling/Ista.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.shootDmg = 13;
            this.dazeAmt = 3;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.shootDmg = 12;
            this.dazeAmt = 2;
        } else {
            this.shootDmg = 11;
            this.dazeAmt = 2;
        }
        this.damage.add(new DamageInfo(this, this.shootDmg));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0)));
                this.slashCount++;
                break;
            case 4:
                this.slashCount++;
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new HexPower(AbstractDungeon.player,1)));
                break;
            case 2:
                this.slashCount++;
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new VulnerablePower(AbstractDungeon.player,2,true),2));
                addToBot(new MakeTempCardInDiscardAction(new Dazed(),this.dazeAmt));
            case 3:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmokeBombEffect(this.hb.cX, this.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)3, Intent.ESCAPE));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (this.firstMove) {
            this.firstMove = false;
            setMove( START,(byte)4, Intent.STRONG_DEBUFF);
            return;
        }
        if (slashCount > 4){
            setMove((byte)3, Intent.ESCAPE);
            return;
        }
        if (num < 50 && !lastMove((byte)2)){
            setMove( START,(byte)2, Intent.DEBUFF);
            return;
        }
        setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
    }

    @Override
    public void die() {
        super.die();
    }
}
