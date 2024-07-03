package shadowverse.monsters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.beyond.Maw;
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker;
import com.megacrit.cardcrawl.monsters.beyond.SpireGrowth;
import com.megacrit.cardcrawl.monsters.city.Chosen;
import com.megacrit.cardcrawl.monsters.city.SnakePlant;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class Spider2 extends CustomMonster {
    public static final String ID = "shadowverse:Spider2";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Spider");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public AbstractMonster m;
    private int strAmt2 = 0;

    private int multAmt = 0;


    public Spider2(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(99, 99), -5.0F, 0.0F, 180.0F, 460.0F, "img/monsters/Spider/Spider2.png", x, y);
        int rnd = AbstractDungeon.aiRng.random(4);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.strAmt2 = 5;
        } else {
            this.strAmt2 = 3;
        }
        switch (rnd) {
            case 0:
                m = new OrbWalker(x, y);
                this.setHp(m.maxHealth);
                break;
            case 1:
                m = new Maw(x, y);
                this.setHp((int) (m.maxHealth * 0.75F));
                break;
            case 2:
                m = new SpireGrowth();
                this.setHp((int) (m.maxHealth * 0.75F));
                break;
            case 3:
                m = new SnakePlant(x, y);
                this.setHp((int) (m.maxHealth * 1.25F));
                break;
            case 4:
                m = new Chosen(x, y);
                this.setHp((int) (m.maxHealth * 1.5F));
                break;
        }
        this.name += m.name;
    }

    public Spider2(){
        this(0.0F,-30.0F);
    }

    @Override
    public void usePreBattleAction() {
        if (m instanceof OrbWalker) {
            if (AbstractDungeon.ascensionLevel >= 17) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GenericStrengthUpPower(this, MOVES[1], 5)));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GenericStrengthUpPower(this, MOVES[1], 3)));
            }
        } else if (m instanceof SnakePlant) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MalleablePower(this)));
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
        } else if (m instanceof Chosen) {
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.strAmt2), this.strAmt2));
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.hb.hovered) {
            String atlass = "";
            String json = "";
            if (m instanceof OrbWalker) {
                atlass = "images/monsters/theForest/orbWalker/skeleton.atlas";
                json = "images/monsters/theForest/orbWalker/skeleton.json";
            } else if (m instanceof Maw) {
                atlass = "images/monsters/theForest/maw/skeleton.atlas";
                json = "images/monsters/theForest/maw/skeleton.json";
            } else if (m instanceof SpireGrowth) {
                atlass = "images/monsters/theForest/spireGrowth/skeleton.atlas";
                json = "images/monsters/theForest/spireGrowth/skeleton.json";
            } else if (m instanceof SnakePlant) {
                atlass = "images/monsters/theCity/snakePlant/skeleton.atlas";
                json = "images/monsters/theCity/snakePlant/skeleton.json";
            } else if (m instanceof Chosen) {
                atlass = "images/monsters/theCity/chosen/skeleton.atlas";
                json = "images/monsters/theCity/chosen/skeleton.json";
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
        if (this.getIntentBaseDmg() > 0) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX +

                    MathUtils.random(-50.0F, 50.0F) * Settings.scale, AbstractDungeon.player.hb.cY +
                    MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.CHARTREUSE
                    .cpy()), 0.2F));
            addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.getIntentDmg()), AbstractGameAction.AttackEffect.NONE));
            if (multAmt > 1){
                for (int i = 0; i < multAmt - 1; i++){
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX +

                            MathUtils.random(-50.0F, 50.0F) * Settings.scale, AbstractDungeon.player.hb.cY +
                            MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.CHARTREUSE
                            .cpy()), 0.2F));
                    addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.getIntentDmg()), AbstractGameAction.AttackEffect.NONE));
                }
            }
        }
        if (m instanceof OrbWalker) {
            if (this.intent == Intent.ATTACK_DEBUFF){
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAndDeckAction(new Burn()));
            }
        }
        if (m instanceof Maw) {
            if (this.intent == Intent.STRONG_DEBUFF){
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MAW_DEATH", 0.1F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, AbstractDungeon.ascensionLevel >= 17 ? 5 : 3, true), AbstractDungeon.ascensionLevel >= 17 ? 5 : 3));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, AbstractDungeon.ascensionLevel >= 17 ? 5 : 3, true), AbstractDungeon.ascensionLevel >= 17 ? 5 : 3));
                ReflectionHacks.setPrivate(this.m,Maw.class,"roared",true);
            }else if (this.intent == Intent.BUFF){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, AbstractDungeon.ascensionLevel >= 17 ? 5 : 3), AbstractDungeon.ascensionLevel >= 17 ? 5 : 3));
            }
        }
        if (m instanceof SpireGrowth){
            if (this.intent == Intent.STRONG_DEBUFF){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ConstrictedPower(AbstractDungeon.player, this, AbstractDungeon.ascensionLevel >= 17 ? 12 : 10)));
            }
        }
        if (m instanceof SnakePlant){
            if (this.intent == Intent.STRONG_DEBUFF){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
            }
        }
        if (m instanceof Chosen){
            if (this.intent == Intent.STRONG_DEBUFF){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new HexPower(AbstractDungeon.player, 1)));
            }else if (this.intent == Intent.ATTACK_DEBUFF){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true), 2));
            }else if (this.intent == Intent.DEBUFF){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 3, true), 3));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 3), 3));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        m.rollMove();
        EnemyMoveInfo moveInfo = ReflectionHacks.getPrivate(m, AbstractMonster.class, "move");
        setMove(moveInfo.nextMove, moveInfo.intent, moveInfo.baseDamage, moveInfo.multiplier, moveInfo.isMultiDamage);
        this.multAmt = moveInfo.multiplier;
    }

}
