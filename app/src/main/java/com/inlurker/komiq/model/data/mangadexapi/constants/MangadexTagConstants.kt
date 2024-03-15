package com.inlurker.komiq.model.data.mangadexapi.constants
enum class GenreTag(val description: String, val hash: String) {
    THRILLER("Thriller", "07251805-a27e-4d59-b488-f0bfbec15168"),
    SCI_FI("Sci-Fi", "256c8bd9-4904-4360-bf4f-508a76d67183"),
    HISTORICAL("Historical", "33771934-028e-4cb3-8744-691e866a923e"),
    ACTION("Action", "391b0423-d847-456f-aff0-8b0cfc03066b"),
    PSYCHOLOGICAL("Psychological", "3b60b75c-a2d7-4860-ab56-05f391bb889c"),
    ROMANCE("Romance", "423e2eae-a7a2-4a8b-ac03-a8351462d71d"),
    COMEDY("Comedy", "4d32cc48-9f00-4cca-9b5a-a839f0764984"),
    MECHA("Mecha", "50880a9d-5440-4732-9afb-8f457127e836"),
    BOYS_LOVE("Boys' Love", "5920b825-4181-4a17-beeb-9918b0ff7a30"),
    CRIME("Crime", "5ca48985-9a9d-4bd8-be29-80dc0303db72"),
    SPORTS("Sports", "69964a64-2f90-4d33-beeb-f3ed2875eb4c"),
    SUPERHERO("Superhero", "7064a261-a137-4d3a-8848-2d385de3a99c"),
    MAGICAL_GIRLS("Magical Girls", "81c836c9-914a-4eca-981a-560dad663e73"),
    ADVENTURE("Adventure", "87cc87cd-a395-47af-b27a-93258283bbc6"),
    GIRLS_LOVE("Girls' Love", "a3c67850-4684-404e-9b7f-c69850ee5da6"),
    WUXIA("Wuxia", "acc803a4-c95a-4c22-86fc-eb6b582d82a2"),
    ISEKAI("Isekai", "ace04997-f6bd-436e-b261-779182193d3d"),
    PHILOSOPHICAL("Philosophical", "b1e97889-25b4-4258-b28b-cd7f4d28ea9b"),
    DRAMA("Drama", "b9af3a63-f058-46de-a9a0-e0c13906197a"),
    MEDICAL("Medical", "c8cbe35b-1b2b-4a3f-9c37-db84c4514856"),
    HORROR("Horror", "cdad7e68-1419-41dd-bdce-27753074a640"),
    FANTASY("Fantasy", "cdc58593-87dd-415e-bbc0-2ec27bf404cc"),
    SLICE_OF_LIFE("Slice of Life", "e5301a23-ebd9-49dd-a0cb-2add944c7fe9"),
    MYSTERY("Mystery", "ee968100-4191-4968-93d3-f82d72be7e46"),
    TRAGEDY("Tragedy", "f8f62932-27da-4fe4-8ee1-6779a8c5edba");

    companion object {
        fun asList(): List<GenreTag> = values().toList()
    }
}

