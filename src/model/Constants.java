package model;

public class Constants {
	public static int GRID_SIZE = 100 ; // largeur de la grille
	public static int NUM_RED = 5 ; // nombre initial de membres de l'equipe rouge
	public static int NUM_BLUE = 5 ; // nombre initial de membres de l'equipe bleue
	public static int NUM_BLUE_FLAGS = 5 ;
	public static int NUM_RED_FLAGS = 5 ;
	public static int MAX_ENERGY = 15 ; // maximum d'énergie d'une personne
	
	//Caractéristiques statiques
	public static int DISTANCE_DEPLACEMENT = 3 ; // l'agent peut se déplacer de X cases à chaque tour.
	public static int DISTANCE_PERCEPTION = 6 ; // l’agent peut percevoir le contenu des n cases adjacentes
	public static int ATTACK = 2;
}
