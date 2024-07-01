import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatOption, MatSelect } from '@angular/material/select';
import { NgFor } from '@angular/common';
import { Author } from '../../models/author';
import { Genre } from '../../models/genre';
import { BookService } from '../../services/book.service';
import { AuthorService } from '../../services/author.service';
import { GenreService } from '../../services/genres.service';
import { MatButtonModule } from '@angular/material/button';
import { MatInput, MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { Book } from "../../models/book";

@Component({
  selector: 'app-book-edit-or-create',
  standalone: true,
  imports: [MatFormField, MatInputModule, MatLabel, MatSelect, MatOption, MatButtonModule,
    NgFor, FormsModule, MatInput, MatFormFieldModule],
  templateUrl: './book-edit-or-create.component.html',
  styleUrls: ['./book-edit-or-create.component.scss']
})
export class BookEditOrCreateComponent implements OnInit {
  bookCreate: Book;
  authors: Author[];
  genres: Genre[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bookService: BookService,
    private authorService: AuthorService,
    private genreService: GenreService
  ) {
    this.bookCreate = { author: {}, genres: [], title: "" };
    this.authors = [];
    this.genres = [];
  }

  saveBook() {
    if (this.bookCreate.id) {
      this.bookService.editBook(this.bookCreate.id, this.bookCreate).subscribe(() => this.goToBookList());
    } else {
      this.bookService.createBook(this.bookCreate).subscribe(() => this.goToBookList());
    }
  }

  goToBookList() {
    this.router.navigate(['book-list']);
  }

  private getBookById(id: number) {
    if (!id) {
      this.bookCreate = { author: {}, genres: [], title: "" };
      return;
    }

    this.bookService.getBook(id).subscribe(data => {
      this.bookCreate = data;

      if (this.bookCreate.author && this.bookCreate.author.id) {
        this.bookCreate.author = this.authors.find(author => author.id === this.bookCreate.author?.id) || {};
      }

      if (this.bookCreate.genres && this.bookCreate.genres.length > 0) {
        this.bookCreate.genres = this.bookCreate.genres.map(genre =>
          this.genres.find(g => g.id === genre.id) || genre
        );
      }
    });
  }

  ngOnInit(): void {
    this.authorService.getAuthorList().subscribe(data => {
      this.authors = data;

      this.route.params.subscribe((params: Params) => {
        let id = params['id'];
        this.getBookById(id);
      });
    });

    this.genreService.getGenreList().subscribe(data => {
      this.genres = data;
    });
  }
}
