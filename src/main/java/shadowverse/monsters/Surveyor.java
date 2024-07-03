package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import shadowverse.powers.BetterExplosionPower;
import shadowverse.powers.BetterFlightPower;

public class Surveyor extends CustomMonster {
    public static final String ID = "shadowverse:Surveyor";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Surveyor");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;


    private int attackDmg;

    private int turnCount = 0;
    public Surveyor(float x, float y) {
        super(NAME, ID, 30, -5.0F, 120.0F, 145.0F, 200.0F, "img/monsters/Aialon/Surveyor.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(32, 36);
        } else {
            setHp(30);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDmg = 11;
        } else {
            this.attackDmg = 9;
        }
        this.damage.add(new DamageInfo(this, this.attackDmg));
    }


    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 2)));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
        }
        addToBot(new ApplyPowerAction(this,this,new BetterFlightPower(this,1),1));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                addToBot(new MakeTempCardInDrawPileAction(new Dazed(),1,true,true));
                addToBot(new MakeTempCardInDiscardAction(new Dazed(),1));
                break;
            case 2:
                addToBot(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 3:
                addToBot(new ApplyPowerAction(this,this,new ThornsPower(this,2+turnCount),2+turnCount));
                break;
            case 4:
                addToBot(new ApplyPowerAction(this,this,new BetterExplosionPower(this,30),30));
                break;
        }
        turnCount++;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (turnCount % 3 == 0 && !lastMove((byte)4) && AbstractDungeon.actionManager.turn != 1){
            setMove((byte)4, Intent.BUFF);
            return;
        }
        if (num < 20 && !lastMove((byte)2)) {
            setMove((byte)2, Intent.ATTACK, (this.damage.get(0)).base);
            return;
        }else if (num < 40 && !lastMove((byte)3)) {
            setMove((byte)3, Intent.BUFF);
        }
        setMove((byte)1, Intent.DEBUFF);
    }

}
