package shadowverseCharbosses.bosses.Urias;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import shadowverse.characters.Vampire;
import shadowverse.effect.ShadowverseEnergyOrb;
import shadowverseCharbosses.actions.unique.EnemyChangeStanceAction;
import shadowverseCharbosses.actions.util.CharbossTurnstartDrawAction;
import shadowverseCharbosses.actions.util.DelayedActionAction;
import shadowverseCharbosses.bosses.AbstractBossDeckArchetype;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.core.EnemyEnergyManager;
import shadowverseCharbosses.stances.EnNeutralStance;
import shadowverseCharbosses.stances.EnVengeance;

public class Urias
        extends AbstractCharBoss {
    public static final String ID = "shadowverse:Urias";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Urias");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static Texture BASE_LAYER = new Texture("img/ui/layer_vamp.png");
    public static shadowverse.animation.AbstractAnimation bigAnimation = new shadowverse.animation.AbstractAnimation("img/monsters/Urias/class_1906.atlas", "img/monsters/Urias/class_1906.json", com.megacrit.cardcrawl.core.Settings.M_W / 1600.0F, com.megacrit.cardcrawl.core.Settings.M_W / 2.0F, com.megacrit.cardcrawl.core.Settings.M_H / 2.0F, 0F, 0F);

    public Urias() {
        super(NAME, ID, 450, -4.0F, -16.0F, 220.0F, 400.0F, "img/monsters/Urias/images/class_1906_i_60_000.png", 0.0F, -20.0F, AbstractPlayer.PlayerClass.IRONCLAD);
        if (Loader.isModLoaded("shadowverse")) {
            this.chosenClass = Vampire.Enums.Vampire;
        }
        this.energyOrb = new ShadowverseEnergyOrb(null, null,null,BASE_LAYER);
        this.energy = new EnemyEnergyManager(3);
        if (Settings.MAX_FPS > 30){
            this.animation = new SpriterAnimation("img/monsters/Urias/images/Urias.scml");
        }
        this.type = EnemyType.BOSS;
        this.dialogX = -100.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        bigAnimation.setVisible(false);
    }


    public void generateDeck() {
        AbstractBossDeckArchetype archetype = new ArchetypeVampire();
        archetype.initialize();
        this.chosenArchetype = archetype;
    }

    public void loseBlock(int amount) {
        super.loseBlock(amount);
    }

    @Override
    public void usePreBattleAction() {
        this.energy.recharge();
        addToBot((AbstractGameAction) new DelayedActionAction((AbstractGameAction) new CharbossTurnstartDrawAction()));
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("UriasBgm");
        this.chosenArchetype.addedPreBattle();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new SFXAction("Urias_Start"));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new TalkAction((AbstractCreature) this, DIALOG[0]));
    }

    public void takeTurn() {
        super.takeTurn();
    }


    public void onPlayAttackCardSound() {
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= this.maxHealth / 2.0F) {
            addToBot(new EnemyChangeStanceAction(new EnVengeance()));
        }
    }

    @Override
    public void heal(int healAmount) {
        super.heal(healAmount);
        if (this.currentHealth > this.maxHealth / 2){
            addToBot(new EnemyChangeStanceAction(new EnNeutralStance()));
        }
    }

    @Override
    public void init() {
        super.init();
        this.energy.energyMaster = 3;
        this.energy.prep();
    }

    public void die() {
        useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();
        AbstractDungeon.scene.fadeInAmbiance();
        onBossVictoryLogic();
    }


    public void render(SpriteBatch sb) {
        super.render(sb);
    }
}


