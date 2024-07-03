package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class Wretch extends CustomMonster {
    public static final String ID = "shadowverse:WretchMonster";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:WretchMonster");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int HP_MIN = 68;

    public static final int HP_MAX = 72;

    public static final int A_2_HP_MIN = 75;

    public static final int A_2_HP_MAX = 79;

    private int fellDmg;

    private int doubleStrikeDmg;

    private int suckDmg;

    private boolean firstMove = true;

    public Wretch(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(75, 78), -5.0F, -20.0F, 145.0F, 330.0F, null, x, y);
        this.animation = new SpriterAnimation("img/monsters/Wretch/Wretch.scml");
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(HP_MIN, HP_MAX);
        } else {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.doubleStrikeDmg = 7;
            this.fellDmg = 18;
            this.suckDmg = 13;
        } else {
            this.doubleStrikeDmg = 6;
            this.fellDmg = 16;
            this.suckDmg = 11;
        }
        this.damage.add(new DamageInfo(this, this.doubleStrikeDmg));
        this.damage.add(new DamageInfo(this, this.fellDmg));
        this.damage.add(new DamageInfo(this, this.suckDmg));
    }

    public Wretch() {
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
                        .get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,this,new VulnerablePower(AbstractDungeon.player,2,true),2));
                break;
            case 2:
                for (i = 0; i < 2; i++) {
                    AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), 1));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX +

                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, AbstractDungeon.player.hb.cY +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.DARK_GRAY
                        .cpy()), 0.0F));
                AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(AbstractDungeon.player, this.damage
                        .get(2), AbstractGameAction.AttackEffect.NONE));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (this.firstMove) {
            this.firstMove = false;
            if (AbstractDungeon.ascensionLevel >= 17) {
                setMove((byte)1, AbstractMonster.Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
                return;
            }
            if (AbstractDungeon.aiRng.randomBoolean()) {
                setMove((byte)2, Intent.ATTACK_DEBUFF, (this.damage.get(0)).base, 2, true);
            } else {
                setMove((byte)3, AbstractMonster.Intent.ATTACK_BUFF, (this.damage.get(2)).base);
            }
            return;
        }
        if (num < 20) {
            if (!lastMove((byte)1)) {
                setMove((byte)1, AbstractMonster.Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
            } else {
                getMove(AbstractDungeon.aiRng.random(20, 99));
            }
        } else if (num < 60) {
            if (!lastTwoMoves((byte)2)) {
                setMove((byte)2, Intent.ATTACK_DEBUFF, (this.damage.get(0)).base, 2, true);
            } else {
                setMove((byte)3, AbstractMonster.Intent.ATTACK_BUFF, (this.damage.get(2)).base);
            }
        } else if (!lastTwoMoves((byte)3)) {
            setMove((byte)3, AbstractMonster.Intent.ATTACK_BUFF, (this.damage.get(2)).base);
        } else {
            setMove((byte)2, Intent.ATTACK_DEBUFF, (this.damage.get(0)).base, 2, true);
        }
    }

}
