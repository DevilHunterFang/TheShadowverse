package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import shadowverse.cards.Neutral.Temp.NaterranGreatTree;
import shadowverse.powers.BetterExplosionPower;

import java.util.ArrayList;

public class Zecilwenshe extends CustomMonster {
    public static final String ID = "shadowverse:Zecilwenshe";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Zecilwenshe");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public int daggerDmg;

    public int strikeDmg;

    public int buffAmount;

    public int blockAmt;

    private int turnCount = 0;

    private static final String DAGGER_NAME = MOVES[0];

    private int deathCount = 0;

    private ArrayList<AbstractGameAction> actions(AbstractPlayer p, AbstractMonster m, int amt) {
        ArrayList<AbstractGameAction> list = new ArrayList<>();
        list.add(new ApplyPowerAction(m, this, new CuriosityPower(m, amt), amt));
        list.add(new ApplyPowerAction(m, this, new AngerPower(m, amt), amt));
        list.add(new ApplyPowerAction(p, this, new WeakPower(p, amt, true)));
        list.add(new ApplyPowerAction(p, this, new FrailPower(p, amt, true)));
        list.add(new MakeTempCardInDiscardAction(new Wound(), 3));
        list.add(new ApplyPowerAction(this, this, new BetterExplosionPower(this, 12)));
        return list;
    }

    public Zecilwenshe() {
        this(80.0F, -50.0F);
    }

    public Zecilwenshe(float x, float y) {
        super(NAME, ID, 100, -0.0F, -20.0F, 205.0F, 540.0F, "img/monsters/Zecilwenshe/images/class_2508_i_60_000.png", x, y);
        if (Settings.MAX_FPS > 30) {
            this.animation = new SpriterAnimation("img/monsters/Zecilwenshe/images/Zecilwenshe.scml");
        }
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(120);
        } else {
            setHp(100);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.daggerDmg = 7;
            this.strikeDmg = 22;
            this.buffAmount = 2;
            this.blockAmt = 20;
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            this.daggerDmg = 7;
            this.strikeDmg = 22;
            this.buffAmount = 1;
            this.blockAmt = 18;
        } else {
            this.strikeDmg = 6;
            this.daggerDmg = 20;
            this.buffAmount = 1;
            this.blockAmt = 15;
        }
        this.damage.add(new DamageInfo(this, this.daggerDmg));
        this.damage.add(new DamageInfo(this, this.strikeDmg));
    }


    @Override
    public void usePreBattleAction() {
        (AbstractDungeon.getCurrRoom()).cannotLose = true;
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                for (int i = 0; i < 3; i++) {
                    addToBot(new VFXAction(new DaggerSprayEffect(true), 0.0F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(0), AbstractGameAction.AttackEffect.NONE));
                }
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
                addToBot(new SFXAction("Zecilwenshe_A"));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 3:
                int powerAmt = 0;
                int atkAmt = 0;
                int defAmt = 0;
                int skillAmt = 0;
                for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                    if (card.type == AbstractCard.CardType.POWER)
                        powerAmt++;
                    if (Loader.isModLoaded("shadowverse")) {
                        if (card.cardsToPreview instanceof NaterranGreatTree)
                            powerAmt++;
                    }
                    if (card.type == AbstractCard.CardType.ATTACK) {
                        if (card.baseBlock > 0) {
                            defAmt++;
                        }
                        if (card.baseDamage > 0) {
                            atkAmt++;
                        }
                    }
                    if (card.type == AbstractCard.CardType.SKILL)
                        skillAmt++;
                }
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
                addToBot(new SFXAction("Zecilwenshe_R2"));
                if (powerAmt > 1) {
                    for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (!m.isDeadOrEscaped() && !m.halfDead)
                            addToBot(actions(AbstractDungeon.player, m, this.buffAmount).get(0));
                    }
                } else if (skillAmt > 3) {
                    for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (!m.isDeadOrEscaped() && !m.halfDead)
                            addToBot(actions(AbstractDungeon.player, m, this.buffAmount).get(1));
                    }
                } else if (atkAmt > 3) {
                    addToBot(actions(AbstractDungeon.player, this, this.buffAmount).get(2));
                } else if (defAmt > 3) {
                    addToBot(actions(AbstractDungeon.player, this, this.buffAmount).get(3));
                } else {
                    int rnd = AbstractDungeon.cardRandomRng.random(4, 5);
                    addToBot(actions(AbstractDungeon.player, this, this.buffAmount).get(rnd));
                }
                break;
            case 4:
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDeadOrEscaped() && !m.halfDead) {
                        addToBot(new GainBlockAction(m, this.blockAmt));
                        addToBot(new RemoveDebuffsAction(m));
                    }
                }
                break;
            case 5:
                if (this.halfDead) {
                    this.halfDead = false;
                    int rnd = AbstractDungeon.aiRng.random(1);
                    if (rnd == 0) {
                        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2]));
                        addToBot(new SFXAction("Zecilwenshe_R"));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3]));
                        addToBot(new SFXAction("Zecilwenshe_R3"));
                    }
                    AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
                    if (AbstractDungeon.getCurrRoom().monsters.monsters.stream().anyMatch(abstractMonster -> abstractMonster instanceof Iceschillendrig))
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this,2),2));
                    for (AbstractRelic r : AbstractDungeon.player.relics)
                        r.onSpawnMonster(this);
                }
                break;
        }
        turnCount++;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (turnCount % 3 == 0 && !lastMove((byte) 3)) {
            setMove((byte) 3, Intent.UNKNOWN);
            return;
        }
        if (num < 60 && !lastMove((byte) 1)) {
            setMove(DAGGER_NAME, (byte) 1, Intent.ATTACK, (this.damage.get(0)).base, 3, true);
            return;
        }
        if (!lastMove((byte) 2) && !lastMove((byte) 4)) {
            int rnd = AbstractDungeon.aiRng.random(100);
            if (rnd <= 70) {
                setMove((byte) 2, Intent.ATTACK, (this.damage.get(1)).base);
            } else {
                setMove((byte) 4, Intent.DEFEND_BUFF);
            }
            return;
        }
        setMove((byte) 1, Intent.ATTACK, (this.damage.get(0)).base, 3, true);
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;
            for (AbstractPower p : this.powers)
                p.onDeath();
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onMonsterDeath(this);
            powers.clear();
            deathCount++;
            int life = 2;
            if (AbstractDungeon.getCurrRoom().monsters.monsters.stream().anyMatch(abstractMonster -> abstractMonster instanceof Iceschillendrig))
                life = 4;
            if (deathCount > life) {
                (AbstractDungeon.getCurrRoom()).cannotLose = false;
                this.halfDead = false;
                this.die();
                return;
            }
            setMove((byte) 5, Intent.UNKNOWN);
            createIntent();
            AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte) 5, Intent.UNKNOWN));
            applyPowers();
        }
    }


    public void die() {
        if (!(AbstractDungeon.getCurrRoom()).cannotLose)
            super.die();
    }

}
