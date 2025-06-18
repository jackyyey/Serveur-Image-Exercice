package org.depinfo.exercices.exos.controller;

import com.google.common.collect.Lists;
import org.depinfo.exercices.exos.dto.Film;
import org.depinfo.exercices.exos.dto.Nombre;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Ceci est un web service pour pratiquer les étudiants sur un exemple déployé
 * Pratiquer l'envoi et la réception de
 * - Valeur simple en envoi et retour
 * - Valeur complexe et JSON / GSON
 * - Liste de valeurs complexes
 * - Valeur d'erreur selon l'objet envoyé en requête
 */

@Controller
public class WebServiceIntra {

	@GetMapping("/exam/{annee}/{mois}/{jour}")
	public @ResponseBody List<Intra> addOne(
			@PathVariable int annee,
			@PathVariable int mois,
			@PathVariable int jour
	) {
		System.out.println("Année " + annee);
		DateTime d = DateTime.now().withYear(annee).withDayOfMonth(jour).withMonthOfYear(mois);
		List<Intra> res = Lists.newArrayList();
		while(d.isBefore(DateTime.now().plusYears(1))) {
			res.add(new Intra(d));
			d = d.plusYears(1);
		}
		return res;
	}

	public static class Intra {
		int jour, mois, annee;

		String jourDeLaSemaine;

		public Intra(DateTime d) {
			this.jour = d.getDayOfMonth();
			this.mois = d.getMonthOfYear();
			this.annee = d.getYear();
			this.jourDeLaSemaine = d.dayOfWeek().getAsText(Locale.FRENCH);
		}
	}


	public static class NooooooooooonnnnnnnnnException extends Exception {}

	@GetMapping("/milieu/{a}/{b}")
	public @ResponseBody double milieu(
			@PathVariable int a,
			@PathVariable int b
	) throws NooooooooooonnnnnnnnnException {
		if (b < a) throw new NooooooooooonnnnnnnnnException();
		return (a+b) / 2.0;
	}


	@GetMapping("/fibo/{a}/{b}")
	public @ResponseBody List<String> fibo(
			@PathVariable long a,
			@PathVariable long b
	)  {
		a = Math.min(a, 50);
		b = Math.min(b, 50);
		List<String> res = Lists.newArrayList();
		for (long i = a ; i <= b ; i++) {
			res.add(Nombre.getLettre(fibo(i)));
		}
		return res;
	}

	Long fibo(long n) {
		if (n == 0) return 0L;
		if (n == 1) return 1L;
		long[] res = new long[(int) n+1];
		for(int i = 0 ; i <= n ; i++) {
			if (i == 0) res[i] = 0;
			if (i == 1) res[i] = 1;
			if (i > 1) res[i] = res[i-1] + res[i-2];
		}
		return res[(int) n];
	}

	@GetMapping("/films/{quantite}")
	public @ResponseBody List<Film> liste(
			@PathVariable long quantite
	)  {
		List<Film> res = Lists.newArrayList();
		Random random = new Random();
		for (long i = 1 ; i <= quantite ; i++) {
			Film filmAleatoire = Film.FILMS_QUEBECOIS_CLASSIQUES.get(
					random.nextInt(Film.FILMS_QUEBECOIS_CLASSIQUES.size())
			);
			res.add(filmAleatoire);
		}
		return res;
	}

}
