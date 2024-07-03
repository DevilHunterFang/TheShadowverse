package shadowverseCharbosses.bosses.Maisha;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import shadowverse.characters.Nemesis;
import shadowverse.effect.ShadowverseEnergyOrb;
import shadowverse.monsters.SpriteCreature;
import shadowverse.powers.CetusPower;
import shadowverse.powers.MaishaPower;
import shadowverseCharbosses.actions.util.CharbossTurnstartDrawAction;
import shadowverseCharbosses.actions.util.DelayedActionAction;
import shadowverseCharbosses.bosses.AbstractBossDeckArchetype;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.core.EnemyEnergyManager;
import shadowverseCharbosses.powers.cardpowers.MaishaPower2;

public class Maisha
        extends AbstractCharBoss implements SpriteCreature {
    public static final String ID = "shadowverse:Maisha";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Maisha");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static Texture BASE_LAYER = new Texture("img/ui/layer_nemesis.png");
    public SpriterAnimation extra;
    public Maisha() {
        super(NAME, ID, 9999, -4.0F, -130.0F, 220.0F, 400.0F, "img/monsters/Maisha/class_3908_00.png", 120.0F, 20.0F, AbstractPlayer.PlayerClass.IRONCLAD);
        if (Loader.isModLoaded("shadowverse")) {
            this.chosenClass = Nemesis.Enums.Nemesis;
        }
        this.energyOrb = new ShadowverseEnergyOrb(null, null,null,BASE_LAYER);
        this.energy = new EnemyEnergyManager(3);
        if (Settings.MAX_FPS > 30){
            this.animation = new SpriterAnimation("img/monsters/Maisha/Maisha.scml");
            extra = new SpriterAnimation("img/monsters/Maisha/extra/extra.scml");
        }
        this.type = EnemyType.BOSS;
        this.dialogX = -100.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
    }


    public void generateDeck() {
        AbstractBossDeckArchetype archetype = new ArchetypeMaisha();
        archetype.initialize();
        this.chosenArchetype = archetype;
    }

    @Override
    public void usePreBattleAction() {
        this.energy.recharge();
        addToBot( new DelayedActionAction(new CharbossTurnstartDrawAction()));
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("Vellsar");
        this.chosenArchetype.addedPreBattle();
        if (AbstractDungeon.ascensionLevel >= 19) {
            addToBot(new ApplyPowerAction(this,this,new MaishaPower(this,250),250));
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            addToBot(new ApplyPowerAction(this,this,new MaishaPower(this,230),220));
        }else {
            addToBot(new ApplyPowerAction(this,this,new MaishaPower(this,210),200));
        }
        addToBot(new ApplyPowerAction(this,this,new MaishaPower2(this,0)));
        addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new CetusPower(AbstractDungeon.player)));
    }

    public void takeTurn() {
        super.takeTurn();
    }


    public void onPlayAttackCardSound() {
    }



    @Override
    public void init() {
        super.init();
        this.energy.energyMaster = 4;
        this.energy.prep();
    }

    public void die() {
        useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();
        AbstractDungeon.scene.fadeInAmbiance();
        onBossVictoryLogic();
        onFinalBossVictoryLogic();
    }


    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    public SpriterAnimation getAnimation() {
        return (SpriterAnimation) this.animation;
    }
    public void setAnimation(SpriterAnimation animation) {
        this.animation = animation;
    }
}


