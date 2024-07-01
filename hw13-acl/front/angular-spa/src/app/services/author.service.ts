import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from '../../enviroments/enviroment';
import { Observable, catchError, throwError } from 'rxjs';
import { Author } from '../models/author';

@Injectable({
  providedIn: 'root'
})
export class AuthorService {

  private apiUrl = environment.apiUrl + 'authors';

  constructor(private http: HttpClient) {}

  public getAuthorList(): Observable<Author[]> {
    return this.http.get<Author[]>(this.apiUrl).pipe(
      catchError(err => throwError(() => new Error(err)))
    );
  }
}
