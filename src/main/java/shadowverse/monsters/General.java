package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import shadowverse.powers.NexusPower;
import shadowverse.powers.ShadePower;
import shadowverse.powers.ShadowMarkPower;

public class General extends CustomMonster {
    public static final String ID = "shadowverse:General";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:General");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;


    private int attackDamage;
    private int debuffDamage;
    private int buffAmt;


    public General(float x, float y) {
        super(NAME, ID, 160, -5.0F, -20.0F, 145.0F, 500.0F, "img/monsters/Shadows/General.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(170);
        } else {
            setHp(150);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDamage = 7;
            this.debuffDamage = 20;
            this.buffAmt = 2;
        } else {
            this.attackDamage = 6;
            this.debuffDamage = 18;
            this.buffAmt = 1;
        }
        this.damage.add(new DamageInfo(this, this.attackDamage));
        this.damage.add(new DamageInfo(this, this.debuffDamage));
    }

    public General(){
        this(65.0F,-30.0F);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this,this,new ShadePower(this,5),5));
        if (!this.hasPower(MinionPower.POWER_ID)){
            addToBot(new ApplyPowerAction(this,this,new RegenerateMonsterPower(this,10),10));
        }
    }

    @Override
    public void takeTurn() {
        int numBlows, i;
        AbstractPlayer abstractPlayer = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                numBlows = 3;
                for (i = 0; i < numBlows; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(abstractPlayer, this.damage
                            .get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                }
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(abstractPlayer, this.damage
                        .get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL, true));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true), 2));

                break;
            case 3:
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if (!mo.isDeadOrEscaped()){
                        addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,buffAmt),buffAmt));
                    }
                }
                addToBot(new ApplyPowerAction(this,this,new BeatOfDeathPower(this,buffAmt),buffAmt));
                addToBot(new ApplyPowerAction(abstractPlayer,this,new ShadowMarkPower(abstractPlayer,2),2));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (AbstractDungeon.ascensionLevel >= 17) {
            if (num < 65) {
                if (lastTwoMoves((byte)1)) {
                    setMove((byte)3, Intent.STRONG_DEBUFF);
                } else {
                    setMove((byte)1, Intent.ATTACK, (this.damage.get(0)).base, 3, true);
                }
            } else if (lastMove((byte)3) || lastMoveBefore((byte)3)) {
                setMove((byte)2, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
            } else {
                setMove((byte)3, Intent.STRONG_DEBUFF);
            }
        } else if (num < 65) {
            if (lastTwoMoves((byte)1)) {
                setMove((byte)3, Intent.STRONG_DEBUFF);
            } else {
                setMove((byte)1, Intent.ATTACK, (this.damage.get(0)).base, 3, true);
            }
        } else if (lastMove((byte)3)) {
            setMove((byte)2, Intent.ATTACK, (this.damage.get(1)).base);
        } else {
            setMove((byte)3, Intent.STRONG_DEBUFF);
        }
    }

    @Override
    public void die() {
        super.die();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m.isDying || m.isDead) {
                continue;
            }
            if (m.hasPower(NexusPower.POWER_ID)) {
                m.getPower(NexusPower.POWER_ID).amount++;
                m.getPower(NexusPower.POWER_ID).updateDescription();
            }
        }
    }

}
