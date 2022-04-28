package enums;

public enum Technologies {
    AGRICULTURE("//s*agriculture//s*"), // keshavarzi
    ANIMAL_HUSBANDRY("//s*animal_husbandry//s*"),
    ARCHERY("//s*archery//s*"),
    BRONZE_WORKING("//s*bronze_working//s*"),
    CALENDAR("//s*calendar//s*"),
    MASONRY("//s*masonry//s*"),
    MINING("//s*mining//s*"),
    POTTERY("//s*pottery//s*"),
    THE_WHEEL("//s*the_wheel//s*"),
    TRAPPING("//s*trapping//s*"),
    WRITING("//s*writing//s*"),
    CLASSICAL_ERA("//s*classical_era//s*"),
    CONSTRUCTION("//s*construction//s*"),
    HORSEBACK_RIDING("//s*horseback_riding//s*"),
    IRON_WORKING("//s*iron_working//s*"),
    MATHEMATICS("//s*mathematics//s*"),
    PHILOSOPHY("//s*philosophy//s*"),
    MEDIEVAL_ERA("//s*medieval_era//s*"),
    CHIVALRY("//s*chivalry//s*"),
    CIVIL_SERVICE("//s*civil_service//s*"),
    CURRENCY("//s*currency//s*"),
    EDUCATION("//s*education//s*"),
    ENGINEERING("//s*engineering//s*"),
    MACHINERY("//s*machinery//s*"),
    METAL_CASTING("//s*metal_casting//s*"),
    PHYSICS("//s*physics//s*"),
    STEEL("//s*steel//s*"),
    THEOLOGY("//s*theology//s*"),
    RENAISSANCE_ERA("//s*renaissance_era//s*"),
    ACOUSTICS("//s*acoustics//s*"),
    ARCHAEOLOGY("//s*archaeology//s*"),
    BANKING("//s*banking//s*"),
    CHEMISTRY("//s*chemistry//s*"),
    ECONOMICS("//s*economics//s*"),
    FERTILIZER("//s*fertilizer//s*"),
    GUNPOWDER("//s*gunpowder//s*"),
    METALLURGY("//s*metallurgy//s*"),
    MILITARY_SCIENCE("//s*military_science//s*"),
    PRINTING_PRESS("//s*printing_press//s*"),
    RIFLING("//s*rifling//s*"),
    SCIENTIFIC_THEORY("//s*scientific_theory//s*"),
    INDUSTRIAL_ERA("//s*industrial_era//s*"),
    BIOLOGY("//s*biology//s*"),
    COMBUSTION("//s*combustion//s*"),
    DYNAMITE("//s*dynamite//s*"),
    ELECTRICITY("//s*electricity//s*"),
    RADIO("//s*radio//s*"),
    RAILROAD("//s*railroad//s*"),
    REPLACEABLE_PARTS("//s*replaceable_parts//s*"),
    STEAM_POWER("//s*steam_power//s*"),
    TELEGRAPH("//s*telegraph//s*");

    private final String regex;

    Technologies(String regex) {
        this.regex = regex;
    }
}
