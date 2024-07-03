package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import shadowverse.powers.AimedPower;
import shadowverse.powers.RapidFirePower;
import shadowverse.powers.RapidPower;

public class Deputy extends CustomMonster {
    public static final String ID = "shadowverse:Deputy";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Deputy");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 48;

    public static final int HP_MAX = 52;

    public static final int A_2_HP_MIN = 50;

    public static final int A_2_HP_MAX = 54;

    private int shootDmg;
    private int reloadBlock;

    public Deputy(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(48, 52), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Deputy/Deputy1.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.shootDmg = 11;
            this.reloadBlock = 8;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.shootDmg = 10;
            this.reloadBlock = 7;
        } else {
            this.shootDmg = 10;
            this.reloadBlock = 6;
        }
        this.damage.add(new DamageInfo(this, this.shootDmg));
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
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new AimedPower(AbstractDungeon.player,reloadBlock),reloadBlock));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new RapidFirePower(AbstractDungeon.player,2,this)));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.reloadBlock));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (num < 40) {
            if (!lastMove((byte)1)) {
                setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
            }else {
                getMove(AbstractDungeon.aiRng.random(40, 99));
            }
        } else if (num < 65) {
            if (!lastMove((byte)3)) {
                setMove((byte) 3, Intent.DEFEND_DEBUFF);
            }else {
                setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
            }
        } else if (!lastMove((byte)2)) {
            setMove((byte)2, Intent.DEBUFF);
        } else {
            getMove(AbstractDungeon.aiRng.random(0, 65));
        }
        createIntent();
    }

}
