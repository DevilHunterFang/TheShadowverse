package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import shadowverse.powers.AimedPower;
import shadowverse.powers.RapidPower;

public class Deputy3 extends CustomMonster {
    public static final String ID = "shadowverse:Deputy3";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Deputy");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 70;

    public static final int HP_MAX = 76;

    public static final int A_2_HP_MIN = 74;

    public static final int A_2_HP_MAX = 80;

    private int shootDmg;
    private int doubleDmg;
    private int blockAmt;

    public Deputy3(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(70, 76), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Deputy/Deputy2.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.shootDmg = 13;
            this.doubleDmg = 7;
            this.blockAmt = 10;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.shootDmg = 12;
            this.doubleDmg = 6;
            this.blockAmt = 8;
        } else {
            this.shootDmg = 11;
            this.doubleDmg = 6;
            this.blockAmt = 8;
        }
        this.damage.add(new DamageInfo(this, this.shootDmg));
        this.damage.add(new DamageInfo(this, this.doubleDmg));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this,this,new RapidPower(this,8)));
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
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new AimedPower(AbstractDungeon.player,5),5));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player,3,true),3));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 4:
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,2),2));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                    addToBot(new GainBlockAction(mo,this.blockAmt));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (num <= 35 && !lastMove((byte)4) && !lastMove((byte)3)) {
            int i = 0;
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                if (!m.isDying && !m.isEscaping)
                    i++;
            }
            if (i > 1 && !lastMove((byte)4)) {
                setMove((byte)4, Intent.DEFEND_BUFF);
            }else if (!lastMove((byte)3)){
                setMove((byte)3, AbstractMonster.Intent.ATTACK, (this.damage.get(1)).base, 2, true);
            }else {
                getMove(AbstractDungeon.aiRng.random(35, 99));
            }
        }else if (num <= 70) {
            if (!lastMove((byte)1)){
                setMove((byte)1, AbstractMonster.Intent.ATTACK, (this.damage.get(0)).base);
            }else if (!lastMove((byte)3)){
                setMove((byte)3, AbstractMonster.Intent.ATTACK, (this.damage.get(1)).base, 2, true);
            }else {
                setMove((byte)2, Intent.STRONG_DEBUFF);
            }
        }
        if (!lastMove((byte)2)){
            setMove((byte)2, Intent.STRONG_DEBUFF);
        }else {
            getMove(AbstractDungeon.aiRng.random(0, 69));
        }
        createIntent();
    }
}
