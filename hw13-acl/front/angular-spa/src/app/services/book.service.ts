import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from "../models/book";
import { environment } from "../../enviroments/enviroment";
import { BookCreateOrEdit } from "../models/book-create-or-edit";
import { catchError } from "rxjs";
import { throwError } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = environment.apiUrl + 'books';

  constructor(private http: HttpClient) {
  }

  getBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.apiUrl).pipe(
      catchError(err => throwError(() => new Error(err))));
  }

  getBook(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${id}`).pipe(
      catchError(err => throwError(() => new Error(err))));
  }

  createBook(book: Book): Observable<Book> {

    const newBook: BookCreateOrEdit = {
      id: book.id,
      title: book.title,
      authorId: book.author?.id ? book.author.id.toString() : "",
      genreIds: book.genres?.map(g => g.id ? g.id.toString() : ""),
    }
    return this.http.post<Book>(this.apiUrl, newBook, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }).pipe(
      catchError(err => throwError(() => new Error(err))));
  }

  editBook(id: number, book: Book): Observable<Book> {

    const editBook: BookCreateOrEdit = {
      id: book.id,
      title: book.title,
      authorId: book.author?.id ? book.author.id.toString() : "",
      genreIds: book.genres?.map(g => g.id ? g.id.toString() : ""),
    }
    return this.http.patch<Book>(`${this.apiUrl}/${id}`, editBook, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }).pipe(
      catchError(err => throwError(() => new Error(err))));
  }

  deleteBook(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(err => throwError(() => new Error(err))));
  }
}
