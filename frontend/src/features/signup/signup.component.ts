import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SignupData } from '../../core/models/signup-data';
import { NgFor, NgIf } from '@angular/common';
import { countries } from 'countries-list'
import { AuthService } from '../../core/services/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  imports: [RouterModule, FormsModule, NgFor, NgIf],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})

export class SignupComponent implements OnInit {

  signupData: SignupData = {
    username: '',
    email: '',
    password: '',
    points: 0,
    proficiency: '',
    region: ''
  }

  allCountries: string[] = [];
  filteredCountries: string[] = [];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.allCountries = Object.values(countries).map(c => c.name).sort();
  }

  onRegionInput(): void {
    const input = this.signupData.region.toLowerCase();
    this.filteredCountries = this.allCountries.filter(country => country.toLowerCase().startsWith(input));
  }

  selectCountry(country: string): void {
    this.signupData.region = country;
    this.filteredCountries = [];
  }

  onSignup() {
    if (!this.allCountries.includes(this.signupData.region)) {
      return;
    }
    this.authService.signup(this.signupData).subscribe({
      next: () => {
          this.router.navigate(['/login']);
      },
      error: err => {
        console.error('Signup error:', err);
      }
    })
  }
}
