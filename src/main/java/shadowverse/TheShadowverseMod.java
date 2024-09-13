package shadowverse;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadowverse.cards.curse.*;
import shadowverse.cards.temp.*;
import shadowverse.dungeons.FinalPurgation;
import shadowverse.dungeons.Rivayle;
import shadowverse.dungeons.WhispersOfPurgation;
import shadowverse.events.GemFortune;
import shadowverse.events.LelouchCollaboration;
import shadowverse.events.Loli;
import shadowverse.events.SellCard;
import shadowverse.monsters.*;
import shadowverse.relics.Bullet;
import shadowverse.relics.GeassRelic;
import shadowverse.relics.ValhoreanDealer;
import shadowverseCharbosses.bosses.Maisha.Maisha;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SpireInitializer
public class TheShadowverseMod implements PostInitializeSubscriber, EditCardsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber{
    public static final Logger logger = LogManager.getLogger(TheShadowverseMod.class.getName());
    public static Map<String, String> tempmusic = new HashMap<>();

    public TheShadowverseMod() {
        logger.info("Subscribing");
        BaseMod.subscribe((ISubscriber) this);
        logger.info("Success subscribe");
    }

    public static void initialize() {
        logger.info("Initializing");
        TheShadowverseMod theShadowverse = new TheShadowverseMod();
        logger.info("Initialization success");
    }

    private static String loadJson(String jsonPath) {
        return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
    }

    private HashMap<String, Sfx> getSoundsMap() {
        return (HashMap<String, Sfx>) ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
    }


    @Override
    public void receiveEditCards() {
        logger.info("Adding cards");
        BaseMod.addCard(new BelphometStatus());
        BaseMod.addCard(new AbyssDoomLord());
        BaseMod.addCard(new DeadSoulTaker());
        BaseMod.addCard(new KMRsPresent());
        BaseMod.addCard(new ImperialSaint());
        BaseMod.addCard(new DivineTreasureGuardian());
        BaseMod.addCard(new DivineTreasureHerald());
        BaseMod.addCard(new JudgmentSpear());
        BaseMod.addCard(new GuiltyShield());
        BaseMod.addCard(new EternalDogma());
        BaseMod.addCard(new SummonDivineTreasure());
        BaseMod.addCard(new UltimateBahmut());
        BaseMod.addCard(new InfiniflameDragon());
        BaseMod.addCard(new ShadowBahmut());
        BaseMod.addCard(new ShiningValkyrie());
        BaseMod.addCard(new SevensForceSorcerer());
        BaseMod.addCard(new LegendSwordCommander());
        BaseMod.addCard(new Geass());
        BaseMod.addCard(new KMRGaze());
        BaseMod.addCard(new WUP());
        BaseMod.addCard(new Altersphere());
        BaseMod.addCard(new Death());
        BaseMod.addCard(new Betray());
        BaseMod.addCard(new Rowen());
        BaseMod.addCard(new ShadowversePain());
        BaseMod.addCard(new MadnessCurse());
        BaseMod.addCard(new LionBless());
        BaseMod.addCard(new ManaForce());
        BaseMod.addCard(new Itsurugi_Slash());
        BaseMod.addCard(new GarnetFlower());
        BaseMod.addCard(new Elena_Story());
        BaseMod.addCard(new Arisa_Story());
        BaseMod.addCard(new Yuwan_Story());
        BaseMod.addCard(new Urias_Story());
        BaseMod.addCard(new Luna_Story());
        BaseMod.addCard(new Iris_Story());
        BaseMod.addCard(new Rowen_Story());
        BaseMod.addCard(new Erika_Story());
        BaseMod.addCard(new Isabelle_Story());
        BaseMod.addCard(new EndOfPurgation());
        BaseMod.addCard(new NervaStatus());
        BaseMod.addCard(new Pleasure());
        BaseMod.addCard(new Anger());
        BaseMod.addCard(new Sorrow());
        BaseMod.addCard(new Joy());
    }

