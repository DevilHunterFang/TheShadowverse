package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import shadowverse.cards.Neutral.Temp.Horse;
import shadowverse.powers.RapidFirePower;

public class Henchman extends CustomMonster {
    public static final String ID = "shadowverse:Henchman";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Henchman");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 22;

    public static final int HP_MAX = 26;

    public static final int A_2_HP_MIN = 26;

    public static final int A_2_HP_MAX = 30;

    private int shootDmg;
    private int reloadBlock;
    private static final String START = MOVES[0];

    public Henchman(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(22, 26), -5.0F, -20.0F, 145.0F, 400.0F, null, x, y);
        this.animation = new SpriterAnimation("img/monsters/Henchman/Henchman.scml");
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.shootDmg = 8;
            this.reloadBlock = 8;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.shootDmg = 7;
            this.reloadBlock = 6;
        } else {
            this.shootDmg = 6;
            this.reloadBlock = 6;
        }
        this.damage.add(new DamageInfo(this, this.shootDmg));
    }

    @Override
    public void usePreBattleAction() {
        if (!this.hasPower(MinionPower.POWER_ID)){
            addToBot(new ApplyPowerAction(this,this,new PlatedArmorPower(this,this.reloadBlock),this.reloadBlock));
            addToBot(new GainBlockAction(this,this.reloadBlock));
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new RapidFirePower(AbstractDungeon.player,2,this)));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2,true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 2), 2));
                if (AbstractDungeon.player.discardPile.size() > 0) {
                    AbstractDungeon.actionManager.addToBottom(new EmptyDeckShuffleAction());
                    AbstractDungeon.actionManager.addToBottom(new ShuffleAction(AbstractDungeon.player.drawPile, false));
                }
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.reloadBlock));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (num < 30) {
            if (!lastMove((byte)1)) {
                setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
            }
        } else if (num < 65) {
            if (!lastTwoMoves((byte)3)) {
                setMove((byte) 3, Intent.ATTACK_DEFEND, this.damage.get(0).base);
            }else {
                setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
            }
        } else if (!lastMove((byte)2)) {
            setMove(START, (byte)2, Intent.DEBUFF);
        } else {
            setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        }
    }

    public void getMove() {
        getMove(99);
    }

    @Override
    public void die() {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Horse()));
        super.die();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m.isDying || m.isDead) {
                continue;
            }
            if (m.hasPower("shadowverse:NahtPower")) {
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(m));
            }
        }
    }
}
