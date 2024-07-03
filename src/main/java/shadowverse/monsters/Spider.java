package shadowverse.monsters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.beyond.Spiker;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.EntangleEffect;

public class Spider extends CustomMonster {
    public static final String ID = "shadowverse:Spider";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Spider");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public AbstractMonster m;

    private int ritualAmount = 0;

    private boolean useEntagle;

    public Spider(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(99, 99), -5.0F, 0.0F, 180.0F, 380.0F, "img/monsters/Spider/Spider.png", x, y);
        int rnd = AbstractDungeon.aiRng.random(6);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.ritualAmount = 4;
        } else {
            this.ritualAmount = 3;
        }
        switch (rnd) {
            case 0:
                m = new Cultist(x, y);
                break;
            case 1:
                m = new SlaverBlue(x, y);
                break;
            case 2:
                m = new SlaverRed(x, y);
                break;
            case 3:
                m = new JawWorm(x, y);
                break;
            case 4:
                m = new Spiker(x, y);
                break;
            case 5:
                m = new Healer(x, y);
                break;
            case 6:
                m = new SpikeSlime_M(x, y);
                break;
        }
        this.setHp(m.maxHealth);
        this.name += m.name;
    }

    @Override
    public void update() {
        super.update();
        if (this.hb.hovered) {
            String atlass = "";
            String json = "";
            if (m instanceof Cultist) {
                atlass = "images/monsters/theBottom/cultist/skeleton.atlas";
                json = "images/monsters/theBottom/cultist/skeleton.json";
            } else if (m instanceof SlaverBlue) {
                atlass = "images/monsters/theBottom/blueSlaver/skeleton.atlas";
                json = "images/monsters/theBottom/blueSlaver/skeleton.json";
            } else if (m instanceof SlaverRed) {
                atlass = "images/monsters/theBottom/redSlaver/skeleton.atlas";
                json = "images/monsters/theBottom/redSlaver/skeleton.json";
            } else if (m instanceof JawWorm) {
                atlass = "images/monsters/theBottom/jawWorm/skeleton.atlas";
                json = "images/monsters/theBottom/jawWorm/skeleton.json";
            } else if (m instanceof Spiker) {
                atlass = "images/monsters/theForest/spiker/skeleton.atlas";
                json = "images/monsters/theForest/spiker/skeleton.json";
            } else if (m instanceof Healer) {
                atlass = "images/monsters/theCity/healer/skeleton.atlas";
                json = "images/monsters/theCity/healer/skeleton.json";
            } else if (m instanceof SpikeSlime_M) {
                atlass = "images/monsters/theBottom/slimeAltM/skeleton.atlas";
                json = "images/monsters/theBottom/slimeAltM/skeleton.json";
            }
            loadAnimation(atlass, json, 1.0F);
        } else {
            this.atlas = null;
            this.skeleton = null;
            this.stateData = null;
            this.state = null;
        }
    }

    @Override
    public void takeTurn() {
        if (nextMove == 9){
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new EntangleEffect(this.hb.cX - 70.0F * Settings.scale, this.hb.cY + 10.0F * Settings.scale, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new EntanglePower(AbstractDungeon.player)));
        }else {
            if (this.getIntentBaseDmg() > 0) {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.getIntentDmg()), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
            if (m instanceof Cultist) {
                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RitualPower(this, this.ritualAmount + 1, false)));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RitualPower(this, this.ritualAmount, false)));
            } else if (m instanceof SlaverBlue) {
                if (this.intent == Intent.ATTACK_DEBUFF) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, AbstractDungeon.ascensionLevel >= 17 ? 2 : 1, true), AbstractDungeon.ascensionLevel >= 17 ? 2 : 1));
                }
            } else if (m instanceof SlaverRed) {
                if (this.intent == Intent.ATTACK_DEBUFF) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, AbstractDungeon.ascensionLevel >= 17 ? 2 : 1, true), AbstractDungeon.ascensionLevel >= 17 ? 2 : 1));
                } else if (this.intent == Intent.STRONG_DEBUFF) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new EntangleEffect(this.hb.cX - 70.0F * Settings.scale, this.hb.cY + 10.0F * Settings.scale, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new EntanglePower(AbstractDungeon.player)));
                }
            } else if (m instanceof JawWorm) {
                if (this.intent == Intent.ATTACK_DEFEND) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 5));
                } else if (this.intent == Intent.DEFEND_BUFF) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, AbstractDungeon.ascensionLevel >= 17 ? 5 : 4), AbstractDungeon.ascensionLevel >= 17 ? 5 : 4));
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, AbstractDungeon.ascensionLevel >= 17 ? 9 : 6));
                }
            } else if (m instanceof Spiker) {
                if (this.intent == Intent.BUFF)
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, 2), 2));
            } else if (m instanceof Healer) {
                if (this.intent == Intent.ATTACK_DEBUFF) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2));
                } else if (m.nextMove == (byte) 2) {
                    for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                        if (!m.isDying && !m.isEscaping)
                            AbstractDungeon.actionManager.addToBottom(new HealAction(m, this, AbstractDungeon.ascensionLevel >= 17 ? 20 : 16));
                    }
                } else {
                    for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                        if (!m.isDying && !m.isEscaping)
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, AbstractDungeon.ascensionLevel >= 17 ? 4 : 3), AbstractDungeon.ascensionLevel >= 17 ? 4 : 3));
                    }
                }
            } else if (m instanceof SpikeSlime_M) {
                if (this.intent == Intent.ATTACK_DEBUFF) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), 1));
                }
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (num < 30 && !lastMove((byte)9) && !useEntagle) {
            useEntagle = true;
            setMove(MOVES[0], (byte) 9, Intent.STRONG_DEBUFF);
        }else {
            m.rollMove();
            EnemyMoveInfo moveInfo = ReflectionHacks.getPrivate(m, AbstractMonster.class, "move");
            setMove(moveInfo.nextMove, moveInfo.intent, moveInfo.baseDamage, moveInfo.multiplier, moveInfo.isMultiDamage);
        }
    }

}
