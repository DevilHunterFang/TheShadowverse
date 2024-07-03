package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import shadowverse.action.ChangeSpriteAction;
import shadowverse.action.ChoiceAction2;
import shadowverse.cards.temp.GarnetFlower;
import shadowverse.cards.temp.Itsurugi_Slash;
import shadowverse.powers.*;

import java.util.HashMap;

public class TaketsumiBOSS extends CustomMonster implements SpriteCreature {
    public static final String ID = "shadowverse:TaketsumiBOSS";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:TaketsumiBOSS");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int heavyDmg;

    private int multiDmg;

    private int strAmount;

    private int debuffAmount;

    private int blockAmount;

    private int fireDmg;

    private float spawnX = -100.0F;

    private HashMap<Integer, AbstractMonster> enemySlots = new HashMap<>();

    private static final String MEMORY = MOVES[0];

    private static final String HUNT = MOVES[1];

    private static final String SLASH = MOVES[2];

    private static final String BIND = MOVES[3];

    public SpriterAnimation extra;

    @Override
    public void setAnimation(SpriterAnimation animation) {
        this.animation = animation;
    }

    @Override
    public SpriterAnimation getAnimation() {
        return (SpriterAnimation) this.animation;
    }

    public TaketsumiBOSS() {
        super(NAME, ID, 1200, 0.0F, -30F, 340.0F, 460.0F, "img/monsters/Taketsumi/class_3502_i_60_000.png", 180.0F, 0.0F);
        if (Settings.MAX_FPS > 30){
            this.animation = new SpriterAnimation("img/monsters/Taketsumi/Taketsumi.scml");
            extra = new SpriterAnimation("img/monsters/Taketsumi/extra/extra.scml");
        }
        this.dialogX = -100.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(1440);
        } else {
            setHp(1200);
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.heavyDmg = 24;
            this.multiDmg = 3;
            this.strAmount = 3;
            this.debuffAmount = 2;
            this.blockAmount = 20;
            this.fireDmg = 20;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.heavyDmg = 24;
            this.multiDmg = 2;
            this.strAmount = 2;
            this.debuffAmount = 2;
            this.blockAmount = 18;
            this.fireDmg = 20;
        } else {
            this.heavyDmg = 22;
            this.multiDmg = 2;
            this.strAmount = 2;
            this.debuffAmount = 1;
            this.blockAmount = 15;
            this.fireDmg = 18;
        }
        this.damage.add(new DamageInfo(this, this.heavyDmg));
        this.damage.add(new DamageInfo(this, this.multiDmg));
        this.damage.add(new DamageInfo(this, this.fireDmg));
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("Ametsuchi");
        addToBot(new SFXAction("Taketsumi_Start"));
        addToBot(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
        if (Settings.MAX_FPS > 30){
            AbstractDungeon.actionManager.addToBottom(new ChangeSpriteAction(extra, this, 3.2F));
        }
        addToBot(new ApplyPowerAction(this,this,new TaketsumiPower2(this)));
    }