enum class ThemeTag(val description: String, val hash: String) {
    REINCARNATION("Reincarnation", "0bc90acb-ccc1-44ca-a34a-b9f3a73259d0"),
    TIME_TRAVEL("Time Travel", "292e862b-2d17-4062-90a2-0356caa4ae27"),
    GENDERSWAP("Genderswap", "2bd2e8d0-f146-434a-9b51-fc9ff2c5fe6a"),
    LOLI("Loli", "2d1f5d56-a1e5-4d0d-a961-2193588b08ec"),
    TRADITIONAL_GAMES("Traditional Games", "31932a7e-5b8e-49a6-9f12-2afa39dc544c"),
    MONSTERS("Monsters", "36fd93ea-e8b8-445e-b836-358f02b3d33d"),
    DEMONS("Demons", "39730448-9a5f-48a2-85b0-a70db87b1233"),
    GHOSTS("Ghosts", "3bb26d85-09d5-4d2e-880c-c34b974339e9"),
    ANIMALS("Animals", "3de8c75d-8ee3-48ff-98ee-e20a65c86451"),
    NINJA("Ninja", "489dd859-9b61-4c37-af75-5b18e88daafc"),
    INCEST("Incest", "5bd0e105-4481-44ca-b6e7-7544da56b1a3"),
    SURVIVAL("Survival", "5fff9cde-849c-4d78-aab0-0d52b2ee1d25"),
    ZOMBIES("Zombies", "631ef465-9aba-4afb-b0fc-ea10efe274a8"),
    REVERSE_HAREM("Reverse Harem", "65761a2a-415e-47f3-bef2-a9dababba7a6"),
    MARTIAL_ARTS("Martial Arts", "799c202e-7daa-44eb-9cf7-8a3c0441531e"),
    SAMURAI("Samurai", "81183756-1453-4c81-aa9e-f6e1b63be016"),
    MAFIA("Mafia", "85daba54-a71c-4554-8a28-9901a8b0afad"),
    VIRTUAL_REALITY("Virtual Reality", "8c86611e-fab7-4986-9dec-d1a2f44acdd5"),
    OFFICE_WORKERS("Office Workers", "92d6d951-ca5e-429c-ac78-451071cbf064"),
    VIDEO_GAMES("Video Games", "9438db5a-7e2a-4ac0-b39e-e0d95a34b8a8"),
    POST_APOCALYPTIC("Post Apocalyptic", "9467335a-1b83-4497-9231-765337a00b96"),
    CROSSDRESSING("Crossdressing", "9ab53f92-3eed-4e9b-903a-917c86035ee3"),
    MAGIC("Magic", "a1f53773-c69a-4ce5-8cab-fffcd90b1565"),
    HAREM("Harem", "aafb99c1-7f60-43fa-b75f-fc9502ce29c7"),
    MILITARY("Military", "ac72833b-c4e9-4878-b9db-6c8a4a99444a"),
    SCHOOL_LIFE("School Life", "caaa44eb-cd40-4177-b930-79d3ef2afe87"),
    VILLAINESS("Villainess", "d14322ac-4d6f-4e9b-afd9-629d5f4d8a41"),
    VAMPIRES("Vampires", "d7d1730f-6eb0-4ba6-9437-602cac38664c"),
    DELINQUENTS("Delinquents", "da2d50ca-3018-4cc0-ac7a-6b7d472a29ea"),
    MONSTER_GIRLS("Monster Girls", "dd1f77c5-dea9-4e2b-97ae-224af09caf99"),
    SHOTA("Shota", "ddefd648-5140-4e5f-ba18-4eca4071d19b"),
    POLICE("Police", "df33b754-73a3-4c54-80e6-1a74a8058539"),
    ALIENS("Aliens", "e64f6742-c834-471d-8d72-dd51fc02b835"),
    COOKING("Cooking", "ea2bc92d-1c26-4930-9b7c-d5c0dc1b6869"),
    SUPERNATURAL("Supernatural", "eabc5b4c-6aff-42f3-b657-3e90cbd00b75"),
    MUSIC("Music", "f42fbf9e-188a-447b-9fdc-f19dc1e4d685"),
    GYARU("Gyaru", "fad12b5e-68ba-460e-b933-9ae8318f5b65");

    companion object {
        fun asList(): List<ThemeTag> = values().toList()
    }
}

enum class FormatTag(val description: String, val hash: String) {
    ONESHOT("Oneshot", "0234a31e-a729-4e28-9d6a-3f87c4966b9e"),
    AWARD_WINNING("Award Winning", "0a39b5a1-b235-4886-a747-1d05d216532d"),
    OFFICIAL_COLORED("Official Colored", "320831a8-4026-470b-94f6-8353740e6f04"),
    LONG_STRIP("Long Strip", "3e2b8dae-350e-4ab8-a8ce-016e844b9f0d"),
    ANTHOLOGY("Anthology", "51d83883-4103-437c-b4b1-731cb73d786c"),
    FAN_COLORED("Fan Colored", "7b2ce280-79ef-4c09-9b58-12b7c23a9b78"),
    SELF_PUBLISHED("Self Published", "891cf039-b895-47f0-9229-bef4c96eccd4"),
    FOUR_KOMA("Four Koma", "b11fda93-8f1d-4bef-b2ed-8803d3733170"),
    DOUJINSHI("Doujinshi", "b13b2a48-c720-44a9-9c77-39c9979373fb"),
    WEB_COMIC("Web Comic", "e197df38-d0e7-43b5-9b09-2842d0c326dd"),
    ADAPTATION("Adaptation", "f4122d1c-3b44-44d0-9936-ff7502c39ad3"),
    FULL_COLOR("Full Color", "f5ba408b-0e7a-484d-8d49-4e9125ac96de");

    companion object {
        fun asList(): List<FormatTag> = values().toList()
    }
}