    @Override
    public void receiveEditKeywords() {

    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new GeassRelic(), RelicType.SHARED);
        BaseMod.addRelic(new ValhoreanDealer(), RelicType.SHARED);
        BaseMod.addRelic(new Bullet(), RelicType.SHARED);
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Adding Strings");
        String cardsPath = "local/cards-" + Settings.language + ".json";
        String powerPath = "local/powers-" + Settings.language + ".json";
        String relicsPath = "local/relics-" + Settings.language + ".json";
        String uiPath = "local/UIStrings-" + Settings.language + ".json";
        String eventPath = "local/events-" + Settings.language + ".json";
        String monsterPath = "local/monsters-" + Settings.language + ".json";
        BaseMod.loadCustomStringsFile(CardStrings.class, cardsPath);
        BaseMod.loadCustomStringsFile(PowerStrings.class, powerPath);
        BaseMod.loadCustomStringsFile(RelicStrings.class, relicsPath);
        BaseMod.loadCustomStringsFile(UIStrings.class, uiPath);
        BaseMod.loadCustomStringsFile(EventStrings.class, eventPath);
        BaseMod.loadCustomStringsFile(MonsterStrings.class, monsterPath);
        logger.info("Success");
    }

    @Override
    public void receivePostInitialize() {
        CustomDungeon.addAct(2,new Rivayle());
        WhispersOfPurgation whispersOfPurgation = new WhispersOfPurgation();
        BaseMod.addMonster(Iceschillendrig.ID, Iceschillendrig::new);
        BaseMod.addMonster(VincentBOSS.ID, VincentBOSS::new);
        BaseMod.addMonster(Naht.ID, Naht::new);
        BaseMod.addBoss(Rivayle.ID, Iceschillendrig.ID, "img/monsters/UI/IC.png", "img/monsters/UI/IC_Outline.png");
        BaseMod.addBoss(Rivayle.ID, VincentBOSS.ID, "img/monsters/UI/VincentBOSS.png", "img/monsters/UI/VincentBOSS_Outline.png");
        BaseMod.addBoss(Rivayle.ID, Naht.ID, "img/monsters/UI/Naht.png", "img/monsters/UI/Naht_Outline.png");
        BaseMod.addMonster(Wretch.ID, () -> new Wretch());
        BaseMod.addMonster("shadowverse:3_Henchman","3_Henchman",() -> new MonsterGroup(new AbstractMonster[] { new Henchman(-465.0F, -30.0F), new Henchman(-130.0F, -30.0F), new Henchman(200.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:2_Scoundrel","2_Scoundrel",() -> new MonsterGroup(new AbstractMonster[] { new Scoundrel2(-130.0F, -30.0F), new Scoundrel(200.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:Wretch_Shade","Wretch_Shade",() -> new MonsterGroup(new AbstractMonster[] { new Wretch(-130.0F, -30.0F), new Shade(200.0F, -30.0F) }));
        BaseMod.addMonster(Zecilwenshe.ID,() -> new Zecilwenshe());
        BaseMod.addMonster(Commander.ID,() -> new Commander());
        BaseMod.addMonster(Assault.ID,() -> new Assault());
        BaseMod.addMonster(Underling.ID,() -> new Underling());
        BaseMod.addMonster("shadowverse:Underlings","Underlings",() -> new MonsterGroup(new AbstractMonster[] { new Underling(-130.0F, -30.0F), new Underling2(200.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:2_Deputy_Weak","2_Deputy_Weak",() -> new MonsterGroup(new AbstractMonster[] { new Deputy(-130.0F, -30.0F), new Deputy2(200.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:2_Deputy","2_Deputy",() -> new MonsterGroup(new AbstractMonster[] { new Deputy3(-130.0F, -30.0F), new Deputy4(200.0F, -30.0F) }));
        BaseMod.addMonster(Wretch6.ID,() -> new Wretch6());
        BaseMod.addMonster("shadowverse:Unturned","Unturned",() -> new MonsterGroup(new AbstractMonster[] { new Wretch2(-465.0F, -30.0F), new Wretch4(-130.0F, -30.0F), new Wretch3(200.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:Underling_Ista","Underling_Ista",() -> new MonsterGroup(new AbstractMonster[] { new Underling(-130.0F, -30.0F), new Ista(200.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:2_Henchman","2_Henchman",() -> new MonsterGroup(new AbstractMonster[] { new Henchman(-130.0F, -30.0F), new Henchman2(200.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:Rivayle_Mix","Rivayle_Mix",() -> new MonsterGroup(new AbstractMonster[] { new Henchman(-465.0F, -30.0F), new Underling3(200.0F, -30.0F) ,new Deputy(-130.0F, -30.0F)}));
        BaseMod.addMonster("shadowverse:3_Mechas","3_Mechas",() -> new MonsterGroup(new AbstractMonster[] { new Surveyor(-400.0F, -30.0F), new MechaSoldier(-130.0F, -30.0F), new Surveyor(170.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:Strong_Mechas","Strong_Mechas",() -> new MonsterGroup(new AbstractMonster[] { new Surveyor(-130.0F, -30.0F), new MegaEnforcer(200.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:3_OOOGGGG","3_OOOGGGG",() -> new MonsterGroup(new AbstractMonster[] { new OOOGGGG(-400.0F, -30.0F), new OOOGGGG(-130.0F, -30.0F), new OOOGGGG(170.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:Strong_Wretch","Strong_Wretch",() -> new MonsterGroup(new AbstractMonster[] { new Wretch7(-130.0F, -30.0F), new Wretch8(200.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:BladeLight","BladeLight",() -> new MonsterGroup(new AbstractMonster[] { new Sword(-130.0F, -30.0F), new Axe(200.0F, -20.0F) }));
        BaseMod.addMonster("shadowverse:2_Spider","2_Spider",() -> new MonsterGroup(new AbstractMonster[] { new Spider(-130.0F, -30.0F), new Spider2(200.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:4_Shades","4_Shades",() -> new MonsterGroup(new AbstractMonster[] { new Shade(-480.0F, -30.0F), new Shade(-240.0F, -30.0F),new Assault(0.0F, -30.0F),new Shade(240.0F, -30.0F) }));
        BaseMod.addMonster("shadowverse:Tentacles","Tentacles",() -> new MonsterGroup(new AbstractMonster[] { new chushou1(-1000.0F, -10.0F), new chushou2(70.0F, -10.0F) }));
        BaseMod.addMonster(Spider2.ID,() -> new Spider2());
        BaseMod.addMonster(MegaEnforcer.ID,() -> new MegaEnforcer());
        BaseMod.addMonster(Megaera.ID, Megaera::new);
        BaseMod.addMonster(Tisiphone.ID, Tisiphone::new);
        BaseMod.addMonster(Alector.ID, Alector::new);
        BaseMod.addMonster(General.ID,() -> new General());
        whispersOfPurgation.addBoss(TaketsumiBOSS.ID,TaketsumiBOSS::new, "img/monsters/UI/Taketsumi.png", "img/monsters/UI/Taketsumi_Outline.png");
        whispersOfPurgation.addBoss(Belphomet.ID,Belphomet::new, "img/monsters/UI/Belphomet.png", "img/monsters/UI/Belphomet_Outline.png");
        whispersOfPurgation.addBoss(Maisha.ID,Maisha::new, "img/monsters/UI/Maisha.png", "img/monsters/UI/Maisha_Outline.png");
        whispersOfPurgation.addBoss(Nexus.ID,() -> new Nexus(), "img/monsters/UI/Nexus.png", "img/monsters/UI/Nexus_Outline.png");
        BaseMod.addMonster(Lelouch.ID, Lelouch::new);
        whispersOfPurgation.addAct("TheBeyond");
        CustomDungeon.addAct(4,new FinalPurgation());
        BaseMod.addMonster(Nerva.ID,Nerva::new);
        BaseMod.addBoss(FinalPurgation.ID, Nerva.ID, "img/monsters/UI/Nerva.png", "img/monsters/UI/Nerva_Outline.png");
        BaseMod.addBoss(FinalPurgation.ID, Nerva.ID, "img/monsters/UI/Nerva.png", "img/monsters/UI/Nerva_Outline.png");
        BaseMod.addBoss(FinalPurgation.ID, Nerva.ID, "img/monsters/UI/Nerva.png", "img/monsters/UI/Nerva_Outline.png");

        /**
        BaseMod.addMonster(KMR.ID, KMR::new);


        BaseMod.addMonster(shadowverseCharbosses.bosses.Urias.Urias.ID, shadowverseCharbosses.bosses.Urias.Urias::new);

        BaseMod.addBoss(TheEnding.ID, KMR.ID, "img/monsters/UI/KMR.png", "img/monsters/UI/KMR_Outline.png");
        BaseMod.addBoss(TheEnding.ID, KMR.ID, "img/monsters/UI/KMR.png", "img/monsters/UI/KMR_Outline.png");
        BaseMod.addBoss(TheEnding.ID, KMR.ID, "img/monsters/UI/KMR.png", "img/monsters/UI/KMR_Outline.png");




        BaseMod.addBoss(TheCity.ID, shadowverseCharbosses.bosses.Urias.Urias.ID, "img/monsters/UI/Urias.png", "img/monsters/UI/Urias_Outline.png");

         **/


        if (Loader.isModLoaded("shadowverse")) {
            BaseMod.addEvent(GemFortune.ID, GemFortune.class, Rivayle.ID);
            BaseMod.addEvent(Loli.ID, Loli.class, Rivayle.ID);
        }

        BaseMod.addEvent(SellCard.ID, SellCard.class, Rivayle.ID);
        BaseMod.addEvent(LelouchCollaboration.ID, LelouchCollaboration.class, WhispersOfPurgation.ID);
        tempmusic.put("GrandBattle", "sounds/GrandBattle.mp3");
        tempmusic.put("Aiolon", "sounds/Aiolon.mp3");
        tempmusic.put("StormOverRivayle", "sounds/StormOverRivayle.mp3");
        tempmusic.put("IceschillendrigBgm", "sounds/IschellendrigBgm.mp3");
        tempmusic.put("Rivayle_Normal", "sounds/Rivayle_Normal.mp3");
        tempmusic.put("Vellsar", "sounds/Vellsar.mp3");
        tempmusic.put("UriasBgm", "sounds/UriasBgm.mp3");
        tempmusic.put("Ametsuchi", "sounds/Ametsuchi.mp3");
        tempmusic.put("YuwanTheme", "sounds/YuwanTheme.mp3");
        tempmusic.put("HeroOfShadowverse", "sounds/HeroOfShadowverse.mp3");
        tempmusic.put("Final", "sounds/Final.mp3");
        HashMap<String, Sfx> reflectedMap = getSoundsMap();
        String voicePath = "sounds/";
        reflectedMap.put("ArdentSister", new Sfx(voicePath + "ArdentSister.wav"));
        reflectedMap.put("DarkPriest", new Sfx(voicePath + "DarkPriest.wav"));
        reflectedMap.put("Lucifer", new Sfx(voicePath + "Lucifer.wav"));
        reflectedMap.put("Lucifer2", new Sfx(voicePath + "Lucifer2.wav"));
        reflectedMap.put("AbsoluteOne", new Sfx(voicePath + "AbsoluteOne.wav"));
        reflectedMap.put("MoonAlmiraj", new Sfx(voicePath + "MoonAlmiraj.wav"));
        reflectedMap.put("RadiantAngel", new Sfx(voicePath + "RadiantAngel.wav"));
        reflectedMap.put("PriestOfTheCudgel", new Sfx(voicePath + "PriestOfTheCudgel.wav"));
        reflectedMap.put("KMR1", new Sfx(voicePath + "KMR1.wav"));
        reflectedMap.put("KMR2", new Sfx(voicePath + "KMR2.wav"));
        reflectedMap.put("KMR3", new Sfx(voicePath + "KMR3.wav"));
        reflectedMap.put("Rowen", new Sfx(voicePath + "Rowen.wav"));
        reflectedMap.put("SevensForceSorcerer", new Sfx(voicePath + "SevensForceSorcerer.wav"));
        reflectedMap.put("SevensForceSorcererEff", new Sfx(voicePath + "SevensForceSorcererEff.wav"));
        reflectedMap.put("ShiningValkyrie", new Sfx(voicePath + "ShiningValkyrie.wav"));
        reflectedMap.put("DeadSoulTaker", new Sfx(voicePath + "DeadSoulTaker.wav"));
        reflectedMap.put("AbyssDoomLord", new Sfx(voicePath + "AbyssDoomLord.wav"));
        reflectedMap.put("SpineLucille", new Sfx(voicePath + "SpineLucille.wav"));
        reflectedMap.put("LegendSwordCommander", new Sfx(voicePath + "LegendSwordCommander.wav"));
        reflectedMap.put("Fairy", new Sfx(voicePath + "Fairy.wav"));
        reflectedMap.put("CalamityMode", new Sfx(voicePath + "CalamityMode.wav"));
        reflectedMap.put("CalamityEnd", new Sfx(voicePath + "CalamityEnd.wav"));
        reflectedMap.put("Ralmia", new Sfx(voicePath + "Ralmia.wav"));
        reflectedMap.put("Ralmia_EH", new Sfx(voicePath + "Ralmia_EH.wav"));
        reflectedMap.put("TerrorNight", new Sfx(voicePath + "TerrorNight.wav"));
        reflectedMap.put("NightmareTime", new Sfx(voicePath + "NightmareTime.wav"));
        reflectedMap.put("BloodyNail", new Sfx(voicePath + "BloodyNail.wav"));
        reflectedMap.put("DreadAura", new Sfx(voicePath + "DreadAura.wav"));
        reflectedMap.put("DarkGeneral", new Sfx(voicePath + "DarkGeneral.wav"));
        reflectedMap.put("DemonCommander", new Sfx(voicePath + "DemonCommander.wav"));
        reflectedMap.put("Mono2", new Sfx(voicePath + "Mono2.wav"));
        reflectedMap.put("Seox", new Sfx(voicePath + "Seox.wav"));
        reflectedMap.put("Seox_SA", new Sfx(voicePath + "Seox_SA.wav"));
        reflectedMap.put("Seox_SSA", new Sfx(voicePath + "Seox_SSA.wav"));
        reflectedMap.put("Urias_Start", new Sfx(voicePath + "Urias_Start.wav"));
        reflectedMap.put("Urias_Ev", new Sfx(voicePath + "Urias_Ev.wav"));
        reflectedMap.put("Alector_PREP", new Sfx(voicePath + "Alector_PREP.wav"));
        reflectedMap.put("Alector_D1", new Sfx(voicePath + "Alector_D1.wav"));
        reflectedMap.put("Alector_D2", new Sfx(voicePath + "Alector_D2.wav"));
        reflectedMap.put("Belphomet2", new Sfx(voicePath + "Belphomet2.wav"));
        reflectedMap.put("Belphomet3", new Sfx(voicePath + "Belphomet3.wav"));
        reflectedMap.put("Belphomet4", new Sfx(voicePath + "Belphomet4.wav"));
        reflectedMap.put("Belphomet5", new Sfx(voicePath + "Belphomet5.wav"));
        reflectedMap.put("Belphomet6", new Sfx(voicePath + "Belphomet6.wav"));
        reflectedMap.put("IC1", new Sfx(voicePath + "IC1.wav"));
        reflectedMap.put("IC2", new Sfx(voicePath + "IC2.wav"));
        reflectedMap.put("IC3", new Sfx(voicePath + "IC3.wav"));
        reflectedMap.put("IC4", new Sfx(voicePath + "IC4.wav"));
        reflectedMap.put("IC5", new Sfx(voicePath + "IC5.wav"));
        reflectedMap.put("MagiTrain", new Sfx(voicePath + "MagiTrain.wav"));
        reflectedMap.put("IC_Stage2", new Sfx(voicePath + "IC_Stage2.wav"));
        reflectedMap.put("IC_Stage2_A1", new Sfx(voicePath + "IC_Stage2_A1.wav"));
        reflectedMap.put("IC_Stage2_A2", new Sfx(voicePath + "IC_Stage2_A2.wav"));
        reflectedMap.put("IC_Stage2_A3", new Sfx(voicePath + "IC_Stage2_A3.wav"));
        reflectedMap.put("Lelouch_GEASS", new Sfx(voicePath + "Lelouch_GEASS.wav"));
        reflectedMap.put("Lelouch_Checkmate", new Sfx(voicePath + "Lelouch_Checkmate.wav"));
        reflectedMap.put("FatalOrder", new Sfx(voicePath + "FatalOrder.wav"));
        reflectedMap.put("Suzaku_SLASH", new Sfx(voicePath + "Suzaku_SLASH.wav"));
        reflectedMap.put("Suzaku_BEAM", new Sfx(voicePath + "Suzaku_BEAM.wav"));
        reflectedMap.put("Suzaku_CURSE", new Sfx(voicePath + "Suzaku_CURSE.wav"));
        reflectedMap.put("Megaera_PREP", new Sfx(voicePath + "Megaera_PREP.wav"));
        reflectedMap.put("Megaera_D1", new Sfx(voicePath + "Megaera_D1.wav"));
        reflectedMap.put("Megaera_D2", new Sfx(voicePath + "Megaera_D2.wav"));
        reflectedMap.put("Naht_A0", new Sfx(voicePath + "Naht_A0.wav"));
        reflectedMap.put("Naht_A1", new Sfx(voicePath + "Naht_A1.wav"));
        reflectedMap.put("Naht_A2", new Sfx(voicePath + "Naht_A2.wav"));
        reflectedMap.put("Naht_A3", new Sfx(voicePath + "Naht_A3.wav"));
        reflectedMap.put("Naht_A4", new Sfx(voicePath + "Naht_A4.wav"));
        reflectedMap.put("Naht_A5", new Sfx(voicePath + "Naht_A5.wav"));
        reflectedMap.put("Naht_C", new Sfx(voicePath + "Naht_C.wav"));
        reflectedMap.put("Naht_D1", new Sfx(voicePath + "Naht_D1.wav"));
        reflectedMap.put("Naht_D2", new Sfx(voicePath + "Naht_D2.wav"));
        reflectedMap.put("Naht_D3", new Sfx(voicePath + "Naht_D3.wav"));
        reflectedMap.put("Naht_D4", new Sfx(voicePath + "Naht_D4.wav"));
        reflectedMap.put("Naht_D5", new Sfx(voicePath + "Naht_D5.wav"));
        reflectedMap.put("Naht_P1", new Sfx(voicePath + "Naht_P1.wav"));
        reflectedMap.put("Naht_P2", new Sfx(voicePath + "Naht_P2.wav"));
        reflectedMap.put("Naht_P3", new Sfx(voicePath + "Naht_P3.wav"));
        reflectedMap.put("HeroOfTheHunt", new Sfx(voicePath + "HeroOfTheHunt.wav"));
        reflectedMap.put("Taketsumi_Start", new Sfx(voicePath + "Taketsumi_Start.wav"));
        reflectedMap.put("Taketsumi_A1", new Sfx(voicePath + "Taketsumi_A1.wav"));
        reflectedMap.put("Taketsumi_A2", new Sfx(voicePath + "Taketsumi_A2.wav"));
        reflectedMap.put("Taketsumi_A3", new Sfx(voicePath + "Taketsumi_A3.wav"));
        reflectedMap.put("Taketsumi_A4", new Sfx(voicePath + "Taketsumi_A4.wav"));
        reflectedMap.put("Taketsumi_A5", new Sfx(voicePath + "Taketsumi_A5.wav"));
        reflectedMap.put("JudgmentWord", new Sfx(voicePath + "JudgmentWord.wav"));
        reflectedMap.put("Tisiphone_SLASH", new Sfx(voicePath + "Tisiphone_SLASH.wav"));
        reflectedMap.put("Tisiphone_D1", new Sfx(voicePath + "Tisiphone_D1.wav"));
        reflectedMap.put("Tisiphone_D2", new Sfx(voicePath + "Tisiphone_D2.wav"));
        reflectedMap.put("Vincent_A1", new Sfx(voicePath + "Vincent_A1.wav"));
        reflectedMap.put("Vincent_A2", new Sfx(voicePath + "Vincent_A2.wav"));
        reflectedMap.put("Vincent_A3", new Sfx(voicePath + "Vincent_A3.wav"));
        reflectedMap.put("Vincent_A4", new Sfx(voicePath + "Vincent_A4.wav"));
        reflectedMap.put("Vincent_E1", new Sfx(voicePath + "Vincent_E1.wav"));
        reflectedMap.put("Vincent_E3", new Sfx(voicePath + "Vincent_E3.wav"));
        reflectedMap.put("Vincent_Start", new Sfx(voicePath + "Vincent_Start.wav"));
        reflectedMap.put("Zecilwenshe_A", new Sfx(voicePath + "Zecilwenshe_A.wav"));
        reflectedMap.put("Zecilwenshe_R", new Sfx(voicePath + "Zecilwenshe_R.wav"));
        reflectedMap.put("Zecilwenshe_R2", new Sfx(voicePath + "Zecilwenshe_R2.wav"));
        reflectedMap.put("Zecilwenshe_R3", new Sfx(voicePath + "Zecilwenshe_R3.wav"));
        reflectedMap.put("Maiser_Story", new Sfx(voicePath + "Maiser_Story.wav"));
        reflectedMap.put("Selena_Story", new Sfx(voicePath + "Selena_Story.wav"));
        reflectedMap.put("Illganeau_Story", new Sfx(voicePath + "Illganeau_Story.wav"));
        reflectedMap.put("Sekka_Story", new Sfx(voicePath + "Sekka_Story.wav"));
        reflectedMap.put("Dracu_Story", new Sfx(voicePath + "Dracu_Story.wav"));
        reflectedMap.put("Kagero_Story", new Sfx(voicePath + "Kagero_Story.wav"));
        reflectedMap.put("VerdictWord", new Sfx(voicePath + "VerdictWord.wav"));
        reflectedMap.put("AbsoluteOrder", new Sfx(voicePath + "AbsoluteOrder.wav"));
        reflectedMap.put("BelphometPower", new Sfx(voicePath + "BelphometPower.wav"));
        reflectedMap.put("BelphometStatus", new Sfx(voicePath + "BelphometStatus.wav"));
        reflectedMap.put("Itsurugi_Slash", new Sfx(voicePath + "Itsurugi_Slash.wav"));
        reflectedMap.put("Flower", new Sfx(voicePath + "Flower.wav"));
        reflectedMap.put("TaketsumiPower2", new Sfx(voicePath + "TaketsumiPower2.wav"));
        reflectedMap.put("DualSwords", new Sfx(voicePath + "DualSwords.wav"));
        reflectedMap.put("EnAmaterasu", new Sfx(voicePath + "EnAmaterasu.wav"));
        reflectedMap.put("EnTsukuyomi", new Sfx(voicePath + "EnTsukuyomi.wav"));
        reflectedMap.put("BladeOfLight", new Sfx(voicePath + "BladeOfLight.wav"));
        reflectedMap.put("BladeOfDark", new Sfx(voicePath + "BladeOfDark.wav"));
        reflectedMap.put("Karula", new Sfx(voicePath + "Karula.wav"));
        reflectedMap.put("KarulaPower", new Sfx(voicePath + "KarulaPower.wav"));
        reflectedMap.put("PurgationBlade", new Sfx(voicePath + "PurgationBlade.wav"));
        reflectedMap.put("OmenOfCraving2", new Sfx(voicePath + "OmenOfCraving2.wav"));
        reflectedMap.put("RavenousSweetness", new Sfx(voicePath + "RavenousSweetness.wav"));
        reflectedMap.put("Rukina", new Sfx(voicePath + "Rukina.wav"));
        reflectedMap.put("UnnamedDetermination", new Sfx(voicePath + "UnnamedDetermination.wav"));
        reflectedMap.put("TrueStrike", new Sfx(voicePath + "TrueStrike.wav"));
        reflectedMap.put("FalseSlash", new Sfx(voicePath + "FalseSlash.wav"));
        reflectedMap.put("TrueSword", new Sfx(voicePath + "TrueSword.wav"));
        reflectedMap.put("FalseSword", new Sfx(voicePath + "FalseSword.wav"));
        reflectedMap.put("MaishaPower", new Sfx(voicePath + "MaishaPower.wav"));
        reflectedMap.put("MercurialMight", new Sfx(voicePath + "MercurialMight.wav"));
        reflectedMap.put("Sion", new Sfx(voicePath + "Sion.wav"));
        reflectedMap.put("NexusPower", new Sfx(voicePath + "NexusPower.wav"));
        reflectedMap.put("Reset", new Sfx(voicePath + "Reset.wav"));
        reflectedMap.put("Restart", new Sfx(voicePath + "Restart.wav"));
        reflectedMap.put("Nexus_A1", new Sfx(voicePath + "Nexus_A1.wav"));
        reflectedMap.put("Nexus_A2", new Sfx(voicePath + "Nexus_A2.wav"));
        reflectedMap.put("Nexus_E1", new Sfx(voicePath + "Nexus_E1.wav"));
        reflectedMap.put("Nexus_E2", new Sfx(voicePath + "Nexus_E2.wav"));
        reflectedMap.put("Nerva_A1", new Sfx(voicePath + "Nerva_A1.wav"));
        reflectedMap.put("Nerva_A2", new Sfx(voicePath + "Nerva_A2.wav"));
        reflectedMap.put("Nerva_Start", new Sfx(voicePath + "Nerva_Start.wav"));
        reflectedMap.put("NervaPower1", new Sfx(voicePath + "NervaPower1.wav"));
        reflectedMap.put("NervaPower2", new Sfx(voicePath + "NervaPower2.wav"));
        reflectedMap.put("NervaPower3", new Sfx(voicePath + "NervaPower3.wav"));
        reflectedMap.put("NervaPower4", new Sfx(voicePath + "NervaPower4.wav"));
        reflectedMap.put("NervaPower5", new Sfx(voicePath + "NervaPower5.wav"));
        reflectedMap.put("NervaPower6_Eff", new Sfx(voicePath + "NervaPower6_Eff.wav"));
        reflectedMap.put("Nerva_End", new Sfx(voicePath + "Nerva_End.wav"));
        reflectedMap.put("Nerva_HP", new Sfx(voicePath + "Nerva_HP.wav"));
        reflectedMap.put("Nerva_MaxHP", new Sfx(voicePath + "Nerva_MaxHP.wav"));
        reflectedMap.put("Nerva_Victory", new Sfx(voicePath + "Nerva_Victory.wav"));
        reflectedMap.put("Nerva_VictoryCard", new Sfx(voicePath + "Nerva_VictoryCard.wav"));
        reflectedMap.put("Arisa_Story", new Sfx(voicePath + "Arisa_Story.wav"));
        reflectedMap.put("Yuwan_Story", new Sfx(voicePath + "Yuwan_Story.wav"));
        reflectedMap.put("Urias_Story", new Sfx(voicePath + "Urias_Story.wav"));
        reflectedMap.put("Luna_Story", new Sfx(voicePath + "Luna_Story.wav"));
        reflectedMap.put("Iris_Story", new Sfx(voicePath + "Iris_Story.wav"));
        reflectedMap.put("Rowen_Story", new Sfx(voicePath + "Rowen_Story.wav"));
        reflectedMap.put("Erika_Story", new Sfx(voicePath + "Erika_Story.wav"));
        reflectedMap.put("Isabelle_Story", new Sfx(voicePath + "Isabelle_Story.wav"));
        reflectedMap.put("Elena_Story", new Sfx(voicePath + "Elena_Story.wav"));
    }
}
