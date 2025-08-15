import { Component } from '@angular/core';

import { TitleTemplate } from '../../shared/title-template/title-template';

@Component({
  selector: 'app-accueil',
  imports: [TitleTemplate],
  templateUrl: './accueil.html'
})
export class Accueil {


   /**
    * - Each subtitle (i.e., Mode en ligne, delf prep, grammaire, pratique de la conversation)
    * has separate point system (user can click level on header, drop down appears with sub levels which updates the points system at each level)
    * 
    * - Page shows points for each subtitle
    * - At the top (under title), page shows ai recommendation (i.e., user is at level x, should practice y, etc etc)
    * 
    * *IMPLEMENt EVERYTHING FIRST - DO POINT SYSTEMS LAST (UPDATE DB to take array)
    * *Traduction Rapides - 1v1 mode? Ai generates french sentence in middle, both users type response - A.I., interprets message and scores based on speed + accuracy
    * *Salons de Discussion
    */
}
