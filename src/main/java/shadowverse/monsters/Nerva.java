package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.StarBounceEffect;
import com.megacrit.cardcrawl.vfx.combat.*;
import shadowverse.action.ChangeSpriteAction;
import shadowverse.cards.temp.*;
import shadowverse.powers.*;
import shadowverseCharbosses.powers.cardpowers.SionPower;

import java.util.*;

public class Nerva extends CustomMonster implements SpriteCreature {
    public static final String ID = "shadowverse:Nerva";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Nerva");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int heavyDmg;

    private int bloodHitCount;

    private int metalAmt;
    private int regenAmt;
    private int strAmount;
    private int sionAmt;

    public float spawnX = -100.0F;
    private boolean isFirstMove = true;
    private int turnCount;
    private boolean firstTimeHP;
    private boolean firstEnd;

    public HashMap<Integer, AbstractMonster> enemySlots = new HashMap<>();

    public static ArrayList<AbstractCard> returnChoice() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new Isabelle_Story());
        list.add(new Erika_Story());
        list.add(new Rowen_Story());
        list.add(new Iris_Story());
        list.add(new Luna_Story());
        list.add(new Urias_Story());
        list.add(new Yuwan_Story());
        list.add(new Arisa_Story());
        list.add(new Elena_Story());
        return list;
    }


    public SpriterAnimation extra;

    @Override
    public void setAnimation(SpriterAnimation animation) {
        this.animation = animation;
    }

    @Override
    public SpriterAnimation getAnimation() {
        return (SpriterAnimation) this.animation;
    }

    public Nerva() {
        super(NAME, ID, 1000, -50.0F, -30F, 340.0F, 500.0F, "img/monsters/Nerva/class_500061-idle_000.png", 180.0F, 0.0F);
        if (Settings.MAX_FPS > 30){
            this.animation = new SpriterAnimation("img/monsters/Nerva/Nerva.scml");
            extra = new SpriterAnimation("img/monsters/Nerva/extra/extra.scml");
        }
        this.dialogX = -100.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(1200);
        } else {
            setHp(1000);
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.heavyDmg = 40;
            this.strAmount = 2;
            this.metalAmt = 15;
            this.regenAmt = 10;
            this.sionAmt = 200;
            this.bloodHitCount = 12;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.heavyDmg = 40;
            this.strAmount = 1;
            this.metalAmt = 10;
            this.regenAmt = 7;
            this.sionAmt = 300;
            this.bloodHitCount = 12;
        } else {
            this.heavyDmg = 35;
            this.strAmount = 1;
            this.metalAmt = 10;
            this.regenAmt = 7;
            this.sionAmt = 300;
            this.bloodHitCount = 10;
        }
        this.damage.add(new DamageInfo(this, this.heavyDmg));
        this.damage.add(new DamageInfo(this, 2));
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("Final");
        (AbstractDungeon.getCurrRoom()).cannotLose = true;
        addToBot(new SFXAction("Nerva_Start"));
        addToBot(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
        if (Settings.MAX_FPS > 30){
            AbstractDungeon.actionManager.addToBottom(new ChangeSpriteAction(extra, this, 2.1F));
        }
        addToBot(new ApplyPowerAction(this,this,new NervaPower1(this,200)));
        addToBot(new MakeTempCardInHandAction(returnChoice().get(0)));
        turnCount++;
    }


    @Override
    public void takeTurn() {
        if (turnCount <= returnChoice().size()-1){
            addToBot(new MakeTempCardInHandAction(returnChoice().get(turnCount)));
        }else {
            Set<Integer> totals = new HashSet<Integer>();
            while (totals.size() < 3){
                totals.add(AbstractDungeon.aiRng.random(0,returnChoice().size()-2));
            }
            for (Integer total : totals) {
                int next = total;
                addToBot(new MakeTempCardInHandAction(returnChoice().get(next)));
            }
        }
        turnCount++;

        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(new Color(0.8F, 0.5F, 1.0F, 1.0F))));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartBuffEffect(this.hb.cX, this.hb.cY)));
                addToBot(new SFXAction("MONSTER_COLLECTOR_SUMMON"));
                for (int i = 1; i < 4; i++) {
                    if (enemySlots.get(i) == null || enemySlots.get(i).isDeadOrEscaped()){
                        AbstractMonster m = new Shade(this.spawnX - 155.0F * i, -20.0F);
                        if (enemySlots.get(i) == null){
                            addToBot(new SpawnMonsterAction(m, true));
                            m.usePreBattleAction();
                            this.enemySlots.put(i, m);
                        }else {
                            if (enemySlots.get(i).isDying){
                                enemySlots.remove(i);
                                addToBot(new SpawnMonsterAction(m, true));
                                m.usePreBattleAction();
                                this.enemySlots.put(i, m);
                            }
                        }
                    }
                }
                addToBot(new MakeTempCardInDrawPileAction(new EndOfPurgation(),1,true,false,false));
                addToBot(new MakeTempCardInDiscardAction(new NervaStatus(),2));
                break;
            case 2:
                addToBot(new SFXAction("Nerva_A1"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, Nerva.DIALOG[1]));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartMegaDebuffEffect()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strAmount), strAmount));
                addToBot(new ApplyPowerAction(this,this,new ArtifactPower(this,2),2));
                addToBot(new HealAction(this,this,200));
                if (this.hasPower(NervaPower1.POWER_ID)){
                    addToBot(new ApplyPowerAction(this,this,new MetallicizePower(this,this.metalAmt),this.metalAmt));
                }else if (this.hasPower(NervaPower2.POWER_ID)){
                    addToBot(new ApplyPowerAction(this,this,new SionPower(this,this.sionAmt),this.sionAmt));
                }else if (this.hasPower(NervaPower3.POWER_ID)){
                    addToBot(new ApplyPowerAction(this,this,new BeatOfDeathPower(this,this.strAmount),this.strAmount));
                }else if (this.hasPower(NervaPower4.POWER_ID)){
                    addToBot(new ApplyPowerAction(this,this,new RegenerateMonsterPower(this,this.regenAmt),this.regenAmt));
                }else if (this.hasPower(NervaPower6.POWER_ID)){
                    if (firstEnd){
                        addToBot(new ApplyPowerAction(this,this,this.getPower(NervaPower6.POWER_ID)));
                    }else {
                        firstEnd = true;
                    }
                }
                int rnd = AbstractDungeon.cardRandomRng.random(0,3);
                switch (rnd){
                    case 0:
                        addToBot(new MakeTempCardInDrawPileAction(new Pleasure(),1,true,true));
                        break;
                    case 1:
                        addToBot(new MakeTempCardInDrawPileAction(new Anger(),1,true,true));
                        break;
                    case 2:
                        addToBot(new MakeTempCardInDrawPileAction(new Sorrow(),1,true,true));
                        break;
                    case 3:
                        addToBot(new MakeTempCardInDrawPileAction(new Joy(),1,true,true));
                        break;
                }
                break;
            case 3:
                addToBot(new SFXAction("Nerva_A2"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, Nerva.DIALOG[2]));
                addToBot(new VFXAction(new ViolentAttackEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.BLUE), 1.0F));
                for (int i = 0; i < this.bloodHitCount; i++){
                    addToBot(new VFXAction(new StarBounceEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                }
                addToBot(new MakeTempCardInDrawPileAction(new NervaStatus(),1,true,false,false));
                break;
            case 4:
                addToBot(new VFXAction(new ViceCrushEffect(AbstractDungeon.player.hb.x,AbstractDungeon.player.hb.y)));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new GainBlockAction(this,this.damage.get(0).output));
                break;
        }
        if (this.hasPower(NervaPower6.POWER_ID)){
            addToBot(new SFXAction("NervaPower6_Eff"));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new LaserBeamEffect(this.hb.cX, this.hb.cY + 60.0F * Settings.scale), 1.5F));
            addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this, ((NervaPower6)this.getPower(NervaPower6.POWER_ID)).amount2 * this.getPower(NervaPower6.POWER_ID).amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (this.isFirstMove) {
            setMove((byte)1, Intent.UNKNOWN);
            this.isFirstMove = false;
            return;
        }
        switch (this.turnCount % 4) {
            case 0:
                setMove((byte)1, Intent.UNKNOWN);
                break;
            case 1:
                setMove((byte)2, Intent.STRONG_DEBUFF);
                break;
            case 2:
                setMove((byte)4, Intent.ATTACK_DEFEND, (this.damage.get(0)).base);
                break;
            case 3:
                setMove((byte)3, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base, this.bloodHitCount, true);
                break;
        }
    }

    public void damage(DamageInfo info) {
        if (info.output >= this.currentBlock + this.currentHealth + TempHPField.tempHp.get(this)){
            if ((AbstractDungeon.getCurrRoom()).cannotLose){
                if (!firstTimeHP){
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("Nerva_HP"));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[4]));
                    firstTimeHP = true;
                }
                info.output = this.currentBlock + this.currentHealth + TempHPField.tempHp.get(this)-1;
            }
        }
        super.damage(info);
    }

    @Override
    public void die() {
        if (!(AbstractDungeon.getCurrRoom()).cannotLose) {
            super.die();
            useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                if (m.isDying || m.isDead) {
                    continue;
                }
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
            }
            onBossVictoryLogic();
            onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        }else {
            if (this.currentHealth <= 0 && !this.halfDead) {
                    this.halfDead = true;
                    for (AbstractPower p : this.powers)
                        p.onDeath();
                    for (AbstractRelic r : AbstractDungeon.player.relics)
                        r.onMonsterDeath(this);
                    addToTop(new ClearCardQueueAction());
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            Nerva.this.halfDead = false;
                            this.isDone = true;
                        }
                    });
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("Nerva_HP"));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[4]));
                    AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, 1));
            }
        }
    }
}
