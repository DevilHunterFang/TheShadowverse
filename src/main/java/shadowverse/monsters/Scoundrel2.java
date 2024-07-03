package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import shadowverse.powers.RapidFirePower;

public class Scoundrel2 extends CustomMonster {
    public static final String ID = "shadowverse:Scoundrel2";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Scoundrel");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 44;

    public static final int HP_MAX = 48;

    public static final int A_2_HP_MIN = 46;

    public static final int A_2_HP_MAX = 50;

    private int shootDmg;
    private int lootDmg;


    public Scoundrel2(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(48, 52), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Scoundrel/Scoundrel2.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.shootDmg = 11;
            this.lootDmg = 8;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.shootDmg = 10;
            this.lootDmg = 7;
        } else {
            this.shootDmg = 10;
            this.lootDmg = 6;
        }
        this.damage.add(new DamageInfo(this, this.lootDmg));
        this.damage.add(new DamageInfo(this, this.shootDmg));
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
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new DrawReductionPower(AbstractDungeon.player,1),1));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 4:
                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 12));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 6));
                }
                break;
            case 5:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmokeBombEffect(this.hb.cX, this.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)5, AbstractMonster.Intent.ESCAPE));
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
        } else if (num < 60) {
            if (!lastTwoMoves((byte)3)) {
                setMove((byte) 3, Intent.ATTACK, this.damage.get(1).base);
            }
        } else if (num < 75 && !lastMove((byte)2)) {
            setMove((byte)2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            if (AbstractDungeon.actionManager.turn < 4){
                setMove((byte)4, Intent.DEFEND, this.damage.get(0).base);
            }else {
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)5, AbstractMonster.Intent.ESCAPE));
            }
        }
    }

    @Override
    public void die() {
        super.die();
    }
}