    @Override
    public void takeTurn() {
        addToBot(new SFXAction("MONSTER_COLLECTOR_SUMMON"));
        for (int i = 1; i < 4; i++) {
            if (enemySlots.get(i) != null && !enemySlots.get(i).isDying){
                addToBot(new ApplyPowerAction(enemySlots.get(i),this,new StrengthPower(enemySlots.get(i),strAmount),strAmount));
                addToBot(new ApplyPowerAction(enemySlots.get(i),this,new RegenerateMonsterPower(enemySlots.get(i),strAmount),strAmount));
            }else {
                AbstractMonster m = new Spider(this.spawnX - 155.0F * i, -20.0F);
                if (enemySlots.get(i) == null){
                    addToBot(new SpawnMonsterAction(m, true));
                    addToBot(new ApplyPowerAction(m,this,new StrengthPower(m,strAmount),strAmount));
                    addToBot(new ApplyPowerAction(m,this,new RegenerateMonsterPower(m,strAmount),strAmount));
                    addToBot(new ApplyPowerAction(m,this, new TaketsumiPower(m,this)));
                    addToBot(new ApplyPowerAction(this,this,new NaterranWorldTreePower(this,1),1));
                    this.enemySlots.put(i, m);
                }else {
                    if (enemySlots.get(i).isDying){
                        enemySlots.remove(i);
                        addToBot(new SpawnMonsterAction(m, true));
                        addToBot(new ApplyPowerAction(m,this,new StrengthPower(m,strAmount),strAmount));
                        addToBot(new ApplyPowerAction(m,this,new RegenerateMonsterPower(m,strAmount),strAmount));
                        addToBot(new ApplyPowerAction(m,this, new TaketsumiPower(m,this)));
                        addToBot(new ApplyPowerAction(this,this,new NaterranWorldTreePower(this,1),1));
                        this.enemySlots.put(i, m);
                    }
                }
            }
        }
        switch (this.nextMove) {
            case 1:
                addToBot(new ShoutAction(this, DIALOG[4], 1.0F, 2.0F));
                addToBot(new SFXAction("Taketsumi_A3"));
                addToBot(new MakeTempCardInDrawPileAction(new Dazed(),2,true,true,false));
                addToBot(new MakeTempCardInDiscardAction(new Dazed(),2));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new WeakPower(AbstractDungeon.player,debuffAmount,true)));
                break;
            case 2:
                addToBot(new ShoutAction(this, DIALOG[2], 1.0F, 2.0F));
                addToBot(new SFXAction("HeroOfTheHunt"));
                addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                addToBot(new VFXAction(new VerticalImpactEffect(AbstractDungeon.player.hb.cX + AbstractDungeon.player.hb.width / 4.0F, AbstractDungeon.player.hb.cY - AbstractDungeon.player.hb.height / 4.0F)));
                addToBot(new ApplyPowerAction(this,this,new HeroOfTheHuntPower(this,1)));
                break;
            case 3:
                addToBot(new ShoutAction(this, DIALOG[1], 1.0F, 2.0F));
                addToBot(new SFXAction("Taketsumi_A1"));
                for (int i = 0;i < 6; i++){
                    addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }
                addToBot(new MakeTempCardInDiscardAction(new Wound(),debuffAmount));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new DrawReductionPower(AbstractDungeon.player,1),1));
                break;
            case 4:
                addToBot(new ShoutAction(this, DIALOG[3], 1.0F, 2.0F));
                addToBot(new SFXAction("Taketsumi_A2"));
                addToBot(new GainBlockAction(this,blockAmount));
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,strAmount),strAmount));
                break;
            case 5:
                addToBot(new ShoutAction(this, DIALOG[5], 1.0F, 2.0F));
                addToBot(new SFXAction("Taketsumi_A4"));
                addToBot(new VFXAction(this, new ScreenOnFireEffect(), 1.0F));
                addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(2), AbstractGameAction.AttackEffect.FIRE));
                addToBot(new MakeTempCardInDiscardAction(new Burn(),debuffAmount));
                break;
            case 6:
                addToBot(new ShoutAction(this, DIALOG[6], 1.0F, 2.0F));
                addToBot(new SFXAction("Taketsumi_A5"));
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,strAmount),strAmount));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new UnseenManaPower(AbstractDungeon.player,7),7));
            default:
                System.out.println("ERROR: Default Take Turn was called on " + this.name);
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        addToBot(new ChoiceAction2(new Itsurugi_Slash(),new GarnetFlower()));
    }

    @Override
    protected void getMove(int i) {
        if (i<25 && !lastMove((byte)1)) {
            setMove(MEMORY,(byte)1, Intent.STRONG_DEBUFF);
            return;
        }else if (i<40 && !lastTwoMoves((byte)2)){
            setMove(HUNT,(byte)2, Intent.ATTACK_BUFF,(this.damage.get(0)).base);
            return;
        }else if (i<60 && !lastMove((byte)3)){
            setMove(SLASH,(byte)3, Intent.ATTACK_DEBUFF,(this.damage.get(1)).base, 6, true);
            return;
        }else if (i<75 && !lastTwoMoves((byte)6) && !AbstractDungeon.player.hasPower(UnseenManaPower.POWER_ID)){
            setMove(BIND,(byte)6, Intent.STRONG_DEBUFF);
            return;
        }else if (!lastMove((byte)4)){
            setMove((byte)4, Intent.DEFEND);
            return;
        }
        setMove((byte)5, Intent.ATTACK_DEBUFF,(this.damage.get(2)).base);
    }

    @Override
    public void die() {
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
    }
}
