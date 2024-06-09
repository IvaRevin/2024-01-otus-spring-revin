import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book.service';
import { Book } from '../../models/book';
import { MatTableModule } from '@angular/material/table';
import { Router } from '@angular/router';
import { MatButton } from '@angular/material/button';
import { NgIf } from "@angular/common";
import { NgForOf } from "@angular/common";

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [MatTableModule, MatButton, NgIf, NgForOf],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit {

  books: Book[] = [];
  displayedColumns: string[] = ['id', 'title', 'author', 'genres', 'actions'];

  constructor(
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.refreshBooks();
  }

  private refreshBooks(): void {
    this.bookService.getBooks().subscribe(data => {
      this.books = data;
    });
  }

  public goToCreateBook() {
    this.router.navigate(['book-edit-or-create']);
  }

  public goToEditBook(book: Book) {
    if (!book || !book.id) {
      return;
    }
    this.router.navigate(['book-edit-or-create', book.id]);
  }

  public showComment(book: Book) {
    if (!book || !book.id) {
      return;
    }
    this.router.navigate(['comment-list', book.id]);
  }

  public deleteBook(book: Book) {
    if (!book || !book.id) {
      return;
    }
    this.bookService.deleteBook(book.id).subscribe(() => {
      this.refreshBooks();
    });
  }
}
