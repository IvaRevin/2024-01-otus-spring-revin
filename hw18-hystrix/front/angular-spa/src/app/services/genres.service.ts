import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../enviroments/enviroment';
import { Observable, catchError, throwError } from 'rxjs';
import { Genre } from '../models/genre';

@Injectable({
  providedIn: 'root'
})
export class GenreService {

  private apiUrl = environment.apiUrl + 'genres';

  constructor(private http: HttpClient) {}

  public getGenreList(): Observable<Genre[]> {
    return this.http.get<Genre[]>(this.apiUrl).pipe(
      catchError(err => throwError(() => new Error(err)))
    );
  }
}
