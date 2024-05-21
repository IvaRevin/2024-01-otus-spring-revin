import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { BookListComponent } from "./components/book-list/book-list.component";

import { MatButtonModule } from '@angular/material/button'
import { MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatLabel } from '@angular/material/form-field';
import { NgFor } from '@angular/common';

import { HttpClientModule } from '@angular/common/http';
import { BookEditOrCreateComponent } from "../app/components/book-edit-or-create/book-edit-or-create.component";
import { CommentListComponent } from "../app/components/comments/comment-list.component";
import { CommentEditComponent } from "../app/components/comment-edit/comment-edit.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,

    HttpClientModule,

    MatButtonModule,
    MatTableModule,
    MatInputModule,
    MatLabel,
    MatSelectModule,
    MatFormFieldModule,

    NgFor,

    BookListComponent,
    BookEditOrCreateComponent,
    CommentListComponent,
    CommentEditComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'angular-spa';
}
