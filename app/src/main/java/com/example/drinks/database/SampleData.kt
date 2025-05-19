package com.example.drinks.database
import com.example.drinks.R

val sampleDrinks = listOf(
    Drink(
        name = "Mojito",
        description = "Orzeźwiający rum z miętą",
        ingredients = listOf("Rum", "Mięta", "Limonka", "Cukier", "Woda gazowana").joinToString(),
        preparation = "Wymieszaj wszystko i podaj z lodem",
        imageResId = R.drawable.mojito,
        type = "classic"
    ),
    Drink(
        name = "Piña Colada",
        description = "Egzotyczny drink z ananasem",
        ingredients = listOf("Rum", "Sok ananasowy", "Mleczko kokosowe", "Lód").joinToString(),
        preparation = "Zmiksuj wszystko i podaj schłodzone",
        imageResId = R.drawable.pinacolada,
        type = "exotic"
    ),
    Drink(
        name = "B52",
        description = "Warstwowy shot z likierów",
        ingredients = listOf("Kahlúa", "Baileys", "Triple sec").joinToString(),
        preparation = "Wlej składniki warstwowo na łyżce",
        imageResId = R.drawable.b52,
        type = "shot"
    ),
    Drink(
        name = "Virgin Mojito",
        description = "Bezalkoholowa wersja klasyka",
        ingredients = listOf("Mięta", "Limonka", "Cukier", "Woda gazowana").joinToString(),
        preparation = "Wymieszaj wszystko i podaj z lodem",
        imageResId = R.drawable.mojito,
        type = "non_alcoholic"
    ),
    Drink(
        name = "Baileys Shake",
        description = "Deserowy drink na bazie likieru",
        ingredients = listOf("Baileys", "Lody waniliowe", "Mleko", "Bita śmietana").joinToString(),
        preparation = "Zmiksuj wszystko na gładko i udekoruj",
        imageResId = R.drawable.baileys,
        type = "dessert"
    ),
    Drink(
        name = "Mai Tai",
        description = "Słodko-cytrusowy koktajl z rumem",
        ingredients = listOf("Jasny rum", "Ciemny rum", "Sok limonkowy", "Triple sec", "Syrop migdałowy").joinToString(),
        preparation = "Wstrząśnij z lodem i udekoruj miętą",
        imageResId = R.drawable.maitai,
        type = "exotic"
    ),
    Drink(
        name = "Cosmopolitan",
        description = "Koktajl z wódką i żurawiną",
        ingredients = listOf("Wódka", "Sok żurawinowy", "Triple sec", "Limonka").joinToString(),
        preparation = "Wstrząśnij z lodem i przecedź do kieliszka",
        imageResId = R.drawable.cosmopolitan,
        type = "classic"
    ),
    Drink(
        name = "Tequila Sunrise",
        description = "Kolorowy drink z grenadyną",
        ingredients = listOf("Tequila", "Sok pomarańczowy", "Grenadyna").joinToString(),
        preparation = "Wlej składniki warstwowo i nie mieszaj",
        imageResId = R.drawable.tequilasunrise,
        type = "exotic"
    ),
    Drink(
        name = "Blue Shot",
        description = "Efektowny niebieski shot",
        ingredients = listOf("Blue Curaçao", "Wódka", "Sok z cytryny").joinToString(),
        preparation = "Wstrząśnij i podaj w kieliszku",
        imageResId = R.drawable.blueshot,
        type = "shot"
    ),
    Drink(
        name = "Strawberry Smoothie",
        description = "Owocowy, bezalkoholowy koktajl",
        ingredients = listOf("Truskawki", "Jogurt", "Miód", "Lód").joinToString(),
        preparation = "Zmiksuj wszystko na gładko",
        imageResId = R.drawable.smoothie,
        type = "non_alcoholic"
    )
)
