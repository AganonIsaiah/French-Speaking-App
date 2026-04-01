import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

interface Mode {
  title: string;
  titleFr: string;
  description: string;
  icon: string;
  route: string;
  badge: string;
}

@Component({
  selector: 'app-accueil',
  imports: [RouterLink, MatIconModule],
  templateUrl: './accueil.html',
})
export class Accueil {
  modes: Mode[] = [
    {
      title: 'Conversation Libre',
      titleFr: 'Conversation Libre',
      description: 'Discutez librement avec un tuteur IA en français. Posez des questions, racontez votre journée, ou explorez n\'importe quel sujet.',
      icon: 'chat_bubble_outline',
      route: '/conversations',
      badge: 'Parler & Écrire',
    },
    {
      title: 'Traductions Rapides',
      titleFr: 'Traductions Rapides',
      description: 'Traduisez 10 phrases françaises adaptées à votre niveau et recevez un score et des commentaires instantanés.',
      icon: 'flash_on',
      route: '/traductions-rapides',
      badge: 'Jeu de Score',
    },
    {
      title: 'Scénarios',
      titleFr: 'Scénarios',
      description: 'Pratiquez le français dans des situations de la vie réelle — au café, à l\'aéroport, au marché ou chez le médecin.',
      icon: 'theater_comedy',
      route: '/scenarios',
      badge: 'Jeu de Rôle',
    },
  ];
}
