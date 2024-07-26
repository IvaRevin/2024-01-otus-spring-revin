import { Routes } from '@angular/router';
import { BookListComponent } from "../app/components/book-list/book-list.component";
import { BookEditOrCreateComponent } from "../app/components/book-edit-or-create/book-edit-or-create.component";
import { CommentListComponent } from "../app/components/comments/comment-list.component";
import { CommentEditComponent } from "../app/components/comment-edit/comment-edit.component";

export const routes: Routes = [
  { path: '', redirectTo: 'book-list', pathMatch: "full" },

  { path: 'book-list', component: BookListComponent },
  { path: 'book-edit-or-create/:id', component: BookEditOrCreateComponent },
  { path: 'book-edit-or-create', component: BookEditOrCreateComponent },
  { path: 'comment-list/:id', component: CommentListComponent },
  { path: 'comment-edit', component: CommentEditComponent },
  { path: 'comment-edit/:id', component: CommentEditComponent },
];
