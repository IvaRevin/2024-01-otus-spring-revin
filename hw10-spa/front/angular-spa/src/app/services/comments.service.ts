import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../enviroments/enviroment';
import { Observable, catchError, throwError } from 'rxjs';
import { CommentDTO } from "../models/comment";
import { CommentCreateOrEdit } from "../models/comment-create-or-edit";

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private commentsApiPath: string = 'comments';
  private booksApiPath: string = 'books';

  private endPoint: string = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  getApiWithId(id: number): string {
    let idStr = (id === null || id === undefined) ? '' : ("" + id);
    return this.endPoint.concat(this.commentsApiPath, '/', idStr);
  }

  private getBookApiWithId(bookId: number): string {
    return this.endPoint.concat(this.booksApiPath, '/',
      ("" + bookId), '/', this.commentsApiPath);
  }

  public getComment(id: number): Observable<CommentDTO> {
    return this.http.get<CommentDTO>(this.getApiWithId(id)).pipe(
      catchError(err => throwError(() => new Error(err)))
    );
  }

  public getCommentsForBook(id: number): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(this.getBookApiWithId(id)).pipe(
      catchError(err => throwError(() => new Error(err)))
    );
  }

  public updateComment(id: number, comment: CommentDTO): Observable<CommentDTO> {
    const editComment: CommentCreateOrEdit = {
      id: comment.id,
      text: comment.text,
      bookId: comment.book?.id ? comment.book.id.toString() : "",
    }
    return this.http.patch<CommentDTO>(this.getApiWithId(id), editComment).pipe(
      catchError(err => throwError(() => new Error(err)))
    );
  }

  public createComment(comment: CommentDTO): Observable<CommentDTO> {
    const newComment: CommentCreateOrEdit = {
      id: comment.id,
      text: comment.text,
      bookId: comment.book?.id ? comment.book.id.toString() : "",
    }
    return this.http.post<CommentDTO>(this.endPoint.concat(this.commentsApiPath), newComment).pipe(
      catchError(err => throwError(() => new Error(err)))
    );
  }

  public deleteComment(id: number) {
    return this.http.delete(this.getApiWithId(id)).pipe(
      catchError(err => throwError(() => new Error(err)))
    );
  }
}
