package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OOOGGGG extends CustomMonster {
    public static final String ID = "shadowverse:OOOGGGG";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:OOOGGGG");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private boolean firstMove = true;

    private boolean usedMegaDebuff = false;

    private int normalDebuffAmt;

    public OOOGGGG(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(44, 52), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/Monster/OOOGGGG.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(46, 55);
        } else {
            setHp(44, 52);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.damage.add(new DamageInfo(this, 20));
            this.damage.add(new DamageInfo(this, 5));
            this.damage.add(new DamageInfo(this, 15));
            this.damage.add(new DamageInfo(this, 9));
        } else {
            this.damage.add(new DamageInfo(this, 16));
            this.damage.add(new DamageInfo(this, 4));
            this.damage.add(new DamageInfo(this, 13));
            this.damage.add(new DamageInfo(this, 7));
        }
        this.normalDebuffAmt = 2;
    }


    @Override
    public void usePreBattleAction() {
        (AbstractDungeon.getCurrRoom()).cannotLose = true;
        AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(this, this, new RegrowPower(this)));
        AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(this, this, new ReactivePower(this)));
        AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(this, this, new MalleablePower(this)));
    }

    @Override
    public void takeTurn() {
        int i;
        switch (this.nextMove) {
            case 0:
                AbstractDungeon.actionManager.addToBottom( new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom( new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom( new AnimateSlowAttackAction(this));
                for (i = 0; i < 3; i++)
                    AbstractDungeon.actionManager.addToBottom( new DamageAction(AbstractDungeon.player, this.damage
                            .get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom( new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom( new GainBlockAction(this, this, (this.damage.get(2)).base));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom( new DamageAction(AbstractDungeon.player, this.damage
                        .get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.normalDebuffAmt, true), this.normalDebuffAmt));
                AbstractDungeon.actionManager.addToBottom( new AnimateFastAttackAction(this));
                break;
            case 4:
                this.usedMegaDebuff = true;
                AbstractDungeon.actionManager.addToBottom( new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom( new AddCardToDeckAction(
                        CardLibrary.getCard("Parasite").makeCopy()));
                break;
            case 5:
                if (MathUtils.randomBoolean()) {
                    AbstractDungeon.actionManager.addToBottom( new SFXAction("DARKLING_REGROW_2",
                            MathUtils.random(-0.1F, 0.1F)));
                } else {
                    AbstractDungeon.actionManager.addToBottom( new SFXAction("DARKLING_REGROW_1",
                            MathUtils.random(-0.1F, 0.1F)));
                }
                AbstractDungeon.actionManager.addToBottom( new HealAction(this, this, this.maxHealth / 2));
                AbstractDungeon.actionManager.addToBottom( new ChangeStateAction(this, "REVIVE"));
                AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(this, this, new RegrowPower(this), 1));
                AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(this, this, new ReactivePower(this)));
                AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(this, this, new MalleablePower(this)));
                for (AbstractRelic r : AbstractDungeon.player.relics)
                    r.onSpawnMonster(this);
                break;
            case 6:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, DIALOG[0]));
                break;
        }
        AbstractDungeon.actionManager.addToBottom( new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (this.halfDead) {
            setMove((byte)5, AbstractMonster.Intent.BUFF);
            return;
        }
        if (this.firstMove) {
            this.firstMove = false;
            if (num < 33) {
                setMove((byte) 1, Intent.ATTACK, (this.damage.get(1)).base, 3, true);
            } else if (num < 66) {
                setMove((byte) 2, Intent.DEFEND);
            } else {
                setMove((byte) 3, Intent.ATTACK_DEBUFF, (this.damage.get(3)).base);
            }
            return;
        }
        if (num < 10) {
            if (!lastMove((byte) 0)) {
                setMove((byte) 0, Intent.ATTACK, (this.damage.get(0)).base);
            } else {
                getMove(AbstractDungeon.aiRng.random(10, 99));
            }
        } else if (num < 20) {
            if (!this.usedMegaDebuff && !lastMove((byte) 4)) {
                setMove((byte) 4, Intent.STRONG_DEBUFF);
            } else if (AbstractDungeon.aiRng.randomBoolean(0.1F)) {
                setMove((byte) 0, Intent.ATTACK, (this.damage.get(0)).base);
            } else {
                getMove(AbstractDungeon.aiRng.random(20, 99));
            }
        } else if (num < 40) {
            if (!lastMove((byte) 3)) {
                setMove((byte) 3, Intent.ATTACK_DEBUFF, (this.damage.get(3)).base);
            } else if (AbstractDungeon.aiRng.randomBoolean(0.4F)) {
                getMove(AbstractDungeon.aiRng.random(19));
            } else {
                getMove(AbstractDungeon.aiRng.random(40, 99));
            }
        } else if (num < 70) {
            if (!lastMove((byte) 1)) {
                setMove((byte) 1, Intent.ATTACK, (this.damage.get(1)).base, 3, true);
            } else if (AbstractDungeon.aiRng.randomBoolean(0.3F)) {
                setMove((byte) 2, Intent.DEFEND);
            } else {
                getMove(AbstractDungeon.aiRng.random(39));
            }
        } else if (!lastMove((byte) 2)) {
            setMove((byte) 2, Intent.DEFEND);
        } else {
            getMove(AbstractDungeon.aiRng.random(69));
        }
        createIntent();
    }

    public void changeState(String key) {
        if (key.equals("REVIVE")) {
            this.halfDead = false;
        }
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;
            for (AbstractPower p : this.powers)
                p.onDeath();
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onMonsterDeath(this);
            this.powers.clear();
            boolean allDead = true;
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                if (m.id.equals(this.id) && !m.halfDead) {
                    allDead = false;
                    break;
                }
            }
            if (!allDead) {
                if (this.nextMove != 6) {
                    setMove((byte)6, AbstractMonster.Intent.UNKNOWN);
                    createIntent();
                    AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)6, AbstractMonster.Intent.UNKNOWN));
                }
            } else {
                (AbstractDungeon.getCurrRoom()).cannotLose = false;
                this.halfDead = false;
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters)
                    m.die();
            }
        }
    }

    public void die() {
        if (!(AbstractDungeon.getCurrRoom()).cannotLose)
            super.die();
    }
}
