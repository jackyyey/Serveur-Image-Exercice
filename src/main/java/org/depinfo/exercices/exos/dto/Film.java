package org.depinfo.exercices.exos.dto;

import java.util.List;
import java.util.Arrays;

public class Film {
    String titre;
    int annee;

    public Film(String titre, int annee) {
        this.titre = titre;
        this.annee = annee;
    }

    public static final List<Film> FILMS_QUEBECOIS_CLASSIQUES = Arrays.asList(
            new Film("Mon oncle Antoine", 1971),
            new Film("Les bons débarras", 1980),
            new Film("Le déclin de l'empire américain", 1986),
            new Film("La grande séduction", 2003),
            new Film("C.R.A.Z.Y.", 2005),
            new Film("Les invasions barbares", 2003),
            new Film("Un zoo la nuit", 1987),
            new Film("Les ordres", 1974),
            new Film("Maelström", 2000),
            new Film("Incendies", 2010),
            new Film("Leolo", 1992),
            new Film("Kamouraska", 1973),
            new Film("La guerre des tuques", 1984),
            new Film("Léolo", 1992),
            new Film("Les Plouffe", 1981),
            new Film("Le Party", 1990),
            new Film("Les boys", 1997)
    );
}


